package com.unicornstudy.singleshop.payments.application.kakaoPay;

import com.unicornstudy.singleshop.payments.application.PaymentService;
import com.unicornstudy.singleshop.payments.application.kakaoPay.dto.*;
import com.unicornstudy.singleshop.exception.payments.ApproveException;
import com.unicornstudy.singleshop.payments.kakaoPay.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class KaKaoPaymentService implements PaymentService {

    private static final String BASE_URL = "https://kapi.kakao.com/v1/payment";
    private static final String PATH = "/api/payments/kakaopay";

    @Value("${admin-key}")
    private String admin_key;

    private WebClient webClient = WebClient.builder()
            .baseUrl(BASE_URL)
            .build();

    @Override
    public HttpHeaders initializePaymentsHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "KakaoAK " + admin_key);
        httpHeaders.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        return httpHeaders;
    }

    @Override
    public String getPaymentPath() {
        return PATH;
    }

    public Mono<KaKaoReadyResponseDto> requestKaKaoToReady(KaKaoReadyRequestDto readyRequestDto) {
        return webClient.post()
                .uri("/ready")
                .headers(headers -> headers.addAll(initializePaymentsHeaders()))
                .body(BodyInserters.fromFormData(readyRequestDto.getBody()))
                .retrieve()
                .bodyToMono(KaKaoReadyResponseDto.class);
    }

    public Mono<KaKaoApproveResponseDto> requestKaKaoToApprove(KaKaoApproveRequestDto approveRequestDto, Long orderId) {
        return webClient.post()
                .uri("/approve")
                .headers(headers -> headers.addAll(initializePaymentsHeaders()))
                .body(BodyInserters.fromFormData(approveRequestDto.getBody()))
                .retrieve()
                .onStatus(status -> status.is4xxClientError()
                                || status.is5xxServerError()
                        , clientResponse ->
                                clientResponse.bodyToMono(String.class)
                                        .map(body -> new ApproveException(orderId, body)))
                .bodyToMono(KaKaoApproveResponseDto.class);
    }

    public Mono<KaKaoCancelResponseDto> requestKaKaoToCancel(KaKaoCancelRequestDto cancelRequestDto) {
        return webClient.post()
                .uri("/cancel")
                .headers(headers -> headers.addAll(initializePaymentsHeaders()))
                .body(BodyInserters.fromFormData(cancelRequestDto.getBody()))
                .retrieve()
                .bodyToMono(KaKaoCancelResponseDto.class);
    }
}
