package com.unicornstudy.singleshop.carts.domain;

import com.unicornstudy.singleshop.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CartItem> cartItems = new ArrayList<>();

    private int price;

    private int count;

    public void addCartItem(CartItem cartItem) {
        cartItems.add(cartItem);
        cartItem.initializeCart(this);
        count++;
        price += cartItem.getItem().getPrice();
    }

    public void subtractCartItem(CartItem cartItem) {
        cartItems.remove(cartItem);
        count--;
        price -= cartItem.getItem().getPrice();
    }

    public static Cart createCart(User user, CartItem cartItem) {
        Cart cart = new Cart();
        cart.updatePriceAndCount();
        cart.addCartItem(cartItem);
        user.initializeCart(cart);
        cart.initializeUser(user);
        return cart;
    }

    public void initializeUser(User user) {
        this.user = user;
    }

    private void updatePriceAndCount() {
        this.price = 0;
        this.count = 0;
    }
}