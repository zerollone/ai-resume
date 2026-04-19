package com.ai.resume.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author ws
 * @date 2026/4/18 16:36
 */
@Data
@Component
@ConfigurationProperties("algo.thread")
public class AlgorithmThreadProperties {

    private Integer corePoolSize;
    private Integer maxPoolSize;
    private Integer queueCapacity;
    private Integer keepAliveSeconds;
    private String threadNamePrefix;
    private Boolean completeShutdown;
    private Integer awaitTerminationSeconds;
}
