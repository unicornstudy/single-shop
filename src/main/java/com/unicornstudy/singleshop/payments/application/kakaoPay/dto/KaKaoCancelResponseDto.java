package com.unicornstudy.singleshop.payments.application.kakaoPay.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KaKaoCancelResponseDto {
    private String aid;
    private String status;
    private String partnerOrderId;
    private String partnerUserId;
    private String code;
}
