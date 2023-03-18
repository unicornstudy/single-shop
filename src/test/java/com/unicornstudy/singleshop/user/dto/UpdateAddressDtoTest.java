package com.unicornstudy.singleshop.user.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UpdateAddressDtoTest {

    private final String city = "testCity";
    private final String street = "testStreet";
    private final String zipcode = "testZipcode";

    private UpdateAddressDto updateAddressDto;

    @BeforeEach
    void setUp() {
        updateAddressDto = new UpdateAddressDto(city, street, zipcode);
    }

    @Test
    void 생성자_테스트() {
        UpdateAddressDto updateAddressDto = new UpdateAddressDto();

        assertThat(updateAddressDto).isNotNull();
    }

    @Test
    void 필드_테스트() {
        String testCity = "testCity";
        String testStreet = "testStreet";
        String testZipcode = "testZipcode";

        assertThat(updateAddressDto.getCity()).isEqualTo(testCity);
        assertThat(updateAddressDto.getStreet()).isEqualTo(testStreet);
        assertThat(updateAddressDto.getZipcode()).isEqualTo(testZipcode);
    }
}