package com.unicornstudy.singleshop.carts;

import com.unicornstudy.singleshop.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
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

    public void setCartOwner(User user) {
        this.user = user;
    }

    public void addCartItem(CartItem cartItem) {
        cartItems.add(cartItem);
        cartItem.setCartItemOwner(this);
        count++;
        price += cartItem.getItem().getPrice();
    }

    public void removeCart(CartItem cartItem) {
        cartItems.remove(cartItem);
        count--;
        price -= cartItem.getItem().getPrice();
    }

    public static Cart createCart(User user, CartItem cartItem) {
        Cart cart = new Cart();
        cart.setPrice(0);
        cart.setCount(0);
        cart.addCartItem(cartItem);
        user.setCart(cart);
        return cart;
    }
}
