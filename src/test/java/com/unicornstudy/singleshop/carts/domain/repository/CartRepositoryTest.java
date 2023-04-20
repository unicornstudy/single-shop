package com.unicornstudy.singleshop.carts.domain.repository;

import com.unicornstudy.singleshop.carts.domain.Cart;
import com.unicornstudy.singleshop.carts.domain.CartItem;
import com.unicornstudy.singleshop.config.TestSetting;
import com.unicornstudy.singleshop.delivery.domain.Address;
import com.unicornstudy.singleshop.exception.carts.CartNotFoundException;
import com.unicornstudy.singleshop.items.domain.Items;
import com.unicornstudy.singleshop.items.domain.repository.ItemsRepository;
import com.unicornstudy.singleshop.user.domain.User;
import com.unicornstudy.singleshop.user.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

    private Items item;
    private CartItem cartItem;
    private User user;
    private Address address;
    private Cart cart;

    @BeforeEach
    void setUp() {
        address = TestSetting.setAddress();
        user = TestSetting.setUser(address);

        userRepository.save(user);

        item = TestSetting.setItem();

        itemsRepository.save(item);

        cartItem = TestSetting.setCartItemWithoutId(item);

        cartItemRepository.save(cartItem);

        cart = TestSetting.setCart(user, cartItem);

        cartRepository.save(cart);
    }

    @DisplayName("장바구니 데이터 저장 테스트")
    @Test
    void save_cart_test() {
        //given
        Cart newCart = TestSetting.setCart(user, cartItem);

        cartRepository.save(newCart);

        //when
        Cart foundCart = cartRepository.findById(newCart.getId())
                .orElseThrow(CartNotFoundException::new);

        //then
        assertThat(foundCart).isNotNull();
        assertThat(foundCart.getId()).isEqualTo(newCart.getId());
    }

    @DisplayName("유저 이메일을 통한 장바구니 조회 테스트")
    @Test
    void findByUserEmail_test() {
        Cart foundCart = cartRepository.findCartByUserEmail(user.getEmail())
                .orElseThrow(CartNotFoundException::new);

        assertThat(foundCart).isNotNull();
        assertThat(foundCart.getUser()).isEqualTo(user);
    }

    @DisplayName("잘못된 유저 이메일을 통한 장바구니 조회 예외 테스트")
    @Test
    void findByUserEmail_exception_test() {
        assertThat(cartRepository.findCartByUserEmail("invalidUser")).isEmpty();
    }
}