import { apiRequest } from "./api";

export function getCart(userId) {
  return apiRequest(`/cart/user/${userId}`);
}

export function addToCart(userId, foodId, quantity) {
  return apiRequest(`/cart/user/${userId}/items`, {
    method: "POST",
    body: JSON.stringify({
      foodId,
      quantity,
    }),
  });
}

export function updateCartQuantity(
  userId,
  cartItemId,
  quantity
) {
  return apiRequest(
    `/cart/user/${userId}/items/${cartItemId}`,
    {
      method: "PUT",
      body: JSON.stringify({
        quantity,
      }),
    }
  );
}

export function removeCartItem(
  userId,
  cartItemId
) {
  return apiRequest(
    `/cart/user/${userId}/items/${cartItemId}`,
    {
      method: "DELETE",
    }
  );
}

export function clearCart(userId) {
  return apiRequest(
    `/cart/user/${userId}/clear`,
    {
      method: "DELETE",
    }
  );
}