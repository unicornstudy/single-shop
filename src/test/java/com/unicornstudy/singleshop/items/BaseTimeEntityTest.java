package com.unicornstudy.singleshop.items;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class BaseTimeEntityTest {

    @Autowired
    private ItemsRepository itemsRepository;

    private final String itemName = "testName";
    private final Integer itemPrice = 1000;
    private final String itemDescription = "testDescription";
    private final Integer itemQuantity = 10;

    @Test
    void 생성_수정_시간_테스트() {
        Items item = Items.builder()
                .name(itemName)
                .price(itemPrice)
                .description(itemDescription)
                .quantity(itemQuantity)
                .build();

        itemsRepository.save(item);

        assertThat(item.getCreatedDate()).isNotNull();
        assertThat(item.getModifiedDate()).isNotNull();
        assertThat(item.getCreatedDate()).isBeforeOrEqualTo(LocalDateTime.now());
        assertThat(item.getModifiedDate()).isBeforeOrEqualTo(LocalDateTime.now());
    }
}