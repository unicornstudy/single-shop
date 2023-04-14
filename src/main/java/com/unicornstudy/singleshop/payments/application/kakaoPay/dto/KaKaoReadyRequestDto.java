package com.unicornstudy.singleshop.payments.application.kakaoPay.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KaKaoReadyRequestDto {
    private final String cid;
    private final String approvalUrl;
    private final String cancelUrl;
    private final String failUrl;
    private final String partnerOrderId;
    private final String partnerUserId;
    private final String itemName;
    private final String quantity;
    private final String totalAmount;
    private final String taxFreeAmount;

    @Builder
    public KaKaoReadyRequestDto(String cid, String approvalUrl, String cancelUrl, String failUrl, String partnerOrderId
            , String partnerUserId, String itemName, String quantity, String totalAmount, String taxFreeAmount) {
        this.cid = cid;
        this.approvalUrl = approvalUrl;
        this.cancelUrl = cancelUrl;
        this.failUrl = failUrl;
        this.partnerOrderId = partnerOrderId;
        this.partnerUserId = partnerUserId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
        this.taxFreeAmount = taxFreeAmount;
    }

    public static KaKaoReadyRequestDto of(String cid, String approvalUrl, String cancelUrl, String failUrl, String partnerOrderId
            , String partnerUserId, String itemName, String quantity, String totalAmount, String taxFreeAmount) {

        return KaKaoReadyRequestDto.builder()
                .cid(cid)
                .approvalUrl(approvalUrl)
                .cancelUrl(cancelUrl)
                .failUrl(failUrl)
                .partnerOrderId(partnerOrderId)
                .partnerUserId(partnerUserId)
                .itemName(itemName)
                .quantity(quantity)
                .totalAmount(totalAmount)
                .taxFreeAmount(taxFreeAmount)
                .build();
    }
}
