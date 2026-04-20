package com.ai.resume.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author ws
 * @date 2026/4/20 20:40
 */
@Data
@Component
@ConfigurationProperties(prefix = "resume")
public class ResumeProperties {

    private Integer rankingNum;
}
