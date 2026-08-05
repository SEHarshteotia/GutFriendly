import {
  ArrowLeft,
  Minus,
  Plus,
  ShoppingBag,
  Trash2,
} from "lucide-react";

import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";

import LoadingSpinner from "../components/LoadingSpinner";

import {
  clearCart,
  getCart,
  removeCartItem,
  updateCartQuantity,
} from "../services/cartService";

function CartPage() {
  const navigate = useNavigate();

  const userId = localStorage.getItem("userId");

  const [cart, setCart] = useState({
    cartId: 0,
    totalItems: 0,
    totalAmount: 0,
    items: [],
  });

  const [loading, setLoading] = useState(true);
  const [changingItemId, setChangingItemId] =
    useState(null);

  const [error, setError] = useState("");
  const [message, setMessage] = useState("");

  useEffect(() => {
    async function loadCart() {
      if (!userId) {
        setError("Please sign in to view your cart.");
        setLoading(false);
        return;
      }

      try {
        setLoading(true);
        setError("");

        const cartData = await getCart(userId);

        setCart(cartData);
      } catch (requestError) {
        setError(requestError.message);
      } finally {
        setLoading(false);
      }
    }

    loadCart();
  }, [userId]);

  async function handleIncrease(item) {
    try {
      setChangingItemId(item.cartItemId);
      setError("");

      const updatedCart =
        await updateCartQuantity(
          userId,
          item.cartItemId,
          item.quantity + 1
        );

      setCart(updatedCart);
    } catch (requestError) {
      setError(requestError.message);
    } finally {
      setChangingItemId(null);
    }
  }

  async function handleDecrease(item) {
    try {
      setChangingItemId(item.cartItemId);
      setError("");

      let updatedCart;

      if (item.quantity === 1) {
        updatedCart = await removeCartItem(
          userId,
          item.cartItemId
        );
      } else {
        updatedCart =
          await updateCartQuantity(
            userId,
            item.cartItemId,
            item.quantity - 1
          );
      }

      setCart(updatedCart);
    } catch (requestError) {
      setError(requestError.message);
    } finally {
      setChangingItemId(null);
    }
  }

  async function handleRemove(item) {
    try {
      setChangingItemId(item.cartItemId);
      setError("");

      const updatedCart = await removeCartItem(
        userId,
        item.cartItemId
      );

      setCart(updatedCart);
    } catch (requestError) {
      setError(requestError.message);
    } finally {
      setChangingItemId(null);
    }
  }

  async function handleClearCart() {
    const confirmed = window.confirm(
      "Are you sure you want to clear your cart?"
    );

    if (!confirmed) {
      return;
    }

    try {
      setError("");

      const updatedCart = await clearCart(userId);

      setCart(updatedCart);
      setMessage("Cart cleared successfully.");

      setTimeout(() => {
        setMessage("");
      }, 2200);
    } catch (requestError) {
      setError(requestError.message);
    }
  }

  if (loading) {
    return <LoadingSpinner />;
  }

  if (error && cart.items.length === 0) {
    return (
      <div className="page-message">
        <ShoppingBag size={44} />

        <h2>Unable to load cart</h2>
        <p>{error}</p>

        <Link to="/home" className="primary-button">
          Browse shops
        </Link>
      </div>
    );
  }

  if (!cart.items || cart.items.length === 0) {
    return (
      <div className="empty-cart-page">
        <div className="empty-cart-icon">
          <ShoppingBag size={42} />
        </div>

        <h1>Your cart is empty</h1>

        <p>
          Add some delicious and trusted food items to
          continue.
        </p>

        <Link to="/home" className="primary-button">
          Explore shops
        </Link>
      </div>
    );
  }

  return (
    <div className="cart-page section-container">
      <div className="cart-page-header">
        <div>
          <Link to="/home" className="cart-back-link">
            <ArrowLeft size={18} />
            Continue shopping
          </Link>

          <h1>Your cart</h1>

          <p>
            Review your selected items before placing the
            order.
          </p>
        </div>

        <button
          type="button"
          className="clear-cart-button"
          onClick={handleClearCart}
        >
          <Trash2 size={17} />
          Clear cart
        </button>
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

      <div className="cart-layout">
        <section className="cart-items-card">
          {cart.items.map((item) => (
            <article
              className="cart-item"
              key={item.cartItemId}
            >
              <img
                src={
                  item.imageUrl ||
                  "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?auto=format&fit=crop&w=500&q=80"
                }
                alt={item.foodName}
              />

              <div className="cart-item-details">
                <h3>{item.foodName}</h3>

                <span>
                  ₹
                  {Number(
                    item.unitPrice || 0
                  ).toFixed(2)}{" "}
                  each
                </span>

                <button
                  type="button"
                  className="remove-item-link"
                  onClick={() =>
                    handleRemove(item)
                  }
                  disabled={
                    changingItemId ===
                    item.cartItemId
                  }
                >
                  Remove
                </button>
              </div>

              <div className="cart-item-actions">
                <div className="quantity-control active-quantity-control">
                  <button
                    type="button"
                    onClick={() =>
                      handleDecrease(item)
                    }
                    disabled={
                      changingItemId ===
                      item.cartItemId
                    }
                  >
                    <Minus size={16} />
                  </button>

                  <span>{item.quantity}</span>

                  <button
                    type="button"
                    onClick={() =>
                      handleIncrease(item)
                    }
                    disabled={
                      changingItemId ===
                      item.cartItemId
                    }
                  >
                    <Plus size={16} />
                  </button>
                </div>

                <strong>
                  ₹
                  {Number(
                    item.itemTotal || 0
                  ).toFixed(2)}
                </strong>
              </div>
            </article>
          ))}
        </section>

        <aside className="cart-summary-card">
          <p className="home-eyebrow">
            Order summary
          </p>

          <h2>Cart total</h2>

          <div className="summary-row">
            <span>Total items</span>
            <strong>{cart.totalItems}</strong>
          </div>

          <div className="summary-row">
            <span>Subtotal</span>

            <strong>
              ₹
              {Number(
                cart.totalAmount || 0
              ).toFixed(2)}
            </strong>
          </div>

          <div className="summary-row muted-row">
            <span>Delivery charges</span>
            <strong>Calculated later</strong>
          </div>

          <div className="summary-total-row">
            <span>Total</span>

            <strong>
              ₹
              {Number(
                cart.totalAmount || 0
              ).toFixed(2)}
            </strong>
          </div>

          <button
            type="button"
            className="checkout-button"
            onClick={() => navigate("/checkout")}
          >
            Proceed to checkout
          </button>

          <p className="cart-note">
            Payment gateway is not connected yet. You can
            place a Cash on Delivery order.
          </p>
        </aside>
      </div>
    </div>
  );
}

export default CartPage;