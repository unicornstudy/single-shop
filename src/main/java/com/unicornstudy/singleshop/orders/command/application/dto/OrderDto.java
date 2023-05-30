package com.unicornstudy.singleshop.orders.command.application.dto;

import com.unicornstudy.singleshop.orders.command.domain.OrderStatus;
import com.unicornstudy.singleshop.orders.command.domain.Orders;
import com.unicornstudy.singleshop.payments.domain.Payment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class OrderDto {

    private final LocalDateTime orderDate;
    private final OrderStatus status;
    private final Payment payment;

    @Builder
    public OrderDto(LocalDateTime orderDate, OrderStatus status, Payment payment) {
        this.orderDate = orderDate;
        this.status = status;
        this.payment = payment;
    }

    public static OrderDto from(Orders order) {
        return OrderDto.builder()
                .orderDate(order.getOrderDate())
                .status(order.getStatus())
                .payment(order.getPayment())
                .build();
    }
}
