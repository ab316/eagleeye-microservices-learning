package com.learning.eagleeye.common.usercontext;

import lombok.Data;

@Data
public class UserContext {
    public static final String CORRELATION_ID = "tmx-correlation-id";
    public static final String AUTH_TOKEN = "Authorization";
    private String correlationId;
    private String authToken;
}
