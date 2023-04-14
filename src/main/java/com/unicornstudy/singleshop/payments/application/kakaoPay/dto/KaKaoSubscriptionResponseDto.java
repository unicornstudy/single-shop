package com.unicornstudy.singleshop.payments.application.kakaoPay.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KaKaoSubscriptionResponseDto {
    private String partnerOrderId;
    private String partnerUserId;
    private String paymentMethodType;
    private String itemName;
    private String itemCode;
    private String quantity;
    private String code;
}
