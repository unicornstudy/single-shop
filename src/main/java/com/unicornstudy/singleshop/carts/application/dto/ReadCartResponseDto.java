package com.unicornstudy.singleshop.carts.application.dto;

import com.unicornstudy.singleshop.carts.domain.CartItem;
import lombok.Getter;

@Getter
public class ReadCartResponseDto {

    private Long cartItemId;
    private String itemName;
    private Integer price;
    private String description;
    private Integer quantity;

    public ReadCartResponseDto(CartItem cartItem) {
        cartItemId = cartItem.getId();
        itemName = cartItem.getItem().getName();
        price = cartItem.getItem().getPrice();
        description = cartItem.getItem().getDescription();
        quantity = cartItem.getItem().getQuantity();
    }
}