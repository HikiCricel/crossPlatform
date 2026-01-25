package com.example.crossPlatform.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(new Info().title("Student Time Management API")
                .description(
                        "REST API for time managment. " + "This API allows students track their work and deadlines")
                .version("1.0.0"));
    }
}
