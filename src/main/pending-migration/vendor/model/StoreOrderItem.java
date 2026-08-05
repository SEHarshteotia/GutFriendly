package com.gutfriendly.app.vendor.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Line item on a {@link StoreOrder}, optionally linked to a {@link MenuItem}.
 * <p>
 * {@code itemName} and {@code unitPrice} are snapshotted at order time.
 */
@Entity
@Table(name = "store_order_item")
@Data
@NoArgsConstructor
public class StoreOrderItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long orderItemId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", nullable = false)
	private StoreOrder order;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	private MenuItem menuItem;

	@Column(nullable = false, length = 120)
	private String itemName;

	@Column(nullable = false)
	private Integer quantity;

	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal unitPrice = BigDecimal.ZERO;
}
