import api from "./api";

const REVIEWS_BASE = "/admin/reviews";

/**
 * Lists customer reviews platform-wide (or scoped to a shop / rating /
 * shop-name search), paginated.
 */
export async function getAllReviews({
    page = 0,
    size = 10,
    sortBy = "createdAt",
    direction = "desc",
    shopId,
    rating,
    shopName,
} = {}) {
    const response = await api.get(REVIEWS_BASE, {
        params: {
            page,
            size,
            sortBy,
            direction,
            ...(shopId ? { shopId } : {}),
            ...(rating ? { rating } : {}),
            ...(shopName ? { shopName } : {}),
        },
    });

    return response.data;
}

export async function getReviewById(reviewId) {
    const response = await api.get(`${REVIEWS_BASE}/${reviewId}`);
    return response.data;
}

export async function getReviewsSummary() {
    const response = await api.get(`${REVIEWS_BASE}/summary`);
    return response.data;
}

export async function hideReview(reviewId, reason) {
    const response = await api.patch(`${REVIEWS_BASE}/${reviewId}/moderate`, {
        active: false,
        reason,
    });
    return response.data;
}

export async function restoreReview(reviewId) {
    const response = await api.patch(`${REVIEWS_BASE}/${reviewId}/moderate`, {
        active: true,
    });
    return response.data;
}
