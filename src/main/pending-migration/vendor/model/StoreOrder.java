package com.gutfriendly.app.vendor.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.gutfriendly.app.vendor.status.StoreOrderStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Customer order placed against a vendor shop.
 * <p>
 * Belongs to one {@link Store} and contains many {@link StoreOrderItem} line items.
 */
@Entity
@Table(name = "store_order")
@Data
@NoArgsConstructor
public class StoreOrder {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long orderId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id", nullable = false)
	private Store store;

	@Column(nullable = false, unique = true, length = 30)
	private String orderNumber;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private StoreOrderStatus status = StoreOrderStatus.NEW;

	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal totalAmount = BigDecimal.ZERO;

	private LocalDateTime createdAt;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<StoreOrderItem> items = new ArrayList<>();

	@PrePersist
	public void prePersist() {
		if (createdAt == null) {
			createdAt = LocalDateTime.now();
		}
	}
}
