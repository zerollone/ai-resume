package com.ai.resume.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author ws
 * @date 2026/4/18 17:17
 */
@Data
@Component
@ConfigurationProperties("algo.path")
public class AlgorithmPathProperties {

    private String mainPath;
    private String rankingPath;
}
