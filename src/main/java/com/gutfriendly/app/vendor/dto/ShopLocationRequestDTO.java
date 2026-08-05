package com.gutfriendly.app.vendor.dto;

import lombok.Data;

/**
 * Request body for saving a shop's physical address.
 * <p>
 * Used by {@code POST /vendor/{vendorId}/shops/{shopId}/location}.
 * {@code vendorId} and {@code shopId} are set from path variables; address fields are required for save.
 */
@Data
public class ShopLocationRequestDTO {

	private Integer vendorId;
	private Long shopId;
    private String houseNo;
    private String street;
    private String city;
    private String state;
    private String pincode;

}
