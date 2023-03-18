package com.unicornstudy.singleshop.carts.dto;

import com.unicornstudy.singleshop.carts.CartItem;
import com.unicornstudy.singleshop.items.Items;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ReadCartResponseDtoTest {

    @Test
    void 생성자_테스트() {
        Items item = Items.builder()
                .id(1L)
                .name("test")
                .price(1000)
                .description("test description")
                .quantity(10)
                .build();

        CartItem cartItem = CartItem.createCartItem(item);

        ReadCartResponseDto readCartResponseDto = new ReadCartResponseDto(cartItem);

        assertThat(readCartResponseDto.getCartItemId()).isEqualTo(cartItem.getId());
        assertThat(readCartResponseDto.getItemName()).isEqualTo(item.getName());
        assertThat(readCartResponseDto.getPrice()).isEqualTo(item.getPrice());
        assertThat(readCartResponseDto.getDescription()).isEqualTo(item.getDescription());
    }
}