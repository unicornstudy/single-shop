package com.unicornstudy.singleshop.orders;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderStatusTest {

    @Test
    void 필드_테스트() {
        assertThat(OrderStatus.ORDER.name()).isEqualTo("ORDER");
        assertThat(OrderStatus.CANCEL.name()).isEqualTo("CANCEL");
    }
}