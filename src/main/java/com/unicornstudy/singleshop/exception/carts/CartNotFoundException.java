package com.unicornstudy.singleshop.exception.carts;

import com.unicornstudy.singleshop.exception.carts.CartException;

public class CartNotFoundException extends CartException {

    public static final String ERROR_MESSAGE = "해당 회원의 장바구니 정보가 없습니다.";

    @Override
    public String getMessage() {
        return ERROR_MESSAGE;
    }
}
