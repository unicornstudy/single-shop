package com.unicornstudy.singleshop.payments.application.kakaoPay;

import com.unicornstudy.singleshop.payments.application.kakaoPay.dto.*;
import com.unicornstudy.singleshop.exception.payments.ApproveException;
import com.unicornstudy.singleshop.payments.application.kakaoPay.utils.MultiValueMapConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class KaKaoPaymentService {

    private static final String BASE_URL = "https://kapi.kakao.com/v1/payment";

    @Value("${admin-key}")
    private String admin_key;

    private WebClient webClient = WebClient.builder()
            .baseUrl(BASE_URL)
            .build();

    public HttpHeaders updatePaymentsHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "KakaoAK " + admin_key);
        httpHeaders.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        return httpHeaders;
    }

    public Mono<KaKaoReadyResponseDto> requestKaKaoToReady(KaKaoReadyRequestDto readyRequestDto) {
        return webClient.post()
                .uri("/ready")
                .headers(headers -> headers.addAll(updatePaymentsHeaders()))
                .body(BodyInserters.fromFormData(MultiValueMapConverter.convert(readyRequestDto)))
                .retrieve()
                .bodyToMono(KaKaoReadyResponseDto.class);
    }

    public Mono<KaKaoApproveResponseDto> requestKaKaoToApprove(KaKaoApproveRequestDto approveRequestDto, Long orderId) {
        return webClient.post()
                .uri("/approve")
                .headers(headers -> headers.addAll(updatePaymentsHeaders()))
                .body(BodyInserters.fromFormData(MultiValueMapConverter.convert(approveRequestDto)))
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
                .headers(headers -> headers.addAll(updatePaymentsHeaders()))
                .body(BodyInserters.fromFormData(MultiValueMapConverter.convert(cancelRequestDto)))
                .retrieve()
                .bodyToMono(KaKaoCancelResponseDto.class);
    }
}
