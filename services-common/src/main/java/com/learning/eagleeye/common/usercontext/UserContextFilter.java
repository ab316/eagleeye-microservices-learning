package com.learning.eagleeye.common.usercontext;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
@Slf4j
public class UserContextFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        UserContextHolder.getUserContext().setCorrelationId(servletRequest.getHeader(UserContext.CORRELATION_ID));

        log.debug("Incoming Correlation id: {}", UserContextHolder.getUserContext().getCorrelationId());

        chain.doFilter(servletRequest, response);
    }
}
