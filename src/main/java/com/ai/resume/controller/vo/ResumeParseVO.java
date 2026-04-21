package com.ai.resume.controller.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author ws
 * @date 2026/4/19 17:11
 */
@Data
public class ResumeParseVO {

    private Long id;

    @Schema(description = "文件名称")
    private String fileName;

    @Schema(description = "文件地址")
    private String fileUrl;

    @Schema(description = "个人描述")
    private String description;

    @Schema(description = "姓名")
    private String fullName;

    @Schema(description = "性别：0未知 1男 2女")
    private Integer gender;

    @Schema(description = "出生日期")
    private LocalDate birthday;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "国籍")
    private String nationality;

    @Schema(description = "所在城市")
    private String location;

    @Schema(description = "简历内容")
    private String summary;

    @Schema(description = "总工作年限（年）")
    private BigDecimal totalWorkYears;

    @Schema(description = "期望薪资")
    private String expectedSalary;

    @Schema(description = "求职状态：0在职考虑 1离职 2应届等")
    private Integer jobStatus;

}
