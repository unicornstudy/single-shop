package com.unicornstudy.singleshop.payments.application.kakaoPay.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class KaKaoApproveRequestDto {
    private final String cid;
    private final String tid;
    private final String partner_order_id;
    private final String partner_user_id;
    private final String pg_token;

    @Builder
    public KaKaoApproveRequestDto(String cid, String tid, String partner_order_id, String partner_user_id, String pg_token) {
        this.cid = cid;
        this.tid = tid;
        this.partner_order_id = partner_order_id;
        this.partner_user_id = partner_user_id;
        this.pg_token = pg_token;
    }

    public static KaKaoApproveRequestDto of(String cid, String tid, String partner_order_id, String partner_user_id, String pg_token) {
        return KaKaoApproveRequestDto.builder()
                .cid(cid)
                .tid(tid)
                .partner_order_id(partner_order_id)
                .partner_user_id(partner_user_id)
                .pg_token(pg_token)
                .build();
    }
}
