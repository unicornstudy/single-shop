package com.unicornstudy.singleshop.orders;

import com.unicornstudy.singleshop.carts.Cart;
import com.unicornstudy.singleshop.carts.CartRepository;
import com.unicornstudy.singleshop.carts.exception.CartNotFoundException;
import com.unicornstudy.singleshop.carts.exception.SessionExpiredException;
import com.unicornstudy.singleshop.delivery.Delivery;
import com.unicornstudy.singleshop.items.Items;
import com.unicornstudy.singleshop.items.OptimisticLockQuantityFacade;
import com.unicornstudy.singleshop.orders.dto.OrderDetailDto;
import com.unicornstudy.singleshop.orders.dto.OrderDto;
import com.unicornstudy.singleshop.orders.exception.OrderExceptionCheckFactory;
import com.unicornstudy.singleshop.orders.exception.OrderNotFoundException;
import com.unicornstudy.singleshop.user.User;
import com.unicornstudy.singleshop.user.UserRepository;
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
    public Long order(String userEmail, Payment payment) throws InterruptedException {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new SessionExpiredException());
        OrderExceptionCheckFactory.checkAddress(user);
        Cart cart = cartRepository.findCartByUserEmail(userEmail).orElseThrow(() -> new CartNotFoundException());
        List<Items> items = cart.getCartItems().stream().map(cartItem -> cartItem.getItem()).collect(Collectors.toList());
        Orders order = Orders.createOrder(user, convertCartItemToOrderItem(items), payment, Delivery.createDelivery(user.getAddress()));
        user.resetCart();

        return orderRepository.save(order).getId();
    }

    private List<OrderItem> convertCartItemToOrderItem(List<Items> items) throws InterruptedException {
        List<OrderItem> orderItems = new ArrayList<>();
        for (Items item : items) {
            optimisticLockQuantityFacade.subtractQuantity(item.getId());
            orderItems.add(OrderItem.createOrderItem(item));
        }
        return orderItems;
    }

    @Transactional
    public void handleOrderPaymentError(Long id) throws InterruptedException {
        Orders order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException());
        OrderExceptionCheckFactory.checkOrderStatus(order);
        order.cancelDelivery();
        changeOrderStatusAndQuantity(order);
    }

    @Transactional
    public OrderDto cancel(Long id) throws InterruptedException {
        Orders order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException());
        OrderExceptionCheckFactory.checkOrderStatus(order);
        OrderExceptionCheckFactory.checkDelivery(order);
        order.cancelDelivery();
        changeOrderStatusAndQuantity(order);

        return OrderDto.createOrderDto(order);
    }

    private void changeOrderStatusAndQuantity(Orders order) throws InterruptedException {
        order.changeOrderStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem: order.getOrderItems()) {
            optimisticLockQuantityFacade.addQuantity(orderItem.getItem().getId());
        }

    }
}