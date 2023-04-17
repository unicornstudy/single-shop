package com.unicornstudy.singleshop.orders.domain;

import com.unicornstudy.singleshop.items.domain.Items;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Orders order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Items item;

    private String name;

    private String description;

    private Integer price;

    public void initializeOrder(Orders order) {
        this.order = order;
    }

    public static OrderItem createOrderItem(Items item) {
        OrderItem orderItem = new OrderItem();
        orderItem.initializeItem(item);
        orderItem.updateItemInfo(item);
        return orderItem;
    }

    public void initializeItem(Items item) {
        this.item = item;
    }

    private void updateItemInfo(Items item) {
        this.name = item.getName();
        this.description = item.getDescription();
        this.price = item.getPrice();
    }
}
