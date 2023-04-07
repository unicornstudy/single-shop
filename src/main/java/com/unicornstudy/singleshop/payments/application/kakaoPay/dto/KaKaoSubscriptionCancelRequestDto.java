package com.unicornstudy.singleshop.payments.application.kakaoPay.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class KaKaoSubscriptionCancelRequestDto {
    private final String cid;
    private final String sid;

    @Builder
    public KaKaoSubscriptionCancelRequestDto(String cid, String sid) {
        this.cid = cid;
        this.sid = sid;
    }

    public static KaKaoSubscriptionCancelRequestDto of(String cid, String sid) {
        return KaKaoSubscriptionCancelRequestDto.builder()
                .cid(cid)
                .sid(sid)
                .build();
    }
}
