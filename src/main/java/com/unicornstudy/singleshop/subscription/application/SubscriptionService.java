package com.unicornstudy.singleshop.subscription.application;

import com.unicornstudy.singleshop.exception.carts.SessionExpiredException;
import com.unicornstudy.singleshop.payments.domain.Payment;
import com.unicornstudy.singleshop.subscription.domain.Subscription;
import com.unicornstudy.singleshop.subscription.domain.repository.SubscriptionRepository;
import com.unicornstudy.singleshop.subscription.application.dto.SubscriptionDto;
import com.unicornstudy.singleshop.exception.subscription.SubscriptionExceptionCheckFactory;
import com.unicornstudy.singleshop.user.domain.Role;
import com.unicornstudy.singleshop.user.domain.User;
import com.unicornstudy.singleshop.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<SubscriptionDto> findSubscriptionByUser(String userEmail, Pageable pageable) {
        return subscriptionRepository.findSubscriptionByUserEmail(userEmail, pageable)
                .stream()
                .map(entity -> SubscriptionDto.from(entity))
                .collect(Collectors.toList());
    }

    @Transactional
    public void subscribe(String userEmail, Payment payment) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new SessionExpiredException());
        SubscriptionExceptionCheckFactory.checkSubscription(user);
        updateUserRoleAndSid(user, payment);
        Subscription subscription = Subscription.createSubscription(user, payment);
        subscriptionRepository.save(subscription);
    }

    @Transactional(readOnly = true)
    public void checkSubscriptionUser(String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new SessionExpiredException());
        SubscriptionExceptionCheckFactory.checkSubscription(user);
    }

    @Transactional
    public void handleSubscriptionPaymentError(String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new SessionExpiredException());
        user.updateRole(Role.USER);
    }

    private void updateUserRoleAndSid(User user, Payment payment) {
        user.updateSid(payment.getSid());
        user.updateRole(Role.SUBSCRIBER);
    }

    @Transactional
    public void reserveCancelSubscription(String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new SessionExpiredException());
        SubscriptionExceptionCheckFactory.checkCanceledSubscription(user);
        user.updateRole(Role.CANCEL_SUBSCRIBER);
    }

    @Transactional
    public void reSubscribe(String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new SessionExpiredException());
        SubscriptionExceptionCheckFactory.checkReSubscription(user);
        user.updateRole(Role.SUBSCRIBER);
    }

    @Transactional
    public void cancelSubscription(String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new SessionExpiredException());
        SubscriptionExceptionCheckFactory.checkReservedCancelSubscription(user);
        user.updateSid(null);
        user.updateRole(Role.USER);
    }
}
