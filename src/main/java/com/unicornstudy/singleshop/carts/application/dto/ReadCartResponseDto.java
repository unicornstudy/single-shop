package com.unicornstudy.singleshop.carts.application.dto;

import com.unicornstudy.singleshop.carts.domain.CartItem;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ReadCartResponseDto {

    private final Long cartItemId;
    private final String itemName;
    private final Integer price;
    private final String description;
    private final Integer quantity;

    @Builder
    public ReadCartResponseDto(Long cartItemId, String itemName, Integer price, String description, Integer quantity) {
        this.cartItemId = cartItemId;
        this.itemName = itemName;
        this.price = price;
        this.description = description;
        this.quantity = quantity;
    }

    public static ReadCartResponseDto from(CartItem cartItem) {
        return ReadCartResponseDto.builder()
                .cartItemId(cartItem.getId())
                .itemName(cartItem.getItem().getName())
                .price(cartItem.getItem().getPrice())
                .description(cartItem.getItem().getDescription())
                .quantity(cartItem.getItem().getQuantity())
                .build();
    }
}
