package com.unicornstudy.singleshop.orders.domain;

import com.unicornstudy.singleshop.carts.domain.Cart;
import com.unicornstudy.singleshop.carts.domain.CartItem;
import com.unicornstudy.singleshop.config.TestSetting;
import com.unicornstudy.singleshop.delivery.domain.Address;
import com.unicornstudy.singleshop.delivery.domain.Delivery;
import com.unicornstudy.singleshop.items.domain.Items;
import com.unicornstudy.singleshop.payments.domain.Payment;
import com.unicornstudy.singleshop.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OrdersTest {

    private User user;
    private Address address;
    private Items item;
    private Cart cart;
    private CartItem cartItem;
    private Orders order;
    private OrderItem orderItem;
    private Payment payment;
    private Delivery delivery;
    private List<Cart> carts = new ArrayList<>();
    private List<OrderItem> orderItems = new ArrayList<>();
    private List<Orders> orders = new ArrayList<>();

    @BeforeEach
    void setUp() {
        address = TestSetting.setAddress();
        user = TestSetting.setUser(address);
        item = TestSetting.setItem();
        cartItem = TestSetting.setCartItem(item);
        cart = TestSetting.setCart(user, cartItem);
        payment = TestSetting.setPayment();
        delivery = TestSetting.setDelivery(address);
        orderItem = TestSetting.setOrderItem(item);
        order = TestSetting.setOrder(user, orderItem, payment, delivery);
    }

    @DisplayName("주문 생성 시간 주입 테스트")
    @Test
    void order_withCreatedDate_test() {
        assertThat(order.getOrderDate()).isBeforeOrEqualTo(LocalDateTime.now());
    }
}