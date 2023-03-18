package com.unicornstudy.singleshop.carts;

import com.unicornstudy.singleshop.items.Items;
import com.unicornstudy.singleshop.user.Role;
import com.unicornstudy.singleshop.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CartItemTest {

    private final String itemName = "testName";
    private final Integer itemPrice = 1000;
    private final String itemDescription = "testDescription";
    private final Integer itemQuantity = 10;
    private final String name = "test";
    private final String email = "testEmail";
    private final String picture = "testPicture";

    private Items newItem;
    private CartItem newCartItem;
    private User user;
    private Cart newCart;

    @BeforeEach
    void setUp() {
        setUser();

        newItem = Items.builder()
                .name(itemName)
                .price(itemPrice)
                .description(itemDescription)
                .quantity(itemQuantity)
                .build();

        newCartItem = CartItem.createCartItem(newItem);

        newCart = Cart.createCart(user, newCartItem);

        newCartItem.initializeCart(newCart);
    }

    @Test
    void 장바구니_상품_필드_테스트() {
        assertThat(newCartItem.getCart()).isNotNull();
        assertThat(newCartItem.getItem().getName()).isEqualTo(itemName);
    }

    private void setUser() {
        user = User.builder()
                .id(1L)
                .name(name)
                .email(email)
                .picture(picture)
                .role(Role.USER)
                .build();
    }
}