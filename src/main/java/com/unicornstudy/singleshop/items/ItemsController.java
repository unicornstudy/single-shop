package com.unicornstudy.singleshop.items;

import com.unicornstudy.singleshop.items.dto.ItemsReadUpdateResponseDto;
import com.unicornstudy.singleshop.items.dto.ItemsRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ItemsController {

    private final ItemsService itemService;

    @GetMapping("/api/items/{id}")
    public ItemsReadUpdateResponseDto findById(@PathVariable Long id) {
        return itemService.findById(id);
    }

    @GetMapping("/api/items")
    public List<ItemsReadUpdateResponseDto> findByAll() {
        return itemService.findAll();
    }

    @PostMapping("/api/items")
    public ItemsReadUpdateResponseDto save(@RequestBody ItemsRequestDto requestDto) {
        return itemService.save(requestDto);
    }

    @PutMapping("/api/items/{id}")
    public ItemsReadUpdateResponseDto update(@PathVariable Long id, @RequestBody ItemsRequestDto requestDto) {
        return itemService.update(id, requestDto);
    }

    @DeleteMapping("/api/items/{id}")
    public Long delete(@PathVariable Long id) {
        itemService.delete(id);

        return id;
    }
}
