package com.gutfriendly.app.admin.enums;

import java.util.EnumSet;
import java.util.Set;

public enum InspectionStatus {
	SCHEDULED,
	ASSIGNED,
	IN_PROGRESS,
	REPORT_SUBMITTED,
	UNDER_ADMIN_REVIEW,
	APPROVED,
	REJECTED,
	CLOSED_FOR_REINSPECTION;

	public static Set<InspectionStatus> activeInspectionStatuses() {
		return EnumSet.of(
				SCHEDULED,
				ASSIGNED,
				IN_PROGRESS,
				REPORT_SUBMITTED,
				UNDER_ADMIN_REVIEW);
	}
}
