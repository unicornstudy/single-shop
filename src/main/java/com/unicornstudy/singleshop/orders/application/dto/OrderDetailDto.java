package com.unicornstudy.singleshop.orders.application.dto;

import com.unicornstudy.singleshop.orders.domain.OrderItem;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class OrderDetailDto {

    private String name;
    private String description;
    private Integer price;

    public static OrderDetailDto createOrderDetailDto(OrderItem orderItem) {
        OrderDetailDto orderDetailDto = new OrderDetailDto();
        orderDetailDto.name = orderItem.getName();
        orderDetailDto.description = orderItem.getDescription();
        orderDetailDto.price = orderItem.getPrice();
        return orderDetailDto;
    }
}
