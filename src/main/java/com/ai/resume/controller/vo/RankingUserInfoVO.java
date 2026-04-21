package com.ai.resume.controller.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author ws
 * @date 2026/4/20 21:26
 */
@Data
@Builder
@AllArgsConstructor
public class RankingUserInfoVO {

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "简历id")
    private Long resumeId;

    @Schema(description = "用户头像")
    private String avatar;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "排名")
    private String rank;

    @Schema(description = "分数")
    private Double score;

}
