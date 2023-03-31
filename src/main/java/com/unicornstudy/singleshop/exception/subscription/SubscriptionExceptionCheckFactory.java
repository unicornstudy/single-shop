package com.unicornstudy.singleshop.exception.subscription;

import com.unicornstudy.singleshop.user.domain.Role;
import com.unicornstudy.singleshop.user.domain.User;

public class SubscriptionExceptionCheckFactory {

    public static void checkCanceledSubscription(User user) {
        if (user.getRole() != Role.SUBSCRIBER) {
            throw new CanceledSubscriptionException();
        }
    }

    public static void checkSubscription(User user) {
        if (user.getRole() != Role.USER) {
            throw new AlreadySubscriptionException();
        }
    }

    public static void checkReSubscription(User user) {
        if (user.getRole() != Role.CANCEL_SUBSCRIBER) {
            throw new ReSubscriptionException();
        }
    }
}
