package com.unicornstudy.singleshop.exception.user;

public class UserException extends RuntimeException {
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
