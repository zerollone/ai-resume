package com.ai.resume.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import com.ai.resume.config.properties.AlgorithmPathProperties;
import com.ai.resume.config.properties.MinioProperties;
import com.ai.resume.config.properties.ResumeProperties;
import com.ai.resume.controller.vo.*;
import com.ai.resume.entity.*;
import com.ai.resume.enums.RankingTypeEnum;
import com.ai.resume.enums.ResumeStatusEnum;
import com.ai.resume.exception.CommonException;
import com.ai.resume.mapper.*;
import com.ai.resume.result.Result;
import com.ai.resume.service.ResumeService;
import com.ai.resume.utils.Base64Util;
import com.ai.resume.utils.MinioUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 简历表 服务实现类
 * </p>
 *
 * @author ws
 * @since 2026-04-18
 */
@Slf4j
@Service
public class ResumeServiceImpl extends ServiceImpl<ResumeMapper, ResumePO> implements ResumeService {

    private static final List<String> FILE_TYPE = Arrays.asList("pdf", "docx", "doc");

    private static final String SLASH = "/";

    private static final Integer FILE_SIZE = 1024 * 1024 * 50;

    private static final String RANK_TOTAL = "rank:overall";
    private static final String RANK_TIAN = "rank:tian";
    private static final String RANK_DI = "rank:di";
    private static final String RANK_XUAN = "rank:xuan";
    private static final String RANK_HUANG = "rank:huang";
    private static final String RANK_OTHER = "rank:other";

    @Autowired
    private MinioUtil minioUtil;

    @Autowired
    @Qualifier("algoExecutor")
    private ThreadPoolTaskExecutor algoExecutor;

    @Autowired
    private AlgorithmPathProperties pathProperties;

    @Autowired
    private ResumeScoreMapper resumeScoreMapper;

    @Autowired
    private ResumeTagMapper resumeTagMapper;

    @Autowired
    private MinioProperties minioProperties;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ResumeProperties resumeProperties;

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private ResumeSuggestMapper resumeSuggestMapper;

    @Override
    public String upload(MultipartFile file) {
        if (file == null) {
            throw new CommonException("请上传简历文件");
        }
        long size = file.getSize();
        if (size > FILE_SIZE) {
            throw new CommonException("简历文件最大支持50MB");
        }
        String filename = file.getOriginalFilename();
        log.info("====== 上传文件名称：{}", filename);
        String fileType = filename.substring(filename.lastIndexOf(".") + 1);

        if (!FILE_TYPE.contains(fileType)) {
            throw new CommonException("不支持的文件类型！");
        }

        // minio文件路径
        String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String objectName = datePath + SLASH + UUID.randomUUID() + "." + fileType;

        // 上传到minio
        String fileUrl = minioUtil.uploadFile(file, objectName);

        long userId = StpUtil.getLoginIdAsLong();
        String mainPath = pathProperties.getMainPath();
        log.error("========= 开始调用解析算法，参数：{}", fileUrl);
        // 调用算法
        algoExecutor.submit(() -> {
            StringBuilder builder = new StringBuilder();
            builder.append(mainPath).append(" ")
                    .append("--user_id").append(" ").append(userId).append(" ")
                    .append("--resume_loc").append(" ")
                    .append("\"").append(fileUrl).append("\"").append(" ")
                    .append("--file_name ").append("\"")
                    .append(filename).append("\"");
            log.info("[parse algo] 调用python命令：{}", builder.toString());
            execPython(builder.toString());
        });
        log.error("========= 调用解析算法结束，参数：{}", fileUrl);
        return Base64Util.encodeUrlSafe(fileUrl);
    }

