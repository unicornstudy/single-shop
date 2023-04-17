package com.unicornstudy.singleshop.user.application.dto;

import com.unicornstudy.singleshop.delivery.domain.Address;
import lombok.Builder;
import lombok.Getter;

@Getter
public class FindAddressDto {

    private final String street;
    private final String city;
    private final String zipcode;

    @Builder
    public FindAddressDto(String street, String city, String zipcode) {
        this.street = street;
        this.city = city;
        this.zipcode = zipcode;
    }

    public static FindAddressDto from(Address address) {
        return FindAddressDto.builder()
                .street(address.getStreet())
                .city(address.getCity())
                .zipcode(address.getZipcode())
                .build();
    }
}
