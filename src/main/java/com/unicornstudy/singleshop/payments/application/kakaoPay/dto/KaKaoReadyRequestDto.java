package com.unicornstudy.singleshop.payments.application.kakaoPay.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class KaKaoReadyRequestDto {
    private final String cid;
    private final String approval_url;
    private final String cancel_url;
    private final String fail_url;
    private final String partner_order_id;
    private final String partner_user_id;
    private final String item_name;
    private final String quantity;
    private final String total_amount;
    private final String tax_free_amount;

    @Builder
    public KaKaoReadyRequestDto(String cid, String approval_url, String cancel_url, String fail_url, String partner_order_id
            , String partner_user_id, String item_name, String quantity, String total_amount, String tax_free_amount) {
        this.cid = cid;
        this.approval_url = approval_url;
        this.cancel_url = cancel_url;
        this.fail_url = fail_url;
        this.partner_order_id = partner_order_id;
        this.partner_user_id = partner_user_id;
        this.item_name = item_name;
        this.quantity = quantity;
        this.total_amount = total_amount;
        this.tax_free_amount = tax_free_amount;
    }

    public static KaKaoReadyRequestDto of(String cid, String approval_url, String cancel_url, String fail_url, String partner_order_id
            , String partner_user_id, String item_name, String quantity, String total_amount, String tax_free_amount) {

        return KaKaoReadyRequestDto.builder()
                .cid(cid)
                .approval_url(approval_url)
                .cancel_url(cancel_url)
                .fail_url(fail_url)
                .partner_order_id(partner_order_id)
                .partner_user_id(partner_user_id)
                .item_name(item_name)
                .quantity(quantity)
                .total_amount(total_amount)
                .tax_free_amount(tax_free_amount)
                .build();
    }
}
