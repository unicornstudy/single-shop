package com.unicornstudy.singleshop.payments.application;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum PaymentInfo {
    KAKAO("/api/payments/kakaopay");

    private final String url;
}
