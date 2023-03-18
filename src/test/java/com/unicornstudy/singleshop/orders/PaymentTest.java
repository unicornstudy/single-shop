package com.unicornstudy.singleshop.orders;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PaymentTest {

    private final String tid = "t1234";
    private final String paymentKind = "testKind";
    private final String sid = "s1234";

    @Test
    void 생성자_테스트() {
        Payment payment = Payment.builder()
                .tid(tid)
                .paymentKind(paymentKind)
                .sid(sid)
                .build();

        assertThat(payment.getTid()).isEqualTo(tid);
        assertThat(payment.getPaymentKind()).isEqualTo(paymentKind);
        assertThat(payment.getSid()).isEqualTo(sid);
    }
}