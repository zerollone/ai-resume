package com.ai.resume.controller.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author ws
 * @date 2026/4/20 21:40
 */
@Data
public class RankingInfoVO {

    @Schema(description = "榜单排名信息")
    private List<RankingUserInfoVO> totalInfo;

    @Schema(description = "当前用户排名信息")
    private RankingUserInfoVO currentUserInfo;
}
