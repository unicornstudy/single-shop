package com.unicornstudy.singleshop.payments.application.kakaoPay.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KaKaoSubscriptionCancelResponseDto {
    private String cid;
    private String sid;
    private String status;
    private String createdAt;
    private String inactivatedAt;
    private String lastApprovedAt;
}
