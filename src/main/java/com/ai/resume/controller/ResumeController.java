package com.ai.resume.controller;


import com.ai.resume.controller.vo.ResumeParseVO;
import com.ai.resume.result.Result;
import com.ai.resume.service.ResumeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @Operation(summary = "简历上传")
    public Result<String> upload(MultipartFile file) {
        return Result.success(resumeService.upload(file));
    }

    @GetMapping("/get/parse/info/{url}")
    @Operation(summary = "查询简历解析数据")
    public Result<ResumeParseVO> getParseInfo(@PathVariable String url) {
        return resumeService.getParseInfo(url);
    }

    @GetMapping("/algo/ranking/{id}")
    @Operation(summary = "简历标签")
    public Result<Boolean> algoRanking(@PathVariable Long id) {
        return Result.success(resumeService.algoRanking(id));
    }

    @GetMapping("/get/resume/info/{id}")
    @Operation(summary = "获取简历分数，标签等信息")
    public Result<Boolean> getResumeInfo(@PathVariable Long id) {
        return Result.success(resumeService.getResumeInfo(id));
    }
}

