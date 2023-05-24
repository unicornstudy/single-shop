package com.unicornstudy.singleshop.payments.application.kakaoPay.utils;

import com.unicornstudy.singleshop.carts.application.dto.CartResponseDto;
import com.unicornstudy.singleshop.user.domain.Role;

import java.util.List;

import static com.unicornstudy.singleshop.subscription.application.utils.DateCalculator.PRICE;
import static com.unicornstudy.singleshop.subscription.application.utils.DateCalculator.SUBSCRIPTION_USER_DELIVERY_PRICE;

public class CartToPaymentItemConverter {

    public static String convertItemName(List<CartResponseDto> cart) {
        return cart.size() == 1 ? cart.get(0).getItemName() : cart.get(0).getItemName() + "외" + (cart.size() - 1) + "개의 상품";
    }

    public static String convertTotalAmount(List<CartResponseDto> cart, Role role) {
        return String.valueOf(cart.stream().mapToInt(CartResponseDto::getPrice).sum() + calculateDeliveryPrice(role));
    }

    private static int calculateDeliveryPrice(Role role) {
        return isSubscriber(role) ? Integer.valueOf(SUBSCRIPTION_USER_DELIVERY_PRICE) : Integer.valueOf(PRICE);
    }

    public static boolean isSubscriber(Role role) {
        return role == Role.SUBSCRIBER;
    }
}
