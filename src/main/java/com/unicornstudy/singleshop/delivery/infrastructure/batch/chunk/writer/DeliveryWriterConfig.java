package com.unicornstudy.singleshop.delivery.infrastructure.batch.chunk.writer;

import com.unicornstudy.singleshop.delivery.domain.Delivery;
import com.unicornstudy.singleshop.delivery.domain.DeliveryStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DeliveryWriterConfig {

    @StepScope
    @Bean
    public ItemWriter<Delivery> deliveryWriter() {
        return chunk -> {
            for (Delivery delivery : chunk.getItems()) {
                delivery.changeDeliveryStatus(DeliveryStatus.START);
            }
        };
    }
}