    @Override
    public Result<ResumeParseVO> getParseInfo(String url) {
        String decodeUrlSafe = Base64Util.decodeUrlSafe(url);
        long userId = StpUtil.getLoginIdAsLong();

        // 查询数据
        List<ResumePO> poList = this.list(Wrappers.<ResumePO>lambdaQuery()
                .eq(ResumePO::getUserId, userId)
                .eq(ResumePO::getFileUrl, decodeUrlSafe));

        if (poList.size() != 1) {
            log.info("查询简历表数据异常，数据条数：{}, 用户id：{}，简历路径：{}", poList.size(), userId, decodeUrlSafe);
            return Result.success(null);
        }

        ResumePO resumePO = poList.get(0);
        if (ObjectUtils.isEmpty(resumePO.getParseStatus())) {
            return Result.success(null);
        }

        // 查询解析状态
        ResumeStatusEnum resumeStatusEnum = ResumeStatusEnum.fromCode(resumePO.getParseStatus());
        if (ObjectUtils.isEmpty(resumeStatusEnum)) {
            return Result.success(null);
        }

        // 返回解析数据
        switch (resumeStatusEnum) {
            case BE_PARSE, PARSING -> {
                return Result.success(null);
            }
            case PARSE_SUCCESS -> {
                ResumeParseVO resumeParseVO = BeanUtil.copyProperties(resumePO, ResumeParseVO.class);
                return Result.success(resumeParseVO);
            }
            case PARSE_FAIL -> {
                return Result.error(500, resumePO.getParseErrorMsg());
            }
            default -> {
                throw new CommonException("解析状态异常");
            }
        }
    }

    @Override
    public Boolean algoRanking(Long id) {
        ResumePO resumePO = this.getById(id);
        if (ObjectUtils.isEmpty(resumePO)) {
            throw new CommonException("数据为空，运行异常");
        }
        String rankingPath = pathProperties.getRankingPath();
        log.info("====== start rank algo");
        execShell(rankingPath, String.valueOf(id));
        return Boolean.TRUE;
    }

    @Override
    @Transactional
    public ResumeInfoVO getResumeInfo(Long id) {
        List<ResumeScorePO> resumeScorePOS = resumeScoreMapper.selectList(Wrappers
                .<ResumeScorePO>lambdaQuery()
                .eq(ResumeScorePO::getResumeId, id)
                .orderByDesc(ResumeScorePO::getCreateTime));
        if (ObjectUtils.isEmpty(resumeScorePOS)) {
            return null;
        }
        ResumeScorePO resumeScorePO = resumeScorePOS.get(0);

        ResumeTagPO resumeTagPO = resumeTagMapper.selectOne(Wrappers.<ResumeTagPO>lambdaQuery()
                .eq(ResumeTagPO::getTagName, resumeScorePO.getAttributeLabel()));

        ResumeInfoVO resumeInfoVO = BeanUtil.copyProperties(resumeScorePO, ResumeInfoVO.class);
        if (ObjectUtils.isNotEmpty(resumeTagPO)) {
            resumeInfoVO.setImageUrl(minioProperties.getEndpoint() + SLASH + resumeTagPO.getImageUrl());
            resumeInfoVO.setModelUrl(minioProperties.getEndpoint() + SLASH + resumeTagPO.getModelUrl());
        }

        // 总分数写入redis中
        Double totalScore = resumeScorePO.getTotalScore().doubleValue();
        // 使用用户id做排名
        String loginIdAsString = StpUtil.getLoginIdAsString();
        rankToRedis(RANK_TOTAL, loginIdAsString, totalScore);
        // 根据学历写入对应榜单
        RankingTypeEnum rankingTypeEnum = RankingTypeEnum.fromCode(resumeScorePO.getEduLevel());
        if (ObjectUtils.isNotEmpty(rankingTypeEnum)) {
            String zsetName = rankingRedisName(rankingTypeEnum);
            rankToRedis(zsetName, loginIdAsString, totalScore);
        }
        return resumeInfoVO;
    }

