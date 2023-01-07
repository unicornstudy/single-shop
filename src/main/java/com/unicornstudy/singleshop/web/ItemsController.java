package com.unicornstudy.singleshop.web;

import com.unicornstudy.singleshop.service.ItemsService;
import com.unicornstudy.singleshop.web.dto.ItemsListResponseDto;
import com.unicornstudy.singleshop.web.dto.ItemsResponseDto;
import com.unicornstudy.singleshop.web.dto.ItemsSaveRequestDto;
import com.unicornstudy.singleshop.web.dto.ItemsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ItemsController {

    private final ItemsService itemService;

    @GetMapping("/api/items/{id}")
    public ItemsResponseDto findById(@PathVariable Long id) {
        return itemService.findById(id);
    }

    @GetMapping("/api/items")
    public List<ItemsListResponseDto> findByAllDesc() {
        return itemService.findAllDesc();
    }

    @PostMapping("/api/items")
    public ItemsSaveRequestDto save(@RequestBody ItemsSaveRequestDto requestDto){
        itemService.save(requestDto);
        return requestDto;
    }

    @PutMapping("/api/items/{id}")
    public ItemsUpdateRequestDto update(@PathVariable Long id, @RequestBody ItemsUpdateRequestDto requestDto) {
        itemService.update(id, requestDto);

        return requestDto;
    }

    @DeleteMapping("/api/items/{id}")
    public Long delete(@PathVariable Long id) {
        itemService.delete(id);

        return id;
    }
}
