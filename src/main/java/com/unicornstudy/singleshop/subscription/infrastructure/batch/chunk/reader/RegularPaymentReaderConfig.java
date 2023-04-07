package com.unicornstudy.singleshop.subscription.infrastructure.batch.chunk.reader;

import com.unicornstudy.singleshop.subscription.infrastructure.batch.job.RegularPaymentJobConfig;
import com.unicornstudy.singleshop.user.domain.Role;
import com.unicornstudy.singleshop.user.domain.User;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RegularPaymentReaderConfig {

    private final EntityManagerFactory emf;

    @StepScope
    @Bean
    public JpaPagingItemReader<User> subscriptionRequestReader() {
        String query = String.format("SELECT u FROM User u WHERE u.role ='%s'", Role.SUBSCRIBER);
        return new JpaPagingItemReaderBuilder<User>()
                .entityManagerFactory(emf)
                .queryString(query)
                .pageSize(RegularPaymentJobConfig.CHUNK_SIZE)
                .name("subscriptionRequestReader")
                .build();
    }

    @StepScope
    @Bean
    public JpaPagingItemReader<User> subscriptionCancelReader() {
        String query = String.format("SELECT u FROM User u WHERE u.role ='%s'", Role.CANCEL_SUBSCRIBER);
        return new JpaPagingItemReaderBuilder<User>()
                .entityManagerFactory(emf)
                .queryString(query)
                .pageSize(RegularPaymentJobConfig.CHUNK_SIZE)
                .name("subscriptionCancelReader")
                .build();
    }
}
