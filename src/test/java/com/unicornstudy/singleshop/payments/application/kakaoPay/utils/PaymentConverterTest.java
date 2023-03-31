package com.unicornstudy.singleshop.payments.application.kakaoPay.utils;

import com.unicornstudy.singleshop.carts.application.dto.ReadCartResponseDto;
import com.unicornstudy.singleshop.carts.domain.CartItem;
import com.unicornstudy.singleshop.config.TestSetting;
import com.unicornstudy.singleshop.items.domain.Items;
import com.unicornstudy.singleshop.user.domain.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class PaymentConverterTest {

    private List<ReadCartResponseDto> carts = new ArrayList<>();
    private CartItem cartItem;
    private Role role;

    @BeforeEach
    void setUp() {
        //given
        cartItem = TestSetting.setCartItem(Items.builder().id(1L).name("test").description("test").price(2).quantity(3).build());
        carts.add(new ReadCartResponseDto(cartItem));
        role = Role.SUBSCRIBER;
    }

    @DisplayName("장바구니를 상품 이름으로 변화시키는 테스트")
    @Test
    void convertItemName_테스트() {
        //when
        String result = PaymentConverter.convertItemName(carts);

        //then
        assertThat(result).isEqualTo(cartItem.getItem().getName());
    }
    @DisplayName("장바구니 상품 가격 계산 테스트")
    @Test
    void convertTotalAmount_테스트() {
        //when
        String result = PaymentConverter.convertTotalAmount(carts, role);

        //then
        assertThat(result).isEqualTo(String.valueOf(cartItem.getItem().getPrice()));
    }
}