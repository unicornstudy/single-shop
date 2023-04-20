package com.unicornstudy.singleshop.user.application;

import com.unicornstudy.singleshop.config.TestSetting;
import com.unicornstudy.singleshop.delivery.domain.Address;
import com.unicornstudy.singleshop.exception.carts.SessionExpiredException;
import com.unicornstudy.singleshop.user.application.dto.UpdateAddressDto;
import com.unicornstudy.singleshop.user.domain.User;
import com.unicornstudy.singleshop.user.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    private User user;
    private Address address;
    private UpdateAddressDto updateAddressDto;

    @BeforeEach
    void setUp() {
        address = TestSetting.setAddress();
        user = TestSetting.setUser(address);
        updateAddressDto = new UpdateAddressDto(
                "updated city", "updated street", "updated zipcode");
    }

    @DisplayName("주소 수정 테스트")
    @Test
    void update_address_test() {
        given(userRepository.findByEmail(any(String.class))).willReturn(Optional.ofNullable(user));

        userService.updateAddress(user.getEmail(), updateAddressDto);

        assertThat(user.getAddress().getCity()).isEqualTo(updateAddressDto.getCity());
        assertThat(user.getAddress().getStreet()).isEqualTo(updateAddressDto.getStreet());
        assertThat(user.getAddress().getZipcode()).isEqualTo(updateAddressDto.getZipcode());
    }

    @DisplayName("주소 수정 예외 테스트")
    @Test
    void update_address_exception_test() {
        given(userRepository.findByEmail(any(String.class))).willReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateAddress(user.getEmail(), updateAddressDto))
                .isInstanceOf(SessionExpiredException.class)
                .hasMessage(SessionExpiredException.ERROR_MESSAGE);
    }
}