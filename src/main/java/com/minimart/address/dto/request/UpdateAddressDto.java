package com.minimart.address.dto.request;

import com.minimart.address.enums.AddressType;
import lombok.Data;

@Data
public class UpdateAddressDto {
    private String street;
    private String city;
    private String state;
    private String zipcode;
    private String country;
    private AddressType addressType;
}
