package com.unicornstudy.singleshop.orders.command.application.dto;

import com.unicornstudy.singleshop.orders.command.domain.OrderStatus;
import com.unicornstudy.singleshop.orders.command.domain.Orders;
import com.unicornstudy.singleshop.orders.query.domain.OrderIndex;
import com.unicornstudy.singleshop.payments.domain.Payment;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.ArrayList;
import java.util.List;

@Getter
public class OrderIndexDto {
    private Long id;
    private String userEmail;
    private List<Long> orderItemsId;
    private Long deliveryId;
    private OrderStatus status;
    private String orderDate;
    private Payment payment;

    @Builder
    public OrderIndexDto(Long id, String userEmail, List<Long> orderItemsId, Long deliveryId, OrderStatus status, String orderDate, Payment payment) {
        this.id = id;
        this.userEmail = userEmail;
        this.orderItemsId = orderItemsId;
        this.deliveryId = deliveryId;
        this.status = status;
        this.orderDate = orderDate;
        this.payment = payment;
    }

    public static OrderIndexDto from(Orders orders) {
        return OrderIndexDto.builder()
                .id(orders.getId())
                .userEmail(orders.getUser().getEmail())
                .orderItemsId(orders.getOrderItems().stream().map((items) -> items.getItem().getId()).toList())
                .deliveryId(orders.getDelivery().getId())
                .status(orders.getStatus())
                .orderDate(orders.getOrderDate().toString())
                .payment(orders.getPayment())
                .build();
    }

}
