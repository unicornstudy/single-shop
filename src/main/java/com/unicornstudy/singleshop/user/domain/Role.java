package com.unicornstudy.singleshop.user.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    USER("ROLE_USER", "일반 사용자"),
    ADMIN("ROLE_ADMIN", "관리 사용자"),
    SUBSCRIBER("ROLE_SUBSCRIBER", "구독 사용자"),
    CANCEL_SUBSCRIBER("ROLE_CANCEL", "구독 취소예정자");

    private final String key;
    private final String title;
}
