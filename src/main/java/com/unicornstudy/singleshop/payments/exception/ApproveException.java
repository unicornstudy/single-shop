package com.unicornstudy.singleshop.payments.exception;

import lombok.Getter;

@Getter
public class ApproveException extends RuntimeException {

    private String ERROR_MESSAGE = "주문 결제 승인 오류로 인해 주문이 취소 되었습니다. 주문 목록으로 돌아가 재주문 해주세요.";

    private Long orderId;

    public ApproveException(Long orderId, String message) {
        this.orderId = orderId;
        this.ERROR_MESSAGE += message;
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
