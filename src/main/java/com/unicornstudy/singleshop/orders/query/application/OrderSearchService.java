package com.unicornstudy.singleshop.orders.query.application;

import com.unicornstudy.singleshop.orders.query.application.dto.OrderSearchDto;
import com.unicornstudy.singleshop.orders.query.domain.repository.OrderSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderSearchService {

    private final OrderSearchRepository orderSearchRepository;

    public List<OrderSearchDto> findOrdersByUser(String userEmail, Pageable pageable) {
        return orderSearchRepository.findAllByUserEmail(userEmail, pageable)
                .stream()
                .map(orders -> OrderSearchDto.from(orders))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<OrderSearchDto> findOrdersByOrderDateAndUserEmail(String startDate, String endDate, String userEmail, Pageable pageable) {
        return orderSearchRepository.findByOrderDateAndUserEmail(startDate, endDate, userEmail, pageable)
                .stream()
                .map(orders -> OrderSearchDto.from(orders))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<OrderSearchDto> findOrdersByOrderDateAndItemId(String startDate, String endDate, Long itemId, Pageable pageable) {
        return orderSearchRepository.findByOrderDatePrefix(startDate, endDate, pageable)
                .stream()
                .filter(orders -> orders.getOrderItemsId().stream().anyMatch(id -> id == itemId))
                .map(orders -> OrderSearchDto.from(orders))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<OrderSearchDto> findOrdersByOrderDate(String startDate, String endDate, Pageable pageable) {
        return orderSearchRepository.findByOrderDatePrefix(startDate, endDate, pageable)
                .stream()
                .map(orders -> OrderSearchDto.from(orders))
                .toList();
    }
}
