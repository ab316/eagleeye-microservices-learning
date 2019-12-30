package com.learning.eagleeye.licenses.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ServiceConfig {
    @Value("${redis.host}")
    private String redisHost = "";

    @Value("${redis.port}")
    private String redisPort = "";
}
