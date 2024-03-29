package com.unicornstudy.singleshop.orders.command.domain;

import com.unicornstudy.singleshop.delivery.domain.Delivery;
import com.unicornstudy.singleshop.payments.domain.Payment;
import com.unicornstudy.singleshop.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private LocalDateTime orderDate;

    @Embedded
    private Payment payment;

    public void initializeUser(User user) {
        this.user = user;
        user.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.initializeOrder(this);
    }

    public static Orders createOrder(User user, List<OrderItem> orderItems, Payment payment, Delivery delivery) {
        Orders order = new Orders();
        order.initializeUser(user);
        orderItems.forEach(entity -> order.addOrderItem(entity));
        order.initializeDelivery(delivery);
        order.updateOrderStatus(OrderStatus.ORDER);
        order.updateOrderDate();
        order.updatePayment(payment);

        return order;
    }

    public void updateOrderStatus(OrderStatus status) {
        this.status = status;
    }

    private void updatePayment(Payment payment) {
        this.payment = payment;
    }

    private void updateOrderDate() {
        this.orderDate = LocalDateTime.now();
    }

    public void initializeDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.initializeOrder(this);
    }

    public void cancelDelivery() {
        this.delivery = null;
    }

    public void createIdForTest(Long id) {
        this.id = id;
    }
}