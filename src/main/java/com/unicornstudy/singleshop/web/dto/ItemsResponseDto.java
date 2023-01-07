package com.unicornstudy.singleshop.web.dto;

import com.unicornstudy.singleshop.domain.items.Items;
import lombok.Getter;

@Getter
public class ItemsResponseDto {

    private Long id;
    private String name;
    private int price;
    private String description;
    private int quantity;

    public ItemsResponseDto(Items items) {
        this.id = items.getId();
        this.name = items.getName();
        this.price = items.getPrice();
        this.description = items.getDescription();
        this.quantity = items.getQuantity();
    }
}
