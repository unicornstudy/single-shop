package com.unicornstudy.singleshop.delivery;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DeliveryTest {

    @Test
    void 생성_테스트() {
        Address address = new Address("testCity", "testStreet", "testZipcode");

        Delivery delivery = Delivery.createDelivery(address);

        assertThat(delivery.getStatus()).isEqualTo(DeliveryStatus.READY);
        assertThat(delivery.getAddress()).isEqualTo(address);
    }

    @Test
    void 상태_변경_테스트() {
        Address address = new Address("testCity", "testStreet", "testZipcode");

        Delivery delivery = Delivery.createDelivery(address);

        delivery.changeDeliveryStatus(DeliveryStatus.START);

        assertThat(delivery.getStatus()).isEqualTo(DeliveryStatus.START);
    }
}