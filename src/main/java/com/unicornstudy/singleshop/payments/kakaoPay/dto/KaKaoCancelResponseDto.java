package com.unicornstudy.singleshop.payments.kakaoPay.dto;

import lombok.Getter;

@Getter
public class KaKaoCancelResponseDto {
    private String aid;
    private String status;
    private String partner_order_id;
    private String partner_user_id;
    private String code;
}
