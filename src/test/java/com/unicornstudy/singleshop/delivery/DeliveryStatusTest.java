package com.unicornstudy.singleshop.delivery;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DeliveryStatusTest {

    @Test
    void 필드_테스트() {
        assertThat(DeliveryStatus.READY.name()).isEqualTo("READY");
        assertThat(DeliveryStatus.START.name()).isEqualTo("START");
        assertThat(DeliveryStatus.COMPLETE.name()).isEqualTo("COMPLETE");
    }
}