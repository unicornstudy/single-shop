package com.unicornstudy.singleshop.carts.exception;

public class SessionExpiredException extends NotFoundException {

    public static final String ERROR_MESSAGE = "세션이 비어있습니다.";

    @Override
    public String getMessage() {
        return ERROR_MESSAGE;
    }
}
