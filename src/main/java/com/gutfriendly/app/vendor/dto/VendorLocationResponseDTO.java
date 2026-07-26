package com.gutfriendly.app.vendor.dto;

import com.gutfriendly.app.vendor.status.VendorStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendorLocationResponseDTO {

    private boolean serviceable;
    private VendorStatus status;
    private String message;

}