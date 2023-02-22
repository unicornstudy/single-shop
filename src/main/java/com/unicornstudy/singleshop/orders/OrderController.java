package com.unicornstudy.singleshop.orders;

import com.unicornstudy.singleshop.oauth2.LoginUser;
import com.unicornstudy.singleshop.oauth2.dto.SessionUser;
import com.unicornstudy.singleshop.orders.dto.OrderDetailDto;
import com.unicornstudy.singleshop.orders.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public List<OrderDto> readOrders(@LoginUser SessionUser user, @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return orderService.findOrdersByUser(user.getEmail(), pageable);
    }

    @GetMapping("{id}")
    public List<OrderDetailDto> readOrderDetails(@PathVariable Long id, @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return orderService.findOrderDetailsById(id, pageable);
    }


}
