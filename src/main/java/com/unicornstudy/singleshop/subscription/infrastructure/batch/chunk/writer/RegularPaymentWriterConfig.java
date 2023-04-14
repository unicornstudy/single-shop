package com.unicornstudy.singleshop.subscription.infrastructure.batch.chunk.writer;

import com.unicornstudy.singleshop.exception.subscription.RegularPaymentException;
import com.unicornstudy.singleshop.payments.application.kakaoPay.KaKaoPaymentService;
import com.unicornstudy.singleshop.payments.application.kakaoPay.dto.KaKaoConfiguration;
import com.unicornstudy.singleshop.payments.application.kakaoPay.dto.KaKaoSubscriptionCancelRequestDto;
import com.unicornstudy.singleshop.payments.application.kakaoPay.dto.KaKaoSubscriptionRequestDto;
import com.unicornstudy.singleshop.payments.domain.Payment;
import com.unicornstudy.singleshop.subscription.application.SubscriptionService;
import com.unicornstudy.singleshop.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class RegularPaymentWriterConfig {

    private final KaKaoPaymentService paymentService;
    private final SubscriptionService subscriptionService;
    private final KaKaoConfiguration kakaoConfiguration;

    @StepScope
    @Bean
    public ItemWriter<User> subscriptionCancelWriter() {
        return chunk -> {
            for (User user : chunk.getItems()) {
                try {
                    KaKaoSubscriptionCancelRequestDto requestDto = KaKaoSubscriptionCancelRequestDto.of(kakaoConfiguration.getSubscriptionCid(), user.getSid());
                    subscriptionService.cancelSubscription(user.getEmail());
                    paymentService.requestKaKaoToSubscriptionCancel(requestDto).block();
                } catch (Exception e) {
                    log.info("sid 등록 오류");
                }
            }
        };
    }

    @StepScope
    @Bean
    public ItemWriter<User> subscriptionRequestWriter() {
        return chunk -> {
            for (User user : chunk.getItems()) {
                try {
                    KaKaoSubscriptionRequestDto subscriptionRequestDto = KaKaoSubscriptionRequestDto.of(kakaoConfiguration.getCid(), user.getSid(), "구독", user.getEmail(), "1", "5000", "0");
                    paymentService.requestKaKaoToSubscription(subscriptionRequestDto, user.getEmail()).block();
                    subscriptionService.subscribe(user.getEmail(), Payment.builder().paymentKind("kakao").sid(user.getSid()).price(subscriptionRequestDto.getTotalAmount()).build());
                } catch (RegularPaymentException e) {
                    log.info("정기구독 오류");
                    subscriptionService.handleSubscriptionPaymentError(user.getEmail());
                } catch (Exception e) {
                    log.info("sid 등록 오류");
                }

            }
        };
    }
}
