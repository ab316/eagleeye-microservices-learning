package com.learning.eagleeye.common.usercontext;

import lombok.Data;

@Data
public class UserContext {
    public static final String AUTH_TOKEN = "Authorization";
    private String authToken;
}
