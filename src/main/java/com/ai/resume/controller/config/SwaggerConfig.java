package com.ai.resume.controller.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ws
 * @date 2026/4/18 11:46
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI api(){
        return new OpenAPI()
                .info(new Info()
                        .title("AI简历接口文档")
                        .version("1.0.0")
                        .description("AI简历")
                        .contact(new Contact()
                                .name("ws")
                                .email("xxx@xxx.com")
                                .url("http://101.35.183.75")));
    }
}
