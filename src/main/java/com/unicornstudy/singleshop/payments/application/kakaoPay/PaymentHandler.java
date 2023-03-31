package com.unicornstudy.singleshop.payments.application.kakaoPay;

import com.unicornstudy.singleshop.payments.application.PaymentInfo;

public class PaymentHandler {

    public static String getPaymentUrl(String payment) {
        return PaymentInfo.valueOf(payment.toUpperCase()).getUrl();
    }
}
