package com.ai.resume;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author ws
 * @date 2026/4/16 20:54
 */
@SpringBootApplication
@MapperScan("com.ai.resume.mapper")
public class AiResumeApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiResumeApplication.class, args);
    }
}
