package com.gutfriendly.app.admin.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gutfriendly.app.admin.dto.response.CategoryPerformanceResponse;
import com.gutfriendly.app.admin.dto.response.DashboardSummaryDto;
import com.gutfriendly.app.admin.dto.response.MonthlyTrendResponse;
import com.gutfriendly.app.admin.dto.response.RecentActivityResponse;
import com.gutfriendly.app.admin.dto.response.UpcomingInspectionResponse;
import com.gutfriendly.app.admin.service.AdminDashBoardService;


@RestController
@RequestMapping("/admin/dashboard")
public class AdminDashboardController {
	
	final AdminDashBoardService service;

	AdminDashboardController(AdminDashBoardService service) {
		this.service = service;
	}
	
	 // this is for the topcards that is summary of the dashboards (Running well )
	@GetMapping("/summary")
	public ResponseEntity<DashboardSummaryDto> dashboardSummary() {
		DashboardSummaryDto summary  = service.getDashboardSummary();
		return ResponseEntity.ok(summary);
	}
	
	@GetMapping("/monthly-trends")
	public ResponseEntity<List<MonthlyTrendResponse>> monthlyTrends(){

	    return ResponseEntity.ok(service.getMonthlyTrends());

	}
	
	@GetMapping("/category-performance")
	public ResponseEntity<List<CategoryPerformanceResponse>>getCategoryPerformance(){

	    return ResponseEntity.ok(service.getCategoryPerformance());

	}
	
	@GetMapping("/recent-activities")
	public ResponseEntity<List<RecentActivityResponse>>getRecentActivity(){

	    return ResponseEntity.ok(service.getRecentActivities());

	}
	
	@GetMapping("/upcoming-inspections")
	public ResponseEntity<List<UpcomingInspectionResponse>> getUpcomingInspections() {
	    return ResponseEntity.ok(service.getUpcomingInspections());
	}
	
	
	
	

}
