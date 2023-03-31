package com.unicornstudy.singleshop.payments.application.kakaoPay.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class KaKaoCancelRequestDto {
    private final String cid;
    private final String tid;
    private final String cancel_amount;
    private final String cancel_tax_free_amount;

    @Builder
    public KaKaoCancelRequestDto(String cid, String tid, String cancel_amount, String cancel_tax_free_amount) {
        this.cid = cid;
        this.tid = tid;
        this.cancel_amount = cancel_amount;
        this.cancel_tax_free_amount = cancel_tax_free_amount;
    }

    public static KaKaoCancelRequestDto of(String cid, String tid, String cancel_amount, String cancel_tax_free_amount) {
        return KaKaoCancelRequestDto.builder()
                .cid(cid)
                .tid(tid)
                .cancel_amount(cancel_amount)
                .cancel_tax_free_amount(cancel_tax_free_amount)
                .build();
    }
}
