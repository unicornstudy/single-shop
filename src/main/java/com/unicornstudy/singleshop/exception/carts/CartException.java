package com.unicornstudy.singleshop.exception.carts;

public class CartException extends RuntimeException {

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

}
