package com.unicornstudy.singleshop.carts.domain;

import com.unicornstudy.singleshop.items.command.domain.Items;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Items item;

    public static CartItem createCartItem(Items item) {
        CartItem cartItem = new CartItem();
        cartItem.initializeItem(item);
        return cartItem;
    }

    public void initializeCart(Cart cart) {
        this.cart = cart;
    }

    private void initializeItem(Items item) {
        this.item = item;
    }

    public void createIdForTest(Long id) {
        this.id = id;
    }
}