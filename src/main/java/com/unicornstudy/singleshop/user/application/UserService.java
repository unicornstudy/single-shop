package com.unicornstudy.singleshop.user.application;

import com.unicornstudy.singleshop.exception.carts.SessionExpiredException;
import com.unicornstudy.singleshop.user.application.dto.AddressResponseDto;
import com.unicornstudy.singleshop.user.domain.User;
import com.unicornstudy.singleshop.user.domain.repository.UserRepository;
import com.unicornstudy.singleshop.user.application.dto.AddressRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public AddressResponseDto updateAddress(String email, AddressRequestDto addressRequestDto) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new SessionExpiredException());
        user.updateAddress(addressRequestDto.toEntity());
        return AddressResponseDto.from(addressRequestDto.toEntity());
    }

    @Transactional(readOnly = true)
    public AddressResponseDto findAddressByUser(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new SessionExpiredException());
        return AddressResponseDto.from(user.getAddress());
    }
}
