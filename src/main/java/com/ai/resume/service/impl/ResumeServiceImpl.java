package com.ai.resume.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import com.ai.resume.controller.config.properties.AlgorithmPathProperties;
import com.ai.resume.controller.vo.ResumeParseVO;
import com.ai.resume.entity.ResumePO;
import com.ai.resume.enums.ResumeStatusEnum;
import com.ai.resume.exception.CommonException;
import com.ai.resume.mapper.ResumeMapper;
import com.ai.resume.result.Result;
import com.ai.resume.service.ResumeService;
import com.ai.resume.utils.Base64Util;
import com.ai.resume.utils.MinioUtil;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

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

    @Autowired
    private MinioUtil minioUtil;

    @Autowired
    @Qualifier("algoExecutor")
    private ThreadPoolTaskExecutor algoExecutor;

    @Autowired
    private AlgorithmPathProperties pathProperties;

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
        String fileType = filename.substring(filename.lastIndexOf(".") + 1);

        if (!FILE_TYPE.contains(fileType)) {
            throw new CommonException("不支持的文件类型！");
        }

        // minio文件路径
        String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String objectName = datePath + SLASH + UUID.randomUUID() + ".pdf";

        // 上传到minio
        String fileUrl = minioUtil.uploadFile(file, objectName);

        long userId  = StpUtil.getLoginIdAsLong();
        String mainPath = pathProperties.getMainPath();
        // 调用算法
        algoExecutor.submit(() -> {
            StringBuilder builder = new StringBuilder();
            builder.append(mainPath).append(" ")
//                    .append("--resume_id").append(" ").append(po.getId()).append(" ")
                    .append("--user_id").append(" ").append(userId).append(" ")
                    .append("--resume_loc").append(" ")
                    .append("\"").append(fileUrl).append("\"");
            log.info("[parse algo] 调用python命令：{}", builder.toString());
            execPython(builder.toString());
        });

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
            return Result.error(500, "数据异常");
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

        algoExecutor.submit(() -> {
            StringBuilder builder = new StringBuilder();
            builder.append(rankingPath).append(" ")
                    .append("--resume_id").append(" ").append(id);
            log.info("[ranking algo] 调用python命令：{}", builder.toString());
            execPython(builder.toString());
        });

        return Boolean.TRUE;
    }

    @Override
    @Transactional
    public Boolean getResumeInfo(Long id) {

        return null;
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
