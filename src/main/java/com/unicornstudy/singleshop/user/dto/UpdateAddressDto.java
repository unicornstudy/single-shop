package com.unicornstudy.singleshop.user.dto;

import com.unicornstudy.singleshop.delivery.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAddressDto {

    private String city;
    private String street;
    private String zipcode;

    public Address toEntity() {
        return new Address(city, street, zipcode);
    }
}
