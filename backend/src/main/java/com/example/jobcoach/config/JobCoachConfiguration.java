package com.example.jobcoach.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AiProperties.class)
public class JobCoachConfiguration {
    @Bean
    ObjectMapper legacyObjectMapper() {
        return new ObjectMapper();
    }
}
