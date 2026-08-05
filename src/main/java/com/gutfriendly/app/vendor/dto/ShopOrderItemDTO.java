package com.gutfriendly.app.vendor.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Line item on a vendor order.
 * <p>
 * Nested inside {@link ShopOrderDTO}.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopOrderItemDTO {

	private Long orderItemId;
	private Long itemId;
	private String itemName;
	private Integer quantity;
	private BigDecimal unitPrice;
}
