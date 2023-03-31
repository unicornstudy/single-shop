package com.unicornstudy.singleshop.subscription.application.utils;

import com.unicornstudy.singleshop.user.domain.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DateCalculatorTest {

    @DisplayName("현재 날짜와 결제일(28일)을 비교하여 구독 결제 금액 계산 테스트")
    @Test
    void calculate_테스트() {
        //given
        Role role = Role.USER;
        LocalDate today = LocalDate.now();
        int paymentDay = 28;
        int subscriptionPrice = 5000;
        int gapOfPaymentDayAndToday = paymentDay - today.getDayOfMonth() < 0 ? today.lengthOfMonth() - today.getDayOfMonth() + paymentDay : paymentDay - today.getDayOfMonth();

        //when
        String result = DateCalculator.calculate(role);
        //then
        assertThat(result).isEqualTo(String.valueOf(subscriptionPrice / 30 * gapOfPaymentDayAndToday));
    }

    @DisplayName("구독한 사용자가 구독 취소후 재구독을 눌렀을 경우 0원 결제 테스트")
    @Test
    void calculate_0원_테스트() {
        //given
        Role role = Role.CANCEL_SUBSCRIBER;

        //when
        String result = DateCalculator.calculate(role);
        //then
        assertThat(result).isEqualTo("0");
    }
}