package com.learning.eagleeye.zuul.filters;

import org.springframework.stereotype.Component;

@Component
public class FilterUtils {

    public static final String PRE_FILTER_TYPE = "pre";
    public static final String POST_FILTER_TYPE = "post";
    public static final String ROUTE_FILTER_TYPE = "route";

    public static final String CORRELATION_ID = "tmx-correlation-id";
}
