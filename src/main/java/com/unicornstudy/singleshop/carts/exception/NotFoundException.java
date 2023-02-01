package com.unicornstudy.singleshop.carts.exception;

public class NotFoundException extends RuntimeException {

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

}
