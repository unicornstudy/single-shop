package com.unicornstudy.singleshop.carts;

import com.unicornstudy.singleshop.carts.exception.CartNotFoundException;
import com.unicornstudy.singleshop.items.Items;
import com.unicornstudy.singleshop.items.ItemsRepository;
import com.unicornstudy.singleshop.user.Role;
import com.unicornstudy.singleshop.user.User;
import com.unicornstudy.singleshop.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class CartRepositoryTest {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemsRepository itemsRepository;

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

        itemsRepository.save(newItem);

        newCartItem = CartItem.createCartItem(newItem);

        cartItemRepository.save(newCartItem);

        userRepository.save(user);

        newCart = Cart.createCart(user, newCartItem);

        cartRepository.save(newCart);
    }

    @Test
    void 장바구니_저장_테스트() {
        Cart foundCart = cartRepository.findById(newCart.getId()).orElseThrow(CartNotFoundException::new);

        assertThat(foundCart).isNotNull();
    }

    @Test
    void 장바구니_유저이메일로_조회_테스트() {
        Cart foundCart = cartRepository.findCartByUserEmail(user.getEmail()).orElseThrow(CartNotFoundException::new);

        assertThat(foundCart).isNotNull();
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