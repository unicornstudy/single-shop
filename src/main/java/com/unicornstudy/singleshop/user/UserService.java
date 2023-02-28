package com.unicornstudy.singleshop.user;

import com.unicornstudy.singleshop.carts.exception.SessionExpiredException;
import com.unicornstudy.singleshop.user.dto.UpdateAddressDto;
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
}
