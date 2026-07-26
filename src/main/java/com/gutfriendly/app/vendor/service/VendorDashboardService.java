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
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.gutfriendly.app.vendor.dto.VendorActiveOrderDTO;
import com.gutfriendly.app.vendor.dto.VendorAddressDTO;
import com.gutfriendly.app.vendor.dto.VendorDashboardResponseDTO;
import com.gutfriendly.app.vendor.dto.VendorDashboardSummaryDTO;
import com.gutfriendly.app.vendor.dto.VendorRecentReviewDTO;
import com.gutfriendly.app.vendor.dto.VendorStoreStatusDTO;
import com.gutfriendly.app.vendor.dto.VendorTopSellingItemDTO;
import com.gutfriendly.app.vendor.model.VendorAddress;
import com.gutfriendly.app.vendor.model.VendorDetails;
import com.gutfriendly.app.vendor.model.VendorOrder;
import com.gutfriendly.app.vendor.model.VendorOrderItem;
import com.gutfriendly.app.vendor.model.VendorReview;
import com.gutfriendly.app.vendor.model.VendorStore;
import com.gutfriendly.app.vendor.repository.ServiceableAreaRepo;
import com.gutfriendly.app.vendor.repository.VendorAddressRepo;
import com.gutfriendly.app.vendor.repository.VendorOrderItemRepo;
import com.gutfriendly.app.vendor.repository.VendorOrderRepo;
import com.gutfriendly.app.vendor.repository.VendorRepo;
import com.gutfriendly.app.vendor.repository.VendorReviewRepo;
import com.gutfriendly.app.vendor.repository.VendorStoreRepo;
import com.gutfriendly.app.vendor.status.VendorOrderStatus;
import com.gutfriendly.app.vendor.status.VendorStatus;

@Service
public class VendorDashboardService {

	private static final int REQUIRED_PROFILE_FIELDS = 6;

	private final VendorRepo vendorRepository;
	private final VendorAddressRepo addressRepository;
	private final ServiceableAreaRepo areaRepository;
	private final VendorStoreRepo storeRepository;
	private final VendorOrderRepo orderRepository;
	private final VendorOrderItemRepo orderItemRepository;
	private final VendorReviewRepo reviewRepository;

	VendorDashboardService(VendorRepo vendorRepository, VendorAddressRepo addressRepository,
			ServiceableAreaRepo areaRepository, VendorStoreRepo storeRepository, VendorOrderRepo orderRepository,
			VendorOrderItemRepo orderItemRepository, VendorReviewRepo reviewRepository) {
		this.vendorRepository = vendorRepository;
		this.addressRepository = addressRepository;
		this.areaRepository = areaRepository;
		this.storeRepository = storeRepository;
		this.orderRepository = orderRepository;
		this.orderItemRepository = orderItemRepository;
		this.reviewRepository = reviewRepository;
	}

