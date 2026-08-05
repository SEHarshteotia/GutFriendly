import {
  ArrowLeft,
  CreditCard,
  MapPin,
  ShoppingBag,
} from "lucide-react";

import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";

import LoadingSpinner from "../components/LoadingSpinner";
import { getCart } from "../services/cartService";
import { placeOrder } from "../services/orderService";

function CheckoutPage() {
  const navigate = useNavigate();
  const userId = localStorage.getItem("userId");

  const [cart, setCart] = useState({
    totalItems: 0,
    totalAmount: 0,
    items: [],
  });

  const [formData, setFormData] = useState({
    deliveryAddress: "",
    paymentMethod: "COD",
  });

  const [loading, setLoading] = useState(true);
  const [placingOrder, setPlacingOrder] =
    useState(false);

  const [error, setError] = useState("");

  useEffect(() => {
    async function loadCheckout() {
      if (!userId) {
        setError("Please sign in first.");
        setLoading(false);
        return;
      }

      try {
        const cartData = await getCart(userId);
        setCart(cartData);
      } catch (requestError) {
        setError(requestError.message);
      } finally {
        setLoading(false);
      }
    }

    loadCheckout();
  }, [userId]);

  function handleChange(event) {
    const { name, value } = event.target;

    setFormData((previousData) => ({
      ...previousData,
      [name]: value,
    }));
  }

  async function handleSubmit(event) {
    event.preventDefault();

    if (!formData.deliveryAddress.trim()) {
      setError("Delivery address is required.");
      return;
    }

    try {
      setPlacingOrder(true);
      setError("");

      const order = await placeOrder(
        userId,
        {
          deliveryAddress:
            formData.deliveryAddress.trim(),
          paymentMethod:
            formData.paymentMethod,
        }
      );

      navigate("/orders", {
        state: {
          message: `Order #${order.orderId} placed successfully.`,
        },
      });
    } catch (requestError) {
      setError(requestError.message);
    } finally {
      setPlacingOrder(false);
    }
  }

  if (loading) {
    return <LoadingSpinner />;
  }

  if (!cart.items || cart.items.length === 0) {
    return (
      <div className="page-message">
        <ShoppingBag size={44} />
        <h2>Your cart is empty</h2>
        <p>Add food before continuing.</p>

        <Link
          to="/home"
          className="primary-button"
        >
          Browse shops
        </Link>
      </div>
    );
  }

  return (
    <div className="checkout-page section-container">
      <div className="checkout-header">
        <Link
          to="/cart"
          className="cart-back-link"
        >
          <ArrowLeft size={18} />
          Back to cart
        </Link>

        <h1>Checkout</h1>

        <p>
          Confirm your delivery details and place the
          order.
        </p>
      </div>

      {error && (
        <div className="error-message">
          {error}
        </div>
      )}

      <div className="checkout-layout">
        <form
          className="checkout-form-card"
          onSubmit={handleSubmit}
        >
          <div className="checkout-section-title">
            <MapPin size={22} />
            <div>
              <h2>Delivery address</h2>
              <p>
                Enter the complete address for this order.
              </p>
            </div>
          </div>

          <div className="form-group">
            <label htmlFor="deliveryAddress">
              Complete address
            </label>

            <textarea
              id="deliveryAddress"
              name="deliveryAddress"
              value={formData.deliveryAddress}
              onChange={handleChange}
              placeholder="House number, street, locality, city and pincode"
              rows="5"
              required
            />
          </div>

          <div className="checkout-section-title payment-title">
            <CreditCard size={22} />

            <div>
              <h2>Payment method</h2>
              <p>
                Online payment is not connected yet.
              </p>
            </div>
          </div>

          <label className="payment-option">
            <input
              type="radio"
              name="paymentMethod"
              value="COD"
              checked={
                formData.paymentMethod === "COD"
              }
              onChange={handleChange}
            />

            <div>
              <strong>Cash on Delivery</strong>
              <span>
                Pay when your order arrives.
              </span>
            </div>
          </label>

          <button
            type="submit"
            className="place-order-button"
            disabled={placingOrder}
          >
            {placingOrder
              ? "Placing order..."
              : "Place order"}
          </button>
        </form>

        <aside className="checkout-summary-card">
          <p className="home-eyebrow">
            Final summary
          </p>

          <h2>Your order</h2>

          <div className="checkout-items">
            {cart.items.map((item) => (
              <div
                className="checkout-item"
                key={item.cartItemId}
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

          <div className="summary-row">
            <span>Total items</span>
            <strong>{cart.totalItems}</strong>
          </div>

          <div className="summary-total-row">
            <span>Payable amount</span>

            <strong>
              ₹
              {Number(
                cart.totalAmount || 0
              ).toFixed(2)}
            </strong>
          </div>
        </aside>
      </div>
    </div>
  );
}

export default CheckoutPage;