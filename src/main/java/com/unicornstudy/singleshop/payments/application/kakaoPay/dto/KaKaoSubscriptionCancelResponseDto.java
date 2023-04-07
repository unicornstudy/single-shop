package com.unicornstudy.singleshop.payments.application.kakaoPay.dto;

import lombok.Getter;

@Getter
public class KaKaoSubscriptionCancelResponseDto {
    private String cid;
    private String sid;
    private String status;
    private String created_at;
    private String inactivated_at;
    private String last_approved_at;
}
