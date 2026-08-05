package com.gutfriendly.app.vendor.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.gutfriendly.app.orders.model.OrderItems;
import com.gutfriendly.app.admin.enums.InspectionStatus;
import com.gutfriendly.app.admin.enums.ServiceAvailabilityStatus;
import com.gutfriendly.app.admin.enums.ShopStatus;
import com.gutfriendly.app.admin.model.ShopDetails;
import com.gutfriendly.app.orders.model.UserOrders;
import com.gutfriendly.app.admin.model.VendorDetails;
import com.gutfriendly.app.admin.model.VendorShopAddress;
import com.gutfriendly.app.orders.repository.OrderItemsRepository;
import com.gutfriendly.app.admin.repository.InspectionDetailsRepository;
import com.gutfriendly.app.admin.repository.PincodeRepository;
import com.gutfriendly.app.admin.repository.ShopDetailsRepository;
import com.gutfriendly.app.orders.repository.UserOrdersRepository;
import com.gutfriendly.app.admin.repository.VendorDetailsRepository;
import com.gutfriendly.app.inspector.model.InspectionDetails;
import com.gutfriendly.app.vendor.dto.OrderOverviewPointDTO;
import com.gutfriendly.app.vendor.dto.ShopActiveOrderDTO;
import com.gutfriendly.app.vendor.dto.ShopDashboardResponseDTO;
import com.gutfriendly.app.vendor.dto.ShopDashboardSummaryDTO;
import com.gutfriendly.app.vendor.dto.ShopRecentReviewDTO;
import com.gutfriendly.app.vendor.dto.ShopStatusDTO;
import com.gutfriendly.app.vendor.dto.ShopTopSellingItemDTO;
import com.gutfriendly.app.vendor.mapper.AddressMapper;
import com.gutfriendly.app.vendor.mapper.OrderStatusMapper;
import com.gutfriendly.app.reviews.model.ShopReview;
import com.gutfriendly.app.reviews.repository.ShopReviewRepository;
import com.gutfriendly.app.vendor.mapper.ShopStatusMapper;
import com.gutfriendly.app.vendor.enums.ShopOrderStatus;
import com.gutfriendly.app.vendor.enums.VendorStatus;

@Service
public class ShopDashboardService {

	private static final int REQUIRED_PROFILE_FIELDS = 6;

	private final VendorDetailsRepository vendorRepository;
	private final ShopDetailsRepository shopRepository;
	private final PincodeRepository pincodeRepository;
	private final InspectionDetailsRepository inspectionRepository;
	private final UserOrdersRepository orderRepository;
	private final OrderItemsRepository orderItemRepository;
	private final ShopReviewRepository reviewRepository;

	ShopDashboardService(VendorDetailsRepository vendorRepository, ShopDetailsRepository shopRepository,
			PincodeRepository pincodeRepository, InspectionDetailsRepository inspectionRepository,
			UserOrdersRepository orderRepository,
			OrderItemsRepository orderItemRepository, ShopReviewRepository reviewRepository) {
		this.vendorRepository = vendorRepository;
		this.shopRepository = shopRepository;
		this.pincodeRepository = pincodeRepository;
		this.inspectionRepository = inspectionRepository;
		this.orderRepository = orderRepository;
		this.orderItemRepository = orderItemRepository;
		this.reviewRepository = reviewRepository;
	}

