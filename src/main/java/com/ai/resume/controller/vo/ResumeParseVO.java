package com.ai.resume.controller.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author ws
 * @date 2026/4/19 17:11
 */
@Data
public class ResumeParseVO {

    private Long id;

    // 文件名称
    private String fileName;

    // 文件地址
    private String fileUrl;

    // 个人描述
    private String description;

    // 姓名
    private String fullName;

    // 性别：0未知 1男 2女
    private Integer gender;

    // 出生日期
    private LocalDate birthday;

    // 手机号
    private String phone;

    // 邮箱
    private String email;

    // 国籍
    private String nationality;

    // 所在城市
    private String location;

    // 简历内容
    private String summary;

    // 总工作年限（年）
    private BigDecimal totalWorkYears;

    // 期望薪资（如：20-30k·15薪）
    private String expectedSalary;

    // 求职状态：0在职考虑 1离职 2应届等
    private Integer jobStatus;

}
