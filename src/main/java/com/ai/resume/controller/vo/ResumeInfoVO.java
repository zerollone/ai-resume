package com.ai.resume.controller.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author ws
 * @date 2026/4/19 19:37
 */
@Data
public class ResumeInfoVO {

    private Long id;

    @Schema(description = "简历ID")
    private Long resumeId;

    @Schema(description = "教育得分")
    private BigDecimal eduScore;

    @Schema(description = "实习得分")
    private BigDecimal internScore;

    @Schema(description = "工作经历得分")
    private BigDecimal jobScore;

    @Schema(description = "项目经历得分")
    private BigDecimal projectScore;

    @Schema(description = "总得分")
    private BigDecimal totalScore;

    @Schema(description = "属性标签(如:大厂牛马型)")
    private String attributeLabel;

    @Schema(description = "学历类型(本科、研究生等)")
    private String eduLevel;

    @Schema(description = "是否应届(1是, 0否)")
    private Boolean isFreshGraduate;

    @Schema(description = "学校类型(985/211/普通本科/大专等")
    private String schoolType;

    @Schema(description = "实习工作时长(月)")
    private Integer internDuration;

    @Schema(description = "实习次数")
    private Integer internCount;

    @Schema(description = "正式工作时长(月)")
    private Integer jobDuration;

    @Schema(description = "正式工作次数")
    private Integer jobCount;

    @Schema(description = "最高公司类型(大厂/中厂/小厂等)")
    private String companyType;

    @Schema(description = "2D Q版头像 MinIO 地址")
    private String imageUrl;

    @Schema(description = "3D 模型 MinIO 地址")
    private String modelUrl;
}
