package com.unicornstudy.singleshop.payments.application.kakaoPay;

import com.unicornstudy.singleshop.payments.application.kakaoPay.dto.*;
import com.unicornstudy.singleshop.exception.payments.ApproveException;
import com.unicornstudy.singleshop.payments.application.kakaoPay.utils.MultiValueMapConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class KaKaoPaymentService {

    private final WebClient webClient;

    public Mono<KaKaoReadyResponseDto> requestKaKaoToReady(KaKaoReadyRequestDto readyRequestDto) {
        return webClient.post()
                .uri("/ready")
                .body(BodyInserters.fromFormData(MultiValueMapConverter.convert(readyRequestDto)))
                .retrieve()
                .bodyToMono(KaKaoReadyResponseDto.class);
    }

    public Mono<KaKaoApproveResponseDto> requestKaKaoToApprove(KaKaoApproveRequestDto approveRequestDto, Long orderId) {
        return webClient.post()
                .uri("/approve")
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
                .body(BodyInserters.fromFormData(MultiValueMapConverter.convert(cancelRequestDto)))
                .retrieve()
                .bodyToMono(KaKaoCancelResponseDto.class);
    }
}
