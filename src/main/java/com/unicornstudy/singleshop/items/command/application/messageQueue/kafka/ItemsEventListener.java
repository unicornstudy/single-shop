package com.unicornstudy.singleshop.items.command.application.messageQueue.kafka;

import com.unicornstudy.singleshop.items.command.application.dto.ItemsResponseDto;
import com.unicornstudy.singleshop.items.query.domain.repository.ItemsSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ItemsEventListener {

    private final ItemsSearchRepository itemsSearchRepository;

    @KafkaListener(topics = "ItemsCreateAndUpdateTopic", groupId = "items")
    public void createAndUpdateItemsIndex(ItemsResponseDto itemsResponseDto) {
        System.out.println("메시지큐 도착");
        log.info("ItemsCreateAndUpdateTopic 큐 메시지 도착");
        itemsSearchRepository.save(itemsResponseDto.toEntity());
    }

    @KafkaListener(topics = "ItemsDeleteTopic", groupId = "items")
    public void deleteItemsIndex(Long id) {
        log.info("ItemsDeleteTopic 큐 메시지 도착");
        itemsSearchRepository.deleteById(id);
    }
}
