package com.unicornstudy.singleshop.carts.domain.repository;

import com.unicornstudy.singleshop.carts.domain.CartItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findAllByCartId(Long id, Pageable pageable);
}
