package com.unicornstudy.singleshop.items.application.dto;

import com.unicornstudy.singleshop.items.domain.Items;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ItemsResponseDto {

    private final Long id;
    private final String name;
    private final Integer price;
    private final String description;
    private final Integer quantity;
    private final LocalDateTime createdDate;
    private final LocalDateTime modifiedDate;

    @Builder
    public ItemsResponseDto(Long id, String name, Integer price, String description, Integer quantity, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.quantity = quantity;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public static ItemsResponseDto from(Items items) {
        return ItemsResponseDto.builder()
                .id(items.getId())
                .name(items.getName())
                .price(items.getPrice())
                .description(items.getDescription())
                .createdDate(items.getCreatedDate())
                .modifiedDate(items.getModifiedDate())
                .build();
    }

}
