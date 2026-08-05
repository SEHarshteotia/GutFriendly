import {
  MessageSquareText,
  Star,
} from "lucide-react";

import { useEffect, useState } from "react";

import LoadingSpinner from "./LoadingSpinner";

import {
  getReviewSummary,
} from "../services/reviewService";

function ShopReviewSummary({ shopId }) {
  const [summary, setSummary] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    async function loadSummary() {
      if (!shopId) {
        return;
      }

      try {
        setLoading(true);
        setError("");

        const data =
          await getReviewSummary(shopId);

        setSummary(data);
      } catch (requestError) {
        setError(requestError.message);
      } finally {
        setLoading(false);
      }
    }

    loadSummary();
  }, [shopId]);

  function calculatePercentage(count) {
    if (!summary?.totalReviews) {
      return 0;
    }

    return Math.round(
      (count / summary.totalReviews) * 100
    );
  }

  const ratingRows = summary
    ? [
        {
          rating: 5,
          count: summary.fiveStar,
        },
        {
          rating: 4,
          count: summary.fourStar,
        },
        {
          rating: 3,
          count: summary.threeStar,
        },
        {
          rating: 2,
          count: summary.twoStar,
        },
        {
          rating: 1,
          count: summary.oneStar,
        },
      ]
    : [];

  if (loading) {
    return (
      <section className="review-summary-section section-container">
        <LoadingSpinner />
      </section>
    );
  }

  if (error) {
    return (
      <section className="review-summary-section section-container">
        <div className="error-message">
          {error}
        </div>
      </section>
    );
  }

  return (
    <section className="review-summary-section section-container">
      <div className="review-summary-header">
        <div>
          <p className="home-eyebrow">
            Rating overview
          </p>

          <h2>Customer rating summary</h2>

          <p>
            Based on verified reviews from delivered
            orders.
          </p>
        </div>

        <div className="review-summary-header-icon">
          <MessageSquareText size={25} />
        </div>
      </div>

      <div className="review-summary-card">
        <div className="review-average-area">
          <div className="review-average-score">
            {Number(
              summary?.averageRating || 0
            ).toFixed(1)}
          </div>

          <div className="review-average-stars">
            {[1, 2, 3, 4, 5].map((star) => (
              <Star
                key={star}
                size={22}
                fill={
                  star <=
                  Math.round(
                    summary?.averageRating || 0
                  )
                    ? "currentColor"
                    : "none"
                }
              />
            ))}
          </div>

          <p>
            Based on{" "}
            <strong>
              {summary?.totalReviews || 0}
            </strong>{" "}
            verified{" "}
            {summary?.totalReviews === 1
              ? "review"
              : "reviews"}
          </p>
        </div>

        <div className="review-rating-breakdown">
          {ratingRows.map((row) => {
            const percentage =
              calculatePercentage(row.count);

            return (
              <div
                className="review-breakdown-row"
                key={row.rating}
              >
                <div className="review-breakdown-label">
                  <span>{row.rating}</span>
                  <Star
                    size={14}
                    fill="currentColor"
                  />
                </div>

                <div className="review-progress-track">
                  <div
                    className="review-progress-fill"
                    style={{
                      width: `${percentage}%`,
                    }}
                  />
                </div>

                <span className="review-breakdown-percentage">
                  {percentage}%
                </span>

                <span className="review-breakdown-count">
                  ({row.count})
                </span>
              </div>
            );
          })}
        </div>
      </div>
    </section>
  );
}

export default ShopReviewSummary;