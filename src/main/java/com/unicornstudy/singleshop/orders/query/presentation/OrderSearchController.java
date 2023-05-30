package com.unicornstudy.singleshop.orders.query.presentation;

import com.unicornstudy.singleshop.orders.query.application.OrderSearchService;
import com.unicornstudy.singleshop.orders.query.application.dto.OrderSearchDto;
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
@RequestMapping("/api/orders/search")
public class OrderSearchController {

    private final OrderSearchService orderSearchService;

    @GetMapping("/orders/{startMonth}/{endMonth}")
    public ResponseEntity<List<OrderSearchDto>> readOrderByOrderDate(@PathVariable String startMonth,
                                                                     @PathVariable String endMonth,
                                                                     @PageableDefault(size=10, sort="orderDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return new ResponseEntity<>(orderSearchService.findOrdersByOrderDate(startMonth, endMonth, pageable), HttpStatus.OK);
    }

    @GetMapping("/items")
    public ResponseEntity<List<OrderSearchDto>> readOrderByOrderDateAndItem(@RequestParam(value = "startDate", defaultValue = "1") String startDate,
                                                                            @RequestParam(value = "endDate", defaultValue = "1") String endDate,
                                                                            @RequestParam(value = "itemId", defaultValue = "0") Long itemId,
                                                                            @PageableDefault(size=10, sort="orderDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return new ResponseEntity<>(orderSearchService.findOrdersByOrderDateAndItemId(startDate, endDate, itemId, pageable), HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<List<OrderSearchDto>> readOrderByOrderDateAndUser(@RequestParam(value = "startDate", defaultValue = "1") String startDate,
                                                                            @RequestParam(value = "endDate", defaultValue = "1") String endDate,
                                                                            @RequestParam(value = "userEmail", defaultValue = "0") String userEmail,
                                                                            @PageableDefault(size=10, sort="orderDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return new ResponseEntity<>(orderSearchService.findOrdersByOrderDateAndUserEmail(startDate, endDate, userEmail, pageable), HttpStatus.OK);
    }

}
