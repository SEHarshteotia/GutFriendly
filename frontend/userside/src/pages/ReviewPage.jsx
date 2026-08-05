import {
  ArrowLeft,
  Check,
  Gift,
  MessageSquareText,
  Star,
} from "lucide-react";

import { useEffect, useState } from "react";

import {
  Link,
  useNavigate,
  useParams,
  useSearchParams,
} from "react-router-dom";

import {
  addReview,
  getReviewById,
  updateReview,
} from "../services/reviewService";

const reviewKeywords = [
  {
    value: "GOOD_HYGIENE",
    label: "Good hygiene",
  },
  {
    value: "GOOD_PACKAGING",
    label: "Good packaging",
  },
  {
    value: "GOOD_QUALITY",
    label: "Good quality",
  },
  {
    value: "FRESH_FOOD",
    label: "Fresh food",
  },
  {
    value: "TASTY",
    label: "Tasty",
  },
  {
    value: "VALUE_FOR_MONEY",
    label: "Value for money",
  },
  {
    value: "ON_TIME_DELIVERY",
    label: "On-time delivery",
  },
  {
    value: "POOR_HYGIENE",
    label: "Poor hygiene",
  },
  {
    value: "BAD_PACKAGING",
    label: "Bad packaging",
  },
  {
    value: "POOR_QUALITY",
    label: "Poor quality",
  },
  {
    value: "STALE_FOOD",
    label: "Stale food",
  },
  {
    value: "LATE_DELIVERY",
    label: "Late delivery",
  },
];

