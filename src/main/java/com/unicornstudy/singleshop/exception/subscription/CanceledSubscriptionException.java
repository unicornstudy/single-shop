package com.unicornstudy.singleshop.exception.subscription;

public class CanceledSubscriptionException extends SubscriptionException {

    public static final String ERROR_MESSAGE = "구독한 사용자가 아닙니다.";

    @Override
    public String getMessage() {
        return ERROR_MESSAGE;
    }
}
