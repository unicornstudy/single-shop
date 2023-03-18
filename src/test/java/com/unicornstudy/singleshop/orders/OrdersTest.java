package com.unicornstudy.singleshop.orders;

import com.unicornstudy.singleshop.carts.Cart;
import com.unicornstudy.singleshop.carts.CartItem;
import com.unicornstudy.singleshop.delivery.Address;
import com.unicornstudy.singleshop.delivery.Delivery;
import com.unicornstudy.singleshop.items.Items;
import com.unicornstudy.singleshop.user.Role;
import com.unicornstudy.singleshop.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OrdersTest {

    private final String name = "test";
    private final String email = "testEmail";
    private final String picture = "testPicture";
    private final Integer price = 10;
    private final String description = "description";
    private final Integer quantity = 1;

    private User user;
    private Items item;
    private Cart cart;
    private Orders order;
    private OrderItem orderItem;
    private List<Cart> cartList = new ArrayList<>();
    private List<OrderItem> orderItems = new ArrayList<>();
    private List<Orders> orders = new ArrayList<>();
    private CartItem cartItem;
    private Payment payment;
    private Delivery delivery;
    private Address address;

    @BeforeEach
    void setUp() {
        setPayment();
        setItem();
        setAddress();
        setDelivery();
        setUser();
        setOrder();
        setCart();
    }

    @Test
    void 주문_필드_테스트() {
        assertThat(order.getId()).isEqualTo(1L);
        assertThat(order.getOrderDate()).isBefore(LocalDateTime.now());
        assertThat(order.getStatus()).isEqualTo(OrderStatus.ORDER);
    }

    @Test
    void 주문_생성시간_테스트() {
        assertThat(order.getOrderDate()).isBefore(LocalDateTime.now());
    }

    @Test
    void 주문상품_추가_테스트() {
        order.addOrderItem(orderItem);
        orderItem.initializeOrder(order);

        assertThat(order.getOrderItems().size()).isEqualTo(2);
    }

    @Test
    void 주문_상태_변경_테스트() {
        order.changeOrderStatus(OrderStatus.CANCEL);

        assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCEL);
    }

    @Test
    void 배송_취소_테스트() {
        order.cancelDelivery();

        assertThat(order.getDelivery()).isNull();
    }

    private void setItem() {
        item = Items.builder()
                .id(1L)
                .name(name)
                .price(price)
                .description(description)
                .quantity(quantity)
                .build();
    }

    private void setUser() {
        user = User.builder()
                .id(1L)
                .name(name)
                .email(email)
                .picture(picture)
                .role(Role.USER)
                .address(address)
                .orders(orders)
                .cart(cart)
                .build();
    }

    private void setCart() {
        cartItem = CartItem.createCartItem(item);
        cart = Cart.createCart(user, cartItem);
        cartList.add(cart);
    }

    private void setAddress() {
        address = new Address("testCity", "testStreet", "testZipcode");
    }

    private void setDelivery() {
        delivery = Delivery.createDelivery(address);
    }

    private void setOrder() {
        orderItem = OrderItem.createOrderItem(item);
        orderItems.add(orderItem);
        order = Orders.createOrder(user, orderItems, payment, delivery);
        order.createIdForTest(1L);
    }

    private void setPayment() {
        payment = Payment.builder().paymentKind("test").tid("test").build();
    }
}