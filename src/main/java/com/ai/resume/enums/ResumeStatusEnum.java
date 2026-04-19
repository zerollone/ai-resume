package com.ai.resume.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResumeStatusEnum {

    BE_PARSE("待解析", 0),
    PARSING("解析中", 1),
    PARSE_SUCCESS("解析成功", 2),
    PARSE_FAIL("解析失败", 3)
    ;

    private final String desc;

    private final Integer code;

    public static ResumeStatusEnum fromCode(int code) {
        for (ResumeStatusEnum status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        return null;
    }
}
