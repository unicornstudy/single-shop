package com.unicornstudy.singleshop.exception.subscription;

import com.unicornstudy.singleshop.user.application.util.UserRoleValidator;
import com.unicornstudy.singleshop.user.domain.Role;
import com.unicornstudy.singleshop.user.domain.User;

public class SubscriptionExceptionCheckFactory {

    public static void checkCanceledSubscription(User user) {
        if (!UserRoleValidator.isSubscriber(user.getRole())) {
            throw new CanceledSubscriptionException();
        }
    }

    public static void checkSubscription(User user) {
        if (!UserRoleValidator.isNormalUser(user.getRole())) {
            throw new AlreadySubscriptionException();
        }
    }

    public static void checkReSubscription(User user) {
        if (!UserRoleValidator.isCancelSubscriber(user.getRole())) {
            throw new ReSubscriptionException();
        }
    }

    public static void checkReservedCancelSubscription(User user) {
        if (!UserRoleValidator.isCancelSubscriber(user.getRole())) {
            throw new NotCanceledSubscriptionException();
        }
    }
}
