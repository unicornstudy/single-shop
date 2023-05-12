package com.unicornstudy.singleshop.items.query.application.dto;

import com.unicornstudy.singleshop.items.query.domain.ItemsIndex;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ItemsSearchDto {
    private final Long id;
    private final String name;
    private final Integer price;
    private final String description;
    private final Integer quantity;
    private final String createdDate;
    private final String modifiedDate;
    private final String parentCategory;
    private final String childCategory;

    @Builder
    public ItemsSearchDto(Long id, String name, int price, String description, int quantity, String createdDate, String modifiedDate, String parentCategory, String childCategory) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.quantity = quantity;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.parentCategory = parentCategory;
        this.childCategory = childCategory;
    }

    public static ItemsSearchDto from(ItemsIndex itemsIndex) {
        return ItemsSearchDto.builder()
                .id(itemsIndex.getId())
                .name(itemsIndex.getName())
                .description(itemsIndex.getDescription())
                .price(itemsIndex.getPrice())
                .quantity(itemsIndex.getQuantity())
                .createdDate(itemsIndex.getCreatedDate())
                .modifiedDate(itemsIndex.getModifiedDate())
                .parentCategory(itemsIndex.getParentCategory().name())
                .childCategory(itemsIndex.getChildCategory().name())
                .build();
    }
}
