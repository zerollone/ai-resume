package com.ai.resume.controller.vo;

import lombok.Data;

import java.util.List;

/**
 * @author ws
 * @date 2026/4/20 21:40
 */
@Data
public class RankingInfoVO {

    private List<RankingUserInfoVO> totalInfo;
    private RankingUserInfoVO currentUserInfo;
}
