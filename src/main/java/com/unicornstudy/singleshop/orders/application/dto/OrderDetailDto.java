package com.unicornstudy.singleshop.orders.application.dto;

import com.unicornstudy.singleshop.orders.domain.OrderItem;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderDetailDto {

    private final String name;
    private final String description;
    private final Integer price;

    @Builder
    public OrderDetailDto(String name, String description, Integer price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public static OrderDetailDto from(OrderItem orderItem) {
        return OrderDetailDto.builder()
                .name(orderItem.getName())
                .description(orderItem.getDescription())
                .price(orderItem.getPrice())
                .build();
    }
}
