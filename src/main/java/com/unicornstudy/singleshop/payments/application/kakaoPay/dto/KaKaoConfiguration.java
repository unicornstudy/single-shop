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
    private String approval_url;

    @Value("${cancel-url}")
    private String cancel_url;

    @Value("${fail-url}")
    private String fail_url;

    @Value("${subscription-cid}")
    private String subscription_cid;

    @Value("${subscription-approve-url}")
    private String subscription_approval_url;

    @Value("${subscription-cancel-url}")
    private String subscription_cancel_url;

    @Value("${subscription-fail-url}")
    private String subscription_fail_url;
}