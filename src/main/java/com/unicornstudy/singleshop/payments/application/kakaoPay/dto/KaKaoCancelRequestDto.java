package com.unicornstudy.singleshop.payments.application.kakaoPay.dto;

import com.unicornstudy.singleshop.orders.application.dto.OrderDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;


@Getter
@NoArgsConstructor
public class KaKaoCancelRequestDto {

    private MultiValueMap body = new LinkedMultiValueMap();

    public static KaKaoCancelRequestDto createKaKaoCancelRequestDtoForOrder(OrderDto orderDto, String cid) {
        KaKaoCancelRequestDto kaKaoCancelRequestDto = new KaKaoCancelRequestDto();
        kaKaoCancelRequestDto.initializeForOrder(orderDto, cid);
        return kaKaoCancelRequestDto;
    }

    public void initializeForOrder(OrderDto orderDto, String cid) {
        body.add("cid", cid);
        body.add("tid", orderDto.getPayment().getTid());
        body.add("cancel_amount", String.valueOf(orderDto.getPayment().getPrice()));
        body.add("cancel_tax_free_amount", "0");
    }
}
