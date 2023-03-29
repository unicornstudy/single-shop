package com.unicornstudy.singleshop.exception.orders;

import com.unicornstudy.singleshop.delivery.domain.DeliveryStatus;
import com.unicornstudy.singleshop.items.domain.Items;
import com.unicornstudy.singleshop.orders.domain.OrderStatus;
import com.unicornstudy.singleshop.orders.domain.Orders;
import com.unicornstudy.singleshop.user.domain.User;

import java.util.Optional;

public class OrderExceptionCheckFactory {

    public static void checkAddress(User user) {
        Optional.ofNullable(user.getAddress()).orElseThrow(() -> new EmptyAddressException());
    }

    public static void checkDelivery(Orders order) {
        if (order.getDelivery().getStatus() != DeliveryStatus.READY) {
            throw new DeliveryStartException();
        }
    }

    public static void checkQuantity(Items item) {
        if (item.getQuantity() == 0) {
            throw new ItemQuantityException(item.getName());
        }
    }

    public static void checkOrderStatus(Orders order) {
        if (order.getStatus() == OrderStatus.CANCEL) {
            throw new OrderStatusException();
        }
    }
}
