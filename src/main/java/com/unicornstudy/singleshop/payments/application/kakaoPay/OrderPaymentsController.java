package com.unicornstudy.singleshop.payments.application.kakaoPay;

import com.unicornstudy.singleshop.orders.application.OrderService;
import com.unicornstudy.singleshop.payments.application.PaymentServiceFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
@Slf4j
public class OrderPaymentsController {

    private final OrderService orderService;

    @GetMapping("/{paymentApi}")
    public ResponseEntity selectPayment(@PathVariable("paymentApi") String paymentApi, @Value("${domain}") String domain)
            throws URISyntaxException {
        String payUrl = domain + PaymentServiceFactory.getService(paymentApi).getPaymentPath();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(new URI(payUrl));

        return ResponseEntity
                .status(HttpStatus.TEMPORARY_REDIRECT)
                .headers(httpHeaders)
                .build();
    }

    @PostMapping("/{id}")
    public ResponseEntity selectCancel(@PathVariable("id") Long id, @Value("${domain}") String domain)
            throws URISyntaxException {
        String paymentKind = orderService.findOrderById(id).getPayment().getPaymentKind();
        String cancelUrl = domain + PaymentServiceFactory.getService(paymentKind).getPaymentPath() + "/" + id;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(new URI(cancelUrl));

        return ResponseEntity
                .status(HttpStatus.TEMPORARY_REDIRECT)
                .headers(httpHeaders)
                .build();
    }
}