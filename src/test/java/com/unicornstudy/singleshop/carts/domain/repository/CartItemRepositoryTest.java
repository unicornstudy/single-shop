package com.unicornstudy.singleshop.carts.domain.repository;

import com.unicornstudy.singleshop.carts.domain.Cart;
import com.unicornstudy.singleshop.carts.domain.CartItem;
import com.unicornstudy.singleshop.config.TestSetting;
import com.unicornstudy.singleshop.delivery.domain.Address;
import com.unicornstudy.singleshop.exception.carts.CartItemNotFoundException;
import com.unicornstudy.singleshop.items.domain.Items;
import com.unicornstudy.singleshop.items.domain.repository.ItemsRepository;
import com.unicornstudy.singleshop.user.domain.User;
import com.unicornstudy.singleshop.user.domain.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class CartItemRepositoryTest {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ItemsRepository itemsRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    private Items item;
    private CartItem cartItem;
    private User user;
    private Cart cart;
    private Pageable pageable;
    private Address address;
    private List<CartItem> cartItems = new ArrayList<>();

    @BeforeEach
    void setUp() {
        address = TestSetting.setAddress();
        user = TestSetting.setUser(address);
        pageable = TestSetting.setPageable();
        item = TestSetting.setItem();

        itemsRepository.save(item);

        cartItem = TestSetting.setCartItemWithoutId(item);

        cartItemRepository.save(cartItem);

        userRepository.save(user);

        cart = TestSetting.setCart(user, cartItem);

        cartRepository.save(cart);
    }

    @AfterEach
    void delete() {
        cartItemRepository.deleteAll();
        cartRepository.deleteAll();
        itemsRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DisplayName("장바구니 상품 저장 성공 테스트")
    @Test
    void cartItem_save_test() {
        //given
        CartItem newCartItem = TestSetting.setCartItemWithoutId(item);

        cartItemRepository.save(newCartItem);

        //when
        CartItem foundCartItem = cartItemRepository.findById(newCartItem.getId())
                .orElseThrow(CartItemNotFoundException::new);



        //then
        assertThat(foundCartItem).isNotNull();
        assertThat(foundCartItem.getId()).isEqualTo(newCartItem.getId());
    }

    @DisplayName("장바구니 Id로 장바구니 내에 있는 상품 전체조회 테스트")
    @Test
    void findAllByCartId_test() {
        //when
        List<CartItem> foundCartItems = cartItemRepository.findAllByCartId(cart.getId(), pageable);

        //then
        assertThat(foundCartItems.size()).isEqualTo(cart.getCount());
    }

    @DisplayName("존재하지 않는 장바구니로 장바구니 상품을 조회할 수 없다.")
    @Test
    void findAllByCartId_exception_test() {
        assertThat(cartItemRepository.findAllByCartId(cart.getId() + 1L, pageable)).isEmpty();
    }
}