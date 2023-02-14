package com.unicornstudy.singleshop.carts.exception;

public class CartExcoption extends RuntimeException {

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

}
