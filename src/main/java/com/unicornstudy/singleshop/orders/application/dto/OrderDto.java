package com.unicornstudy.singleshop.orders.application.dto;

import com.unicornstudy.singleshop.orders.domain.OrderStatus;
import com.unicornstudy.singleshop.orders.domain.Orders;
import com.unicornstudy.singleshop.payments.domain.Payment;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class OrderDto {

    private LocalDateTime orderDate;

    private OrderStatus status;

    private Payment payment;

    public static OrderDto createOrderDto(Orders order) {
        OrderDto orderDto = new OrderDto();
        orderDto.initialize(order);
        return orderDto;
    }

    private void initialize(Orders order) {
        this.orderDate = order.getOrderDate();
        this.status = order.getStatus();
        this.payment = order.getPayment();
    }
}
