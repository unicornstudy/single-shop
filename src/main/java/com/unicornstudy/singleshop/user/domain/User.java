package com.unicornstudy.singleshop.user.domain;

import com.unicornstudy.singleshop.carts.domain.Cart;
import com.unicornstudy.singleshop.delivery.domain.Address;
import com.unicornstudy.singleshop.orders.command.domain.Orders;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column
    private String picture;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Cart cart;

    @OneToMany(mappedBy = "user")
    private List<Orders> orders = new ArrayList<>();

    @Embedded
    private Address address;

    private String sid;

    public void updateSid(String sid) {
        this.sid = sid;
    }

    public void updateRole(Role role) {
        this.role = role;
    }

    public void initializeCart(Cart cart) {
        this.cart = cart;
    }

    public User update(String name, String picture) {
        this.name = name;
        this.picture = picture;

        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }

    public void updateAddress(Address address) {
        this.address = address;
    }

    public void resetCart() {
        this.cart = null;
    }

}