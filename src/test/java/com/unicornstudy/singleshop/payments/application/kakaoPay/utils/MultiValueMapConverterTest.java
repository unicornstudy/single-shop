package com.unicornstudy.singleshop.payments.application.kakaoPay.utils;

import com.unicornstudy.singleshop.payments.application.kakaoPay.dto.KaKaoCancelRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.util.MultiValueMap;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class MultiValueMapConverterTest {

    @DisplayName("Dto를 MultiValueMap으로 변환")
    @Test
    void MultiValueConverter_테스트() {
        //given
        String cid = "cid";
        String tid = "tid";
        String cancel_amount = "cancel_amount";
        String cancel_tax_free_amount = "cancel_tax_free_amount";

        KaKaoCancelRequestDto test = KaKaoCancelRequestDto.of(cid, tid, cancel_amount, cancel_tax_free_amount);

        //when
        MultiValueMap<String, String> result = MultiValueMapConverter.convert(test);

        //then
        assertThat(result.get("cid").get(0)).isEqualTo(cid);
        assertThat(result.get("tid").get(0)).isEqualTo(tid);
        assertThat(result.get("cancel_amount").get(0)).isEqualTo(cancel_amount);
        assertThat(result.get("cancel_tax_free_amount").get(0)).isEqualTo(cancel_tax_free_amount);

    }
}