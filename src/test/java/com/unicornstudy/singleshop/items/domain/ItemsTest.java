package com.unicornstudy.singleshop.items.domain;

import com.unicornstudy.singleshop.config.TestSetting;
import com.unicornstudy.singleshop.exception.orders.ItemQuantityException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class ItemsTest {

    private Items item;

    @BeforeEach
    void setUp() {
        item = TestSetting.setItem();
    }

    @DisplayName("재고 증가 메소드 테스트")
    @Test
    void add_quantity_test() {
        //given
        Integer currentQuantity = item.getQuantity();

        //when
        item.addQuantity();

        //then
        assertThat(item.getQuantity()).isEqualTo(currentQuantity + 1);
    }

    @DisplayName("재고 감소 메소드 테스트")
    @Test
    void subtract_quantity_test() {
        //given
        Integer currentQuantity = item.getQuantity();

        //when
        item.subtractQuantity();

        //then
        assertThat(item.getQuantity()).isEqualTo(currentQuantity - 1);
    }

    @DisplayName("재고 0인 상품 재고 감소 예외 테스트")
    @Test
    void subtract_quantity_exception_test() {
        //given
        Items newItem = Items.builder()
                .name("test")
                .price(1000)
                .description("test")
                .quantity(0)
                .build();

        //then
        assertThatExceptionOfType(ItemQuantityException.class)
                .isThrownBy(() -> newItem.subtractQuantity());
    }

    @DisplayName("상품 데이터 수정 메소드 테스트")
    @Test
    void item_update_test() {
        //given
        Long id = item.getId();
        String updatedName = "updated name";
        Integer updatedPrice = 2000;
        String updatedDescription = "updated description";
        Integer updatedQuantity = 20;
        LocalDateTime modifiedDate = LocalDateTime.now();

        //when
        item.update(id, updatedName, updatedPrice, updatedDescription, updatedQuantity, modifiedDate);

        //then
        assertThat(item.getName()).isEqualTo(updatedName);
        assertThat(item.getPrice()).isEqualTo(updatedPrice);
        assertThat(item.getDescription()).isEqualTo(updatedDescription);
        assertThat(item.getQuantity()).isEqualTo(updatedQuantity);
        assertThat(item.getModifiedDate()).isEqualTo(modifiedDate);
    }
}