	private VendorDetails findVendor(Integer vendorId) {
		return vendorRepository.findById(vendorId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vendor not found"));
	}

	@Transactional(readOnly = true)
	public VendorDashboardResponseDTO getDashboard(Integer vendorId) {
		VendorDetails vendor = findVendor(vendorId);

		VendorAddress address = addressRepository.findByVendor(vendor).orElse(null);
		boolean serviceableLocation = isServiceableStatus(vendor.getStatus());
		List<String> pendingRequirements = getPendingRequirements(vendor, address);

		return new VendorDashboardResponseDTO(
				vendor.getVendor_id(),
				buildFullName(vendor),
				vendor.getPhoneNo(),
				vendor.getEmail(),
				vendor.getIsActive(),
				vendor.getStatus(),
				vendor.getJoining_date(),
				VendorAddressDTO.from(address),
				serviceableLocation,
				calculateProfileCompletion(vendor, address),
				getNextAction(vendor.getStatus(), address, serviceableLocation),
				pendingRequirements,
				buildSummary(vendor),
				buildActiveOrders(vendor),
				buildTopSellingItems(vendor),
				buildRecentReviews(vendor),
				buildStoreStatus(vendor));
	}

	@Transactional(readOnly = true)
	public VendorDashboardSummaryDTO getDashboardSummary(Integer vendorId) {
		VendorDetails vendor = findVendor(vendorId);
		return buildSummary(vendor);
	}

	@Transactional(readOnly = true)
	public List<VendorActiveOrderDTO> getActiveOrders(Integer vendorId) {
		VendorDetails vendor = findVendor(vendorId);
		return buildActiveOrders(vendor);
	}

	@Transactional(readOnly = true)
	public List<VendorTopSellingItemDTO> getTopSellingItems(Integer vendorId) {
		VendorDetails vendor = findVendor(vendorId);
		return buildTopSellingItems(vendor);
	}

	@Transactional(readOnly = true)
	public List<VendorRecentReviewDTO> getRecentReviews(Integer vendorId) {
		VendorDetails vendor = findVendor(vendorId);
		return buildRecentReviews(vendor);
	}

	@Transactional(readOnly = true)
	public VendorStoreStatusDTO getStoreStatus(Integer vendorId) {
		VendorDetails vendor = findVendor(vendorId);
		return buildStoreStatus(vendor);
	}

	@Transactional
	public VendorDashboardResponseDTO recheckServiceability(Integer vendorId) {
		VendorDetails vendor = findVendor(vendorId);

		VendorAddress address = addressRepository.findByVendor(vendor)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vendor location not found"));

		boolean serviceableLocation = areaRepository.existsByPincode(address.getPincode());
		vendor.setStatus(getStatusAfterServiceabilityCheck(vendor.getStatus(), serviceableLocation));
		vendorRepository.save(vendor);

		return getDashboard(vendorId);
	}

	private VendorDashboardSummaryDTO buildSummary(VendorDetails vendor) {
		LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
		LocalDateTime endOfDay = startOfDay.plusDays(1).minusNanos(1);
		long todaysOrders = orderRepository.countByVendorAndCreatedAtBetween(vendor, startOfDay, endOfDay);
		BigDecimal todaysRevenue = orderRepository.sumRevenueByVendorAndCreatedAtBetween(vendor, startOfDay, endOfDay);
		BigDecimal averageOrderValue = todaysOrders == 0
				? BigDecimal.ZERO
				: todaysRevenue.divide(BigDecimal.valueOf(todaysOrders), 2, RoundingMode.HALF_UP);
		Double averageRating = reviewRepository.averageRatingByVendor(vendor);
		long reviewCount = reviewRepository.countByVendor(vendor);

		return new VendorDashboardSummaryDTO(
				todaysOrders,
				todaysRevenue,
				averageOrderValue,
				roundRating(averageRating),
				reviewCount);
	}

	private List<VendorActiveOrderDTO> buildActiveOrders(VendorDetails vendor) {
		List<VendorOrderStatus> activeStatuses = Arrays.asList(
				VendorOrderStatus.NEW,
				VendorOrderStatus.PREPARING,
				VendorOrderStatus.OUT_FOR_DELIVERY);

		return orderRepository.findTop5ByVendorAndStatusInOrderByCreatedAtDesc(vendor, activeStatuses)
				.stream()
				.map(this::toActiveOrderDTO)
				.toList();
	}

	private VendorActiveOrderDTO toActiveOrderDTO(VendorOrder order) {
		return new VendorActiveOrderDTO(
				order.getOrderId(),
				order.getOrderNumber(),
				buildItemsSummary(order.getItems()),
				order.getStatus(),
				getOrderStatusLabel(order.getStatus()),
				minutesAgo(order.getCreatedAt()));
	}

	private String buildItemsSummary(List<VendorOrderItem> items) {
		if (items == null || items.isEmpty()) {
			return "";
		}

		return items.stream()
				.map(item -> item.getQuantity() + " x " + item.getItemName())
				.collect(Collectors.joining(", "));
	}

	private List<VendorTopSellingItemDTO> buildTopSellingItems(VendorDetails vendor) {
		LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
		LocalDateTime endOfDay = startOfDay.plusDays(1).minusNanos(1);
		List<Object[]> rows = orderItemRepository.findTopSellingItems(vendor, startOfDay, endOfDay);
		List<VendorTopSellingItemDTO> topSellingItems = new ArrayList<>();

		for (int i = 0; i < rows.size() && i < 5; i++) {
			Object[] row = rows.get(i);
			topSellingItems.add(new VendorTopSellingItemDTO(
					i + 1,
					Objects.toString(row[0], ""),
					((Number) row[1]).longValue()));
		}

		return topSellingItems;
	}

	private List<VendorRecentReviewDTO> buildRecentReviews(VendorDetails vendor) {
		return reviewRepository.findTop3ByVendorOrderByCreatedAtDesc(vendor)
				.stream()
				.map(this::toRecentReviewDTO)
				.toList();
	}

	private VendorRecentReviewDTO toRecentReviewDTO(VendorReview review) {
		return new VendorRecentReviewDTO(
				review.getReviewId(),
				review.getCustomerName(),
				review.getCustomerImageUrl(),
				review.getRating(),
				review.getComment(),
				minutesAgo(review.getCreatedAt()));
	}

	private VendorStoreStatusDTO buildStoreStatus(VendorDetails vendor) {
		Optional<VendorStore> store = storeRepository.findByVendor(vendor);
		String fallbackStoreName = buildFullName(vendor);

		if (store.isEmpty()) {
			return new VendorStoreStatusDTO(
					fallbackStoreName.isBlank() ? "Vendor Store" : fallbackStoreName,
					null,
					false,
					null,
					false,
					false,
					null);
		}

		VendorStore vendorStore = store.get();
		return new VendorStoreStatusDTO(
				vendorStore.getStoreName(),
				vendorStore.getImageUrl(),
				Boolean.TRUE.equals(vendorStore.getIsOpen()),
				vendorStore.getOpenTime(),
				Boolean.TRUE.equals(vendorStore.getOnlineOrdersEnabled()),
				Boolean.TRUE.equals(vendorStore.getDeliveryPartnersEnabled()),
				vendorStore.getEstimatedPrepTimeMinutes());
	}

	private String buildFullName(VendorDetails vendor) {
		List<String> nameParts = new ArrayList<>();
		addIfPresent(nameParts, vendor.getFName());
		addIfPresent(nameParts, vendor.getMName());
		addIfPresent(nameParts, vendor.getLName());
		return String.join(" ", nameParts);
	}

	private int calculateProfileCompletion(VendorDetails vendor, VendorAddress address) {
		int completed = 0;

		completed += hasText(vendor.getFName()) ? 1 : 0;
		completed += hasText(vendor.getLName()) ? 1 : 0;
		completed += hasText(vendor.getPhoneNo()) ? 1 : 0;
		completed += hasText(vendor.getEmail()) ? 1 : 0;
		completed += hasText(vendor.getAadharNo()) || hasText(vendor.getPanNo()) ? 1 : 0;
		completed += address != null ? 1 : 0;

		return Math.round((completed * 100f) / REQUIRED_PROFILE_FIELDS);
	}

	private List<String> getPendingRequirements(VendorDetails vendor, VendorAddress address) {
		List<String> pendingRequirements = new ArrayList<>();

		if (!hasText(vendor.getEmail())) {
			pendingRequirements.add("Add email address");
		}

		if (!hasText(vendor.getAadharNo()) && !hasText(vendor.getPanNo())) {
			pendingRequirements.add("Add PAN or Aadhar details");
		}

		if (address == null) {
			pendingRequirements.add("Add vendor location");
		}

		if (vendor.getStatus() == VendorStatus.NOT_SERVICEABLE) {
			pendingRequirements.add("Choose a serviceable location");
		}

		if (vendor.getStatus() == VendorStatus.REJECTED || vendor.getStatus() == VendorStatus.SUSPENDED) {
			pendingRequirements.add("Contact support for account review");
		}

		return pendingRequirements;
	}

	private String getNextAction(VendorStatus status, VendorAddress address, boolean serviceableLocation) {
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
			return "Vendor account is ready for operations.";
		}

		if (status == VendorStatus.SUSPENDED) {
			return "Resolve suspension with support.";
		}

		if (status == VendorStatus.REJECTED) {
			return "Review rejection reason with support.";
		}

		return "Check account status.";
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

	private String getOrderStatusLabel(VendorOrderStatus status) {
		if (status == VendorOrderStatus.NEW) {
			return "New";
		}

		if (status == VendorOrderStatus.PREPARING) {
			return "Preparing";
		}

		if (status == VendorOrderStatus.OUT_FOR_DELIVERY) {
			return "Out for Delivery";
		}

		if (status == VendorOrderStatus.DELIVERED) {
			return "Delivered";
		}

		if (status == VendorOrderStatus.CANCELLED) {
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
}
