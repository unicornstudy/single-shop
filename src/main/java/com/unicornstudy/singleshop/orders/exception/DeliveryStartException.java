package com.unicornstudy.singleshop.orders.exception;

public class DeliveryStartException extends OrderException {
    public static final String ERROR_MESSAGE = "이미 배송된 상품입니다.";

    @Override
    public String getMessage() {
        return ERROR_MESSAGE;
    }
}
