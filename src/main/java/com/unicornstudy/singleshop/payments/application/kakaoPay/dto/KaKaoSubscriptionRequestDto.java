package com.unicornstudy.singleshop.payments.application.kakaoPay.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class KaKaoSubscriptionRequestDto {
    private final String cid;
    private final String sid;
    private final String partner_order_id;
    private final String partner_user_id;
    private final String quantity;
    private final String total_amount;
    private final String tax_free_amount;

    @Builder
    public KaKaoSubscriptionRequestDto(String cid, String sid, String partner_order_id, String partner_user_id, String quantity, String total_amount, String tax_free_amount) {
        this.cid = cid;
        this.sid = sid;
        this.partner_order_id = partner_order_id;
        this.partner_user_id = partner_user_id;
        this.quantity = quantity;
        this.total_amount = total_amount;
        this.tax_free_amount = tax_free_amount;
    }

    public static KaKaoSubscriptionRequestDto of(String cid, String sid, String partner_order_id, String partner_user_id, String quantity, String total_amount, String tax_free_amount) {
        return KaKaoSubscriptionRequestDto.builder()
                .cid(cid)
                .sid(sid)
                .partner_order_id(partner_order_id)
                .partner_user_id(partner_user_id)
                .quantity(quantity)
                .total_amount(total_amount)
                .tax_free_amount(tax_free_amount)
                .build();
    }
}
