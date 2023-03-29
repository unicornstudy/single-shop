package com.unicornstudy.singleshop.payments.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    private String tid;
    private String paymentKind;
    private String sid;
    private String price;
}
