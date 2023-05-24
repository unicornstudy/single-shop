package com.unicornstudy.singleshop.carts.presentation;

import com.unicornstudy.singleshop.carts.application.CartService;
import com.unicornstudy.singleshop.carts.application.dto.CartResponseDto;
import com.unicornstudy.singleshop.oauth2.LoginUser;
import com.unicornstudy.singleshop.oauth2.dto.SessionUser;
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
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<List<CartResponseDto>> readCart(@LoginUser SessionUser user, @PageableDefault(size=10, sort="id", direction = Sort.Direction.DESC) Pageable pageable) {
        return new ResponseEntity<>(cartService.findCartItemListByUser(user.getEmail(), pageable), HttpStatus.OK);
    }

    @PostMapping("/{id}")
    public ResponseEntity<CartResponseDto> addCart(@LoginUser SessionUser user, @PathVariable Long id) {
        return new ResponseEntity<>(cartService.addCart(user.getEmail(), id), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeCart(@LoginUser SessionUser user, @PathVariable Long id) {
        cartService.removeCartItem(user.getEmail(), id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}