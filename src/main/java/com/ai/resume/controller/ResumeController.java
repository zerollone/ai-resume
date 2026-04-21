package com.ai.resume.controller;


import com.ai.resume.controller.vo.RankingInfoVO;
import com.ai.resume.controller.vo.ResumeHistoryVO;
import com.ai.resume.controller.vo.ResumeInfoVO;
import com.ai.resume.controller.vo.ResumeParseVO;
import com.ai.resume.enums.RankingTypeEnum;
import com.ai.resume.result.Result;
import com.ai.resume.service.ResumeService;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
    public Result<ResumeInfoVO> getResumeInfo(@PathVariable Long id) {
        return Result.success(resumeService.getResumeInfo(id));
    }

    @GetMapping("/ranking/info/{type}")
    @Operation(summary = "查询榜单信息，并且包含当前登录用户的排名信息")
    public Result<RankingInfoVO> rankingInfo(@PathVariable RankingTypeEnum type) {
        return Result.success(resumeService.rankingInfo(type));
    }

    @GetMapping("/suggest/{resumeId}")
    @Operation(summary = "简历建议")
    public Result<Boolean> suggest(@PathVariable Long resumeId) {
        return Result.success(resumeService.suggest(resumeId));
    }

    @GetMapping("/get/suggest/info/{resumeId}")
    @Operation(summary = "查询简历建议数据")
    public Result<String> getSuggestInfo(@PathVariable Long resumeId) {
        return Result.success(resumeService.getSuggestInfo(resumeId));
    }

    @GetMapping("/history/{current}/{size}")
    @Operation(summary = "查看当前用户简历历史数据")
    public Result<IPage<ResumeHistoryVO>> history(@PathVariable Integer current, @PathVariable Integer size) {
        return Result.success(resumeService.history(current, size));
    }
}