    @Override
    public RankingInfoVO rankingInfo(RankingTypeEnum type) {
        // 查询前20名用户
        Integer rankingNum = resumeProperties.getRankingNum();
        String rankType = rankingRedisName(type);
        Map<Long, Double> topInfo = getIdByTopN(rankType, rankingNum);
        if (ObjectUtils.isEmpty(topInfo)) {
            return null;
        }
        List<Long> userIds = new ArrayList<>(topInfo.keySet());

        // 通过top榜查询简历表
        List<ResumePO> resumePOS = this.list(Wrappers.<ResumePO>lambdaQuery()
                .in(ResumePO::getUserId, userIds)
                .eq(ResumePO::getDeleted, 0));

        Map<Long, List<ResumePO>> resumeRank = resumePOS.stream().collect(Collectors.groupingBy(ResumePO::getUserId));

        // 查询top榜对应的用户信息
        List<SysUserPO> userPOS = userMapper.selectByIds(userIds);
        Map<Long, SysUserPO> userRank = userPOS.stream().collect(Collectors.toMap(SysUserPO::getId, s -> s));

        RankingInfoVO vo = new RankingInfoVO();
        List<RankingUserInfoVO> userInfoVOS = new ArrayList<>();
        int rank = 1;
        for (Long userId : userIds) {
            List<ResumePO> resumeList = resumeRank.get(userId);
            ResumePO newResume = null;
            if (resumeList.size() == 1) {
                newResume = resumeList.get(0);
            } else {
                newResume = resumeList.stream()
                        .sorted(Comparator.comparing(ResumePO::getCreateTime).reversed())
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("排行榜数据异常"));
            }
            SysUserPO newUser = userRank.get(userId);

            RankingUserInfoVO build = RankingUserInfoVO.builder()
                    .userId(newUser.getId())
                    .username(newUser.getUsername())
                    .avatar(newUser.getAvatar())
                    .rank(String.valueOf(rank))
                    .resumeId(newResume.getId())
                    .score(topInfo.get(userId))
                    .build();
            userInfoVOS.add(build);
            rank++;
        }

        vo.setTotalInfo(userInfoVOS);

        // 查询当前用户的排名和分数
        Long rankingById = getRankingById(rankType, StpUtil.getLoginIdAsString());
        Double rankingScore = getRankingScore(rankType, StpUtil.getLoginIdAsString());
        List<ResumePO> list = this.list(Wrappers.<ResumePO>lambdaQuery()
                .eq(ResumePO::getUserId, rankingById)
                .orderByDesc(ResumePO::getCreateTime));
        SysUserPO sysUserPO = userMapper.selectById(StpUtil.getLoginIdAsLong());
        RankingUserInfoVO currentUserInfo = RankingUserInfoVO.builder()
                .userId(sysUserPO.getId())
                .username(sysUserPO.getUsername())
                .avatar(sysUserPO.getAvatar())
                .rank(rankingById > 99 ? "99+" : String.valueOf(rankingById + 1))
                .resumeId(list.get(0).getId())
                .score(rankingScore).build();

