package com.gutfriendly.app.vendor.dto;

import com.gutfriendly.app.vendor.enums.VendorStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response after saving shop location with serviceability outcome.
 * <p>
 * Used by {@code POST /vendor/{vendorId}/shops/{shopId}/location}.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopLocationResponseDTO {

    private boolean serviceable;
    private VendorStatus status;
    private String message;

}