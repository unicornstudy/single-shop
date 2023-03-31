package com.unicornstudy.singleshop.exception.subscription;

import com.unicornstudy.singleshop.exception.payments.PaymentsException;
import lombok.Getter;

@Getter
public class RegularPaymentException extends PaymentsException {

    public static final String ERROR_MESSAGE = "정기 결제 오류";

    private String userEmail;

    public RegularPaymentException(String userEmail) {
        this.userEmail = userEmail;
    }

    @Override
    public String getMessage() {
        return ERROR_MESSAGE;
    }
}
