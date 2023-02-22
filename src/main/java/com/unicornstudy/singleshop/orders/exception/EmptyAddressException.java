package com.unicornstudy.singleshop.orders.exception;

public class EmptyAddressException extends OrderException {
    public static final String ERROR_MESSAGE = "주소가 비어있습니다.";

    @Override
    public String getMessage() {
        return ERROR_MESSAGE;
    }
}