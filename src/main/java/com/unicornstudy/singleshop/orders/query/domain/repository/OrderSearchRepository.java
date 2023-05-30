package com.unicornstudy.singleshop.orders.query.domain.repository;

import com.unicornstudy.singleshop.orders.query.domain.OrderIndex;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderSearchRepository extends ElasticsearchRepository<OrderIndex, Long>, CrudRepository<OrderIndex, Long>, CustomOrderSearchRepository {
    List<OrderIndex> findAllByUserEmail(String email, Pageable pageable);
}
