package com.unicornstudy.singleshop.orders.domain.repository;

import com.unicornstudy.singleshop.orders.domain.Orders;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Orders, Long> {
    List<Orders> findAllByUserEmail(String email, Pageable pageable);
}
