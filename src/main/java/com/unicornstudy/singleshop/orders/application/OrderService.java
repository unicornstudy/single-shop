package com.unicornstudy.singleshop.orders.application;

import com.unicornstudy.singleshop.carts.domain.Cart;
import com.unicornstudy.singleshop.carts.domain.repository.CartRepository;
import com.unicornstudy.singleshop.carts.application.CartService;
import com.unicornstudy.singleshop.exception.carts.CartNotFoundException;
import com.unicornstudy.singleshop.exception.carts.SessionExpiredException;
import com.unicornstudy.singleshop.delivery.domain.Delivery;
import com.unicornstudy.singleshop.items.domain.Items;
import com.unicornstudy.singleshop.items.application.OptimisticLockQuantityFacade;
import com.unicornstudy.singleshop.orders.domain.OrderStatus;
import com.unicornstudy.singleshop.payments.domain.Payment;
import com.unicornstudy.singleshop.orders.application.dto.OrderDetailDto;
import com.unicornstudy.singleshop.orders.application.dto.OrderDto;
import com.unicornstudy.singleshop.orders.domain.OrderItem;
import com.unicornstudy.singleshop.orders.domain.repository.OrderItemRepository;
import com.unicornstudy.singleshop.orders.domain.repository.OrderRepository;
import com.unicornstudy.singleshop.orders.domain.Orders;
import com.unicornstudy.singleshop.exception.orders.OrderExceptionCheckFactory;
import com.unicornstudy.singleshop.exception.orders.OrderNotFoundException;
import com.unicornstudy.singleshop.user.domain.User;
import com.unicornstudy.singleshop.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartService cartService;
    private final OptimisticLockQuantityFacade optimisticLockQuantityFacade;

    @Transactional(readOnly = true)
    public List<OrderDto> findOrdersByUser(String userEmail, Pageable pageable) {
        return orderRepository.findAllByUserEmail(userEmail, pageable)
                .stream()
                .map(orders -> OrderDto.createOrderDto(orders))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrderDetailDto> findOrderDetailsById(Long id, Pageable pageable) {
        return orderItemRepository.findAllByOrderId(id, pageable)
                .stream()
                .map(orderItems -> OrderDetailDto.createOrderDetailDto(orderItems))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OrderDto findOrderById(Long id) {
        return orderRepository.findById(id)
                .map(entity -> OrderDto.createOrderDto(entity))
                .orElseThrow(() -> new OrderNotFoundException());
    }

    @Transactional
    public Long order(String userEmail, Payment payment) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new SessionExpiredException());
        OrderExceptionCheckFactory.checkAddress(user);
        Cart cart = cartRepository.findCartByUserEmail(userEmail).orElseThrow(() -> new CartNotFoundException());
        List<Items> items = cart.getCartItems().stream().map(cartItem -> cartItem.getItem()).collect(Collectors.toList());
        Orders order = Orders.createOrder(user, convertCartItemToOrderItem(items), payment, Delivery.createDelivery(user.getAddress()));
        user.resetCart();

        return orderRepository.save(order).getId();
    }

    private List<OrderItem> convertCartItemToOrderItem(List<Items> items) {
        List<OrderItem> orderItems = new ArrayList<>();
        for (Items item : items) {
            optimisticLockQuantityFacade.subtractQuantity(item.getId());
            orderItems.add(OrderItem.createOrderItem(item));
        }
        return orderItems;
    }

    @Transactional
    public void handleOrderPaymentError(Long id) {
        Orders order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException());
        OrderExceptionCheckFactory.checkOrderStatus(order);
        order.cancelDelivery();
        changeOrderStatusAndQuantity(order);
    }

    @Transactional
    public OrderDto cancel(Long id) {
        Orders order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException());
        OrderExceptionCheckFactory.checkOrderStatus(order);
        OrderExceptionCheckFactory.checkDelivery(order);
        order.cancelDelivery();
        changeOrderStatusAndQuantity(order);

        return OrderDto.createOrderDto(order);
    }

    private void changeOrderStatusAndQuantity(Orders order) {
        order.changeOrderStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem: order.getOrderItems()) {
            optimisticLockQuantityFacade.addQuantity(orderItem.getItem().getId());
        }
    }

    @Transactional
    public void reOrder(String userEmail, Long id) {
        Orders order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException());
        order.getOrderItems().stream()
                .map(orderItem -> orderItem.getItem())
                .forEach(item -> cartService.addCart(userEmail, item.getId()));
    }
}