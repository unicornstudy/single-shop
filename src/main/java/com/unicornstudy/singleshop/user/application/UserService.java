package com.unicornstudy.singleshop.user.application;

import com.unicornstudy.singleshop.exception.carts.SessionExpiredException;
import com.unicornstudy.singleshop.user.application.dto.FindAddressDto;
import com.unicornstudy.singleshop.user.domain.User;
import com.unicornstudy.singleshop.user.domain.repository.UserRepository;
import com.unicornstudy.singleshop.user.application.dto.UpdateAddressDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void updateAddress(String email, UpdateAddressDto updateAddressDto) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new SessionExpiredException());
        user.updateAddress(updateAddressDto.toEntity());
    }

    @Transactional(readOnly = true)
    public FindAddressDto findAddressByUser(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new SessionExpiredException());
        return FindAddressDto.from(user.getAddress());
    }
}
