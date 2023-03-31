package com.unicornstudy.singleshop.subscription.application.utils;

import com.unicornstudy.singleshop.user.domain.Role;

import java.time.LocalDate;

public class DateCalculator {

    private static final Integer PAYMENT_DAY = 28;
    private static final Integer PRICE = 5000;

    public static String calculate(Role role) {

        if (isCancelSubscriber(role)) {
            return "0";
        }

        LocalDate localDate = LocalDate.now();
        Integer gap = PAYMENT_DAY - localDate.getDayOfMonth() < 0 ?
                localDate.lengthOfMonth() - localDate.getDayOfMonth() + PAYMENT_DAY : PAYMENT_DAY - localDate.getDayOfMonth();

        return String.valueOf(PRICE / 30 * gap);
    }

    private static boolean isCancelSubscriber(Role role) {
        return role == Role.CANCEL_SUBSCRIBER;
    }

}
