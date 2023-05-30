package com.unicornstudy.singleshop.items.command.application.dto;

import com.unicornstudy.singleshop.items.command.domain.ChildCategory;
import com.unicornstudy.singleshop.items.command.domain.Items;
import com.unicornstudy.singleshop.items.command.domain.ParentCategory;
import com.unicornstudy.singleshop.items.query.domain.ItemsIndex;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ItemsResponseDto {

    private Long id;
    private String name;
    private Integer price;
    private String description;
    private Integer quantity;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private String parentCategory;
    private String childCategory;

    @Builder
    public ItemsResponseDto(Long id, String name, Integer price, String description, Integer quantity, LocalDateTime createdDate, LocalDateTime modifiedDate, String parentCategory, String childCategory) {
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

    public static ItemsResponseDto from(Items items) {
        return ItemsResponseDto.builder()
                .id(items.getId())
                .name(items.getName())
                .price(items.getPrice())
                .quantity(items.getQuantity())
                .description(items.getDescription())
                .createdDate(items.getCreatedDate())
                .modifiedDate(items.getModifiedDate())
                .parentCategory(items.getParentCategory().name())
                .childCategory(items.getChildCategory().name())
                .build();
    }

    public ItemsIndex toEntity() {

        return ItemsIndex.builder()
                .id(getId())
                .name(getName())
                .price(getPrice())
                .description(getDescription())
                .quantity(getQuantity())
                .modifiedDate(getModifiedDate().toString())
                .createdDate(getCreatedDate().toString())
                .parentCategory(ParentCategory.valueOf(getParentCategory()))
                .childCategory(ChildCategory.valueOf(getChildCategory()))
                .build();
    }

}
