package com.unicornstudy.singleshop.exception.subscription;

public class SubscriptionException extends RuntimeException {
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
