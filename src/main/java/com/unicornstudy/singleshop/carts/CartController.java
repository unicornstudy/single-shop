package com.unicornstudy.singleshop.carts;

import com.unicornstudy.singleshop.carts.dto.CartRequestDto;
import com.unicornstudy.singleshop.carts.dto.ReadCartResponseDto;
import com.unicornstudy.singleshop.oauth2.LoginUser;
import com.unicornstudy.singleshop.oauth2.dto.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/carts")
public class CartController {
    private final CartService cartService;

    @GetMapping
    public List<ReadCartResponseDto> readCart(@LoginUser SessionUser user) {
        return cartService.findCartItemListByUser(user.getEmail());
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
