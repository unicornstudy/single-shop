package com.unicornstudy.singleshop.carts.exception;

public class SessionExpiredException extends CartException {

    public static final String ERROR_MESSAGE = "존재하지 않는 회원이거나, 세션이 만료되었습니다.";

    @Override
    public String getMessage() {
        return ERROR_MESSAGE;
    }
}
