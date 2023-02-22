package com.unicornstudy.singleshop.orders.exception;

public class OrderException extends RuntimeException {

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
