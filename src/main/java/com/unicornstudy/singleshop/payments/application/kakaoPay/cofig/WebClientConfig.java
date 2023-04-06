package com.unicornstudy.singleshop.payments.application.kakaoPay.cofig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient(@Value("${kakao-base-url}") String baseUrl, @Value("${admin-key}") String adminKey) {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.set("Authorization", "KakaoAK " + adminKey);
                    httpHeaders.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
                })
                .build();
    }
}
