package com.unicornstudy.singleshop.items.query.application;

import com.unicornstudy.singleshop.items.query.application.dto.ItemsSearchDto;
import com.unicornstudy.singleshop.items.query.domain.repository.ItemsSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemsSearchService {

    private final ItemsSearchRepository itemsSearchRepository;

    public List<ItemsSearchDto> findAll(Pageable pageable) {
        return itemsSearchRepository.findAll(pageable)
                .stream()
                .map(items -> ItemsSearchDto.from(items))
                .toList();
    }

    public List<ItemsSearchDto> findItems(String name, int price1, int price2, String parentCategory, String childCategory, Pageable pageable) {
        return itemsSearchRepository.findItems(name, price1, price2, parentCategory, childCategory, pageable)
                .stream()
                .map(items -> ItemsSearchDto.from(items))
                .toList();
    }
}
