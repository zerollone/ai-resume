package com.ai.resume.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ws
 * @date 2026/4/20 20:15
 */
@Getter
@AllArgsConstructor
public enum RankingTypeEnum {

    TOTAL("总榜", "总榜"),
    TIAN("天", "博士"),
    DI("地", "硕士"),
    XUAN("玄","本科"),
    HUANG("黄","专科")
    ;

    private final String type;
    private final String code;

    public static RankingTypeEnum fromCode(String code) {
        for (RankingTypeEnum status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        return null;
    }
}
