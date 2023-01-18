package com.unicornstudy.singleshop.carts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    @Query("select c from Cart c where c.user.email = ?1")
    Optional<Cart> findByUser(String email);
}
