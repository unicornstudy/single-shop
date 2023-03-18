package com.unicornstudy.singleshop.items;

import com.unicornstudy.singleshop.orders.exception.ItemQuantityException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ItemsTest {

    private final String itemName = "testName";
    private final Integer itemPrice = 1000;
    private final String itemDescription = "testDescription";
    private final Integer itemQuantity = 10;

    private Items item;

    @BeforeEach
    void setUp() {
        item = Items.builder()
                .id(1L)
                .name(itemName)
                .price(itemPrice)
                .description(itemDescription)
                .quantity(itemQuantity)
                .build();
    }

    @Test
    void 재고_추가_테스트() {
        item.addQuantity();

        assertThat(item.getQuantity()).isEqualTo(itemQuantity + 1);
    }

    @Test
    void 재고_감소_테스트() {
        item.subtractQuantity();

        assertThat(item.getQuantity()).isEqualTo(itemQuantity - 1);
    }

    @Test
    void 재고_0이하_예외() {
        item = Items.builder()
                .name("test")
                .price(1000)
                .description("test")
                .quantity(0)
                .build();

        assertThatExceptionOfType(ItemQuantityException.class)
                .isThrownBy(() -> item.subtractQuantity());
    }

    @Test
    void 상품_수정_테스트() {
        Long id = item.getId();
        String newName = "new name";
        Integer newPrice = 2000;
        String newDescription = "new description";
        Integer newQuantity = 20;
        LocalDateTime newModifiedDate = LocalDateTime.now();

        item.update(id, newName, newPrice, newDescription, newQuantity, newModifiedDate);

        assertThat(item.getName()).isEqualTo(newName);
        assertThat(item.getPrice()).isEqualTo(newPrice);
        assertThat(item.getDescription()).isEqualTo(newDescription);
        assertThat(item.getQuantity()).isEqualTo(newQuantity);
        assertThat(item.getModifiedDate()).isEqualTo(newModifiedDate);
    }
}