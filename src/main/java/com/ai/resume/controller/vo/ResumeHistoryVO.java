package com.ai.resume.controller.vo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author ws
 * @date 2026/4/20 22:37
 */
@Data
@Builder
public class ResumeHistoryVO {

    private Long id;

    // 文件名称
    private String fileName;

    // 文件地址
    private String fileUrl;

    // 总得分
    private BigDecimal totalScore;

    // 属性标签(如:大厂牛马型)
    private String attributeLabel;

}
