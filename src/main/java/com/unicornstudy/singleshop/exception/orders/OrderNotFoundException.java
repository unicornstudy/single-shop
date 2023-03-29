package com.unicornstudy.singleshop.exception.orders;

import com.unicornstudy.singleshop.exception.orders.OrderException;

public class OrderNotFoundException extends OrderException {

    public static final String ERROR_MESSAGE = "주문 정보가 없습니다.";

    @Override
    public String getMessage() {
        return ERROR_MESSAGE;
    }
}
