package com.unicornstudy.singleshop.user.application.dto;

import com.unicornstudy.singleshop.delivery.domain.Address;
import lombok.Getter;

@Getter
public class UpdateAddressDto {

    private String city;
    private String street;
    private String zipcode;

    public Address toEntity() {
        return new Address(city, street, zipcode);
    }
}
