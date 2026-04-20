package com.ai.resume.controller.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author ws
 * @date 2026/4/20 21:26
 */
@Data
@Builder
@AllArgsConstructor
public class RankingUserInfoVO {

    private Long userId;
    private Long resumeId;
    private String avatar;
    private String username;
    private String rank;
    private Double score;

}
