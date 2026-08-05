import { apiRequest } from "./api";

export function getWishlist(userId) {
  return apiRequest(`/wishlist/user/${userId}`);
}

export function addToWishlist(userId, shopId) {
  return apiRequest(
    `/wishlist/user/${userId}/shop/${shopId}`,
    {
      method: "POST",
    }
  );
}

export function removeFromWishlist(userId, shopId) {
  return apiRequest(
    `/wishlist/user/${userId}/shop/${shopId}`,
    {
      method: "DELETE",
    }
  );
}

export function checkWishlistStatus(userId, shopId) {
  return apiRequest(
    `/wishlist/user/${userId}/shop/${shopId}/status`
  );
}