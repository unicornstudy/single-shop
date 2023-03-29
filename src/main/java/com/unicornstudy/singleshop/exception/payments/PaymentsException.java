package com.unicornstudy.singleshop.exception.payments;

public class PaymentsException extends RuntimeException {

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
