package com.unicornstudy.singleshop.items;

import com.unicornstudy.singleshop.items.dto.ItemsReadUpdateResponseDto;
import com.unicornstudy.singleshop.items.dto.ItemsRequestDto;
import com.unicornstudy.singleshop.items.exception.ItemsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;;import static com.unicornstudy.singleshop.items.exception.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class ItemsService {

    private final ItemsRepository itemsRepository;

    @Transactional(readOnly = true)
    public ItemsReadUpdateResponseDto findById(Long id) {
        Items items = itemsRepository.findById(id).orElseThrow(() -> new ItemsException(BAD_REQUEST_ITEMS_READ));

        return new ItemsReadUpdateResponseDto(items);
    }

    @Transactional(readOnly = true)
    public List<ItemsReadUpdateResponseDto> findAll() {
        return itemsRepository.findAll().stream()
                .map(items -> new ItemsReadUpdateResponseDto(items))
                .collect(Collectors.toList());
    }

    @Transactional
    public ItemsReadUpdateResponseDto save(ItemsRequestDto requestDto) {
        Items items = itemsRepository.save(requestDto.toEntity());

        return new ItemsReadUpdateResponseDto(items);
    }

    @Transactional
    public ItemsReadUpdateResponseDto update(Long id, ItemsRequestDto requestDto) {
        Items items = itemsRepository.findById(id).orElseThrow(() -> new ItemsException(BAD_REQUEST_ITEMS_READ));

        items.update(id, requestDto.getName(), requestDto.getPrice(), requestDto.getDescription(), requestDto.getQuantity(), items.getModifiedDate());

        return new ItemsReadUpdateResponseDto(items);
    }

    @Transactional
    public void delete(Long id) {
        itemsRepository.findById(id).orElseThrow(() -> new ItemsException(BAD_REQUEST_ITEMS_READ));

        itemsRepository.deleteById(id);

    }

}
