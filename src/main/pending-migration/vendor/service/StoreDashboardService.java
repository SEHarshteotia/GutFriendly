package com.gutfriendly.app.vendor.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.gutfriendly.app.vendor.dto.OrderOverviewPointDTO;
import com.gutfriendly.app.vendor.dto.StoreActiveOrderDTO;
import com.gutfriendly.app.vendor.dto.StoreAddressDTO;
import com.gutfriendly.app.vendor.dto.StoreDashboardResponseDTO;
import com.gutfriendly.app.vendor.dto.StoreDashboardSummaryDTO;
import com.gutfriendly.app.vendor.dto.StoreRecentReviewDTO;
import com.gutfriendly.app.vendor.dto.StoreStatusDTO;
import com.gutfriendly.app.vendor.dto.StoreTopSellingItemDTO;
import com.gutfriendly.app.vendor.model.StoreAddress;
import com.gutfriendly.app.vendor.model.VendorDetails;
import com.gutfriendly.app.vendor.model.StoreOrder;
import com.gutfriendly.app.vendor.model.StoreOrderItem;
import com.gutfriendly.app.vendor.model.StoreReview;
import com.gutfriendly.app.vendor.model.Store;
import com.gutfriendly.app.vendor.repository.ServiceableAreaRepo;
import com.gutfriendly.app.vendor.repository.StoreAddressRepo;
import com.gutfriendly.app.vendor.repository.StoreOrderItemRepo;
import com.gutfriendly.app.vendor.repository.StoreOrderRepo;
import com.gutfriendly.app.vendor.repository.VendorRepo;
import com.gutfriendly.app.vendor.repository.StoreReviewRepo;
import com.gutfriendly.app.vendor.repository.StoreRepo;
import com.gutfriendly.app.vendor.status.StoreOrderStatus;
import com.gutfriendly.app.vendor.status.VendorStatus;

/**
 * Aggregates dashboard analytics, onboarding status, and serviceability checks for vendor shops.
 */
@Service
public class StoreDashboardService {

	private static final int REQUIRED_PROFILE_FIELDS = 6;

	private final VendorRepo vendorRepository;
	private final StoreRepo storeRepository;
	private final StoreAddressRepo addressRepository;
	private final ServiceableAreaRepo areaRepository;
	private final StoreOrderRepo orderRepository;
	private final StoreOrderItemRepo orderItemRepository;
	private final StoreReviewRepo reviewRepository;

	StoreDashboardService(VendorRepo vendorRepository, StoreRepo storeRepository,
			StoreAddressRepo addressRepository, ServiceableAreaRepo areaRepository,
			StoreOrderRepo orderRepository, StoreOrderItemRepo orderItemRepository,
			StoreReviewRepo reviewRepository) {
		this.vendorRepository = vendorRepository;
		this.storeRepository = storeRepository;
		this.addressRepository = addressRepository;
		this.areaRepository = areaRepository;
		this.orderRepository = orderRepository;
		this.orderItemRepository = orderItemRepository;
		this.reviewRepository = reviewRepository;
	}

