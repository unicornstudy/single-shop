package com.unicornstudy.singleshop.subscription.domain.repository;

import com.unicornstudy.singleshop.config.TestSetting;
import com.unicornstudy.singleshop.delivery.domain.Address;
import com.unicornstudy.singleshop.payments.domain.Payment;
import com.unicornstudy.singleshop.subscription.domain.Subscription;
import com.unicornstudy.singleshop.user.domain.User;
import com.unicornstudy.singleshop.user.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class SubscriptionRepositoryTest {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private UserRepository userRepository;

    private Subscription subscription;
    private User user;
    private Address address;
    private Payment payment;
    private Pageable pageable;
    private List<Subscription> subscriptions = new ArrayList<>();

    @BeforeEach
    void setUp() {
        pageable = TestSetting.setPageable();
        address = TestSetting.setAddress();
        user = TestSetting.setUser(address);

        userRepository.save(user);

        payment = TestSetting.setPayment();
        subscription = TestSetting.setSubscription(user, payment);

        subscriptionRepository.save(subscription);
    }

    @DisplayName("회원 이메일을 통한 구독 조회 테스트")
    @Test
    void findByUserEmail_test() {
        subscriptions.add(subscription);

        List<Subscription> foundSubscriptions = subscriptionRepository.findSubscriptionByUserEmail(user.getEmail(), pageable);

        assertThat(foundSubscriptions.size()).isEqualTo(subscriptions.size());
        assertThat(foundSubscriptions.get(0).getUser()).isEqualTo(subscriptions.get(0).getUser());
    }

    @DisplayName("잘못된 회원 이메일을 통한 구독 조회 예외 테스트")
    @Test
    void findByUserEmail_exception_test() {
        assertThat(subscriptionRepository.findSubscriptionByUserEmail("invalidUser", pageable)).isEmpty();
    }
}