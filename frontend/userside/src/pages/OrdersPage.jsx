import {
  CalendarDays,
  ChevronDown,
  ChevronUp,
  PackageCheck,
  ShoppingBag,
  XCircle,
} from "lucide-react";

import { useEffect, useState } from "react";
import { Link, useLocation } from "react-router-dom";

import LoadingSpinner from "../components/LoadingSpinner";

import {
  cancelOrder,
  getMyOrders,
} from "../services/orderService";

import {
  canCancelOrder,
  formatOrderStatusLabel,
  getOrderStatusCssClass,
  isDeliveredOrder,
} from "../utils/orderStatus";

function OrdersPage() {
  const location = useLocation();
  const userId = localStorage.getItem("userId");

  const [orders, setOrders] = useState([]);
  const [expandedOrderId, setExpandedOrderId] =
    useState(null);

  const [loading, setLoading] = useState(true);
  const [cancellingOrderId, setCancellingOrderId] =
    useState(null);

  const [error, setError] = useState("");
  const [message, setMessage] = useState(
    location.state?.message || ""
  );

  useEffect(() => {
    async function loadOrders() {
      if (!userId) {
        setError("Please sign in to view your orders.");
        setLoading(false);
        return;
      }

      try {
        setLoading(true);
        setError("");

        const data = await getMyOrders(userId);

        setOrders(data || []);
      } catch (requestError) {
        setError(requestError.message);
      } finally {
        setLoading(false);
      }
    }

    loadOrders();
  }, [userId]);

  async function handleCancel(orderId) {
    const confirmed = window.confirm(
      "Are you sure you want to cancel this order?"
    );

    if (!confirmed) {
      return;
    }

    try {
      setCancellingOrderId(orderId);
      setError("");

      const updatedOrder = await cancelOrder(
        userId,
        orderId
      );

      setOrders((currentOrders) =>
        currentOrders.map((order) =>
          order.orderId === orderId
            ? updatedOrder
            : order
        )
      );

      setMessage(
        `Order #${orderId} cancelled successfully.`
      );

      setTimeout(() => {
        setMessage("");
      }, 2500);
    } catch (requestError) {
      setError(requestError.message);
    } finally {
      setCancellingOrderId(null);
    }
  }

  function toggleOrder(orderId) {
    setExpandedOrderId((currentId) =>
      currentId === orderId ? null : orderId
    );
  }

  if (loading) {
    return <LoadingSpinner />;
  }

  if (error && orders.length === 0) {
    return (
      <div className="page-message">
        <ShoppingBag size={44} />
        <h2>Unable to load orders</h2>
        <p>{error}</p>

        <Link to="/home" className="primary-button">
          Return home
        </Link>
      </div>
    );
  }

  if (orders.length === 0) {
    return (
      <div className="page-message">
        <PackageCheck size={48} />
        <h2>No orders yet</h2>
        <p>Your placed orders will appear here.</p>

        <Link to="/home" className="primary-button">
          Explore shops
        </Link>
      </div>
    );
  }

  return (
    <div className="orders-page section-container">
      <div className="orders-header">
        <p className="home-eyebrow">
          Order history
        </p>

        <h1>My orders</h1>

        <p>
          Track your orders and review delivered food.
        </p>
      </div>

      {message && (
        <div className="success-message">
          {message}
        </div>
      )}

      {error && (
        <div className="error-message">
          {error}
        </div>
      )}

      <div className="orders-list">
        {orders.map((order) => {
          const isExpanded =
            expandedOrderId === order.orderId;

          const canCancel =
            canCancelOrder(order.orderStatus);

          const isDelivered =
            isDeliveredOrder(order.orderStatus);

          const hasReview =
            order.reviewSubmitted === true &&
            order.reviewId !== null &&
            order.reviewId !== undefined;

          return (
            <article
              className="order-card"
              key={order.orderId}
            >
              <div className="order-card-top">
                <div>
                  <span className="order-number">
                    Order #{order.orderId}
                  </span>

                  <h2>{order.shopName}</h2>

                  <div className="order-date">
                    <CalendarDays size={16} />

                    <span>
                      {order.orderedAt
                        ? new Date(
                            order.orderedAt
                          ).toLocaleString()
                        : "Date unavailable"}
                    </span>
                  </div>
                </div>

                <div className="order-status-area">
                  <span
                    className={`order-status ${getOrderStatusCssClass(
                      order.orderStatus
                    )}`}
                  >
                    {formatOrderStatusLabel(
                      order.orderStatus
                    )}
                  </span>

                  <strong>
                    ₹
                    {Number(
                      order.totalAmount || 0
                    ).toFixed(2)}
                  </strong>
                </div>
              </div>

              <div className="order-summary-row">
                <span>
                  {order.items?.length || 0} food item
                  {(order.items?.length || 0) !== 1
                    ? "s"
                    : ""}
                </span>

                <span>
                  Payment: {order.paymentMethod}
                </span>

                <span>
                  {order.paymentStatus}
                </span>
              </div>

              <button
                type="button"
                className="order-expand-button"
                onClick={() =>
                  toggleOrder(order.orderId)
                }
              >
                {isExpanded ? (
                  <>
                    Hide details
                    <ChevronUp size={18} />
                  </>
                ) : (
                  <>
                    View details
                    <ChevronDown size={18} />
                  </>
                )}
              </button>

              {isExpanded && (
                <div className="order-details">
                  <div className="order-address">
                    <strong>Delivery address</strong>
                    <p>{order.deliveryAddress}</p>
                  </div>

                  <div className="order-items-list">
                    {order.items?.map((item) => (
                      <div
                        className="order-item-row"
                        key={item.orderItemId}
                      >
                        <div>
                          <strong>
                            {item.foodName}
                          </strong>

                          <span>
                            {item.quantity} × ₹
                            {Number(
                              item.unitPrice || 0
                            ).toFixed(2)}
                          </span>
                        </div>

                        <strong>
                          ₹
                          {Number(
                            item.itemTotal || 0
                          ).toFixed(2)}
                        </strong>
                      </div>
                    ))}
                  </div>

                  <div className="order-actions">
                    {canCancel && (
                      <button
                        type="button"
                        className="cancel-order-button"
                        onClick={() =>
                          handleCancel(order.orderId)
                        }
                        disabled={
                          cancellingOrderId ===
                          order.orderId
                        }
                      >
                        <XCircle size={18} />

                        {cancellingOrderId ===
                        order.orderId
                          ? "Cancelling..."
                          : "Cancel order"}
                      </button>
                    )}

                    {isDelivered && !hasReview && (
                      <Link
                        to={`/reviews/${order.orderId}`}
                        className="review-order-button"
                      >
                        Write a review
                      </Link>
                    )}

                    {isDelivered && hasReview && (
                      <Link
                        to={`/reviews/${order.orderId}?reviewId=${order.reviewId}`}
                        className="review-order-button edit-review-button"
                      >
                        Edit review
                      </Link>
                    )}
                  </div>
                </div>
              )}
            </article>
          );
        })}
      </div>
    </div>
  );
}

export default OrdersPage;