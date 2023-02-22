package com.unicornstudy.singleshop.user.dto;

import com.unicornstudy.singleshop.delivery.Address;
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
