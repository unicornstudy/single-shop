package com.unicornstudy.singleshop.items.query.domain.repository;

import com.unicornstudy.singleshop.items.query.domain.ItemsIndex;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomItemsSearchRepository {
    List<ItemsIndex> findItems(String name, Integer price1, Integer price2, String parentCategory, String childCategory, Pageable pageable);
}