        vo.setCurrentUserInfo(currentUserInfo);
        return vo;
    }

    @Override
    public Boolean suggest(Long resumeId) {
        ResumePO resumePO = this.getById(resumeId);
        if (ObjectUtils.isEmpty(resumePO)) {
            throw new CommonException("数据为空，运行异常");
        }

        String suggestPath = pathProperties.getSuggestPath();
        execShell(suggestPath, String.valueOf(resumeId));

        return Boolean.TRUE;
    }

    @Override
    public String getSuggestInfo(Long resumeId) {
        List<ResumeSuggestPO> resumeSuggestPOS = resumeSuggestMapper.selectList(
                Wrappers.<ResumeSuggestPO>lambdaQuery()
                        .eq(ResumeSuggestPO::getResumeId, resumeId)
                        .orderByDesc(ResumeSuggestPO::getCreateTime));
        if (ObjectUtils.isNotEmpty(resumeSuggestPOS)) {
            return resumeSuggestPOS.get(0).getSuggestContent();
        }
        return null;
    }

    @Override
    public IPage<ResumeHistoryVO> history(Integer current, Integer size) {

        Page<ResumePO> page = new Page<>(current, size);
        long userId = StpUtil.getLoginIdAsLong();
        LambdaQueryWrapper<ResumePO> wrapper = Wrappers.<ResumePO>lambdaQuery()
                .eq(ResumePO::getUserId, userId)
                .orderByDesc(ResumePO::getCreateTime);

        Page<ResumePO> resumePOPage = this.baseMapper.selectPage(page, wrapper);
        List<ResumePO> records = resumePOPage.getRecords();

        List<Long> resumeIds = records.stream().map(ResumePO::getId).toList();
        List<ResumeScorePO> resumeScorePOS = resumeScoreMapper.selectList(
                Wrappers.<ResumeScorePO>lambdaQuery()
                        .in(ResumeScorePO::getResumeId, resumeIds));
        Map<Long, List<ResumeScorePO>> scoreList = resumeScorePOS.stream().collect(Collectors.groupingBy(ResumeScorePO::getResumeId));

        List<ResumeHistoryVO> voList = new ArrayList<>();
        for (ResumePO record : records) {
            List<ResumeScorePO> scoreWithResume = scoreList.get(record.getId());
            ResumeScorePO scorePO = scoreWithResume.stream()
                    .max(Comparator.comparing(ResumeScorePO::getCreateTime))
                    .orElseThrow(() -> new RuntimeException("历史数据异常"));

            ResumeHistoryVO historyVO = ResumeHistoryVO.builder()
                    .id(record.getId())
                    .fileName(record.getFileName())
                    .fileUrl(record.getFileUrl())
                    .totalScore(scorePO.getTotalScore())
                    .attributeLabel(scorePO.getAttributeLabel()).build();
            voList.add(historyVO);
        }

        IPage<ResumeHistoryVO> pageData = new Page<>();
        pageData.setSize(resumePOPage.getSize());
        pageData.setCurrent(resumePOPage.getCurrent());
        pageData.setTotal(resumePOPage.getTotal());
        pageData.setRecords(voList);
        return pageData;
    }

    private void execShell(String path, String resumeId) {
        log.info("******* 异步执行标签算法");
        algoExecutor.submit(() -> {
            StringBuilder builder = new StringBuilder();
            builder.append(path).append(" ")
                    .append("--resume_id").append(" ").append(resumeId);
            log.info("调用python命令：{}", builder.toString());
            execPython(builder.toString());
        });
    }

    private String rankingRedisName(RankingTypeEnum rankingTypeEnum) {
        String zsetName = null;
        switch (rankingTypeEnum) {
            case TOTAL -> zsetName = RANK_TOTAL;
            case TIAN -> zsetName = RANK_TIAN;
            case DI -> zsetName = RANK_DI;
            case XUAN -> zsetName = RANK_XUAN;
            case HUANG -> zsetName = RANK_HUANG;
            default -> zsetName = RANK_OTHER;
        }
        return zsetName;
    }

    private Double getRankingScore(String key, String id) {
        return stringRedisTemplate.opsForZSet().score(key, id);
    }

    private Long getRankingById(String key, String id) {
        return stringRedisTemplate.opsForZSet().reverseRank(key, id);
    }

    private Map<Long, Double> getIdByTopN(String key, int n) {
        Set<ZSetOperations.TypedTuple<String>> typedTuples = stringRedisTemplate.opsForZSet().reverseRangeWithScores(key, 0, n - 1);

        Map<Long, Double> topInfo = new LinkedHashMap<>();
        for (ZSetOperations.TypedTuple<String> tuple : typedTuples) {
            String value = tuple.getValue();
            Double score = tuple.getScore();
            topInfo.put(Long.valueOf(value), score);
        }
        return topInfo;
    }

    private void rankToRedis(String key, String resumeId, Double score) {
        stringRedisTemplate.opsForZSet().add(key, resumeId, score);
    }

    private void execPython(String command) {
        try {
            ProcessBuilder pb = new ProcessBuilder("sh", "-c", command);
            pb.redirectOutput(ProcessBuilder.Redirect.DISCARD);
            pb.redirectError(ProcessBuilder.Redirect.DISCARD);
            pb.start();
        } catch (Exception e) {
            log.error("执行python算法命令报错，错误原因：{}", e.getMessage());
        }
    }
}
