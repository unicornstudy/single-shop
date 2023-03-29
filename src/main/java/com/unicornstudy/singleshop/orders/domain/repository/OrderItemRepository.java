package com.unicornstudy.singleshop.orders.domain.repository;

import com.unicornstudy.singleshop.orders.domain.OrderItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findAllByOrderId(Long id, Pageable pageable);
}
