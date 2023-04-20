package com.unicornstudy.singleshop.delivery.domain;

import com.unicornstudy.singleshop.config.TestSetting;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DeliveryTest {

    @DisplayName("배송 상태 변경 테스트")
    @Test
    void delivery_status_change_test() {
        Address address = TestSetting.setAddress();
        Delivery delivery = TestSetting.setDelivery(address);

        delivery.changeDeliveryStatus(DeliveryStatus.START);

        assertThat(delivery.getStatus()).isEqualTo(DeliveryStatus.START);
    }
}