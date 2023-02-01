package com.unicornstudy.singleshop.carts;

import com.unicornstudy.singleshop.items.Items;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

    public void linkCartItemOwner(Cart cart) {
        this.cart = cart;
    }

    public static CartItem createCartItem(Items item) {
        CartItem cartItem = new CartItem();
        cartItem.linkItem(item);
        return cartItem;
    }

    private void linkItem(Items item) {
        this.item = item;
    }

    public void createIdForTest(Long id) {
        this.id = id;
    }
}