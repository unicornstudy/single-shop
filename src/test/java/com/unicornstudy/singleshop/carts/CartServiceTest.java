package com.unicornstudy.singleshop.carts;

import com.unicornstudy.singleshop.carts.dto.ReadCartResponseDto;
import com.unicornstudy.singleshop.items.Items;
import com.unicornstudy.singleshop.items.ItemsRepository;
import com.unicornstudy.singleshop.user.Role;
import com.unicornstudy.singleshop.user.User;
import com.unicornstudy.singleshop.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@WithMockUser(roles = "USER")
public class CartServiceTest {

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemsRepository itemsRepository;

    private final String name = "test";
    private final String email = "testEmail";
    private final String picture = "testPicture";
    private final Integer price = 10;
    private final String description = "description";
    private final Integer quantity = 10;

    private User user;
    private Items item;
    private CartService cartService;
    private Cart cart;
    private List<Cart> cartList = new ArrayList<>();
    private CartItem cartItem;

    @BeforeEach
    public void setUp() {
        setItem();
        setUser();
        setCart();
        cartService = new CartService(cartRepository, cartItemRepository, userRepository, itemsRepository);
    }

    @Test
    public void 장바구니_생성() {
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.ofNullable(user));
        when(itemsRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(item));
        when(cartRepository.findByUser(any(String.class))).thenReturn(Optional.ofNullable(cart));
        when(cartRepository.findAll()).thenReturn(cartList);

        cartService.addCart(user.getEmail(), item.getId());

        Cart result = cartRepository.findAll().get(0);
        assertThat(result.getUser()).isEqualTo(user);
        assertThat(result.getCartItems().get(0)).isEqualTo(cartItem);
        assertThat(result.getCartItems().get(0).getItem()).isEqualTo(item);
    }

    @Test
    public void 장바구니_삭제() {
        when(cartRepository.findAll()).thenReturn(cartList);
        when(cartRepository.findByUser(any(String.class))).thenReturn(Optional.ofNullable(cart));
        when(cartItemRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(cartItem));

        cartService.removeCartItem(user.getEmail(), item.getId());

        Cart result = cartRepository.findAll().get(0);
        assertThat(result.getCartItems().size()).isEqualTo(0);
    }

    @Test
    public void 장바구니_조회() {
        when(cartRepository.findByUser(any(String.class))).thenReturn(Optional.ofNullable(user.getCart()));

        List<ReadCartResponseDto> result = cartService.findCartItemListByUser(user.getEmail());
        assertThat(result.size()).isEqualTo(1);
    }

    private void setItem() {
        item = Items.builder()
                .id(1L)
                .name(name)
                .price(price)
                .description(description)
                .quantity(quantity)
                .build();
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

    private void setCart() {
        cartItem = CartItem.createCartItem(item);
        cart = Cart.createCart(user, cartItem);
        cartList.add(cart);
    }
}