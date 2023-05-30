package com.unicornstudy.singleshop.items.command.application.dto;

import com.unicornstudy.singleshop.exception.items.validator.ValidChildCategory;
import com.unicornstudy.singleshop.exception.items.validator.ValidParentCategory;
import com.unicornstudy.singleshop.items.command.domain.ChildCategory;
import com.unicornstudy.singleshop.items.command.domain.Items;
import com.unicornstudy.singleshop.items.command.domain.ParentCategory;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemsRequestDto {

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotNull(message = "Price cannot be null")
    @Min(value = 0, message = "Price cannot be less than zero")
    private Integer price;

    @NotBlank(message = "Description cannot be blank")
    private String description;

    @NotNull(message = "Quantity cannot be null")
    @Min(value = 0, message = "Quantity cannot be less than zero")
    private Integer quantity;

    @NotNull(message = "ParentCategory cannot be null")
    @ValidParentCategory(message = "ParentCategory is invalid")
    private String parentCategory;

    @NotNull(message = "ChildCategory cannot be null")
    @ValidChildCategory(message = "ChildCategory cannot is invalid")
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

    public void updateItems(Items items) {
        items.update(getName(), getPrice(), getDescription(), getQuantity(), ParentCategory.valueOf(getParentCategory()), ChildCategory.valueOf(getChildCategory()));
    }
}
