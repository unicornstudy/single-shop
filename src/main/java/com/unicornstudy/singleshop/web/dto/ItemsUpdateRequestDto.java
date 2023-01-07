package com.unicornstudy.singleshop.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ItemsUpdateRequestDto {

    private String name;
    private int price;
    private String description;
    private int quantity;

    @Builder
    public ItemsUpdateRequestDto(String name, int price, String description, int quantity) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.quantity = quantity;
    }
}
