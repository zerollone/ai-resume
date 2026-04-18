package com.ai.resume.service;


import com.ai.resume.entity.ResumePO;
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

    Boolean upload(MultipartFile file);
}
