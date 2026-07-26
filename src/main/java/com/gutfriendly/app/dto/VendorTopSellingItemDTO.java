package com.gutfriendly.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendorTopSellingItemDTO {

	private int rank;
	private String itemName;
	private long quantitySold;
}
