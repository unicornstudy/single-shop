package com.unicornstudy.singleshop.payments.application.kakaoPay.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

public class MultiValueMapConverter {

    public static MultiValueMap<String, String> convert(Object dto) {
            ObjectMapper objectMapper = new ObjectMapper();
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            Map<String, String> map = objectMapper.convertValue(dto, new TypeReference<Map<String, String>>() {});
            params.setAll(map);

            return params;
    }
}
