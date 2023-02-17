package com.unicornstudy.singleshop.carts.exception;

public class CartException extends RuntimeException {

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

}
