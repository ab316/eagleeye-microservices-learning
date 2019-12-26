package com.learning.eagleeye.common.hystrix;

import com.learning.eagleeye.common.usercontext.UserContext;
import com.learning.eagleeye.common.usercontext.UserContextHolder;

import java.util.concurrent.Callable;

public class DelegatingUserContextCallable<V> implements Callable<V> {

    private final Callable<V> delegate;
    private UserContext originalUserContext;

    public DelegatingUserContextCallable(Callable<V> callable, UserContext userContext) {
        this.delegate = callable;
        originalUserContext = userContext;
    }

    @Override
    public V call() throws Exception {
        UserContextHolder.setUserContext(originalUserContext);
        try {
            return delegate.call();
        } finally {
            this.originalUserContext = null;
        }
    }
}
