package com.learning.eagleeye.zuul.filters;

import brave.Tracer;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ResponseFilter extends ZuulFilter {
    private static final int FILTER_ORDER = 1;
    private static final boolean SHOULD_FILTER = true;
    private final FilterUtils filterUtils;

    Tracer tracer;

    @Autowired
    public ResponseFilter(FilterUtils filterUtils, Tracer tracer) {
        this.filterUtils = filterUtils;
        this.tracer = tracer;
    }

    @Override
    public String filterType() {
        return FilterUtils.POST_FILTER_TYPE;
    }

    @Override
    public int filterOrder() {
        return FILTER_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        return SHOULD_FILTER;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();

        ctx.getResponse().addHeader(FilterUtils.CORRELATION_ID, tracer.currentSpan().context().traceIdString());

        log.debug("Completing outgoing request for {}", ctx.getRequest().getRequestURI());

        return null;
    }
}
