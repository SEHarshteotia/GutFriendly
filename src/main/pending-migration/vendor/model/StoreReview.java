package com.gutfriendly.app.vendor.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
 * Customer review and optional vendor reply for a shop.
 * <p>
 * Belongs to one {@link Store}.
 */
@Entity
@Table(name = "store_review")
@Data
@NoArgsConstructor
public class StoreReview {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long reviewId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id", nullable = false)
	private Store store;

	@Column(nullable = false, length = 100)
	private String customerName;

	@Column(length = 255)
	private String customerImageUrl;

	@Column(nullable = false)
	private Integer rating;

	@Column(length = 500)
	private String comment;

	@Column(length = 500)
	private String vendorReply;

	private LocalDateTime repliedAt;

	private LocalDateTime createdAt;

	@PrePersist
	public void prePersist() {
		if (createdAt == null) {
			createdAt = LocalDateTime.now();
		}
	}
}
