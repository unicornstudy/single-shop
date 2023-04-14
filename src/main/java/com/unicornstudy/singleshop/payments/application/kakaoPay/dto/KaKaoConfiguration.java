package com.unicornstudy.singleshop.payments.application.kakaoPay.dto;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class KaKaoConfiguration {
    @Value("${cid}")
    private String cid;

    @Value("${approve-url}")
    private String approvalUrl;

    @Value("${cancel-url}")
    private String cancelUrl;

    @Value("${fail-url}")
    private String failUrl;

    @Value("${subscription-cid}")
    private String subscriptionCid;

    @Value("${subscription-approve-url}")
    private String subscriptionApprovalUrl;

    @Value("${subscription-cancel-url}")
    private String subscriptionCancelUrl;

    @Value("${subscription-fail-url}")
    private String subscriptionFailUrl;
}