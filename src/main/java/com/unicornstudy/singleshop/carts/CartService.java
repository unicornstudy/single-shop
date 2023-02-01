package com.unicornstudy.singleshop.carts;

import com.unicornstudy.singleshop.carts.dto.ReadCartResponseDto;
import com.unicornstudy.singleshop.carts.exception.CartItemNotFoundException;
import com.unicornstudy.singleshop.carts.exception.CartNotFoundException;
import com.unicornstudy.singleshop.items.Items;
import com.unicornstudy.singleshop.items.ItemsRepository;
import com.unicornstudy.singleshop.items.exception.ItemsException;
import com.unicornstudy.singleshop.user.User;
import com.unicornstudy.singleshop.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.unicornstudy.singleshop.items.exception.ErrorCode.BAD_REQUEST_ITEMS_READ;

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
        return cartItemRepository.findAllByCart_Id(validateCart(userEmail).getId(), pageable)
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
        return cartRepository.findCartByUser_Email(userEmail)
                .orElseThrow(() -> new CartNotFoundException());
    }

    @Transactional
    public void addCart(String userEmail, Long itemId) {
        User user = userRepository.findByEmail(userEmail).get();
        Items item = itemsRepository.findById(itemId).orElseThrow(() -> new ItemsException(BAD_REQUEST_ITEMS_READ));//3
        CartItem cartItem = CartItem.createCartItem(item);

        saveOrUpdate(userEmail, user, cartItem);
    }

    private void saveOrUpdate(String userEmail, User user, CartItem cartItem) {
        /*cartRepository.findCartByUser_Email(userEmail)
                .map(entity -> entity.addCartItem(cartItem))
                .orElse(Cart.createCart(user, cartItem));*/
        Optional<Cart> cart = cartRepository.findCartByUser_Email(userEmail);

        if (!cart.isPresent()) {
            cartRepository.save(Cart.createCart(user, cartItem));
            return;
        }
        cart.get().addCartItem(cartItem);
    }

    @Transactional
    public void removeCartItem(String userEmail, Long cartItemId) {
        Cart cart = cartRepository.findCartByUser_Email(userEmail).orElseThrow(() -> new CartNotFoundException());
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(() -> new CartItemNotFoundException());

        cart.subtractCartItem(cartItem);
        cartItemRepository.delete(cartItem);
    }
}