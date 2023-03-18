package com.unicornstudy.singleshop.user;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RoleTest {

    @Test
    void 역할_필드_테스트() {
        assertThat(Role.USER.getKey()).isEqualTo("ROLE_USER");
        assertThat(Role.USER.getTitle()).isEqualTo("일반 사용자");
        assertThat(Role.ADMIN.getKey()).isEqualTo("ROLE_ADMIN");
        assertThat(Role.ADMIN.getTitle()).isEqualTo("관리 사용자");
    }
}