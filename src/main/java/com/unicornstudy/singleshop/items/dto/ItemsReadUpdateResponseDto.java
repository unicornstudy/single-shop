package com.unicornstudy.singleshop.items.dto;

import com.unicornstudy.singleshop.items.Items;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ItemsReadUpdateResponseDto extends ItemsResponseDto {

    private LocalDateTime modifiedDate;

    public ItemsReadUpdateResponseDto(Items items) {
        super(items);
        this.modifiedDate = items.getModifiedDate();
    }
}
