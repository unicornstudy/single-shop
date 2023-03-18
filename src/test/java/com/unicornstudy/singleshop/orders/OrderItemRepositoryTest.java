package com.unicornstudy.singleshop.orders;

import com.unicornstudy.singleshop.items.Items;
import com.unicornstudy.singleshop.items.ItemsRepository;
import com.unicornstudy.singleshop.items.exception.ItemsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static com.unicornstudy.singleshop.exception.ErrorCode.BAD_REQUEST_ITEMS_READ;
import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class OrderItemRepositoryTest {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ItemsRepository itemsRepository;

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

        itemsRepository.save(items);

        orderItemRepository.save(orderItem);
    }

    @Test
    void 주문상품_저장_테스트() {
        OrderItem foundOrderItem = orderItemRepository.findById(orderItem.getId()).orElseThrow(() -> new ItemsException(BAD_REQUEST_ITEMS_READ));

        assertThat(foundOrderItem).isNotNull();
    }
}