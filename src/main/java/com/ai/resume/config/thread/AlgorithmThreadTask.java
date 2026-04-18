package com.ai.resume.config.thread;

import com.ai.resume.config.properties.AlgorithmThreadProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author ws
 * @date 2026/4/18 16:36
 */
@Configuration
public class AlgorithmThreadTask {

    @Autowired
    private AlgorithmThreadProperties threadProperties;

    @Bean("algoExecutor")
    @Qualifier("algoExecutor")
    public ThreadPoolTaskExecutor algoExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(threadProperties.getCorePoolSize());
        executor.setMaxPoolSize(threadProperties.getMaxPoolSize());
        executor.setQueueCapacity(threadProperties.getQueueCapacity());
        executor.setKeepAliveSeconds(threadProperties.getKeepAliveSeconds());
        executor.setThreadNamePrefix(threadProperties.getThreadNamePrefix());
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(threadProperties.getCompleteShutdown());
        executor.setAwaitTerminationSeconds(threadProperties.getAwaitTerminationSeconds());
        executor.initialize();
        return executor;
    }

}
