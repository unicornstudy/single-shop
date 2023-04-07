package com.unicornstudy.singleshop.payments.presentation;

import com.unicornstudy.singleshop.oauth2.LoginUser;
import com.unicornstudy.singleshop.oauth2.dto.SessionUser;
import com.unicornstudy.singleshop.payments.application.kakaoPay.KaKaoPaymentService;
import com.unicornstudy.singleshop.payments.application.kakaoPay.dto.*;
import com.unicornstudy.singleshop.payments.domain.Payment;
import com.unicornstudy.singleshop.subscription.application.SubscriptionService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import static com.unicornstudy.singleshop.subscription.application.utils.DateCalculator.calculate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments/kakaopay/subscription")
@Slf4j
public class SubscriptionKaKaoPaymentController {

    private final KaKaoPaymentService paymentsService;
    private final SubscriptionService subscriptionService;
    private final KaKaoConfiguration kakaoConfiguration;

    @GetMapping
    public ResponseEntity<KaKaoReadyResponseDto> requestReady(@LoginUser SessionUser user, HttpSession httpSession) {

        subscriptionService.checkSubscriptionUser(user.getEmail());
        KaKaoReadyRequestDto readyRequestDto = KaKaoReadyRequestDto.of(kakaoConfiguration.getSubscription_cid(), kakaoConfiguration.getSubscription_approval_url(), kakaoConfiguration.getSubscription_cancel_url(), kakaoConfiguration.getSubscription_fail_url(), "구독 결제", user.getEmail(), "구독", "1", calculate(user.getRole()), "0");
        KaKaoReadyResponseDto readyResponse = paymentsService.requestKaKaoToReady(readyRequestDto).block();

        httpSession.setAttribute("tid", readyResponse.getTid());
        httpSession.setAttribute("readyRequestDto", readyRequestDto);

        return ResponseEntity
                .status(HttpStatus.TEMPORARY_REDIRECT)
                .location(URI.create(readyResponse.getNext_redirect_pc_url()))
                .build();
    }

    @GetMapping("/approve")
    public ResponseEntity<KaKaoApproveResponseDto> requestApprove(@LoginUser SessionUser user, HttpSession httpSession, @RequestParam("pg_token") String pg_token) {
        String tid = (String) httpSession.getAttribute("tid");
        KaKaoReadyRequestDto readyRequestDto = (KaKaoReadyRequestDto) httpSession.getAttribute("readyRequestDto");

        KaKaoApproveRequestDto approveRequestDto = KaKaoApproveRequestDto.of(readyRequestDto.getCid(), tid, readyRequestDto.getPartner_order_id(), user.getEmail(), pg_token);
        KaKaoApproveResponseDto responseDto = paymentsService.requestKaKaoToApprove(approveRequestDto).block();
        subscriptionService.subscribe(user.getEmail(), Payment.builder().paymentKind("kakao").tid(tid).sid(responseDto.getSid()).price(readyRequestDto.getTotal_amount()).build());

        httpSession.removeAttribute("tid");
        httpSession.removeAttribute("readyRequestDto");

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}