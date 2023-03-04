package com.unicornstudy.singleshop.orders;

import com.unicornstudy.singleshop.delivery.Delivery;
import com.unicornstudy.singleshop.user.User;
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

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
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
        order.changeOrderStatus(OrderStatus.ORDER);
        order.addOrderDate();
        order.addPayment(payment);

        return order;
    }

    public void changeOrderStatus(OrderStatus status) {
        this.status = status;
    }

    private void addPayment(Payment payment) {
        this.payment = payment;
    }

    private void addOrderDate() {
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