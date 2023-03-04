package com.unicornstudy.singleshop.payments;

import com.unicornstudy.singleshop.payments.exception.InvalidPaymentException;
import com.unicornstudy.singleshop.payments.kakaoPay.KaKaoPaymentService;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class PaymentServiceFactory {

    private static final Map<String, Supplier<PaymentService>> PAYMENT_SERVICE_SUPPLIERS = new HashMap<>();

    static {
        PAYMENT_SERVICE_SUPPLIERS.put("kakao", KaKaoPaymentService::new);
    }

    public static PaymentService getService(String apiName) {
        Supplier<PaymentService> supplier = PAYMENT_SERVICE_SUPPLIERS.get(apiName.toLowerCase());
        if (supplier == null) {
            throw new InvalidPaymentException();
        }
        return supplier.get();
    }
}