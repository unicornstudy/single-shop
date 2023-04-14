package com.unicornstudy.singleshop.payments.application.kakaoPay.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KaKaoApproveRequestDto {
    private final String cid;
    private final String tid;
    private final String partnerOrderId;
    private final String partnerUserId;
    private final String pgToken;

    @Builder
    public KaKaoApproveRequestDto(String cid, String tid, String partnerOrderId, String partnerUserId, String pgToken) {
        this.cid = cid;
        this.tid = tid;
        this.partnerOrderId = partnerOrderId;
        this.partnerUserId = partnerUserId;
        this.pgToken = pgToken;
    }

    public static KaKaoApproveRequestDto of(String cid, String tid, String partnerOrderId, String partnerUserId, String pgToken) {
        return KaKaoApproveRequestDto.builder()
                .cid(cid)
                .tid(tid)
                .partnerOrderId(partnerOrderId)
                .partnerUserId(partnerUserId)
                .pgToken(pgToken)
                .build();
    }
}
