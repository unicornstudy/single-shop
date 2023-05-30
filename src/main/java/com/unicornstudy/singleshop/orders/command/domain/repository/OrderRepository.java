package com.unicornstudy.singleshop.orders.command.domain.repository;

import com.unicornstudy.singleshop.orders.command.domain.Orders;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Orders, Long> {
    List<Orders> findAllByUserEmail(String email, Pageable pageable);
}
