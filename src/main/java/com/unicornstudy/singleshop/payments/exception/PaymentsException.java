package com.unicornstudy.singleshop.payments.exception;

public class PaymentsException extends RuntimeException {

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
