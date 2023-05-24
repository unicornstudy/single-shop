package com.unicornstudy.singleshop.user.presentation;

import com.unicornstudy.singleshop.oauth2.LoginUser;
import com.unicornstudy.singleshop.oauth2.dto.SessionUser;
import com.unicornstudy.singleshop.user.application.UserService;
import com.unicornstudy.singleshop.user.application.dto.AddressResponseDto;
import com.unicornstudy.singleshop.user.application.dto.AddressRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @PutMapping
    public ResponseEntity<AddressResponseDto> updateAddress(@LoginUser SessionUser user, @RequestBody @Valid AddressRequestDto addressRequestDto) {
        return new ResponseEntity<>(userService.updateAddress(user.getEmail(), addressRequestDto), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<AddressResponseDto> findAddressByUser(@LoginUser SessionUser user) {
        return new ResponseEntity<>(userService.findAddressByUser(user.getEmail()), HttpStatus.OK);
    }
}
