package com.unicornstudy.singleshop.payments;

import org.springframework.http.HttpHeaders;

public interface PaymentService {
    HttpHeaders initializePaymentsHeaders();
    String getPaymentPath();
}