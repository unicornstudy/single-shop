package com.unicornstudy.singleshop.user;

import com.unicornstudy.singleshop.carts.exception.SessionExpiredException;
import com.unicornstudy.singleshop.user.dto.UpdateAddressDto;
import org.junit.jupiter.api.BeforeEach;
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

    private final String name = "test";
    private final String email = "testEmail";
    private final String picture = "testPicture";
    private final String city = "testCity";
    private final String street = "testStreet";
    private final String zipcode = "testZipcode";

    private User user;
    private UpdateAddressDto updateAddressDto;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name(name)
                .email(email)
                .picture(picture)
                .role(Role.USER)
                .build();

        updateAddressDto = new UpdateAddressDto(city, street, zipcode);
    }

    @Test
    void 주소_수정_테스트() {
        given(userRepository.findByEmail(any(String.class))).willReturn(Optional.ofNullable(user));

        userService.updateAddress(email, updateAddressDto);

        assertThat(user.getAddress().getCity()).isEqualTo(city);
        assertThat(user.getAddress().getStreet()).isEqualTo(street);
        assertThat(user.getAddress().getZipcode()).isEqualTo(zipcode);
    }

    @Test
    void 주소_수정_예외() {
        assertThatThrownBy(() -> userService.updateAddress(email, updateAddressDto))
                .isInstanceOf(SessionExpiredException.class)
                .hasMessage(SessionExpiredException.ERROR_MESSAGE);
    }
}