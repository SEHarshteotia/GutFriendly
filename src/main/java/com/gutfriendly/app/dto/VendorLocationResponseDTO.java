package com.gutfriendly.app.dto;

import com.gutfriendly.app.status.VendorStatus;

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