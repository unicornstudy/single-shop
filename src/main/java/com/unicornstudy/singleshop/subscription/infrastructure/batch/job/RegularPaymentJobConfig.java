package com.unicornstudy.singleshop.subscription.infrastructure.batch.job;

import com.unicornstudy.singleshop.user.domain.User;
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
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class RegularPaymentJobConfig {

    public static final Integer CHUNK_SIZE = 5;

    @Bean
    public Job regularPaymentJob(JobRepository jobRepository, Step regularPaymentStep, Step regularPaymentCancelStep) {
        return new JobBuilder("regularPaymentJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(regularPaymentCancelStep)
                .next(regularPaymentStep)
                .build();
    }

    @JobScope
    @Bean
    public Step regularPaymentCancelStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                                         ItemReader subscriptionCancelReader, ItemWriter subscriptionCancelWriter, TaskExecutor taskExecutor) {
        return new StepBuilder("regularPaymentCancelStep", jobRepository)
                .<User, User>chunk(CHUNK_SIZE, transactionManager)
                .reader(subscriptionCancelReader)
                .writer(subscriptionCancelWriter)
                .taskExecutor(taskExecutor)
                .build();
    }

    @JobScope
    @Bean
    public Step regularPaymentStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                                   ItemReader subscriptionRequestReader, ItemWriter subscriptionRequestWriter, TaskExecutor taskExecutor) {
        return new StepBuilder("regularPaymentStep", jobRepository)
                .<User, User>chunk(CHUNK_SIZE, transactionManager)
                .reader(subscriptionRequestReader)
                .writer(subscriptionRequestWriter)
                .taskExecutor(taskExecutor)
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(CHUNK_SIZE);
        taskExecutor.setMaxPoolSize(CHUNK_SIZE * 2);
        taskExecutor.setThreadNamePrefix("async-thread");
        return taskExecutor;
    }
}