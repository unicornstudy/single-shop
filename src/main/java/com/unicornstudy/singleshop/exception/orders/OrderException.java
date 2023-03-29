package com.unicornstudy.singleshop.exception.orders;

public class OrderException extends RuntimeException {

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
