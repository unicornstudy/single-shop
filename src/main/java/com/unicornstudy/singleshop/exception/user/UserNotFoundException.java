package com.unicornstudy.singleshop.exception.user;

public class UserNotFoundException extends UserException {

    public static final String ERROR_MESSAGE = "해당 회원을 찾을 수 없습니다.";

    @Override
    public String getMessage() {
        return ERROR_MESSAGE;
    }
}
