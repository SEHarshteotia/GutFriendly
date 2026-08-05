package com.gutfriendly.app.vendor.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.gutfriendly.app.vendor.status.PayoutStatus;

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
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Payout settlement record for earnings owed to a vendor shop.
 * <p>
 * Belongs to one {@link Store} and tracks amount, status, and payment period.
 */
@Entity
@Table(name = "store_payout")
@Data
@NoArgsConstructor
public class StorePayout {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long payoutId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id", nullable = false)
	private Store store;

	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal amount;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private PayoutStatus status = PayoutStatus.PENDING;

	private LocalDate periodStart;

	private LocalDate periodEnd;

	private LocalDateTime paidAt;

	@Column(length = 50)
	private String referenceNumber;

	@Column(length = 255)
	private String description;

	private LocalDateTime createdAt;

	@PrePersist
	public void prePersist() {
		if (createdAt == null) {
			createdAt = LocalDateTime.now();
		}
	}
}
