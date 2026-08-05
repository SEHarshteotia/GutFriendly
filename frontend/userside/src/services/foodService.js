import { apiRequest } from "./api";

export function getMenuByShop(
  shopId,
  page = 0,
  size = 20,
  sortBy = "foodName",
  direction = "asc"
) {
  return apiRequest(
    `/foods/shop/${shopId}` +
      `?page=${page}` +
      `&size=${size}` +
      `&sortBy=${sortBy}` +
      `&direction=${direction}`
  );
}

export function getFoodById(foodId) {
  return apiRequest(`/foods/${foodId}`);
}

export function searchFood(
  keyword,
  page = 0,
  size = 20
) {
  return apiRequest(
    `/foods/search?keyword=${encodeURIComponent(keyword)}` +
      `&page=${page}` +
      `&size=${size}`
  );
}