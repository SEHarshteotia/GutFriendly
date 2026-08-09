package com.gutfriendly.app.admin.dto.request;

public class ModerateReviewRequest {


	

		private boolean active;
		private String reason;

		public ModerateReviewRequest() {
		}

		public ModerateReviewRequest(boolean active, String reason) {
			this.active = active;
			this.reason = reason;
		}

		public boolean isActive() {
			return active;
		}

		public void setActive(boolean active) {
			this.active = active;
		}

		public String getReason() {
			return reason;
		}

		public void setReason(String reason) {
			this.reason = reason;
		}
	}



