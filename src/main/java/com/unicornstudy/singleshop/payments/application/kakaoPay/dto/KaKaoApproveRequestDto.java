package com.unicornstudy.singleshop.payments.application.kakaoPay.dto;

import lombok.Getter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;


@Getter
public class KaKaoApproveRequestDto {

    private MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

    public static KaKaoApproveRequestDto createApproveRequestDto(String tid, String pg_token, KaKaoReadyRequestDto readyRequestDto) {
        KaKaoApproveRequestDto approveRequestDto = new KaKaoApproveRequestDto();
        approveRequestDto.initialize(tid, pg_token, readyRequestDto);
        return approveRequestDto;
    }

    public void initialize(String tid, String pg_token, KaKaoReadyRequestDto readyRequestDto) {
        body.add("cid", readyRequestDto.getBody().get("cid").get(0));
        body.add("tid", tid);
        body.add("partner_order_id", readyRequestDto.getBody().get("partner_order_id").get(0));
        body.add("partner_user_id", readyRequestDto.getBody().get("partner_user_id").get(0));
        body.add("pg_token", pg_token);
    }
}
