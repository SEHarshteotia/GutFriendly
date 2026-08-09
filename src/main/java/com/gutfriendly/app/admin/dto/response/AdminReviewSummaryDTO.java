package com.gutfriendly.app.admin.dto.response;

public class AdminReviewSummaryDTO {


		private double averageRating;
		private long totalReviews;
		private long shopsReviewed;
		private long fiveStar;
		private long fourStar;
		private long threeStar;
		private long twoStar;
		private long oneStar;

		public AdminReviewSummaryDTO() {
		}

		public AdminReviewSummaryDTO(double averageRating, long totalReviews, long shopsReviewed, long fiveStar,
				long fourStar, long threeStar, long twoStar, long oneStar) {
			this.averageRating = averageRating;
			this.totalReviews = totalReviews;
			this.shopsReviewed = shopsReviewed;
			this.fiveStar = fiveStar;
			this.fourStar = fourStar;
			this.threeStar = threeStar;
			this.twoStar = twoStar;
			this.oneStar = oneStar;
		}

		public double getAverageRating() {
			return averageRating;
		}

		public void setAverageRating(double averageRating) {
			this.averageRating = averageRating;
		}

		public long getTotalReviews() {
			return totalReviews;
		}

		public void setTotalReviews(long totalReviews) {
			this.totalReviews = totalReviews;
		}

		public long getShopsReviewed() {
			return shopsReviewed;
		}

		public void setShopsReviewed(long shopsReviewed) {
			this.shopsReviewed = shopsReviewed;
		}

		public long getFiveStar() {
			return fiveStar;
		}

		public void setFiveStar(long fiveStar) {
			this.fiveStar = fiveStar;
		}

		public long getFourStar() {
			return fourStar;
		}

		public void setFourStar(long fourStar) {
			this.fourStar = fourStar;
		}

		public long getThreeStar() {
			return threeStar;
		}

		public void setThreeStar(long threeStar) {
			this.threeStar = threeStar;
		}

		public long getTwoStar() {
			return twoStar;
		}

		public void setTwoStar(long twoStar) {
			this.twoStar = twoStar;
		}

		public long getOneStar() {
			return oneStar;
		}

		public void setOneStar(long oneStar) {
			this.oneStar = oneStar;
		}
	}



