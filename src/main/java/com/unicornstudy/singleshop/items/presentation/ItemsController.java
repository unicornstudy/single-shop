package com.unicornstudy.singleshop.items.presentation;

import com.unicornstudy.singleshop.items.application.ItemsService;
import com.unicornstudy.singleshop.items.application.dto.ItemsRequestDto;
import com.unicornstudy.singleshop.items.application.dto.ItemsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/items")
public class ItemsController {

    private final ItemsService itemService;

    @GetMapping("/{id}")
    public ResponseEntity<ItemsResponseDto> findById(@PathVariable Long id) {
        return new ResponseEntity<>(itemService.findById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ItemsResponseDto>> findByAll(@PageableDefault(size=10, sort="id", direction = Sort.Direction.DESC) Pageable pageable) {
        return new ResponseEntity<>(itemService.findAll(pageable), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ItemsResponseDto> save(@RequestBody ItemsRequestDto requestDto) {
        return new ResponseEntity<>(itemService.save(requestDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemsResponseDto> update(@PathVariable Long id, @RequestBody ItemsRequestDto requestDto) {
        return new ResponseEntity<>(itemService.update(id, requestDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> delete(@PathVariable Long id) {
        return new ResponseEntity<>(itemService.delete(id), HttpStatus.NO_CONTENT);
    }
}
