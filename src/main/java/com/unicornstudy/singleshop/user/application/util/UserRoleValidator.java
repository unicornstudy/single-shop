package com.unicornstudy.singleshop.user.application.util;

import com.unicornstudy.singleshop.user.domain.Role;

public class UserRoleValidator {

    public static boolean isCancelSubscriber(Role role) {
        return role == Role.CANCEL_SUBSCRIBER;
    }

    public static boolean isSubscriber(Role role) {
        return role == Role.SUBSCRIBER;
    }

    public static boolean isNormalUser(Role role) {
        return role == Role.USER;
    }
}
