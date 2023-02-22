package com.unicornstudy.singleshop.orders.exception;

public class OrderStatusException extends OrderException {
    public static final String ERROR_MESSAGE = "이미 취소한 주문입니다.";

    @Override
    public String getMessage() {
        return ERROR_MESSAGE;
    }
}
