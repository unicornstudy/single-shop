package com.unicornstudy.singleshop.payments.application.kakaoPay.dto;

import lombok.Getter;

@Getter
public class KaKaoSubscriptionResponseDto {
    private String partner_order_id;
    private String partner_user_id;
    private String payment_method_type;
    private String item_name;
    private String item_code;
    private String quantity;
    private String code;
}
