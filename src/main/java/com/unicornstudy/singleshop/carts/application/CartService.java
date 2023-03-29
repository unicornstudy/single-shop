package com.unicornstudy.singleshop.carts.application;

import com.unicornstudy.singleshop.carts.domain.Cart;
import com.unicornstudy.singleshop.carts.domain.CartItem;
import com.unicornstudy.singleshop.carts.domain.repository.CartItemRepository;
import com.unicornstudy.singleshop.carts.domain.repository.CartRepository;
import com.unicornstudy.singleshop.carts.application.dto.ReadCartResponseDto;
import com.unicornstudy.singleshop.exception.carts.CartItemNotFoundException;
import com.unicornstudy.singleshop.exception.carts.CartNotFoundException;
import com.unicornstudy.singleshop.exception.carts.SessionExpiredException;
import com.unicornstudy.singleshop.items.domain.Items;
import com.unicornstudy.singleshop.items.domain.repository.ItemsRepository;
import com.unicornstudy.singleshop.exception.items.ItemsException;
import com.unicornstudy.singleshop.user.domain.User;
import com.unicornstudy.singleshop.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.unicornstudy.singleshop.exception.ErrorCode.BAD_REQUEST_ITEMS_READ;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ItemsRepository itemsRepository;

    @Transactional(readOnly = true)
    public List<ReadCartResponseDto> findCartItemListByUser(String userEmail, Pageable pageable) {
        return cartItemRepository.findAllByCartId(validateCart(userEmail).getId(), pageable)
                .stream()
                .map(cartItem -> new ReadCartResponseDto(cartItem))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReadCartResponseDto> findAllCartItemListByUser(String userEmail) {
        return validateCart(userEmail)
                .getCartItems()
                .stream()
                .map(cartItem -> new ReadCartResponseDto(cartItem))
                .collect(Collectors.toList());
    }

    private Cart validateCart(String userEmail) {
        return cartRepository.findCartByUserEmail(userEmail)
                .orElseThrow(() -> new CartNotFoundException());
    }

    @Transactional
    public void addCart(String userEmail, Long itemId) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new SessionExpiredException());
        Items item = itemsRepository.findById(itemId).orElseThrow(() -> new ItemsException(BAD_REQUEST_ITEMS_READ));
        CartItem cartItem = CartItem.createCartItem(item);

        saveOrUpdate(user, cartItem);
    }

    private void saveOrUpdate(User user, CartItem cartItem) {
        cartRepository.findCartByUserEmail(user.getEmail())
                .ifPresentOrElse(
                        entity -> entity.addCartItem(cartItem),
                        () -> cartRepository.save(Cart.createCart(user, cartItem)));
    }

    @Transactional
    public void removeCartItem(String userEmail, Long cartItemId) {
        Cart cart = cartRepository.findCartByUserEmail(userEmail).orElseThrow(() -> new CartNotFoundException());
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(() -> new CartItemNotFoundException());

        cart.subtractCartItem(cartItem);
        cartItemRepository.delete(cartItem);
    }
}