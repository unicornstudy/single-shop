package com.unicornstudy.singleshop.items;

import com.unicornstudy.singleshop.items.exception.ItemsException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
public class OptimisticLockQuantityFacadeTest {

    @Autowired
    private ItemsRepository itemsRepository;

    @Autowired
    private OptimisticLockQuantityFacade optimisticLockQuantityFacade;

    private Items item;
    private final int threadCount = 30;
    private ExecutorService executorService;
    private CountDownLatch latch;
    private AtomicInteger successCount;
    private Long id = 1L;
    @BeforeEach
    public void setUp() {
        item = Items.builder()
                .name("testItem")
                .quantity(30)
                .description("test")
                .price(2000)
                .build();
        itemsRepository.save(item);
        executorService = Executors.newFixedThreadPool(threadCount);
        latch = new CountDownLatch(threadCount);
        successCount = new AtomicInteger();
    }

    @AfterEach
    public void delete() {
        itemsRepository.deleteAll();
    }

    @Test
    public void 동시에_30개_감소_요청() throws InterruptedException {
        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                try {
                    optimisticLockQuantityFacade.subtractQuantity(id);
                    System.out.println("성공");
                    successCount.getAndIncrement();
                } catch (ItemsException | InterruptedException ie) {
                    System.out.println("예외발생");
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        int result = itemsRepository.findById(id).get().getQuantity();
        assertThat(result).isEqualTo(item.getQuantity() - successCount.get());
        System.out.println("성공횟수: " + successCount.get());
    }

    @Test
    public void 동시에_30개_증가_요청() throws InterruptedException {
        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                try {
                    optimisticLockQuantityFacade.addQuantity(id);
                    System.out.println("성공");
                    successCount.getAndIncrement();
                } catch (ItemsException | InterruptedException ie) {
                    System.out.println("예외발생");
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        int result = itemsRepository.findById(id).get().getQuantity();
        assertThat(result).isEqualTo(item.getQuantity() + successCount.get());
        System.out.println("성공횟수: " + successCount.get());
    }
}