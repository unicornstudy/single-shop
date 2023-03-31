package com.unicornstudy.singleshop.exception.subscription;

public class AlreadySubscriptionException extends SubscriptionException {

    public static final String ERROR_MESSAGE = "이미 구독한 사용자 입니다.";

    @Override
    public String getMessage() {
        return ERROR_MESSAGE;
    }
}
