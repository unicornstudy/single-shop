package com.unicornstudy.singleshop.subscription;

import com.unicornstudy.singleshop.config.TestSetting;
import com.unicornstudy.singleshop.payments.domain.Payment;
import com.unicornstudy.singleshop.subscription.application.SubscriptionService;
import com.unicornstudy.singleshop.subscription.application.dto.SubscriptionDto;
import com.unicornstudy.singleshop.subscription.domain.Subscription;
import com.unicornstudy.singleshop.subscription.domain.repository.SubscriptionRepository;
import com.unicornstudy.singleshop.user.domain.Role;
import com.unicornstudy.singleshop.user.domain.User;
import com.unicornstudy.singleshop.user.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@WithMockUser(roles = "USER")
public class SubscriptionServiceTest {

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private UserRepository userRepository;

    private SubscriptionService subscriptionService;

    private List<Subscription> subscriptionList = new ArrayList<>();

    private Subscription subscription;
    private Payment payment;
    private User user;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        pageable = TestSetting.setPageable();
        payment = TestSetting.setPayment();
        user = TestSetting.setUser(TestSetting.setAddress());
        subscription = TestSetting.setSubscription(user, payment);
        subscriptionList.add(subscription);
        subscriptionService = new SubscriptionService(subscriptionRepository, userRepository);
    }

    @Test
    @DisplayName("현재까지 사용자가 결제한 구독 정보 조회 테스트")
    void 구독_조회_테스트() {
        when(subscriptionRepository.findSubscriptionByUserEmail(any(String.class), any(Pageable.class))).thenReturn(subscriptionList);

        List<SubscriptionDto> result = subscriptionService.findSubscriptionByUser(user.getEmail(), pageable);

        assertThat(result.size()).isEqualTo(subscriptionList.size());
    }

    @Test
    @DisplayName("사용자가 요청한 구독이 서비스 계층에서 올바르게 작동하는지 테스트")
    void 구독_테스트() {
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.ofNullable(user));

        subscriptionService.subscribe(user.getEmail(), payment);

        assertThat(user.getRole()).isEqualTo(Role.SUBSCRIBER);
    }

    @Test
    @DisplayName("사용자가 구독 취소 요청시 바로 구독 취소 되지 않고 28일까지 기다리기 위해 취소 예약이 되는지 확인하는 테스트")
    void 구독_취소_예약_테스트() {
        user.updateRole(Role.SUBSCRIBER);
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.ofNullable(user));

        subscriptionService.reserveCancelSubscription(user.getEmail());

        assertThat(user.getRole()).isEqualTo(Role.CANCEL_SUBSCRIBER);
    }

    @Test
    @DisplayName("사용자가 요청한 구독취소가 28일날 자동으로 취소가 되는지 확인하는 테스트")
    void 구독_취소_테스트() {
        user.updateRole(Role.CANCEL_SUBSCRIBER);
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.ofNullable(user));

        subscriptionService.cancelSubscription(user.getEmail());

        assertThat(user.getRole()).isEqualTo(Role.USER);
    }
}