package com.unicornstudy.singleshop.subscription.domain;

import com.unicornstudy.singleshop.payments.domain.Payment;
import com.unicornstudy.singleshop.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;

@Entity
@Getter
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscription_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDate payDate;

    @Embedded
    private Payment payment;

    public static Subscription createSubscription(User user, Payment payment) {
        Subscription subscription = new Subscription();
        subscription.initializeSubscription(user, payment);
        return subscription;
    }

    private void initializeSubscription(User user, Payment payment) {
        this.payDate = LocalDate.now();
        this.user = user;
        this.payment = payment;
    }
}