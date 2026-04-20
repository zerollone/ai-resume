package com.ai.resume.service;


import com.ai.resume.controller.vo.RankingInfoVO;
import com.ai.resume.controller.vo.ResumeInfoVO;
import com.ai.resume.controller.vo.ResumeParseVO;
import com.ai.resume.entity.ResumePO;
import com.ai.resume.enums.RankingTypeEnum;
import com.ai.resume.result.Result;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 简历表 服务类
 * </p>
 *
 * @author ws
 * @since 2026-04-18
 */
public interface ResumeService extends IService<ResumePO> {

    String upload(MultipartFile file);

    Result<ResumeParseVO> getParseInfo(String url);

    Boolean algoRanking(Long id);

    ResumeInfoVO getResumeInfo(Long id);

    RankingInfoVO rankingInfo(RankingTypeEnum type);

    Boolean suggest(Long resumeId);

    String getSuggestInfo(Long resumeId);
}
