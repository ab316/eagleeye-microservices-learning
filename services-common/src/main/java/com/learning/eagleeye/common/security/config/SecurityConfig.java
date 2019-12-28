package com.learning.eagleeye.common.security.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class SecurityConfig {
    @Value("${signing.key}")
    private String jwtSigningKey = "";
}
