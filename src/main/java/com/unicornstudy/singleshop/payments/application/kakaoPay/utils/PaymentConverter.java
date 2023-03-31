package com.unicornstudy.singleshop.payments.application.kakaoPay.utils;

import com.unicornstudy.singleshop.carts.application.dto.ReadCartResponseDto;
import com.unicornstudy.singleshop.user.domain.Role;

import java.util.List;

public class PaymentConverter {

    public static String convertItemName(List<ReadCartResponseDto> cart) {
        return cart.size() == 1 ? cart.get(0).getItemName() : cart.get(0).getItemName() + "외" + (cart.size() - 1) + "개의 상품";
    }

    public static String convertTotalAmount(List<ReadCartResponseDto> cart, Role role) {
        return String.valueOf(cart.stream().mapToInt(ReadCartResponseDto::getPrice).sum() + calculateDeliveryPrice(role));
    }

    private static int calculateDeliveryPrice(Role role) {
        return isSubscriber(role) ? 0 : 3000;
    }

    public static boolean isSubscriber(Role role) {
        return role == Role.SUBSCRIBER;
    }
}
