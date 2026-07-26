package com.gutfriendly.app.dto;

import lombok.Data;

@Data
public class VendorLocationRequestDTO {

    private Integer vendorId;
    private String houseNo;
    private String street;
    private String city;
    private String state;
    private String pincode;

}
