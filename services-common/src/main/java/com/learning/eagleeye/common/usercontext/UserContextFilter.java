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
        UserContextHolder.getUserContext().setAuthToken(servletRequest.getHeader(UserContext.AUTH_TOKEN));

        chain.doFilter(servletRequest, response);
    }
}
