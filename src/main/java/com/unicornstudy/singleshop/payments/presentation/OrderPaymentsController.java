package com.unicornstudy.singleshop.payments.presentation;

import com.unicornstudy.singleshop.orders.command.application.OrderService;
import com.unicornstudy.singleshop.payments.application.kakaoPay.PaymentHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class OrderPaymentsController {

    private final OrderService orderService;

    @GetMapping("/{paymentApi}")
    public ResponseEntity selectPayment(@PathVariable("paymentApi") String paymentApi, @Value("${domain}") String domain) {
        return ResponseEntity
                .status(HttpStatus.TEMPORARY_REDIRECT)
                .location(URI.create(domain + PaymentHandler.getPaymentUrl(paymentApi)))
                .build();
    }

    @PostMapping("/{id}")
    public ResponseEntity selectCancel(@PathVariable("id") Long id, @Value("${domain}") String domain) {
        String paymentKind = orderService.findOrderById(id).getPayment().getPaymentKind();

        return ResponseEntity
                .status(HttpStatus.TEMPORARY_REDIRECT)
                .location(URI.create(domain + PaymentHandler.getPaymentUrl(paymentKind)+ "/" + id))
                .build();
    }
}