package com.unicornstudy.singleshop.payments.exception;

import lombok.Getter;

@Getter
public class ApproveException extends RuntimeException {

    public static final String ERROR_MESSAGE = "주문 결제 승인 오류";

    private Long orderId;

    public ApproveException(Long orderId) {
        this.orderId = orderId;
    }

    @Override
    public String getMessage() {
        return ERROR_MESSAGE;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