	private Store findShop(Integer vendorId, Long shopId) {
		VendorDetails vendor = vendorRepository.findById(vendorId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vendor not found"));

		return storeRepository.findByStoreIdAndVendor(shopId, vendor)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Shop not found"));
	}

	/**
	 * Builds the complete dashboard response for a shop.
	 *
	 * @param vendorId the vendor's primary key
	 * @param shopId the shop's primary key
	 * @return full dashboard payload including summary, orders, reviews, and onboarding hints
	 * @throws ResponseStatusException with {@link HttpStatus#NOT_FOUND} if vendor or shop not found
	 */
	@Transactional(readOnly = true)
	public StoreDashboardResponseDTO getDashboard(Integer vendorId, Long shopId) {
		VendorDetails vendor = vendorRepository.findById(vendorId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vendor not found"));
		Store store = findShop(vendorId, shopId);

		StoreAddress address = addressRepository.findByStore(store).orElse(null);
		boolean serviceableLocation = isServiceableStatus(store.getStatus());
		List<String> pendingRequirements = getPendingRequirements(vendor, store, address);

		return new StoreDashboardResponseDTO(
				vendor.getVendor_id(),
				store.getStoreId(),
				store.getStoreName(),
				buildFullName(vendor),
				vendor.getPhoneNo(),
				vendor.getEmail(),
				vendor.getIsActive(),
				store.getStatus(),
				vendor.getJoining_date(),
				StoreAddressDTO.from(address),
				serviceableLocation,
				calculateProfileCompletion(vendor, address),
				getNextAction(store.getStatus(), address, serviceableLocation),
				pendingRequirements,
				buildSummary(store),
				buildActiveOrders(store),
				buildTopSellingItems(store),
				buildRecentReviews(store),
				buildStoreStatus(store));
	}

	/**
	 * Returns hourly order count and revenue for the current day (24 data points).
	 *
	 * @param vendorId the vendor's primary key
	 * @param shopId the shop's primary key
	 * @return list of hourly overview points
	 * @throws ResponseStatusException with {@link HttpStatus#NOT_FOUND} if vendor or shop not found
	 */
	@Transactional(readOnly = true)
	public List<OrderOverviewPointDTO> getOrderOverview(Integer vendorId, Long shopId) {
		Store store = findShop(vendorId, shopId);
		LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
		LocalDateTime endOfDay = startOfDay.plusDays(1).minusNanos(1);

		List<Object[]> rows = orderRepository.findHourlyOrderStats(store, startOfDay, endOfDay);
		List<OrderOverviewPointDTO> points = new ArrayList<>();

		for (int hour = 0; hour < 24; hour++) {
			long orders = 0;
			BigDecimal revenue = BigDecimal.ZERO;

			for (Object[] row : rows) {
				if (((Number) row[0]).intValue() == hour) {
					orders = ((Number) row[1]).longValue();
					revenue = (BigDecimal) row[2];
					break;
				}
			}

			points.add(new OrderOverviewPointDTO(hour, formatHourLabel(hour), orders, revenue));
		}

		return points;
	}

	/**
	 * Returns today's summary metrics with day-over-day percentage changes.
	 *
	 * @param vendorId the vendor's primary key
	 * @param shopId the shop's primary key
	 * @return dashboard summary DTO
	 * @throws ResponseStatusException with {@link HttpStatus#NOT_FOUND} if vendor or shop not found
	 */
	@Transactional(readOnly = true)
	public StoreDashboardSummaryDTO getDashboardSummary(Integer vendorId, Long shopId) {
		return buildSummary(findShop(vendorId, shopId));
	}

	/**
	 * Returns up to five most recent active orders for dashboard display.
	 *
	 * @param vendorId the vendor's primary key
	 * @param shopId the shop's primary key
	 * @return list of active order summaries
	 * @throws ResponseStatusException with {@link HttpStatus#NOT_FOUND} if vendor or shop not found
	 */
	@Transactional(readOnly = true)
	public List<StoreActiveOrderDTO> getActiveOrders(Integer vendorId, Long shopId) {
		return buildActiveOrders(findShop(vendorId, shopId));
	}

	/**
	 * Returns up to five top-selling items for today.
	 *
	 * @param vendorId the vendor's primary key
	 * @param shopId the shop's primary key
	 * @return ranked top-selling item list
	 * @throws ResponseStatusException with {@link HttpStatus#NOT_FOUND} if vendor or shop not found
	 */
	@Transactional(readOnly = true)
	public List<StoreTopSellingItemDTO> getTopSellingItems(Integer vendorId, Long shopId) {
		return buildTopSellingItems(findShop(vendorId, shopId));
	}

	/**
	 * Returns up to three most recent customer reviews.
	 *
	 * @param vendorId the vendor's primary key
	 * @param shopId the shop's primary key
	 * @return recent review summaries
	 * @throws ResponseStatusException with {@link HttpStatus#NOT_FOUND} if vendor or shop not found
	 */
	@Transactional(readOnly = true)
	public List<StoreRecentReviewDTO> getRecentReviews(Integer vendorId, Long shopId) {
		return buildRecentReviews(findShop(vendorId, shopId));
	}

	/**
	 * Returns operational store status flags for dashboard display.
	 *
	 * @param vendorId the vendor's primary key
	 * @param shopId the shop's primary key
	 * @return store open/online/delivery status
	 * @throws ResponseStatusException with {@link HttpStatus#NOT_FOUND} if vendor or shop not found
	 */
	@Transactional(readOnly = true)
	public StoreStatusDTO getStoreStatus(Integer vendorId, Long shopId) {
		return buildStoreStatus(findShop(vendorId, shopId));
	}

	/**
	 * Re-checks pincode serviceability for the shop address and updates shop status accordingly.
	 *
	 * @param vendorId the vendor's primary key
	 * @param shopId the shop's primary key
	 * @return refreshed full dashboard after status update
	 * @throws ResponseStatusException with {@link HttpStatus#NOT_FOUND} if vendor or shop not found
	 * @throws ResponseStatusException with {@link HttpStatus#BAD_REQUEST} if shop has no saved address
	 */
	@Transactional
	public StoreDashboardResponseDTO recheckServiceability(Integer vendorId, Long shopId) {
		Store store = findShop(vendorId, shopId);

		StoreAddress address = addressRepository.findByStore(store)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Shop location not found"));

		boolean serviceableLocation = areaRepository.existsByPincode(address.getPincode());
		store.setStatus(getStatusAfterServiceabilityCheck(store.getStatus(), serviceableLocation));
		storeRepository.save(store);

		return getDashboard(vendorId, shopId);
	}

	private StoreDashboardSummaryDTO buildSummary(Store store) {
		LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
		LocalDateTime endOfDay = startOfDay.plusDays(1).minusNanos(1);
		LocalDateTime yesterdayStart = startOfDay.minusDays(1);
		LocalDateTime yesterdayEnd = startOfDay.minusNanos(1);

		long todaysOrders = orderRepository.countByStoreAndCreatedAtBetween(store, startOfDay, endOfDay);
		long yesterdaysOrders = orderRepository.countByStoreAndCreatedAtBetween(store, yesterdayStart, yesterdayEnd);

		BigDecimal todaysRevenue = orderRepository.sumRevenueByStoreAndCreatedAtBetween(store, startOfDay, endOfDay);
		BigDecimal yesterdaysRevenue = orderRepository.sumRevenueByStoreAndCreatedAtBetween(store, yesterdayStart,
				yesterdayEnd);

		BigDecimal averageOrderValue = todaysOrders == 0
				? BigDecimal.ZERO
				: todaysRevenue.divide(BigDecimal.valueOf(todaysOrders), 2, RoundingMode.HALF_UP);

		BigDecimal yesterdaysAvgOrderValue = yesterdaysOrders == 0
				? BigDecimal.ZERO
				: yesterdaysRevenue.divide(BigDecimal.valueOf(yesterdaysOrders), 2, RoundingMode.HALF_UP);

		Double averageRating = reviewRepository.averageRatingByStore(store);
		long reviewCount = reviewRepository.countByStore(store);

		return new StoreDashboardSummaryDTO(
				todaysOrders,
				todaysRevenue,
				averageOrderValue,
				roundRating(averageRating),
				reviewCount,
				percentChange(todaysOrders, yesterdaysOrders),
				percentChange(todaysRevenue, yesterdaysRevenue),
				percentChange(averageOrderValue, yesterdaysAvgOrderValue));
	}

	private List<StoreActiveOrderDTO> buildActiveOrders(Store store) {
		List<StoreOrderStatus> activeStatuses = Arrays.asList(
				StoreOrderStatus.NEW,
				StoreOrderStatus.PREPARING,
				StoreOrderStatus.OUT_FOR_DELIVERY);

		return orderRepository.findTop5ByStoreAndStatusInOrderByCreatedAtDesc(store, activeStatuses)
				.stream()
				.map(this::toActiveOrderDTO)
				.toList();
	}

	private StoreActiveOrderDTO toActiveOrderDTO(StoreOrder order) {
		return new StoreActiveOrderDTO(
				order.getOrderId(),
				order.getOrderNumber(),
				buildItemsSummary(order.getItems()),
				order.getStatus(),
				getOrderStatusLabel(order.getStatus()),
				minutesAgo(order.getCreatedAt()));
	}

	private String buildItemsSummary(List<StoreOrderItem> items) {
		if (items == null || items.isEmpty()) {
			return "";
		}

		return items.stream()
				.map(item -> item.getQuantity() + " x " + item.getItemName())
				.collect(Collectors.joining(", "));
	}

	private List<StoreTopSellingItemDTO> buildTopSellingItems(Store store) {
		LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
		LocalDateTime endOfDay = startOfDay.plusDays(1).minusNanos(1);
		List<Object[]> rows = orderItemRepository.findTopSellingItems(store, startOfDay, endOfDay);
		List<StoreTopSellingItemDTO> topSellingItems = new ArrayList<>();

		for (int i = 0; i < rows.size() && i < 5; i++) {
			Object[] row = rows.get(i);
			topSellingItems.add(new StoreTopSellingItemDTO(
					i + 1,
					Objects.toString(row[0], ""),
					((Number) row[1]).longValue()));
		}

		return topSellingItems;
	}

	private List<StoreRecentReviewDTO> buildRecentReviews(Store store) {
		return reviewRepository.findTop3ByStoreOrderByCreatedAtDesc(store)
				.stream()
				.map(this::toRecentReviewDTO)
				.toList();
	}

	private StoreRecentReviewDTO toRecentReviewDTO(StoreReview review) {
		return new StoreRecentReviewDTO(
				review.getReviewId(),
				review.getCustomerName(),
				review.getCustomerImageUrl(),
				review.getRating(),
				review.getComment(),
				minutesAgo(review.getCreatedAt()));
	}

	private StoreStatusDTO buildStoreStatus(Store store) {
		return new StoreStatusDTO(
				store.getStoreId(),
				store.getStoreName(),
				store.getImageUrl(),
				Boolean.TRUE.equals(store.getIsOpen()),
				store.getOpenTime(),
				Boolean.TRUE.equals(store.getOnlineOrdersEnabled()),
				store.getEstimatedPrepTimeMinutes(),
				store.getRating(),
				store.getRatingCount());
	}

	private String buildFullName(VendorDetails vendor) {
		List<String> nameParts = new ArrayList<>();
		addIfPresent(nameParts, vendor.getFName());
		addIfPresent(nameParts, vendor.getMName());
		addIfPresent(nameParts, vendor.getLName());
		return String.join(" ", nameParts);
	}

	private int calculateProfileCompletion(VendorDetails vendor, StoreAddress address) {
		int completed = 0;

		completed += hasText(vendor.getFName()) ? 1 : 0;
		completed += hasText(vendor.getLName()) ? 1 : 0;
		completed += hasText(vendor.getPhoneNo()) ? 1 : 0;
		completed += hasText(vendor.getEmail()) ? 1 : 0;
		completed += hasText(vendor.getAadharNo()) || hasText(vendor.getPanNo()) ? 1 : 0;
		completed += address != null ? 1 : 0;

		return Math.round((completed * 100f) / REQUIRED_PROFILE_FIELDS);
	}

	private List<String> getPendingRequirements(VendorDetails vendor, Store store, StoreAddress address) {
		List<String> pendingRequirements = new ArrayList<>();

		if (!hasText(vendor.getEmail())) {
			pendingRequirements.add("Add email address");
		}

		if (!hasText(vendor.getAadharNo()) && !hasText(vendor.getPanNo())) {
			pendingRequirements.add("Add PAN or Aadhar details");
		}

		if (address == null) {
			pendingRequirements.add("Add shop location for " + store.getStoreName());
		}

		if (store.getStatus() == VendorStatus.NOT_SERVICEABLE) {
			pendingRequirements.add("Choose a serviceable location for " + store.getStoreName());
		}

		if (store.getStatus() == VendorStatus.REJECTED || store.getStatus() == VendorStatus.SUSPENDED) {
			pendingRequirements.add("Contact support for shop review: " + store.getStoreName());
		}

		return pendingRequirements;
	}

	private String getNextAction(VendorStatus status, StoreAddress address, boolean serviceableLocation) {
		if (address == null) {
			return "Add your shop location to check serviceability.";
		}

		if (!serviceableLocation || status == VendorStatus.NOT_SERVICEABLE) {
			return "Update the shop address to a serviceable pincode.";
		}

		if (status == VendorStatus.PENDING || status == VendorStatus.UNDER_REVIEW) {
			return "Wait for admin approval.";
		}

		if (status == VendorStatus.APPROVED || status == VendorStatus.SERVICEABLE) {
			return "Shop is ready for operations.";
		}

		if (status == VendorStatus.SUSPENDED) {
			return "Resolve suspension with support.";
		}

		if (status == VendorStatus.REJECTED) {
			return "Review rejection reason with support.";
		}

		return "Check shop status.";
	}

	private VendorStatus getStatusAfterServiceabilityCheck(VendorStatus currentStatus, boolean serviceableLocation) {
		if (!serviceableLocation) {
			return VendorStatus.NOT_SERVICEABLE;
		}

		if (currentStatus == VendorStatus.NOT_SERVICEABLE || currentStatus == VendorStatus.PENDING) {
			return VendorStatus.UNDER_REVIEW;
		}

		return currentStatus;
	}

	private boolean isServiceableStatus(VendorStatus status) {
		return status == VendorStatus.SERVICEABLE
				|| status == VendorStatus.UNDER_REVIEW
				|| status == VendorStatus.APPROVED;
	}

	private String getOrderStatusLabel(StoreOrderStatus status) {
		if (status == StoreOrderStatus.NEW) {
			return "New";
		}

		if (status == StoreOrderStatus.PREPARING) {
			return "Preparing";
		}

		if (status == StoreOrderStatus.OUT_FOR_DELIVERY) {
			return "Out for Delivery";
		}

		if (status == StoreOrderStatus.DELIVERED) {
			return "Delivered";
		}

		if (status == StoreOrderStatus.CANCELLED) {
			return "Cancelled";
		}

		return "Unknown";
	}

	private long minutesAgo(LocalDateTime createdAt) {
		if (createdAt == null) {
			return 0;
		}

		return Math.max(0, Duration.between(createdAt, LocalDateTime.now()).toMinutes());
	}

	private double roundRating(Double rating) {
		if (rating == null) {
			return 0;
		}

		return BigDecimal.valueOf(rating)
				.setScale(1, RoundingMode.HALF_UP)
				.doubleValue();
	}

	private void addIfPresent(List<String> values, String value) {
		if (hasText(value)) {
			values.add(value.trim());
		}
	}

	private boolean hasText(String value) {
		return value != null && !value.isBlank();
	}

	private double percentChange(long current, long previous) {
		if (previous == 0) {
			return current > 0 ? 100.0 : 0.0;
		}
		return BigDecimal.valueOf((current - previous) * 100.0 / previous)
				.setScale(1, RoundingMode.HALF_UP)
				.doubleValue();
	}

	private double percentChange(BigDecimal current, BigDecimal previous) {
		if (previous == null || previous.signum() == 0) {
			return current != null && current.signum() > 0 ? 100.0 : 0.0;
		}
		return current.subtract(previous)
				.multiply(BigDecimal.valueOf(100))
				.divide(previous, 1, RoundingMode.HALF_UP)
				.doubleValue();
	}

	private String formatHourLabel(int hour) {
		if (hour == 0) {
			return "12 AM";
		}
		if (hour < 12) {
			return hour + " AM";
		}
		if (hour == 12) {
			return "12 PM";
		}
		return (hour - 12) + " PM";
	}
}
