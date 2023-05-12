package com.unicornstudy.singleshop.items.command.application.dto;

import com.unicornstudy.singleshop.items.domain.ChildCategory;
import com.unicornstudy.singleshop.items.domain.Items;
import com.unicornstudy.singleshop.items.domain.ParentCategory;
import lombok.*;

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
    private String parentCategory;
    private String childCategory;

    public Items toEntity() {
        return Items.builder()
                .name(getName())
                .price(getPrice())
                .description(getDescription())
                .quantity(getQuantity())
                .createdDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .parentCategory(ParentCategory.valueOf(getParentCategory()))
                .childCategory(ChildCategory.valueOf(getChildCategory()))
                .build();
    }

}
