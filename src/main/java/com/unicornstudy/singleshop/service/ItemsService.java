package com.unicornstudy.singleshop.service;

import com.unicornstudy.singleshop.domain.items.Items;
import com.unicornstudy.singleshop.domain.items.ItemsRepository;
import com.unicornstudy.singleshop.web.dto.ItemsListResponseDto;
import com.unicornstudy.singleshop.web.dto.ItemsResponseDto;
import com.unicornstudy.singleshop.web.dto.ItemsSaveRequestDto;
import com.unicornstudy.singleshop.web.dto.ItemsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ItemsService {

    private final ItemsRepository itemsRepository;

    @Transactional
    public ItemsResponseDto findById(Long id) {
        Items entity = itemsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 상품은 존재하지 않습니다. id = " + id));

        return new ItemsResponseDto(entity);
    }

    @Transactional(readOnly = true)
    public List<ItemsListResponseDto> findAllDesc() {
        return itemsRepository.findAllDesc().stream()
                .map(items -> new ItemsListResponseDto(items))
                .collect(Collectors.toList());
    }

    @Transactional
    public Long save(ItemsSaveRequestDto requestDto) {
        return itemsRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, ItemsUpdateRequestDto requestDto) {
        Items items = itemsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 상품은 존재하지 않습니다. id = " + id));

        items.update(requestDto.getName(), requestDto.getPrice(), requestDto.getDescription(), requestDto.getQuantity());

        return id;
    }

    @Transactional
    public void delete (Long id) {
        Items items = itemsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 상품은 없습니다. id = " + id));

        itemsRepository.delete(items);
    }

}
