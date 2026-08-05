import { request, shopPath } from './client'

export const vendorApi = {
  register: (body) =>
    request('/register', {
      method: 'POST',
      body: JSON.stringify(body),
    }),

  login: (body) =>
    request('/login', { method: 'POST', body: JSON.stringify(body) }),

  listShops: (vendorId) =>
    request(`/${vendorId}/shops`).then((r) => r.shops),

  createShop: (vendorId, body) =>
    request(`/${vendorId}/shops`, { method: 'POST', body: JSON.stringify(body) }),

  getDashboard: (vendorId, shopId) =>
    request(shopPath(vendorId, shopId, 'dashboard')),

  getOrderOverview: (vendorId, shopId) =>
    request(shopPath(vendorId, shopId, 'dashboard/order-overview')).then((r) => r.points),

  getActiveOrderCount: (vendorId, shopId) =>
    request(shopPath(vendorId, shopId, 'orders/active-count')).then((r) => r.count),

  listOrders: (vendorId, shopId, status) =>
    request(shopPath(vendorId, shopId, `orders${status ? `?status=${status}` : ''}`)).then(
      (r) => r.orders,
    ),

  updateOrderStatus: (vendorId, shopId, orderId, status) =>
    request(shopPath(vendorId, shopId, `orders/${orderId}/status`), {
      method: 'PATCH',
      body: JSON.stringify({ status }),
    }),

  listMenuItems: (vendorId, shopId, activeOnly = false) =>
    request(shopPath(vendorId, shopId, `menu?activeOnly=${activeOnly}`)).then((r) => r.items),

  listMenuCategories: (vendorId, shopId) =>
    request(shopPath(vendorId, shopId, 'menu/categories')).then((r) => r.categories),

  createMenuItem: (vendorId, shopId, body) =>
    request(shopPath(vendorId, shopId, 'menu'), {
      method: 'POST',
      body: JSON.stringify(body),
    }),

  updateMenuItem: (vendorId, shopId, itemId, body) =>
    request(shopPath(vendorId, shopId, `menu/${itemId}`), {
      method: 'PUT',
      body: JSON.stringify(body),
    }),

  toggleMenuItem: (vendorId, shopId, itemId) =>
    request(shopPath(vendorId, shopId, `menu/${itemId}/toggle`), { method: 'PATCH' }),

  deleteMenuItem: (vendorId, shopId, itemId) =>
    request(shopPath(vendorId, shopId, `menu/${itemId}`), {
      method: 'DELETE',
    }),

  getStoreDetails: (vendorId, shopId) =>
    request(shopPath(vendorId, shopId, 'settings')),

  updateStore: (vendorId, shopId, body) =>
    request(shopPath(vendorId, shopId, 'settings'), {
      method: 'PUT',
      body: JSON.stringify({
        shopName: body.shopName,
        isOpen: body.isOpen,
        openTime: body.openTime,
        closeTime: body.closeTime,
        onlineOrdersEnabled: body.onlineOrdersEnabled,
        estimatedPrepTimeMinutes: body.estimatedPrepTimeMinutes,
      }),
    }),

  bookInspection: (vendorId, shopId, inspectionDate) =>
    request(shopPath(vendorId, shopId, 'inspections/book'), {
      method: 'POST',
      body: JSON.stringify({ inspectionDate }),
    }),

  saveLocation: (vendorId, shopId, body) =>
    request(shopPath(vendorId, shopId, 'location'), {
      method: 'POST',
      body: JSON.stringify(body),
    }),

  getPayoutSummary: (vendorId, shopId) =>
    request(shopPath(vendorId, shopId, 'payouts/summary')),

  listPayouts: (vendorId, shopId) =>
    request(shopPath(vendorId, shopId, 'payouts')).then((r) => r.payouts),

  listReviews: (vendorId, shopId) =>
    request(shopPath(vendorId, shopId, 'reviews')).then((r) => r.reviews),

  getReviewStats: (vendorId, shopId) =>
    request(shopPath(vendorId, shopId, 'reviews/stats')),

  getProfile: (vendorId) =>
    request(`/${vendorId}/settings/profile`),

  updateProfile: (vendorId, body) =>
    request(`/${vendorId}/settings/profile`, {
      method: 'PUT',
      body: JSON.stringify(body),
    }),

  changePassword: (vendorId, currentPassword, newPassword) =>
    request(`/${vendorId}/settings/change-password`, {
      method: 'POST',
      body: JSON.stringify({ currentPassword, newPassword }),
    }),

  changePhone: (vendorId, newPhoneNo, password) =>
    request(`/${vendorId}/settings/change-phone`, {
      method: 'POST',
      body: JSON.stringify({ newPhoneNo, password }),
    }),

  getDashboardSummary: (vendorId, shopId) =>
    request(shopPath(vendorId, shopId, 'dashboard/summary')),
}
