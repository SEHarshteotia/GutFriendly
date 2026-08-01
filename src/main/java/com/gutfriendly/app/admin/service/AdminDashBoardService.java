package com.gutfriendly.app.admin.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gutfriendly.app.admin.dto.response.CategoryPerformanceResponse;
import com.gutfriendly.app.admin.dto.response.DashboardSummaryDto;
import com.gutfriendly.app.admin.dto.response.MonthlyTrendResponse;
import com.gutfriendly.app.admin.dto.response.RecentActivityResponse;
import com.gutfriendly.app.admin.dto.response.UpcomingInspectionResponse;


@Service
public interface AdminDashBoardService {
	
public DashboardSummaryDto getDashboardSummary(); // this returns (averageGutTrustScore,totalVerfiedVendors,activeInspections,
//pendingVendorApprovals,pendingComplaints,expiringCertificates;)
	
public  List<MonthlyTrendResponse> getMonthlyTrends();

public List<RecentActivityResponse> getRecentActivities();

public List<CategoryPerformanceResponse> getCategoryPerformance();

public List<UpcomingInspectionResponse> getUpcomingInspections();



}
