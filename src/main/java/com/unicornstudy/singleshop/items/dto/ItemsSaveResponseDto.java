package com.unicornstudy.singleshop.items.dto;

import com.unicornstudy.singleshop.items.Items;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ItemsSaveResponseDto extends ItemsResponseDto {

    private LocalDateTime createdDate;

    public ItemsSaveResponseDto(Items items) {
        super(items);
        this.createdDate = items.getCreatedDate();
    }
}
