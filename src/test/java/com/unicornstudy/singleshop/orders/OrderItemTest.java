package com.unicornstudy.singleshop.orders;

import com.unicornstudy.singleshop.items.Items;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderItemTest {

    private final String name = "test";
    private final String description = "description";
    private final Integer price = 1000;
    private final Integer quantity = 10;

    private Items items;
    private OrderItem orderItem;

    @BeforeEach
    void setUp() {
        items = Items.builder()
                .name(name)
                .description(description)
                .price(price)
                .quantity(quantity)
                .build();

        orderItem = OrderItem.createOrderItem(items);
    }

    @Test
    void 주문상품_필드_테스트() {
        assertThat(orderItem.getName()).isEqualTo(name);
        assertThat(orderItem.getDescription()).isEqualTo(description);
        assertThat(orderItem.getPrice()).isEqualTo(price);
    }
}