import {
  ChevronLeft,
  ChevronRight,
  MessageSquareText,
  Star,
} from "lucide-react";

import { useEffect, useState } from "react";

import LoadingSpinner from "./LoadingSpinner";
import { getReviewsByShop } from "../services/reviewService";

function ShopReviews({ shopId }) {
  const [reviews, setReviews] = useState([]);

  const [pageData, setPageData] = useState({
    number: 0,
    totalPages: 0,
    totalElements: 0,
    first: true,
    last: true,
  });

  const [page, setPage] = useState(0);

  const [ratingFilter, setRatingFilter] =
    useState("ALL");

  const [sortOption, setSortOption] =
    useState("NEWEST");

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    async function loadReviews() {
      if (!shopId) {
        return;
      }

      try {
        setLoading(true);
        setError("");

        let sortBy = "createdAt";
        let direction = "desc";

        if (sortOption === "OLDEST") {
          sortBy = "createdAt";
          direction = "asc";
        }

        if (sortOption === "HIGHEST") {
          sortBy = "rating";
          direction = "desc";
        }

        if (sortOption === "LOWEST") {
          sortBy = "rating";
          direction = "asc";
        }

        const data = await getReviewsByShop(
          shopId,
          page,
          5,
          sortBy,
          direction
        );

        setReviews(data.content || []);

        setPageData({
          number: data.number || 0,
          totalPages: data.totalPages || 0,
          totalElements: data.totalElements || 0,
          first: data.first ?? true,
          last: data.last ?? true,
        });
      } catch (requestError) {
        setError(requestError.message);
      } finally {
        setLoading(false);
      }
    }

    loadReviews();
  }, [shopId, page, sortOption]);

  function handleSortChange(event) {
    setSortOption(event.target.value);
    setPage(0);
  }

  function handleRatingFilter(rating) {
    setRatingFilter(rating);
    setPage(0);
  }

  function formatDate(dateValue) {
    if (!dateValue) {
      return "Date unavailable";
    }

    return new Date(dateValue).toLocaleDateString(
      "en-IN",
      {
        day: "numeric",
        month: "short",
        year: "numeric",
      }
    );
  }

  function formatText(value) {
    if (!value) {
      return "";
    }

    return value
      .replaceAll("_", " ")
      .toLowerCase()
      .replace(/\b\w/g, (letter) =>
        letter.toUpperCase()
      );
  }

  const displayedReviews =
    ratingFilter === "ALL"
      ? reviews
      : reviews.filter(
          (review) =>
            Number(review.rating) ===
            Number(ratingFilter)
        );

  if (loading) {
    return (
      <section className="shop-reviews-section section-container">
        <LoadingSpinner />
      </section>
    );
  }

  return (
    <section className="shop-reviews-section section-container">
      <div className="shop-reviews-header">
        <div>
          <p className="home-eyebrow">
            Verified feedback
          </p>

          <h2>Customer reviews</h2>

          <span>
            {pageData.totalElements} verified{" "}
            {pageData.totalElements === 1
              ? "review"
              : "reviews"}
          </span>
        </div>

        <div className="review-header-icon">
          <MessageSquareText size={25} />
        </div>
      </div>

      <div className="review-toolbar">
        <div className="rating-filter">
          <button
            type="button"
            className={
              ratingFilter === "ALL"
                ? "active"
                : ""
            }
            onClick={() =>
              handleRatingFilter("ALL")
            }
          >
            All
          </button>

          {[5, 4, 3, 2, 1].map((rating) => (
            <button
              type="button"
              key={rating}
              className={
                ratingFilter === String(rating)
                  ? "active"
                  : ""
              }
              onClick={() =>
                handleRatingFilter(
                  String(rating)
                )
              }
            >
              {rating}
              <Star
                size={14}
                fill="currentColor"
              />
            </button>
          ))}
        </div>

        <select
          value={sortOption}
          onChange={handleSortChange}
          aria-label="Sort customer reviews"
        >
          <option value="NEWEST">
            Newest first
          </option>

          <option value="OLDEST">
            Oldest first
          </option>

          <option value="HIGHEST">
            Highest rating
          </option>

          <option value="LOWEST">
            Lowest rating
          </option>
        </select>
      </div>

      {error && (
        <div className="error-message">
          {error}
        </div>
      )}

      {!error && reviews.length === 0 ? (
        <div className="empty-shop-reviews">
          <MessageSquareText size={38} />

          <h3>No reviews yet</h3>

          <p>
            Reviews from delivered and verified orders
            will appear here.
          </p>
        </div>
      ) : !error &&
        displayedReviews.length === 0 ? (
        <div className="empty-shop-reviews">
          <Star size={38} />

          <h3>
            No {ratingFilter}-star reviews on this page
          </h3>

          <p>
            Try another rating filter or move to a
            different page.
          </p>
        </div>
      ) : (
        <>
          <div className="shop-review-list">
            {displayedReviews.map((review) => (
              <article
                className="shop-review-card"
                key={review.reviewId}
              >
                <div className="review-card-top">
                  <div className="reviewer-information">
                    <div className="reviewer-avatar">
                      {review.userName
                        ?.charAt(0)
                        .toUpperCase() || "U"}
                    </div>

                    <div>
                      <h3>
                        {review.userName ||
                          "GutFriendly User"}
                      </h3>

                      <span>
                        {formatDate(review.createdAt)}
                      </span>
                    </div>
                  </div>

                  <div className="review-rating">
                    <Star
                      size={17}
                      fill="currentColor"
                    />

                    <strong>
                      {review.rating}
                    </strong>

                    <span>/5</span>
                  </div>
                </div>

                <div className="review-card-meta">
                  <span className="verified-review-label">
                    Verified order
                  </span>

                  {review.reviewType && (
                    <span className="review-type-label">
                      {formatText(
                        review.reviewType
                      )}
                    </span>
                  )}
                </div>

                {review.keywords?.length > 0 && (
                  <div className="display-review-keywords">
                    {review.keywords.map(
                      (keyword) => (
                        <span key={keyword}>
                          {formatText(keyword)}
                        </span>
                      )
                    )}
                  </div>
                )}

                {review.comment && (
                  <p className="shop-review-comment">
                    {review.comment}
                  </p>
                )}
              </article>
            ))}
          </div>

          {pageData.totalPages > 1 && (
            <div className="review-pagination">
              <button
                type="button"
                onClick={() =>
                  setPage((currentPage) =>
                    Math.max(
                      currentPage - 1,
                      0
                    )
                  )
                }
                disabled={pageData.first}
              >
                <ChevronLeft size={18} />
                Previous
              </button>

              <span>
                Page {pageData.number + 1} of{" "}
                {pageData.totalPages}
              </span>

              <button
                type="button"
                onClick={() =>
                  setPage((currentPage) =>
                    currentPage + 1
                  )
                }
                disabled={pageData.last}
              >
                Next
                <ChevronRight size={18} />
              </button>
            </div>
          )}
        </>
      )}
    </section>
  );
}

export default ShopReviews;