package com.unicornstudy.singleshop.orders.query.application.dto;

import com.unicornstudy.singleshop.orders.command.domain.OrderStatus;
import com.unicornstudy.singleshop.orders.query.domain.OrderIndex;
import com.unicornstudy.singleshop.payments.domain.Payment;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderSearchDto {

    private final String orderDate;
    private final OrderStatus status;
    private final Payment payment;

    @Builder
    public OrderSearchDto(String orderDate, OrderStatus status, Payment payment) {
        this.orderDate = orderDate;
        this.status = status;
        this.payment = payment;
    }

    public static OrderSearchDto from(OrderIndex order) {
        return OrderSearchDto.builder()
                .orderDate(order.getOrderDate())
                .status(order.getStatus())
                .payment(order.getPayment())
                .build();
    }
}
