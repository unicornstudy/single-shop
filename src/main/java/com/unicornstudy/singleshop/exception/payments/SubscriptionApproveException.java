package com.unicornstudy.singleshop.exception.payments;

import lombok.Getter;

@Getter
public class SubscriptionApproveException extends PaymentsException {

    public static final String ERROR_MESSAGE = "구독 결제 승인 오류";

    @Override
    public String getMessage() {
        return ERROR_MESSAGE;
    }
}
