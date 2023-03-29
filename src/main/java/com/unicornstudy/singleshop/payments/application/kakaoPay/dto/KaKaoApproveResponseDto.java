package com.unicornstudy.singleshop.payments.application.kakaoPay.dto;

import lombok.Getter;


@Getter
public class KaKaoApproveResponseDto {
    private String code;
    private String aid;
    private String tid;
    private String cid;
    private String sid;
    private String partner_order_id;
    private String partner_user_id;
    private String payment_method_type;
    private String item_name;
    private String item_code;
    private int quantity;
    private String created_at;
    private String approved_at;
    private String payload;
}
