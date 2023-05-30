package com.unicornstudy.singleshop.delivery.domain;

import com.unicornstudy.singleshop.orders.command.domain.Orders;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Orders order;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    @Embedded
    private Address address;

    public void initializeOrder(Orders order) {
        this.order = order;
    }

    public static Delivery createDelivery(Address address) {
        Delivery delivery = new Delivery();
        delivery.address = address;
        delivery.changeDeliveryStatus(DeliveryStatus.READY);
        return delivery;
    }

    public void changeDeliveryStatus(DeliveryStatus status) {
        this.status = status;
    }
}
