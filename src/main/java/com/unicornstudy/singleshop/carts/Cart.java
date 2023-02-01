package com.unicornstudy.singleshop.carts;

import com.unicornstudy.singleshop.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<CartItem> cartItems = new ArrayList<>();

    private int price;

    private int count;

    public void linkCartOwner(User user) {
        this.user = user;
    }

    public Cart addCartItem(CartItem cartItem) {
        cartItems.add(cartItem);
        cartItem.linkCartItemOwner(this);
        count++;
        price += cartItem.getItem().getPrice();
        return this;
    }

    public void subtractCartItem(CartItem cartItem) {
        cartItems.remove(cartItem);
        count--;
        price -= cartItem.getItem().getPrice();
    }

    public static Cart createCart(User user, CartItem cartItem) {
        Cart cart = new Cart();
        cart.initializeCart();
        cart.addCartItem(cartItem);
        user.createCart(cart);
        return cart;
    }

    private void initializeCart() {
        this.price = 0;
        this.count = 0;
    }
}

