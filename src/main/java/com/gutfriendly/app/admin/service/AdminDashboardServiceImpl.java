package com.gutfriendly.app.admin.service;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.gutfriendly.app.admin.dto.response.CategoryPerformanceResponse;
import com.gutfriendly.app.admin.dto.response.DashboardSummaryDto;
import com.gutfriendly.app.admin.dto.response.MonthlyTrendResponse;
import com.gutfriendly.app.admin.dto.response.RecentActivityResponse;
import com.gutfriendly.app.admin.dto.response.UpcomingInspectionResponse;
import com.gutfriendly.app.admin.enums.InspectionStatus;
import com.gutfriendly.app.admin.model.Certificate;
import com.gutfriendly.app.inspector.model.InspectionDetails;
import com.gutfriendly.app.admin.model.ShopDetails;
import com.gutfriendly.app.reviews.model.UserReviews;
import com.gutfriendly.app.admin.repository.CertificateRepository;
import com.gutfriendly.app.admin.repository.InspectionDetailsRepository;
import com.gutfriendly.app.admin.repository.ShopDetailsRepository;
import com.gutfriendly.app.reviews.repository.UserReviewsRepository;
import com.gutfriendly.app.admin.repository.VendorDetailsRepository;

@Service
public class AdminDashboardServiceImpl implements AdminDashBoardService {

	final ShopDetailsRepository shopDetailsRepo;

	final VendorDetailsRepository vendorDetailsRepo;

	final InspectionDetailsRepository inspectionDetailsRepo;

	final UserReviewsRepository reviewsRepo;

	final CertificateRepository certificateRepo;

	AdminDashboardServiceImpl(ShopDetailsRepository shopDetailsRepo, VendorDetailsRepository vendorDetailsRepo,
			InspectionDetailsRepository inspectionDetailsRepo, UserReviewsRepository reviewsRepo,
			CertificateRepository certificateRepo) {
		this.shopDetailsRepo = shopDetailsRepo;
		this.vendorDetailsRepo = vendorDetailsRepo;
		this.inspectionDetailsRepo = inspectionDetailsRepo;
		this.reviewsRepo = reviewsRepo;
		this.certificateRepo = certificateRepo;
	}

	@Override
	public DashboardSummaryDto getDashboardSummary() {

		DashboardSummaryDto summary = new DashboardSummaryDto();

		// 4. Total Verified Vendors
		summary.setTotalVerfiedVendors(vendorDetailsRepo.countByIsActive(true));

		summary.setActiveInspections(inspectionDetailsRepo.countScheduledInspections());

		// 6. Pending Vendor Approvals
		// Implement after adding approvalStatus in VendorDetails
		summary.setPendingVendorApprovals(0);

		// 7. Pending Complaints
		// Complaint module not ready yet
		summary.setPendingComplaints(0);

		// 8. Expiring Certificates
		// Implement when Certificate module is ready
		summary.setExpiringCertificates(0);

		return summary;
	}

	@Override
	public List<MonthlyTrendResponse> getMonthlyTrends() {

		List<Object[]> result = shopDetailsRepo.getMonthlyTrustTrend();

		List<MonthlyTrendResponse> response = new ArrayList<>();

		for (Object[] row : result) {

			int month = ((Number) row[0]).intValue();
			double score = ((Number) row[1]).doubleValue();

			response.add(
					new MonthlyTrendResponse(Month.of(month).getDisplayName(TextStyle.SHORT, Locale.ENGLISH), score));
		}

		return response;
	}

	@Override
	public List<RecentActivityResponse> getRecentActivities() {
		Pageable pageable = PageRequest.of(0, 5);
		List<RecentActivityResponse> activities = new ArrayList<>();

		List<ShopDetails> shops = shopDetailsRepo.findAllByOrderByCreatedAtDesc(pageable).getContent();
		List<InspectionDetails> inspections = inspectionDetailsRepo.findAllByOrderByInspectionDateDesc(pageable)
				.getContent();
		List<UserReviews> reviews = reviewsRepo.findAllByOrderByCreatedAtDesc(pageable).getContent();
		List<Certificate> certificates = certificateRepo.findAllByOrderByIssueDateDesc(pageable).getContent();

		for (ShopDetails shop : shops) {
			activities.add(new RecentActivityResponse("SHOP_REGISTERED", "New Shop" + shop.getShopName() + "registered",
					shop.getCreatedAt()));
		}
		for (InspectionDetails inspection : inspections) {
			activities.add(new RecentActivityResponse("INSPECTION",
					"Inspection Completed For" + inspection.getShop().getShopName(), inspection.getInspectionDate()));
		}
		for (UserReviews review : reviews) {
			activities.add(new RecentActivityResponse("REVIEW", review.getUser().getFname() + " "
					+ review.getUser().getLname() + "Reviewd" + review.getShop().getShopName(),
					review.getCreatedAt()));
		}
		for (Certificate certificate : certificates) {
			activities.add(new RecentActivityResponse("CERTIFICATE",
					"Certificate issued for" + certificate.getShop().getShopName(), certificate.getIssue_date()));
		}

		activities.sort(Comparator.comparing(RecentActivityResponse::getTime).reversed());
		return activities.stream().limit(5).toList();
	}

	@Override
	public List<CategoryPerformanceResponse> getCategoryPerformance() {
		List<Object[]> result = shopDetailsRepo.getCategoryPerformance();
		List<CategoryPerformanceResponse> response = new ArrayList<>();
		for (Object[] row : result) {
			String category = row[0].toString();
			Double score = ((Number) row[1]).doubleValue();
			response.add(new CategoryPerformanceResponse(category, score));
		}
		return response;
	}

	@Override
	public List<UpcomingInspectionResponse> getUpcomingInspections() {
		Pageable pageable = PageRequest.of(0, 5);

		List<InspectionDetails> inspections = inspectionDetailsRepo
				.findByStatusAndInspectionDateAfterOrderByInspectionDateAsc(InspectionStatus.SCHEDULED,
						LocalDateTime.now(), pageable)
				.getContent();

		List<UpcomingInspectionResponse> response = new ArrayList<>();

		for (InspectionDetails inspection : inspections) {

			response.add(new UpcomingInspectionResponse(inspection.getShop().getShopName(),
					inspection.getVendor().getFirstName() + " " + inspection.getVendor().getLastName(),
					inspection.getInspectionDate(), inspection.getStatus().name()));
		}

		return response;
	}

};
