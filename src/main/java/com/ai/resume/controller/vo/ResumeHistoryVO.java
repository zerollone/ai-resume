package com.ai.resume.controller.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author ws
 * @date 2026/4/20 22:37
 */
@Data
@Builder
public class ResumeHistoryVO {

    private Long id;

    @Schema(description = "文件名称")
    private String fileName;

    @Schema(description = "文件地址")
    private String fileUrl;

    @Schema(description = "总得分")
    private BigDecimal totalScore;

    @Schema(description = "属性标签(如:大厂牛马型)")
    private String attributeLabel;

}
