package com.unicornstudy.singleshop.subscription.application.utils;

import com.unicornstudy.singleshop.user.domain.Role;

import java.time.LocalDate;

import static com.unicornstudy.singleshop.user.application.util.UserRoleValidator.isCancelSubscriber;

public class DateCalculator {

    private static final Integer PAYMENT_DAY = 28;
    public static final Integer PRICE = 5000;
    public static final String SUBSCRIPTION_USER_DELIVERY_PRICE = "0";

    public static String calculate(Role role) {

        if (isCancelSubscriber(role)) {
            return SUBSCRIPTION_USER_DELIVERY_PRICE;
        }

        LocalDate localDate = LocalDate.now();
        Integer gap = PAYMENT_DAY - localDate.getDayOfMonth() < 0 ?
                localDate.lengthOfMonth() - localDate.getDayOfMonth() + PAYMENT_DAY : PAYMENT_DAY - localDate.getDayOfMonth();

        return String.valueOf(PRICE / 30 * gap);
    }

}
