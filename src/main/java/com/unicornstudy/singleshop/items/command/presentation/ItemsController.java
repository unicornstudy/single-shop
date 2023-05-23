package com.unicornstudy.singleshop.items.command.presentation;

import com.unicornstudy.singleshop.items.command.application.ItemsService;
import com.unicornstudy.singleshop.items.command.application.dto.ItemsRequestDto;
import com.unicornstudy.singleshop.items.command.application.dto.ItemsResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/items")
public class ItemsController {

    private final ItemsService itemService;

    @PostMapping
    public ResponseEntity<ItemsResponseDto> save(@RequestBody @Valid ItemsRequestDto requestDto) {
        return new ResponseEntity<>(itemService.save(requestDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemsResponseDto> update(@PathVariable Long id, @RequestBody @Valid ItemsRequestDto requestDto) {
        return new ResponseEntity<>(itemService.update(id, requestDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> delete(@PathVariable Long id) {
        return new ResponseEntity<>(itemService.delete(id), HttpStatus.NO_CONTENT);
    }
}
