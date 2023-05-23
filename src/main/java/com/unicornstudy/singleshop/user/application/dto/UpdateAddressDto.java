package com.unicornstudy.singleshop.user.application.dto;

import com.unicornstudy.singleshop.delivery.domain.Address;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAddressDto {

    @NotBlank(message = "City cannot be blank")
    private String city;

    @NotBlank(message = "Street cannot be blank")
    private String street;

    @NotBlank(message = "Zipcode cannot be blank")
    @Pattern(regexp = "^[0-9]{5}$", message = "Invalid zipcode format")
    private String zipcode;

    public Address toEntity() {
        return new Address(city, street, zipcode);
    }
}
