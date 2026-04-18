package com.ai.resume.controller;


import com.ai.resume.result.Result;
import com.ai.resume.service.ResumeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 简历表 前端控制器
 * </p>
 *
 * @author ws
 * @since 2026-04-18
 */
@RestController
@RequestMapping("/resume")
@Tag(name = "简历处理")
public class ResumeController {

    @Autowired
    private ResumeService resumeService;

    @PostMapping("/upload")
    public Result<Boolean> upload(MultipartFile file) {
        return Result.success(resumeService.upload(file));
    }
}

