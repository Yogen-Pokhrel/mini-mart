package com.minimart.address.dto.response;

import com.minimart.address.enums.AddressType;
import lombok.Data;

@Data
public class AddressDetailDto {
    private int id;

    private String street;
    private String city;
    private String state;
    private String zipcode;
    private String country;
    private AddressType addressType;
}
