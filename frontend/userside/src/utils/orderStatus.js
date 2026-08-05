/**
 * User order API returns OrderStatus (PLACED, ACCEPTED, …).
 * Canonical DB status is ORDER_PLACED — map both for display/cancel checks.
 */

const ORDER_FLOW = [
  "PLACED",
  "ACCEPTED",
  "PREPARING",
  "OUT_FOR_DELIVERY",
  "DELIVERED",
];

export function normalizeOrderStatus(status) {
  if (!status) {
    return "";
  }

  if (status === "ORDER_PLACED") {
    return "PLACED";
  }

  return status;
}

export function formatOrderStatusLabel(status) {
  const normalized = normalizeOrderStatus(status);

  if (normalized === "PLACED") {
    return "Order placed";
  }

  return normalized.replaceAll("_", " ").toLowerCase();
}

export function getOrderStatusCssClass(status) {
  return `status-${normalizeOrderStatus(status).toLowerCase()}`;
}

export function canCancelOrder(status) {
  return normalizeOrderStatus(status) === "PLACED";
}

export function isDeliveredOrder(status) {
  return normalizeOrderStatus(status) === "DELIVERED";
}

export function getOrderFlowSteps() {
  return ORDER_FLOW;
}
