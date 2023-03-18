package com.unicornstudy.singleshop.delivery;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AddressTest {

    @Test
    void 생성자_테스트() {
        Address address = new Address();

        assertThat(address).isNotNull();
    }
}