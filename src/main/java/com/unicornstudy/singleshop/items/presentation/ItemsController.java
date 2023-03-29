package com.unicornstudy.singleshop.items.presentation;

import com.unicornstudy.singleshop.items.application.ItemsService;
import com.unicornstudy.singleshop.items.application.dto.ItemsReadUpdateResponseDto;
import com.unicornstudy.singleshop.items.application.dto.ItemsRequestDto;
import com.unicornstudy.singleshop.items.application.dto.ItemsSaveResponseDto;
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
    public ItemsSaveResponseDto save(@RequestBody ItemsRequestDto requestDto) {
        return itemService.save(requestDto);
    }

    @PutMapping("/api/items/{id}")
    public ItemsReadUpdateResponseDto update(@PathVariable Long id, @RequestBody ItemsRequestDto requestDto) {
        return itemService.update(id, requestDto);
    }

    @DeleteMapping("/api/items/{id}")
    public Long delete(@PathVariable Long id) {
        return itemService.delete(id);
    }
}
