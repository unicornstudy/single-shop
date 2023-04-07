package com.unicornstudy.singleshop.payments.presentation;

import com.unicornstudy.singleshop.oauth2.LoginUser;
import com.unicornstudy.singleshop.oauth2.dto.SessionUser;
import com.unicornstudy.singleshop.payments.application.kakaoPay.PaymentHandler;
import com.unicornstudy.singleshop.subscription.application.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subscription/payments")
@Slf4j
public class SubscriptionPaymentsController {

    private final SubscriptionService subscriptionService;

    @GetMapping("/{paymentApi}")
    public ResponseEntity selectPayment(@PathVariable("paymentApi") String paymentApi, @Value("${domain}") String domain) {
        return ResponseEntity
                .status(HttpStatus.TEMPORARY_REDIRECT)
                .location(URI.create(domain + PaymentHandler.getPaymentUrl(paymentApi) + "/subscription"))
                .build();
    }

    @PostMapping
    public ResponseEntity requestCancel(@LoginUser SessionUser user) {
        subscriptionService.reserveCancelSubscription(user.getEmail());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity reSubscribe(@LoginUser SessionUser user) {
        subscriptionService.reSubscribe(user.getEmail());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}