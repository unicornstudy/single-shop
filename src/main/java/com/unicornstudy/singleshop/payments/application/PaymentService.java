package com.unicornstudy.singleshop.payments.application;

import org.springframework.http.HttpHeaders;

public interface PaymentService {
    HttpHeaders initializePaymentsHeaders();
    String getPaymentPath();
}