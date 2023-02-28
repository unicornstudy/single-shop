package com.unicornstudy.singleshop.orders.dto;

import com.unicornstudy.singleshop.items.dto.ItemsReadUpdateResponseDto;
import com.unicornstudy.singleshop.orders.OrderStatus;
import com.unicornstudy.singleshop.orders.Orders;
import com.unicornstudy.singleshop.orders.Payment;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class OrderDto {

    private LocalDateTime orderDate;

    private OrderStatus status;

    private Integer orderPrice;

    private Payment payment;

    public static OrderDto createOrderDto(Orders order) {
        OrderDto orderDto = new OrderDto();
        orderDto.initialize(order);
        return orderDto;
    }

    private void initialize(Orders order) {
        this.orderDate = order.getOrderDate();
        this.status = order.getStatus();
        this.orderPrice = order.getOrderPrice();
        this.payment = order.getPayment();
    }
}
