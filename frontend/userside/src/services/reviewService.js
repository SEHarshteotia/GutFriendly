import { apiRequest } from "./api";

export function addReview(userId, reviewData) {
  return apiRequest(`/reviews/user/${userId}`, {
    method: "POST",
    body: JSON.stringify(reviewData),
  });
}

export function getReviewById(reviewId) {
  return apiRequest(`/reviews/${reviewId}`);
}

export function updateReview(
  userId,
  reviewId,
  reviewData
) {
  return apiRequest(
    `/reviews/user/${userId}/${reviewId}`,
    {
      method: "PUT",
      body: JSON.stringify(reviewData),
    }
  );
}

export function getReviewsByShop(
  shopId,
  page = 0,
  size = 5,
  sortBy = "createdAt",
  direction = "desc"
) {
  return apiRequest(
    `/reviews/shop/${shopId}` +
      `?page=${page}` +
      `&size=${size}` +
      `&sortBy=${sortBy}` +
      `&direction=${direction}`
  );
}

export function getReviewSummary(shopId) {
  return apiRequest(
    `/reviews/shop/${shopId}/summary`
  );
}