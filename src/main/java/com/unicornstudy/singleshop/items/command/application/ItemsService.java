package com.unicornstudy.singleshop.items.command.application;

import com.unicornstudy.singleshop.items.command.application.dto.ItemsRequestDto;
import com.unicornstudy.singleshop.items.command.application.dto.ItemsResponseDto;
import com.unicornstudy.singleshop.items.command.domain.Items;
import com.unicornstudy.singleshop.items.command.domain.repository.ItemsRepository;
import com.unicornstudy.singleshop.exception.items.ItemsException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import static com.unicornstudy.singleshop.exception.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class ItemsService {

    private final ItemsRepository itemsRepository;

    @Transactional(readOnly = true)
    public ItemsResponseDto findById(Long id) {
        Items items = itemsRepository.findById(id).orElseThrow(() -> new ItemsException(BAD_REQUEST_ITEMS_READ));
        return ItemsResponseDto.from(items);
    }   

    @Transactional(readOnly = true)
    public List<ItemsResponseDto> findAll(Pageable pageable) {
        return itemsRepository.findAll(pageable).stream()
                .map(items -> ItemsResponseDto.from(items))
                .collect(Collectors.toList());
    }

    @Transactional
    public ItemsResponseDto save(ItemsRequestDto requestDto) {
        Items items = itemsRepository.save(requestDto.toEntity());
        return ItemsResponseDto.from(items);
    }

    @Transactional
    public ItemsResponseDto update(Long id, ItemsRequestDto requestDto) {
        Items items = itemsRepository.findById(id).orElseThrow(() -> new ItemsException(BAD_REQUEST_ITEMS_READ));
        requestDto.updateItems(items);
        return ItemsResponseDto.from(items);
    }

    @Transactional
    public Long delete(Long id) {
        Items items = itemsRepository.findById(id).orElseThrow(() -> new ItemsException(BAD_REQUEST_ITEMS_READ));
        itemsRepository.delete(items);
        return id;
    }

    @Transactional
    public ItemsResponseDto subtractQuantity(Long id) {
        Items item = itemsRepository.findById(id).orElseThrow(() -> new ItemsException(BAD_REQUEST_ITEMS_READ));
        item.decreaseQuantity();
        return ItemsResponseDto.from(item);
    }

    @Transactional
    public ItemsResponseDto addQuantity(Long id) {
        Items item = itemsRepository.findById(id).orElseThrow(() -> new ItemsException(BAD_REQUEST_ITEMS_READ));
        item.increaseQuantity();
        return ItemsResponseDto.from(item);
    }
}
