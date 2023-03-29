package com.unicornstudy.singleshop.items.application.dto;

import com.unicornstudy.singleshop.items.domain.Items;
import lombok.Getter;

@Getter
public abstract class ItemsResponseDto {

    private Long id;
    private String name;
    private Integer price;
    private String description;
    private Integer quantity;

    public ItemsResponseDto(Items items) {
        this.id = items.getId();
        this.name = items.getName();
        this.price = items.getPrice();
        this.description = items.getDescription();
        this.quantity = items.getQuantity();
    }

}
