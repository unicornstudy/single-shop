package com.unicornstudy.singleshop.orders;

import com.unicornstudy.singleshop.carts.domain.Cart;
import com.unicornstudy.singleshop.carts.domain.CartItem;
import com.unicornstudy.singleshop.carts.domain.repository.CartRepository;
import com.unicornstudy.singleshop.carts.application.CartService;
import com.unicornstudy.singleshop.config.TestSetting;
import com.unicornstudy.singleshop.delivery.domain.Address;
import com.unicornstudy.singleshop.delivery.domain.Delivery;
import com.unicornstudy.singleshop.delivery.domain.DeliveryStatus;
import com.unicornstudy.singleshop.exception.orders.*;
import com.unicornstudy.singleshop.items.domain.Items;
import com.unicornstudy.singleshop.items.application.OptimisticLockQuantityFacade;
import com.unicornstudy.singleshop.orders.application.OrderService;
import com.unicornstudy.singleshop.orders.application.dto.OrderDto;
import com.unicornstudy.singleshop.orders.domain.*;
import com.unicornstudy.singleshop.orders.domain.repository.OrderItemRepository;
import com.unicornstudy.singleshop.orders.domain.repository.OrderRepository;
import com.unicornstudy.singleshop.payments.domain.Payment;
import com.unicornstudy.singleshop.user.domain.User;
import com.unicornstudy.singleshop.user.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@WithMockUser(roles = "USER")
public class OrderServiceTest {

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OptimisticLockQuantityFacade optimisticLockQuantityFacade;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartService cartService;

    private OrderService orderService;

    private User user;
    private Items item;
    private Cart cart;
    private Orders order;
    private OrderItem orderItem;
    private List<OrderItem> orderItems = new ArrayList<>();
    private List<Orders> orders = new ArrayList<>();
    private CartItem cartItem;
    private Pageable pageable;
    private Payment payment;
    private Delivery delivery;
    private Address address;

    @BeforeEach
    public void setUp() {
        payment = TestSetting.setPayment();
        item = TestSetting.setItem();
        address = TestSetting.setAddress();
        pageable = TestSetting.setPageable();
        delivery = TestSetting.setDelivery(address);
        user = TestSetting.setUser(address);
        orderItem = TestSetting.setOrderItem(item);
        order = TestSetting.setOrder(user, orderItem, payment, delivery);
        orders = TestSetting.setOrders(order);
        cartItem = TestSetting.setCartItem(item);
        cart = TestSetting.setCart(user, cartItem);
        orderService = new OrderService(userRepository, orderRepository, cartRepository, orderItemRepository, cartService, optimisticLockQuantityFacade);
    }

    @Test
    public void 주문_상세_조회() {
        when(orderItemRepository.findAllByOrderId(any(Long.class), any(Pageable.class))).thenReturn(orderItems);
        orderService.findOrderDetailsById(order.getId(), pageable);
    }

    @Test
    public void 주문_조회() {
        when(orderRepository.findAllByUserEmail(any(String.class), any(Pageable.class))).thenReturn(orders);
        List<OrderDto> result = orderService.findOrdersByUser(user.getEmail(), pageable);
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void 주문() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.ofNullable(user));
        when(cartRepository.findCartByUserEmail(user.getEmail())).thenReturn(Optional.ofNullable(cart));
        when(orderRepository.save(any(Orders.class))).thenReturn(order);

        Long result = orderService.order(user.getEmail(), payment);
        assertThat(result).isEqualTo(order.getId());
    }

    @Test
    public void 주문_취소() {
        when(orderRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(order));
        OrderDto orderDto = orderService.cancel(order.getId());
        assertThat(orderDto.getPayment().getTid()).isEqualTo(payment.getTid());
    }

    @Test
    public void 재주문() {
        when(orderRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(order));

        orderService.reOrder(user.getEmail(), order.getId());
        assertThat(user.getCart().getCartItems().size()).isEqualTo(order.getOrderItems().size());
    }

    @Test
    public void 배송지_예외() {
        user.updateAddress(null);
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.ofNullable(user));
        assertThatThrownBy(() -> orderService.order(user.getEmail(), payment))
                .isInstanceOf(EmptyAddressException.class)
                .hasMessage(EmptyAddressException.ERROR_MESSAGE);

    }

    @Test
    public void 배송_출발_주문취소_예외() {
        order.getDelivery().changeDeliveryStatus(DeliveryStatus.START);
        when(orderRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(order));
        assertThatThrownBy(() -> orderService.cancel(order.getId()))
                .isInstanceOf(DeliveryStartException.class)
                .hasMessage(DeliveryStartException.ERROR_MESSAGE);
    }

    @Test
    public void 주문_재고_부족_예외() {
        item.subtractQuantity();
        assertThatThrownBy(() -> item.subtractQuantity())
                .isInstanceOf(ItemQuantityException.class)
                .hasMessage(item.getName() + ItemQuantityException.ERROR_MESSAGE);
    }

    @Test
    public void 주문_상태_예외() {
        order.changeOrderStatus(OrderStatus.CANCEL);
        when(orderRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(order));
        assertThatThrownBy(() -> orderService.cancel(order.getId()))
                .isInstanceOf(OrderStatusException.class)
                .hasMessage(OrderStatusException.ERROR_MESSAGE);
    }

    @Test
    public void 주문_조회_예외() {
        assertThatThrownBy(() -> orderService.cancel(order.getId()))
                .isInstanceOf(OrderNotFoundException.class)
                .hasMessage(OrderNotFoundException.ERROR_MESSAGE);
    }
}