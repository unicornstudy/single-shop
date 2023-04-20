package com.unicornstudy.singleshop.orders.domain.repository;

import com.unicornstudy.singleshop.carts.domain.Cart;
import com.unicornstudy.singleshop.carts.domain.CartItem;
import com.unicornstudy.singleshop.carts.domain.repository.CartItemRepository;
import com.unicornstudy.singleshop.carts.domain.repository.CartRepository;
import com.unicornstudy.singleshop.config.TestSetting;
import com.unicornstudy.singleshop.delivery.domain.Address;
import com.unicornstudy.singleshop.delivery.domain.Delivery;
import com.unicornstudy.singleshop.exception.items.ItemsException;
import com.unicornstudy.singleshop.items.domain.Items;
import com.unicornstudy.singleshop.items.domain.repository.ItemsRepository;
import com.unicornstudy.singleshop.orders.domain.OrderItem;
import com.unicornstudy.singleshop.orders.domain.Orders;
import com.unicornstudy.singleshop.payments.domain.Payment;
import com.unicornstudy.singleshop.user.domain.User;
import com.unicornstudy.singleshop.user.domain.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static com.unicornstudy.singleshop.exception.ErrorCode.BAD_REQUEST_ITEMS_READ;
import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class OrderItemRepositoryTest {

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

    @DisplayName("주문 상품 저장 테스트")
    @Test
    void save_orderItem_test() {
        OrderItem newOrderItem = TestSetting.setOrderItem(item);

        orderItemRepository.save(newOrderItem);

        OrderItem foundOrderItem = orderItemRepository.findById(newOrderItem.getId())
                .orElseThrow(() -> new ItemsException(BAD_REQUEST_ITEMS_READ));

        assertThat(foundOrderItem).isNotNull();
        assertThat(foundOrderItem.getId()).isEqualTo(newOrderItem.getId());
    }

    @DisplayName("주문 id를 통한 주문 상품 전체 조회 테스트")
    @Test
    void findAllByOrderId_tes() {
        List<OrderItem> orderItems = orderItemRepository.findAllByOrderId(order.getId(), pageable);

        assertThat(orderItems.size()).isEqualTo(order.getOrderItems().size());
        assertThat(orderItems.get(0).getName()).isEqualTo(order.getOrderItems().get(0).getName());
    }

    @DisplayName("잘못된 주문 id를 통한 주문 상품 전체 조회 예외 테스트")
    @Test
    void findAllByOrderId_exception_test() {
        assertThat(orderItemRepository.findAllByOrderId(2L, pageable)).isEmpty();
    }
}