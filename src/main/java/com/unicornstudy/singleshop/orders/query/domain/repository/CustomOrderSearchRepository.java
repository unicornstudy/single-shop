package com.unicornstudy.singleshop.orders.query.domain.repository;

import com.unicornstudy.singleshop.orders.query.domain.OrderIndex;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomOrderSearchRepository {
    List<OrderIndex> findByOrderDatePrefix(String startDate, String endDate, Pageable pageable);
    List<OrderIndex> findByOrderDateAndUserEmail(String startDate, String endDate, String userEmail, Pageable pageable);
}
