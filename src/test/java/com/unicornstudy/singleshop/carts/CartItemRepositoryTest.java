package com.unicornstudy.singleshop.carts;

import com.unicornstudy.singleshop.carts.exception.CartItemNotFoundException;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        setUser();
        setPageable();

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
    void 장바구니_상품_저장_테스트() {
        CartItem foundCartItem = cartItemRepository.findById(newCartItem.getId()).orElseThrow(CartItemNotFoundException::new);

        assertThat(foundCartItem).isNotNull();
    }

    @Test
    void 장바구니_ID로_전체조회_테스트() {
        List<CartItem> foundCartItems = cartItemRepository.findAllByCartId(newCart.getId(), pageable);

        assertThat(foundCartItems.size()).isEqualTo(newCart.getCount());
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

    private void setPageable() {
        pageable = PageRequest.of(0, 10, Sort.by("id").descending());
    }
}