package com.unicornstudy.singleshop.web.dto;

import com.unicornstudy.singleshop.domain.items.Items;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ItemsSaveRequestDto {

    private String name;
    private int price;
    private String description;
    private int quantity;

    @Builder
    public ItemsSaveRequestDto(String name, int price, String description, int quantity) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.quantity = quantity;
    }

    public Items toEntity() {
        return Items.builder()
                .name(name)
                .price(price)
                .description(description)
                .quantity(quantity)
                .build();
    }
}
