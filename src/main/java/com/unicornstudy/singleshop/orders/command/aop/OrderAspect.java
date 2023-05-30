package com.unicornstudy.singleshop.orders.command.aop;

import com.unicornstudy.singleshop.orders.command.application.dto.OrderDto;
import com.unicornstudy.singleshop.orders.command.application.dto.OrderIndexDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@Aspect
@RequiredArgsConstructor
@Slf4j
public class OrderAspect {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @AfterReturning(pointcut = "execution(* com.unicornstudy.singleshop.orders.command.application.OrderService.order()) " +
            "|| execution(* com.unicornstudy.singleshop.orders.command.application.OrderService.cancel())" +
            "|| execution(* com.unicornstudy.singleshop.orders.command.application.OrderService.handleOrderPaymentError())", returning = "orderIndexDto")
    public void sendToCreateAndUpdate(OrderIndexDto orderIndexDto) {
        log.info("주문 rdbms 저장 성공 메시지큐 전송");
        sendToMessageQueue("OrdersCreateAndUpdateTopic", "OrdersCreateAndUpdateDlq", orderIndexDto);
    }

    private void sendToMessageQueue(String topic, String dlq, Object payload) {
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, payload);

        future.whenComplete((result, exception) -> {
            if (exception != null) {
                log.error("주문 메시지 전달 실패 Dead Letter Queue에 메시지 전송");
                kafkaTemplate.send(dlq, payload);
            } else {
                log.info("주문 메시지 큐에 전송 성공");
            }
        });
    }
}
