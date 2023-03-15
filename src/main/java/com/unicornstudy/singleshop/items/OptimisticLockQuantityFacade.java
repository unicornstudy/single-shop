package com.unicornstudy.singleshop.items;

import com.unicornstudy.singleshop.exception.ErrorCode;
import com.unicornstudy.singleshop.items.exception.ItemsException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OptimisticLockQuantityFacade {

    private final ItemsService itemsService;

    public void subtractQuantity(Long id) {
        try {
            itemsService.subtractQuantity(id);
        } catch (OptimisticLockingFailureException e) {
            throw new ItemsException(ErrorCode.ITEMS_LOCK_EXCEPTION);
        }
    }

    public void addQuantity(Long id) {
        try {
            itemsService.addQuantity(id);
        } catch (OptimisticLockingFailureException e) {
            throw new ItemsException(ErrorCode.ITEMS_LOCK_EXCEPTION);
        }
    }
}
