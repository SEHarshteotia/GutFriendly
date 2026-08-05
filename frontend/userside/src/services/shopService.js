import { apiRequest } from "./api";

export function getAllShops() {
  return apiRequest("/shops");
}

export function getTrustedVendors() {
  return apiRequest("/shops/trusted-vendors");
}

export function searchShops(keyword) {
  return apiRequest(
    `/shops/search?keyword=${encodeURIComponent(keyword)}`
  );
}

export function getShopsByCategory(category) {
  return apiRequest(`/shops/category/${category}`);
}

export function getShopById(shopId) {
  return apiRequest(`/shops/${shopId}`);
}