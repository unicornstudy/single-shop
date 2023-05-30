package com.unicornstudy.singleshop.items.command.application.aop;

import com.unicornstudy.singleshop.items.command.application.dto.ItemsResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class ItemsAspect {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @AfterReturning(pointcut = "execution(* com.unicornstudy.singleshop.items.command.application.ItemsService.save(..)) " +
            "|| execution(* com.unicornstudy.singleshop.items.command.application.ItemsService.update(..))" +
            "|| execution(* com.unicornstudy.singleshop.items.command.application.ItemsService.subtractQuantity(..))" +
            "|| execution(* com.unicornstudy.singleshop.items.command.application.ItemsService.addQuantity(..))"
            , returning = "itemsResponseDto")
    public void sendToCreateAndUpdateMessageQueue(ItemsResponseDto itemsResponseDto) {
        log.info("ItemsService 저장 성공, 메시지큐에 메시지 전송");
        sendToMessageQueue("ItemsCreateAndUpdateTopic", "CreateAndUpdateDLQ", itemsResponseDto);
    }

    @AfterReturning(pointcut = "execution(* com.unicornstudy.singleshop.items.command.application.ItemsService.delete(..))", returning = "id")
    public void sendToDeleteMessageQueue(Long id) {
        log.info("ItemsService 삭제 성공, 메시지큐에 메시지 전송");
        sendToMessageQueue("ItemsDeleteTopic", "DeleteDLQ", id);
    }

    private void sendToMessageQueue(String topic, String dlq, Object payload) {
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, payload);

        future.whenComplete((result, exception) -> {
            if (exception != null) {
                log.error("상품 메시지 전달 실패 Dead Letter Queue에 메시지 전송");
                kafkaTemplate.send(dlq, payload);
            } else {
                log.info("상품 메시지 큐에 전송 성공");
            }
        });
    }
}