	private ShopDetails findShop(Integer vendorId, Long shopId) {
		VendorDetails vendor = vendorRepository.findById(vendorId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vendor not found"));

		return shopRepository.findByShopIdAndVendor(shopId.intValue(), vendor)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Shop not found"));
	}

	@Transactional(readOnly = true)
	public ShopDashboardResponseDTO getDashboard(Integer vendorId, Long shopId) {
		VendorDetails vendor = vendorRepository.findById(vendorId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vendor not found"));
		ShopDetails shop = findShop(vendorId, shopId);

		VendorShopAddress address = shop.getAddress_id();
		VendorStatus vendorStatus = ShopStatusMapper.toVendorStatus(shop);
		boolean serviceableLocation = ShopStatusMapper.isServiceableStatus(vendorStatus);
		List<String> pendingRequirements = getPendingRequirements(vendor, shop, address);

		return new ShopDashboardResponseDTO(
				vendor.getVendorId(),
				(long) shop.getShopId(),
				shop.getShopName(),
				buildFullName(vendor),
				vendor.getPhoneNo(),
				vendor.getEmail(),
				vendor.isActive(),
				vendorStatus,
				vendor.getJoiningDate() != null ? java.sql.Timestamp.valueOf(vendor.getJoiningDate()) : null,
				AddressMapper.toDto(address),
				serviceableLocation,
				calculateProfileCompletion(vendor, address),
				getNextAction(shop, address, serviceableLocation),
				pendingRequirements,
				buildSummary(shop),
				buildActiveOrders(shop),
				buildTopSellingItems(shop),
				buildRecentReviews(shop),
				buildShopStatus(shop));
	}

	@Transactional(readOnly = true)
	public List<OrderOverviewPointDTO> getOrderOverview(Integer vendorId, Long shopId) {
		ShopDetails shop = findShop(vendorId, shopId);
		LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
		LocalDateTime endOfDay = startOfDay.plusDays(1).minusNanos(1);

		List<Object[]> rows = orderRepository.findHourlyOrderStats(shop, startOfDay, endOfDay);
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

	@Transactional(readOnly = true)
	public ShopDashboardSummaryDTO getDashboardSummary(Integer vendorId, Long shopId) {
		return buildSummary(findShop(vendorId, shopId));
	}

	@Transactional(readOnly = true)
	public List<ShopActiveOrderDTO> getActiveOrders(Integer vendorId, Long shopId) {
		return buildActiveOrders(findShop(vendorId, shopId));
	}

	@Transactional(readOnly = true)
	public List<ShopTopSellingItemDTO> getTopSellingItems(Integer vendorId, Long shopId) {
		return buildTopSellingItems(findShop(vendorId, shopId));
	}

	@Transactional(readOnly = true)
	public List<ShopRecentReviewDTO> getRecentReviews(Integer vendorId, Long shopId) {
		return buildRecentReviews(findShop(vendorId, shopId));
	}

	@Transactional(readOnly = true)
	public ShopStatusDTO getShopStatus(Integer vendorId, Long shopId) {
		return buildShopStatus(findShop(vendorId, shopId));
	}

	@Transactional
	public ShopDashboardResponseDTO recheckServiceability(Integer vendorId, Long shopId) {
		ShopDetails shop = findShop(vendorId, shopId);

		VendorShopAddress address = shop.getAddress_id();
		if (address == null || address.getPinCode() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Shop location not found");
		}

		boolean serviceableLocation = pincodeRepository.existsById(address.getPinCode().getPin_code());
		ShopStatusMapper.applyServiceabilityResult(shop, serviceableLocation);
		shopRepository.save(shop);

		return getDashboard(vendorId, shopId);
	}

	private ShopDashboardSummaryDTO buildSummary(ShopDetails shop) {
		LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
		LocalDateTime endOfDay = startOfDay.plusDays(1).minusNanos(1);
		LocalDateTime yesterdayStart = startOfDay.minusDays(1);
		LocalDateTime yesterdayEnd = startOfDay.minusNanos(1);

		long todaysOrders = orderRepository.countByShopAndOrderedAtBetween(shop, startOfDay, endOfDay);
		long yesterdaysOrders = orderRepository.countByShopAndOrderedAtBetween(shop, yesterdayStart, yesterdayEnd);

		BigDecimal todaysRevenue = orderRepository.sumRevenueByShopAndOrderedAtBetween(shop, startOfDay, endOfDay);
		BigDecimal yesterdaysRevenue = orderRepository.sumRevenueByShopAndOrderedAtBetween(shop, yesterdayStart,
				yesterdayEnd);

		BigDecimal averageOrderValue = todaysOrders == 0
				? BigDecimal.ZERO
				: todaysRevenue.divide(BigDecimal.valueOf(todaysOrders), 2, RoundingMode.HALF_UP);

		BigDecimal yesterdaysAvgOrderValue = yesterdaysOrders == 0
				? BigDecimal.ZERO
				: yesterdaysRevenue.divide(BigDecimal.valueOf(yesterdaysOrders), 2, RoundingMode.HALF_UP);

		Double averageRating = reviewRepository.calculateAverageRating(shop.getShopId());
		long reviewCount = reviewRepository.countByShopShopIdAndActiveTrue(shop.getShopId());

		return new ShopDashboardSummaryDTO(
				todaysOrders,
				todaysRevenue,
				averageOrderValue,
				roundRating(averageRating),
				reviewCount,
				percentChange(todaysOrders, yesterdaysOrders),
				percentChange(todaysRevenue, yesterdaysRevenue),
				percentChange(averageOrderValue, yesterdaysAvgOrderValue));
	}

	private List<ShopActiveOrderDTO> buildActiveOrders(ShopDetails shop) {
		return orderRepository
				.findTop5ByShopAndStatusInOrderByOrderedAtDesc(shop, OrderStatusMapper.activeStatuses())
				.stream()
				.map(this::toActiveOrderDTO)
				.toList();
	}

	private ShopActiveOrderDTO toActiveOrderDTO(UserOrders order) {
		ShopOrderStatus status = OrderStatusMapper.toShopOrderStatus(order.getStatus());
		return new ShopActiveOrderDTO(
				(long) order.getOrderId(),
				"ORD-" + order.getOrderId(),
				buildItemsSummary(order.getOrderItems()),
				status,
				OrderStatusMapper.statusLabel(status),
				minutesAgo(order.getOrderedAt()));
	}

	private String buildItemsSummary(List<OrderItems> items) {
		if (items == null || items.isEmpty()) {
			return "";
		}

		return items.stream()
				.map(item -> item.getQuantity() + " x "
						+ (item.getFood() != null ? item.getFood().getFoodName() : "Item"))
				.collect(Collectors.joining(", "));
	}

	private List<ShopTopSellingItemDTO> buildTopSellingItems(ShopDetails shop) {
		LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
		LocalDateTime endOfDay = startOfDay.plusDays(1).minusNanos(1);
		List<Object[]> rows = orderItemRepository.findTopSellingItems(shop, startOfDay, endOfDay);
		List<ShopTopSellingItemDTO> topSellingItems = new ArrayList<>();

		for (int i = 0; i < rows.size() && i < 5; i++) {
			Object[] row = rows.get(i);
			topSellingItems.add(new ShopTopSellingItemDTO(
					i + 1,
					Objects.toString(row[0], ""),
					((Number) row[1]).longValue()));
		}

		return topSellingItems;
	}

	private List<ShopRecentReviewDTO> buildRecentReviews(ShopDetails shop) {
		return reviewRepository.findTop3ByShop_ShopIdAndActiveTrueOrderByCreatedAtDesc(shop.getShopId())
				.stream()
				.map(this::toRecentReviewDTO)
				.toList();
	}

	private ShopRecentReviewDTO toRecentReviewDTO(ShopReview review) {
		String customerName = review.getUser().getFname() + " " + review.getUser().getLname();
		return new ShopRecentReviewDTO(
				(long) review.getReviewId(),
				customerName,
				null,
				review.getRating(),
				review.getComment(),
				minutesAgo(review.getCreatedAt()));
	}

	private ShopStatusDTO buildShopStatus(ShopDetails shop) {
		return new ShopStatusDTO(
				(long) shop.getShopId(),
				shop.getShopName(),
				shop.getImageUrl(),
				Boolean.TRUE.equals(shop.getIsOpen()),
				shop.getOpenTime(),
				Boolean.TRUE.equals(shop.getOnlineOrdersEnabled()),
				shop.getEstimatedPrepTimeMinutes(),
				shop.getRating(),
				shop.getRatingCount() != null ? shop.getRatingCount() : 0L);
	}

	private String buildFullName(VendorDetails vendor) {
		List<String> nameParts = new ArrayList<>();
		addIfPresent(nameParts, vendor.getFirstName());
		addIfPresent(nameParts, vendor.getMiddleName());
		addIfPresent(nameParts, vendor.getLastName());
		return String.join(" ", nameParts);
	}

	private int calculateProfileCompletion(VendorDetails vendor, VendorShopAddress address) {
		int completed = 0;

		completed += hasText(vendor.getFirstName()) ? 1 : 0;
		completed += hasText(vendor.getLastName()) ? 1 : 0;
		completed += hasText(vendor.getPhoneNo()) ? 1 : 0;
		completed += hasText(vendor.getEmail()) ? 1 : 0;
		completed += hasText(vendor.getAdharNo()) || hasText(vendor.getPanNo()) ? 1 : 0;
		completed += address != null ? 1 : 0;

		return Math.round((completed * 100f) / REQUIRED_PROFILE_FIELDS);
	}

	private List<String> getPendingRequirements(VendorDetails vendor, ShopDetails shop, VendorShopAddress address) {
		List<String> pendingRequirements = new ArrayList<>();
		VendorStatus status = ShopStatusMapper.toVendorStatus(shop);

		if (!hasText(vendor.getEmail())) {
			pendingRequirements.add("Add email address");
		}

		if (!hasText(vendor.getAdharNo()) && !hasText(vendor.getPanNo())) {
			pendingRequirements.add("Add PAN or Aadhar details");
		}

		if (address == null) {
			pendingRequirements.add("Add shop location for " + shop.getShopName());
		}

		if (status == VendorStatus.NOT_SERVICEABLE) {
			pendingRequirements.add("Choose a serviceable location for " + shop.getShopName());
		}

		if (shop.getServiceAvailabilityStatus() == ServiceAvailabilityStatus.SERVICEABLE
				&& !hasActiveInspection(shop.getShopId())) {
			pendingRequirements.add("Book an inspection for " + shop.getShopName());
		}

		if (status == VendorStatus.REJECTED || status == VendorStatus.SUSPENDED) {
			pendingRequirements.add("Contact support for shop review: " + shop.getShopName());
		}

		return pendingRequirements;
	}

	private String getNextAction(ShopDetails shop, VendorShopAddress address, boolean serviceableLocation) {
		VendorStatus status = ShopStatusMapper.toVendorStatus(shop);

		if (status == VendorStatus.SUSPENDED) {
			return "Resolve suspension with support.";
		}

		if (status == VendorStatus.REJECTED) {
			return "Review rejection reason with support.";
		}

		if (address == null) {
			return "Add your shop location to check serviceability.";
		}

		if (!serviceableLocation || shop.getServiceAvailabilityStatus() == ServiceAvailabilityStatus.NOT_SERVICEABLE) {
			return "Update the shop address to a serviceable pincode.";
		}

		Optional<InspectionDetails> activeInspection = findActiveInspection(shop.getShopId());
		if (activeInspection.isEmpty()) {
			if (shop.getStatus() == ShopStatus.VERIFIED) {
				return "Shop is ready for operations.";
			}
			return "Book inspection.";
		}

		return switch (activeInspection.get().getStatus()) {
			case SCHEDULED -> "Wait for inspector assignment.";
			case ASSIGNED -> "Inspection scheduled. Waiting for inspector to start.";
			case IN_PROGRESS -> "Inspection in progress.";
			case REPORT_SUBMITTED, UNDER_ADMIN_REVIEW -> "Wait for admin approval.";
			default -> "Check shop status.";
		};
	}

	private boolean hasActiveInspection(int shopId) {
		return inspectionRepository.existsByShop_ShopIdAndStatusIn(shopId,
				InspectionStatus.activeInspectionStatuses());
	}

	private Optional<InspectionDetails> findActiveInspection(int shopId) {
		return inspectionRepository.findFirstByShop_ShopIdAndStatusInOrderByInspectionDateDesc(shopId,
				InspectionStatus.activeInspectionStatuses());
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
