package com.unicornstudy.singleshop.carts.dto;

import com.unicornstudy.singleshop.carts.CartItem;
import lombok.Getter;

@Getter
public class ReadCartResponseDto {
    private Long cartItemId;
    private String itemName;
    private Integer price;
    private String description;

    public ReadCartResponseDto(CartItem cartItem) {
        cartItemId = cartItem.getId();
        itemName = cartItem.getItem().getName();
        price = cartItem.getItem().getPrice();
        description = cartItem.getItem().getDescription();
    }
}
