package com.unicornstudy.singleshop.user;

import com.unicornstudy.singleshop.carts.Cart;
import com.unicornstudy.singleshop.carts.CartItem;
import com.unicornstudy.singleshop.delivery.Address;
import com.unicornstudy.singleshop.items.Items;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    private final String name = "test";
    private final String email = "testEmail";
    private final String picture = "testPicture";
    private final String itemName = "testName";
    private final Integer itemPrice = 1000;
    private final String itemDescription = "testDescription";
    private final Integer itemQuantity = 10;

    private Items newItem;
    private CartItem newCartItem;
    private User user;
    private Cart newCart;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name(name)
                .email(email)
                .picture(picture)
                .role(Role.USER)
                .build();

        newItem = Items.builder()
                .name(itemName)
                .price(itemPrice)
                .description(itemDescription)
                .quantity(itemQuantity)
                .build();

        newCartItem = CartItem.createCartItem(newItem);

        newCart = Cart.createCart(user, newCartItem);
    }

    @Test
    void 유저_생성_테스트() {
        assertThat(user).isNotNull();
        assertThat(user.getName()).isEqualTo(name);
        assertThat(user.getEmail()).isEqualTo(email);
    }

    @Test
    void 유저_수정_테스트() {
        String updatedName = "updatedName";
        String updatedPicture = "updatedPicture";

        user.update(updatedName, updatedPicture);

        assertThat(user.getName()).isEqualTo(updatedName);
        assertThat(user.getPicture()).isEqualTo(updatedPicture);
    }

    @Test
    void 유저_필드_테스트() {
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getRole()).isEqualTo(Role.USER);
        assertThat(user.getRoleKey()).isEqualTo(Role.USER.getKey());
    }

    @Test
    void 유저_주소_수정_테스트() {
        Address address = new Address("testCity", "testStreet", "testZipcode");

        user.updateAddress(address);

        assertThat(user.getAddress()).isEqualTo(address);
    }

    @Test
    void 유저_카트_초기화_테스트() {
        user.initializeCart(newCart);

        user.resetCart();

        assertThat(user.getCart()).isNull();
    }

    @Test
    void 유저_역할_키_테스트() {
        String roleKey = user.getRoleKey();

        assertThat(roleKey).isEqualTo(Role.USER.getKey());
    }
}