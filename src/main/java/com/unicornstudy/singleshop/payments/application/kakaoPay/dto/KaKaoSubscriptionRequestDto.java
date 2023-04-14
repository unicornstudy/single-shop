package com.unicornstudy.singleshop.payments.application.kakaoPay.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KaKaoSubscriptionRequestDto {
    private final String cid;
    private final String sid;
    private final String partnerOrderId;
    private final String partnerUserId;
    private final String quantity;
    private final String totalAmount;
    private final String taxFreeAmount;

    @Builder
    public KaKaoSubscriptionRequestDto(String cid, String sid, String partnerOrderId, String partnerUserId, String quantity, String totalAmount, String taxFreeAmount) {
        this.cid = cid;
        this.sid = sid;
        this.partnerOrderId = partnerOrderId;
        this.partnerUserId = partnerUserId;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
        this.taxFreeAmount = taxFreeAmount;
    }

    public static KaKaoSubscriptionRequestDto of(String cid, String sid, String partnerOrderId, String partnerUserId, String quantity, String totalAmount, String taxFreeAmount) {
        return KaKaoSubscriptionRequestDto.builder()
                .cid(cid)
                .sid(sid)
                .partnerOrderId(partnerOrderId)
                .partnerUserId(partnerUserId)
                .quantity(quantity)
                .totalAmount(totalAmount)
                .taxFreeAmount(taxFreeAmount)
                .build();
    }
}
