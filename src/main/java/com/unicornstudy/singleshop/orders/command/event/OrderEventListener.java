package com.unicornstudy.singleshop.orders.command.event;

import com.unicornstudy.singleshop.orders.command.application.dto.OrderDto;
import com.unicornstudy.singleshop.orders.command.application.dto.OrderIndexDto;
import com.unicornstudy.singleshop.orders.query.domain.OrderIndex;
import com.unicornstudy.singleshop.orders.query.domain.repository.OrderSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventListener {

    private final OrderSearchRepository orderSearchRepository;

    @KafkaListener(topics = "OrdersCreateAndUpdateTopic", groupId = "orders")
    public void createAndUpdateOrdersIndex(OrderIndexDto orderIndexDto) {
        orderSearchRepository.save(OrderIndex.createElasticsearchOrders(orderIndexDto));
        log.info("주문 메시지큐 받기 성공");
    }
}
