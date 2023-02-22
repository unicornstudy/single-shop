package com.unicornstudy.singleshop.orders;

import com.unicornstudy.singleshop.carts.Cart;
import com.unicornstudy.singleshop.carts.CartItem;
import com.unicornstudy.singleshop.carts.CartRepository;
import com.unicornstudy.singleshop.delivery.Address;
import com.unicornstudy.singleshop.delivery.Delivery;
import com.unicornstudy.singleshop.delivery.DeliveryStatus;
import com.unicornstudy.singleshop.items.Items;
import com.unicornstudy.singleshop.items.OptimisticLockQuantityFacade;
import com.unicornstudy.singleshop.orders.dto.OrderDto;
import com.unicornstudy.singleshop.orders.exception.*;
import com.unicornstudy.singleshop.user.Role;
import com.unicornstudy.singleshop.user.User;
import com.unicornstudy.singleshop.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    private OrderService orderService;

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
    public void setUp() {
        setPayment();
        setItem();
        setAddress();
        setPageable();
        setDelivery();
        setUser();
        setOrder();
        setCart();
        orderService = new OrderService(userRepository, orderRepository, cartRepository, orderItemRepository, optimisticLockQuantityFacade);
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
    public void 주문() throws InterruptedException {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.ofNullable(user));
        when(cartRepository.findCartByUserEmail(user.getEmail())).thenReturn(Optional.ofNullable(cart));
        when(orderRepository.save(any(Orders.class))).thenReturn(order);

        Long result = orderService.order(user.getEmail(), payment);
        assertThat(result).isEqualTo(order.getId());
    }

    @Test
    public void 주문_취소() throws InterruptedException {
        when(orderRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(order));
        OrderDto orderDto = orderService.cancel(order.getId());
        assertThat(orderDto.getPayment().getTid()).isEqualTo(payment.getTid());
    }

    @Test
    public void 배송지_예외() throws InterruptedException {
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
        order.createIdForTest(1L);
    }

    private void setPayment() {
        payment = Payment.builder().paymentKind("test").tid("test").build();
    }
}