package com.unicornstudy.singleshop.exception.payments;

public class InvalidPaymentException extends PaymentsException {

    public static final String ERROR_MESSAGE = "존재하지 않는 결제 수단 입니다.";

    @Override
    public String getMessage() {
        return ERROR_MESSAGE;
    }
}
