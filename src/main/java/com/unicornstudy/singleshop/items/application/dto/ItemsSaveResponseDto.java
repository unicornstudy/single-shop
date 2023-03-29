package com.unicornstudy.singleshop.items.application.dto;

import com.unicornstudy.singleshop.items.domain.Items;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ItemsSaveResponseDto extends ItemsResponseDto {

    private LocalDateTime createdDate;

    public ItemsSaveResponseDto(Items items) {
        super(items);
        this.createdDate = items.getCreatedDate();
    }

    public static ItemsSaveResponseDto from(Items items) {
        return new ItemsSaveResponseDto(items);
    }
}
