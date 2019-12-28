package com.learning.eagleeye.authentication.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class ServiceConfig {
    @Value("${signing.key}")
    private String jwtSigningKey = "";
}
