import { apiRequest } from "./api";

export function placeOrder(userId, orderData) {
  return apiRequest(`/orders/user/${userId}`, {
    method: "POST",
    body: JSON.stringify(orderData),
  });
}

export function getMyOrders(userId) {
  return apiRequest(`/orders/user/${userId}`);
}

export function getOrderById(userId, orderId) {
  return apiRequest(
    `/orders/user/${userId}/${orderId}`
  );
}

export function cancelOrder(userId, orderId) {
  return apiRequest(
    `/orders/user/${userId}/${orderId}/cancel`,
    {
      method: "PUT",
    }
  );
}