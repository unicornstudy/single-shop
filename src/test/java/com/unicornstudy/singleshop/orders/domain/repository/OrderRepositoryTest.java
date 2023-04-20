package com.unicornstudy.singleshop.orders.domain.repository;

import com.unicornstudy.singleshop.carts.domain.Cart;
import com.unicornstudy.singleshop.carts.domain.CartItem;
import com.unicornstudy.singleshop.carts.domain.repository.CartItemRepository;
import com.unicornstudy.singleshop.carts.domain.repository.CartRepository;
import com.unicornstudy.singleshop.config.TestSetting;
import com.unicornstudy.singleshop.delivery.domain.Address;
import com.unicornstudy.singleshop.delivery.domain.Delivery;
import com.unicornstudy.singleshop.exception.carts.CartNotFoundException;
import com.unicornstudy.singleshop.exception.orders.OrderNotFoundException;
import com.unicornstudy.singleshop.items.domain.Items;
import com.unicornstudy.singleshop.items.domain.repository.ItemsRepository;
import com.unicornstudy.singleshop.orders.domain.OrderItem;
import com.unicornstudy.singleshop.orders.domain.Orders;
import com.unicornstudy.singleshop.payments.domain.Payment;
import com.unicornstudy.singleshop.user.domain.User;
import com.unicornstudy.singleshop.user.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;

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

    private User user;
    private Address address;
    private Items item;
    private Cart cart;
    private CartItem cartItem;
    private Orders order;
    private OrderItem orderItem;
    private Pageable pageable;
    private Payment payment;
    private Delivery delivery;
    private List<OrderItem> orderItems = new ArrayList<>();

    @BeforeEach
    void setUp() {
        item = TestSetting.setItem();

        itemsRepository.save(item);

        address = TestSetting.setAddress();

        user = TestSetting.setUser(address);

        userRepository.save(user);

        pageable = TestSetting.setPageable();

        payment = TestSetting.setPayment();

        delivery = TestSetting.setDelivery(address);

        cartItem = TestSetting.setCartItemWithoutId(item);

        cartItemRepository.save(cartItem);

        cart = TestSetting.setCart(user, cartItem);

        cartRepository.save(cart);

        orderItem = TestSetting.setOrderItem(item);

        orderItemRepository.save(orderItem);

        order = TestSetting.setOrder(user, orderItem, payment, delivery);

        orderRepository.save(order);
    }

    @DisplayName("주문 저장 테스트")
    @Test
    void save_order_test() {
        Orders newOrder = TestSetting.setOrder(user, orderItem, payment, delivery);

        orderRepository.save(newOrder);

        Orders foundOrder = orderRepository.findById(newOrder.getId())
                .orElseThrow(OrderNotFoundException::new);

        assertThat(foundOrder).isNotNull();
        assertThat(foundOrder.getId()).isEqualTo(newOrder.getId());
    }

    @DisplayName("유저 이메일을 통해 해당 유저의 주문을 조회한다.")
    @Test
    void findAllByUserEmail_test() {
        orderItems.add(orderItem);

        List<Orders> foundOrders = orderRepository.findAllByUserEmail(user.getEmail(), pageable);

        assertThat(foundOrders.size()).isEqualTo(orderItems.size());
    }

    @DisplayName("잘못된 유저 이메일을 통한 유저 주문 조회 예외 테스트")
    @Test
    void findAllByUserEmail_exception_test() {
        assertThat(orderRepository.findAllByUserEmail("invalidUser", pageable)).isEmpty();
    }
}