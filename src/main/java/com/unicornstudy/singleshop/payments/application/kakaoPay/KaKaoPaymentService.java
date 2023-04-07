package com.unicornstudy.singleshop.payments.application.kakaoPay;

import com.unicornstudy.singleshop.exception.payments.SubscriptionApproveException;
import com.unicornstudy.singleshop.exception.subscription.RegularPaymentException;
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

    public Mono<KaKaoApproveResponseDto> requestKaKaoToApprove(KaKaoApproveRequestDto approveRequestDto) {
        return webClient.post()
                .uri("/approve")
                .body(BodyInserters.fromFormData(MultiValueMapConverter.convert(approveRequestDto)))
                .retrieve()
                .onStatus(status -> status.is4xxClientError()
                                || status.is5xxServerError()
                        , clientResponse -> {
                            throw new SubscriptionApproveException();
                        })
                .bodyToMono(KaKaoApproveResponseDto.class);
    }

    public Mono<KaKaoSubscriptionResponseDto> requestKaKaoToSubscription(KaKaoSubscriptionRequestDto subscriptionRequestDto, String userEmail) {
        return webClient.post()
                .uri("/subscription")
                .body(BodyInserters.fromFormData(MultiValueMapConverter.convert(subscriptionRequestDto)))
                .retrieve()
                .onStatus(status -> status.is4xxClientError()
                                || status.is5xxServerError()
                        , clientResponse -> {
                            throw new RegularPaymentException(userEmail);
                        })
                .bodyToMono(KaKaoSubscriptionResponseDto.class);
    }

    public Mono<KaKaoSubscriptionCancelResponseDto> requestKaKaoToSubscriptionCancel(KaKaoSubscriptionCancelRequestDto subscriptionCancelRequestDto) {
        return webClient.post()
                .uri("/manage/subscription/inactive")
                .body(BodyInserters.fromFormData(MultiValueMapConverter.convert(subscriptionCancelRequestDto)))
                .retrieve()
                .bodyToMono(KaKaoSubscriptionCancelResponseDto.class);
    }


}
