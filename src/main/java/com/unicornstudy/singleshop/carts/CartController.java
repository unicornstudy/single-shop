package com.unicornstudy.singleshop.carts;

import com.unicornstudy.singleshop.carts.dto.CartRequestDto;
import com.unicornstudy.singleshop.carts.dto.ReadCartResponseDto;
import com.unicornstudy.singleshop.oauth2.LoginUser;
import com.unicornstudy.singleshop.oauth2.dto.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/carts")
public class CartController {
    private final CartService cartService;

    @GetMapping
    public List<ReadCartResponseDto> readCart(@LoginUser SessionUser user, @PageableDefault(size=10, sort="id", direction = Sort.Direction.DESC) Pageable pageable) {
        return cartService.findCartItemListByUser(user.getEmail(), pageable);
    }

    @PostMapping
    public void addCart(@LoginUser SessionUser user, @RequestBody CartRequestDto requestDto) {
        cartService.addCart(user.getEmail(), requestDto.getId());
    }

    @DeleteMapping("/{id}")
    public void removeCart(@LoginUser SessionUser user, @PathVariable Long id) {
        cartService.removeCartItem(user.getEmail(), id);
    }
}