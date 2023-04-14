package com.unicornstudy.singleshop.payments.application.kakaoPay.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KaKaoCancelRequestDto {
    private final String cid;
    private final String tid;
    private final String cancelAmount;
    private final String cancelTaxFreeAmount;

    @Builder
    public KaKaoCancelRequestDto(String cid, String tid, String cancelAmount, String cancelTaxFreeAmount) {
        this.cid = cid;
        this.tid = tid;
        this.cancelAmount = cancelAmount;
        this.cancelTaxFreeAmount = cancelTaxFreeAmount;
    }

    public static KaKaoCancelRequestDto of(String cid, String tid, String cancelAmount, String cancelTaxFreeAmount) {
        return KaKaoCancelRequestDto.builder()
                .cid(cid)
                .tid(tid)
                .cancelAmount(cancelAmount)
                .cancelTaxFreeAmount(cancelTaxFreeAmount)
                .build();
    }
}
