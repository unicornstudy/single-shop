package com.unicornstudy.singleshop.orders.query.domain;

import com.unicornstudy.singleshop.orders.command.application.dto.OrderIndexDto;
import com.unicornstudy.singleshop.orders.command.domain.OrderStatus;
import com.unicornstudy.singleshop.orders.command.domain.Orders;
import com.unicornstudy.singleshop.payments.domain.Payment;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.ArrayList;
import java.util.List;

@Document(indexName = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class OrderIndex {

    @Id
    @Field(type = FieldType.Long)
    private Long id;

    @Field(type = FieldType.Keyword)
    private String userEmail;

    private List<Long> orderItemsId = new ArrayList<>();

    @Field(type = FieldType.Long)
    private Long deliveryId;

    @Field(type = FieldType.Keyword)
    private OrderStatus status;

    @Field(type = FieldType.Date)
    private String orderDate;

    private Payment payment;

    public static OrderIndex createElasticsearchOrders(OrderIndexDto orders) {
        return OrderIndex.builder()
                .id(orders.getId())
                .userEmail(orders.getUserEmail())
                .orderItemsId(orders.getOrderItemsId())
                .deliveryId(orders.getDeliveryId())
                .status(orders.getStatus())
                .orderDate(orders.getOrderDate())
                .payment(orders.getPayment())
                .build();
    }
}
