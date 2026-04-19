package com.ai.resume.controller.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author ws
 * @date 2026/4/19 19:37
 */
@Data
public class ResumeInfoVO {

    private Long id;

    // 简历ID
    private Long resumeId;

    // 教育得分
    private BigDecimal eduScore;

    // 实习得分
    private BigDecimal internScore;

    // 工作经历得分
    private BigDecimal jobScore;

    // 项目经历得分
    private BigDecimal projectScore;

    // 总得分
    private BigDecimal totalScore;

    // 属性标签(如:大厂牛马型)
    private String attributeLabel;

    // 学历类型(本科、研究生等)
    private String eduLevel;

    // 是否应届(1是, 0否)
    private Boolean isFreshGraduate;

    // 学校类型(985/211/普通本科/大专等)
    private String schoolType;

    // 实习工作时长(月)
    private Integer internDuration;

    // 实习次数
    private Integer internCount;

    // 正式工作时长(月)
    private Integer jobDuration;

    // 正式工作次数
    private Integer jobCount;

    // 最高公司类型(大厂/中厂/小厂等)
    private String companyType;

    // 2D Q版头像 MinIO 地址
    private String imageUrl;

    // 3D 模型 MinIO 地址
    private String modelUrl;
}
