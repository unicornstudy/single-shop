package com.unicornstudy.singleshop.items;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OptimisticLockQuantityFacade {

    private final ItemsService itemsService;

    public void subtractQuantity(Long id) throws InterruptedException {
        while (true) {
            try {
                itemsService.subtractQuantity(id);
                break;
            } catch (OptimisticLockingFailureException e) {
                Thread.sleep(50);
            }
        }
    }

    public void addQuantity(Long id) throws InterruptedException {
        while (true) {
            try {
                itemsService.addQuantity(id);
                break;
            } catch (OptimisticLockingFailureException e) {
                Thread.sleep(50);
            }
        }
    }
}
