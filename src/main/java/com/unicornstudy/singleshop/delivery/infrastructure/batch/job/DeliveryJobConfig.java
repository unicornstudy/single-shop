package com.unicornstudy.singleshop.delivery.infrastructure.batch.job;

import com.unicornstudy.singleshop.delivery.domain.Delivery;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@RequiredArgsConstructor
@Configuration
public class DeliveryJobConfig {
    public static final Integer CHUNK_SIZE = 5;

    @Bean
    public Job deliveryJob(JobRepository jobRepository, Step deliveryStep) {
        return new JobBuilder("deliveryJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(deliveryStep)
                .build();
    }

    @JobScope
    @Bean
    public Step deliveryStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                             ItemReader deliveryReader, ItemWriter deliveryWriter, TaskExecutor taskExecutor) {
        return new StepBuilder("deliveryStep", jobRepository)
                .<Delivery, Delivery>chunk(CHUNK_SIZE, transactionManager)
                .reader(deliveryReader)
                .writer(deliveryWriter)
                .taskExecutor(taskExecutor)
                .build();
    }
}
