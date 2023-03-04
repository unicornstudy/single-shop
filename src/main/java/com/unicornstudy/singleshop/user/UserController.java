package com.unicornstudy.singleshop.user;

import com.unicornstudy.singleshop.oauth2.LoginUser;
import com.unicornstudy.singleshop.oauth2.dto.SessionUser;
import com.unicornstudy.singleshop.user.dto.UpdateAddressDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @PutMapping
    public ResponseEntity updateAddress(@LoginUser SessionUser user, @RequestBody UpdateAddressDto updateAddressDto) {
        userService.updateAddress(user.getEmail(), updateAddressDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
