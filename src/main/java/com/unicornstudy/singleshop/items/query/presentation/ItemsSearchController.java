package com.unicornstudy.singleshop.items.query.presentation;

import com.unicornstudy.singleshop.items.query.application.ItemsSearchService;
import com.unicornstudy.singleshop.items.query.application.dto.ItemsSearchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/items")
public class ItemsSearchController {

    private final ItemsSearchService itemsSearchService;

    @GetMapping("/{name}")
    public ResponseEntity<List<ItemsSearchDto>> searchItemsByNamePriceAndCategory(@PathVariable String name,
                                                         @RequestParam(value = "minPrice", defaultValue = "0") int minPrice,
                                                         @RequestParam(value= "maxPrice", defaultValue = "2147483647") int maxPrice,
                                                         @RequestParam(value = "parentCategory", defaultValue = "") String parentCategory,
                                                         @RequestParam(value = "childCategory", defaultValue = "") String childCategory,
                                                         @PageableDefault(size=10, sort="createdDate", direction = Sort.Direction.DESC) Pageable pageable) {

        return new ResponseEntity<>(itemsSearchService.searchItemsByNamePriceAndCategory(name, minPrice, maxPrice, parentCategory, childCategory, pageable), HttpStatus.OK);

    }

    @GetMapping
    public ResponseEntity<List<ItemsSearchDto>> findAll(@PageableDefault(size=10, sort="id", direction = Sort.Direction.DESC) Pageable pageable) {
        return new ResponseEntity<>(itemsSearchService.findAll(pageable), HttpStatus.OK);
    }

}
