package com.unicornstudy.singleshop.exception.subscription;

public class ReSubscriptionException extends SubscriptionException {

    public static final String ERROR_MESSAGE = "이번 달 구독 취소자가 아닙니다.";

    @Override
    public String getMessage() {
        return ERROR_MESSAGE;
    }
}
