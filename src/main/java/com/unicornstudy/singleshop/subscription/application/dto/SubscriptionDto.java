package com.unicornstudy.singleshop.subscription.application.dto;

import com.unicornstudy.singleshop.payments.domain.Payment;
import com.unicornstudy.singleshop.subscription.domain.Subscription;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class SubscriptionDto {
    private final LocalDate payDate;
    private final Payment payment;

    @Builder
    public SubscriptionDto(LocalDate payDate, Payment payment) {
        this.payDate = payDate;
        this.payment = payment;
    }

    public static SubscriptionDto from(Subscription subscription) {
        return SubscriptionDto.builder()
                .payDate(subscription.getPayDate())
                .payment(subscription.getPayment())
                .build();
    }
}
