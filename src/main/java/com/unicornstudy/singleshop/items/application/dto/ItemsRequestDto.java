package com.unicornstudy.singleshop.items.application.dto;

import com.unicornstudy.singleshop.items.domain.Items;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemsRequestDto {

    private String name;
    private Integer price;
    private String description;
    private Integer quantity;

    public Items toEntity() {
        return Items.builder()
                .name(getName())
                .price(getPrice())
                .description(getDescription())
                .quantity(getQuantity())
                .createdDate(LocalDateTime.now())
                .build();
    }

}
