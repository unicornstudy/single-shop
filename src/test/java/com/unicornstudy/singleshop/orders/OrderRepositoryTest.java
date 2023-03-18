package com.unicornstudy.singleshop.orders;

import com.unicornstudy.singleshop.carts.Cart;
import com.unicornstudy.singleshop.carts.CartItem;
import com.unicornstudy.singleshop.carts.CartItemRepository;
import com.unicornstudy.singleshop.carts.CartRepository;
import com.unicornstudy.singleshop.delivery.Address;
import com.unicornstudy.singleshop.delivery.Delivery;
import com.unicornstudy.singleshop.items.Items;
import com.unicornstudy.singleshop.items.ItemsRepository;
import com.unicornstudy.singleshop.orders.exception.OrderNotFoundException;
import com.unicornstudy.singleshop.user.Role;
import com.unicornstudy.singleshop.user.User;
import com.unicornstudy.singleshop.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ItemsRepository itemsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

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
    private Pageable pageable;
    private Payment payment;
    private Delivery delivery;
    private Address address;

    @BeforeEach
    void setUp() {
        setPayment();
        setItem();
        setAddress();
        setPageable();
        setDelivery();
        setUser();
        setOrder();
        setCart();

        itemsRepository.save(item);
        userRepository.save(user);
        cartItemRepository.save(cartItem);
        cartRepository.save(cart);
        orderItemRepository.save(orderItem);
        orderRepository.save(order);
    }

    @Test
    void 주문_저장_테스트() {
        Orders foundOrder = orderRepository.findById(order.getId()).orElseThrow(OrderNotFoundException::new);

        assertThat(foundOrder).isNotNull();
    }

    @Test
    void 주문_조회_테스트() {
        List<Orders> foundOrders = orderRepository.findAllByUserEmail(user.getEmail(), pageable);

        assertThat(foundOrders.size()).isEqualTo(orders.size());
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

    private void setPageable() {
        pageable = PageRequest.of(0, 10, Sort.by("id").descending());
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
    }

    private void setPayment() {
        payment = Payment.builder().paymentKind("test").tid("test").build();
    }
}