function ReviewPage() {
  const { orderId } = useParams();
  const [searchParams] = useSearchParams();

  const navigate = useNavigate();

  const userId = localStorage.getItem("userId");

  const reviewId = searchParams.get("reviewId");
  const isEditing = Boolean(reviewId);

  const [rating, setRating] = useState(0);
  const [hoverRating, setHoverRating] =
    useState(0);

  const [comment, setComment] = useState("");
  const [keywords, setKeywords] = useState([]);

  const [loadingReview, setLoadingReview] =
    useState(isEditing);

  const [submitting, setSubmitting] =
    useState(false);

  const [error, setError] = useState("");

  useEffect(() => {
    async function loadExistingReview() {
      if (!isEditing) {
        setLoadingReview(false);
        return;
      }

      try {
        setLoadingReview(true);
        setError("");

        const existingReview =
          await getReviewById(reviewId);

        setRating(existingReview.rating || 0);
        setComment(existingReview.comment || "");
        setKeywords(existingReview.keywords || []);
      } catch (requestError) {
        setError(requestError.message);
      } finally {
        setLoadingReview(false);
      }
    }

    loadExistingReview();
  }, [isEditing, reviewId]);

  function toggleKeyword(keyword) {
    setKeywords((currentKeywords) => {
      if (currentKeywords.includes(keyword)) {
        return currentKeywords.filter(
          (item) => item !== keyword
        );
      }

      return [...currentKeywords, keyword];
    });
  }

  function getExpectedReward() {
    const length = comment.trim().length;

    if (length >= 200) {
      return 20;
    }

    if (length >= 100) {
      return 15;
    }

    if (keywords.length > 0) {
      return 10;
    }

    return 5;
  }

  function getReviewType() {
    const length = comment.trim().length;

    if (length >= 100) {
      return "Detailed review";
    }

    if (keywords.length > 0) {
      return "Rapid review";
    }

    return "Basic review";
  }

  async function handleSubmit(event) {
    event.preventDefault();

    if (!userId) {
      setError("Please sign in before reviewing.");
      return;
    }

    if (rating < 1 || rating > 5) {
      setError(
        "Please select a rating between 1 and 5."
      );
      return;
    }

    try {
      setSubmitting(true);
      setError("");

      const reviewData = {
        orderId: Number(orderId),
        rating,
        comment: comment.trim(),
        keywords,
      };

      let review;

      if (isEditing) {
        review = await updateReview(
          userId,
          reviewId,
          reviewData
        );
      } else {
        review = await addReview(
          userId,
          reviewData
        );
      }

      if (!isEditing) {
        const currentPoints = Number(
          localStorage.getItem("rewardPoints") ||
            0
        );

        localStorage.setItem(
          "rewardPoints",
          String(
            currentPoints +
              Number(
                review.pointsAwarded || 0
              )
          )
        );
      }

      navigate("/orders", {
        state: {
          message: isEditing
            ? "Review updated successfully. Reward points remain unchanged."
            : `Review submitted successfully. You earned ${review.pointsAwarded} points.`,
        },
      });
    } catch (requestError) {
      setError(requestError.message);
    } finally {
      setSubmitting(false);
    }
  }

  const characterCount =
    comment.trim().length;

  if (loadingReview) {
    return (
      <div className="page-message">
        <h2>Loading review...</h2>
        <p>Please wait.</p>
      </div>
    );
  }

  return (
    <div className="review-page section-container">
      <div className="review-header">
        <Link
          to="/orders"
          className="cart-back-link"
        >
          <ArrowLeft size={18} />
          Back to orders
        </Link>

        <p className="home-eyebrow">
          Verified experience
        </p>

        <h1>
          {isEditing ? "Edit" : "Review"} order #
          {orderId}
        </h1>

        <p>
          Your feedback helps other customers choose
          safer and more trustworthy food.
        </p>
      </div>

      {error && (
        <div className="error-message">
          {error}
        </div>
      )}

      <div className="review-layout">
        <form
          className="review-form-card"
          onSubmit={handleSubmit}
        >
          <section className="review-form-section">
            <div className="review-section-heading">
              <Star size={22} />

              <div>
                <h2>Rate your experience</h2>
                <p>
                  Choose a rating between 1 and 5.
                </p>
              </div>
            </div>

            <div className="star-rating">
              {[1, 2, 3, 4, 5].map((star) => {
                const selected =
                  star <=
                  (hoverRating || rating);

                return (
                  <button
                    type="button"
                    key={star}
                    onClick={() =>
                      setRating(star)
                    }
                    onMouseEnter={() =>
                      setHoverRating(star)
                    }
                    onMouseLeave={() =>
                      setHoverRating(0)
                    }
                    aria-label={`Rate ${star} stars`}
                  >
                    <Star
                      size={38}
                      fill={
                        selected
                          ? "currentColor"
                          : "none"
                      }
                    />
                  </button>
                );
              })}
            </div>

            <p className="selected-rating-text">
              {rating
                ? `${rating} out of 5 selected`
                : "No rating selected"}
            </p>
          </section>

          <section className="review-form-section">
            <div className="review-section-heading">
              <Check size={22} />

              <div>
                <h2>Quick review keywords</h2>
                <p>
                  Select everything that matches your
                  experience.
                </p>
              </div>
            </div>

            <div className="review-keywords">
              {reviewKeywords.map((keyword) => {
                const selected =
                  keywords.includes(
                    keyword.value
                  );

                return (
                  <button
                    type="button"
                    key={keyword.value}
                    className={
                      selected
                        ? "review-keyword selected"
                        : "review-keyword"
                    }
                    onClick={() =>
                      toggleKeyword(
                        keyword.value
                      )
                    }
                  >
                    {selected && (
                      <Check size={15} />
                    )}

                    {keyword.label}
                  </button>
                );
              })}
            </div>
          </section>

          <section className="review-form-section">
            <div className="review-section-heading">
              <MessageSquareText size={22} />

              <div>
                <h2>Tell us more</h2>
                <p>
                  Add details about hygiene, quality,
                  packaging or delivery.
                </p>
              </div>
            </div>

            <div className="form-group">
              <label htmlFor="reviewComment">
                Review comment
              </label>

              <textarea
                id="reviewComment"
                value={comment}
                onChange={(event) =>
                  setComment(
                    event.target.value
                  )
                }
                rows="7"
                maxLength="500"
                placeholder="How was the food, hygiene, packaging and overall experience?"
              />

              <div className="review-character-row">
                <span>
                  {characterCount}/500 characters
                </span>

                <span>
                  100+ characters = detailed review
                </span>
              </div>
            </div>
          </section>

          <button
            type="submit"
            className="submit-review-button"
            disabled={submitting}
          >
            {submitting
              ? isEditing
                ? "Updating review..."
                : "Submitting review..."
              : isEditing
                ? "Update review"
                : "Submit review"}
          </button>
        </form>

        <aside className="review-reward-card">
          <div className="review-reward-icon">
            <Gift size={28} />
          </div>

          <p className="home-eyebrow">
            Review rewards
          </p>

          <h2>
            {isEditing
              ? "Already granted"
              : `${getExpectedReward()} points`}
          </h2>

          <p>
            {isEditing
              ? "Editing this review will not change the reward points already earned."
              : "Expected reward based on your current review."}
          </p>

          <div className="review-type-box">
            <span>Current review type</span>
            <strong>
              {getReviewType()}
            </strong>
          </div>

          <div className="reward-rule-list">
            <div>
              <span>Rating only</span>
              <strong>5 points</strong>
            </div>

            <div>
              <span>Rapid keywords</span>
              <strong>10 points</strong>
            </div>

            <div>
              <span>100+ characters</span>
              <strong>15 points</strong>
            </div>

            <div>
              <span>200+ characters</span>
              <strong>20 points</strong>
            </div>
          </div>

          <p className="review-reward-note">
            Rewards are granted only once. Editing or
            deleting the review later will not change the
            reward points already earned.
          </p>
        </aside>
      </div>
    </div>
  );
}

export default ReviewPage;