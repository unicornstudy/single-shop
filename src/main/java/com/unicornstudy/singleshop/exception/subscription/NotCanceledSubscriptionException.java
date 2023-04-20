package com.unicornstudy.singleshop.exception.subscription;

public class NotCanceledSubscriptionException extends SubscriptionException {

    public static final String ERROR_MESSAGE = "구독 취소 예정자가 아닙니다.";

    @Override
    public String getMessage() {
        return ERROR_MESSAGE;
    }
}
