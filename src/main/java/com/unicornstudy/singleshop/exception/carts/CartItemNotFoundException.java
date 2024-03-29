package com.unicornstudy.singleshop.exception.carts;

import com.unicornstudy.singleshop.exception.carts.CartException;

public class CartItemNotFoundException extends CartException {

    public static final String ERROR_MESSAGE = "장바구니 상품이 존재하지 않습니다.";

    @Override
    public String getMessage() {
        return ERROR_MESSAGE;
    }
}
