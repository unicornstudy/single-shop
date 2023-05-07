package com.unicornstudy.singleshop.delivery.infrastructure.batch.chunk.reader;

import com.unicornstudy.singleshop.delivery.domain.Delivery;
import com.unicornstudy.singleshop.delivery.domain.DeliveryStatus;
import com.unicornstudy.singleshop.delivery.infrastructure.batch.job.DeliveryJobConfig;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DeliveryReaderConfig {

    private final EntityManagerFactory emf;

    @StepScope
    @Bean
    public JpaPagingItemReader<Delivery> deliveryReader() {
        String query = String.format("SELECT d FROM Delivery d Where d.status = '%s'", DeliveryStatus.READY);
        return new JpaPagingItemReaderBuilder<Delivery>()
                .entityManagerFactory(emf)
                .queryString(query)
                .pageSize(DeliveryJobConfig.CHUNK_SIZE)
                .name("deliveryReader")
                .build();
    }
}
