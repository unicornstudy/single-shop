package com.unicornstudy.singleshop.carts.domain.repository;

import com.unicornstudy.singleshop.carts.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findCartByUserEmail(String email);
}
