package com.unicornstudy.singleshop.carts;

import com.unicornstudy.singleshop.carts.application.CartService;
import com.unicornstudy.singleshop.carts.domain.Cart;
import com.unicornstudy.singleshop.carts.domain.CartItem;
import com.unicornstudy.singleshop.carts.domain.repository.CartItemRepository;
import com.unicornstudy.singleshop.carts.domain.repository.CartRepository;
import com.unicornstudy.singleshop.carts.application.dto.CartResponseDto;
import com.unicornstudy.singleshop.exception.carts.CartItemNotFoundException;
import com.unicornstudy.singleshop.exception.carts.CartNotFoundException;
import com.unicornstudy.singleshop.exception.carts.SessionExpiredException;
import com.unicornstudy.singleshop.config.TestSetting;
import com.unicornstudy.singleshop.items.domain.Items;
import com.unicornstudy.singleshop.items.domain.repository.ItemsRepository;
import com.unicornstudy.singleshop.user.domain.User;
import com.unicornstudy.singleshop.user.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@WithMockUser(roles = "USER")
@Disabled
public class CartServiceTest {

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemsRepository itemsRepository;

    private User user;
    private Items item;
    private CartService cartService;
    private Cart cart;
    private List<Cart> cartList = new ArrayList<>();
    private CartItem cartItem;

    @BeforeEach
    public void setUp() {
        item = TestSetting.setItem();
        user = TestSetting.setUser(TestSetting.setAddress());
        cartItem = TestSetting.setCartItem(item);
        cart = TestSetting.setCart(user, cartItem);
        cartList.add(cart);
        cartService = new CartService(cartRepository, cartItemRepository, userRepository, itemsRepository);
    }

    @Test
    public void 장바구니_생성() {
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.ofNullable(user));
        when(itemsRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(item));
        when(cartRepository.findCartByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(cart));
        when(cartRepository.findAll()).thenReturn(cartList);

        cartService.addCart(user.getEmail(), item.getId());

        Cart result = cartRepository.findAll().get(0);
        assertThat(result.getUser()).isEqualTo(user);
        assertThat(result.getCartItems().get(0).getItem()).isEqualTo(item);
    }

    @Test
    public void 장바구니_삭제() {
        when(cartRepository.findAll()).thenReturn(cartList);
        when(cartRepository.findCartByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(cart));
        when(cartItemRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(cartItem));

        cartService.removeCartItem(user.getEmail(), item.getId());

        Cart result = cartRepository.findAll().get(0);
        assertThat(result.getCartItems().size()).isEqualTo(0);
    }

    @Test
    public void 장바구니_조회() {
        when(cartRepository.findCartByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(user.getCart()));
        List<CartResponseDto> result = cartService.findAllCartItemListByUser(user.getEmail());

        assertThat(result.size()).isEqualTo(user.getCart().getCartItems().size());
    }

    @Test
    public void 장바구니_조회_페이징() {
        for (int i = 0; i < 100; i++) {
            cart.addCartItem(cartItem);
        }
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
        when(cartRepository.findCartByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(user.getCart()));
        when(cartItemRepository.findAllByCartId(any(), any())).thenReturn(cart.getCartItems().stream().limit(10).collect(Collectors.toList()));
        List<CartResponseDto> result = cartService.findCartItemListByUser(user.getEmail(), pageable);
        assertThat(result.size()).isEqualTo(10);
    }

    @Test
    public void 장바구니_조회_예외() {
        assertThatThrownBy(() -> cartService.findAllCartItemListByUser(user.getEmail()))
                .isInstanceOf(CartNotFoundException.class)
                .hasMessage(CartNotFoundException.ERROR_MESSAGE);
    }

    @Test
    public void 장바구니_상품_조회_예외() {
        when(cartRepository.findCartByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(user.getCart()));
        assertThatThrownBy(() -> cartService.removeCartItem(user.getEmail(), 1l))
                .isInstanceOf(CartItemNotFoundException.class)
                .hasMessage(CartItemNotFoundException.ERROR_MESSAGE);
    }

    @Test
    public void 세션_만료_예외() {
        assertThatThrownBy(() -> cartService.addCart(user.getEmail(), item.getId()))
                .isInstanceOf(SessionExpiredException.class)
                .hasMessage(SessionExpiredException.ERROR_MESSAGE);
    }
}