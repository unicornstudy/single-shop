package com.unicornstudy.singleshop.config;

import com.unicornstudy.singleshop.carts.Cart;
import com.unicornstudy.singleshop.carts.CartItem;
import com.unicornstudy.singleshop.delivery.Address;
import com.unicornstudy.singleshop.delivery.Delivery;
import com.unicornstudy.singleshop.items.Items;
import com.unicornstudy.singleshop.orders.OrderItem;
import com.unicornstudy.singleshop.orders.Orders;
import com.unicornstudy.singleshop.orders.Payment;
import com.unicornstudy.singleshop.user.Role;
import com.unicornstudy.singleshop.user.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public class TestSetting {

    public static Items setItem() {
        return Items.builder()
                .id(1L)
                .name("test")
                .price(10)
                .description("description")
                .quantity(1)
                .build();
    }

    public static User setUser(Address address) {
        return User.builder()
                .id(1L)
                .name("test")
                .email("testEmail")
                .picture("testPicture")
                .role(Role.USER)
                .orders(new ArrayList<>())
                .address(address)
                .build();
    }

    public static Cart setCart(User user, CartItem cartItem) {
        return Cart.createCart(user, cartItem);
    }

    public static CartItem setCartItem(Items item) {
        CartItem cartItem = CartItem.createCartItem(item);
        cartItem.createIdForTest(1L);
        return cartItem;
    }

    public static Pageable setPageable() {
        return PageRequest.of(0, 10, Sort.by("id").descending());
    }

    public static Address setAddress() {
        return new Address("testCity", "testStreet", "testZipcode");
    }

    public static Delivery setDelivery(Address address) {
        return Delivery.createDelivery(address);
    }

    public static Orders setOrder(User user, OrderItem orderItem, Payment payment, Delivery delivery) {
        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(orderItem);
        Orders order = Orders.createOrder(user, orderItems, payment, delivery);
        order.createIdForTest(1L);

        return order;
    }

    public static Payment setPayment() {
        return Payment.builder().paymentKind("test").tid("test").build();
    }

    public static OrderItem setOrderItem(Items item) {
        return OrderItem.createOrderItem(item);
    }

    public static List<OrderItem> setOrderItems(OrderItem orderItem) {
        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(orderItem);
        return orderItems;
    }

    public static List<Orders> setOrders(Orders order) {
        List<Orders> orders = new ArrayList<>();
        orders.add(order);
        return orders;
    }
}
