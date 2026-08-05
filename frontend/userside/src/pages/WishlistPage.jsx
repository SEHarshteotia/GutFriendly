import {
  Heart,
  MapPin,
  ShieldCheck,
  Trash2,
} from "lucide-react";

import { useEffect, useState } from "react";
import { Link } from "react-router-dom";

import LoadingSpinner from "../components/LoadingSpinner";

import {
  getWishlist,
  removeFromWishlist,
} from "../services/wishlistService";

import { getShopTrustScore } from "../utils/shopMapper";

function WishlistPage() {
  const userId = localStorage.getItem("userId");

  const [wishlist, setWishlist] = useState([]);
  const [loading, setLoading] = useState(true);
  const [removingShopId, setRemovingShopId] =
    useState(null);

  const [error, setError] = useState("");
  const [message, setMessage] = useState("");

  useEffect(() => {
    async function loadWishlist() {
      if (!userId) {
        setError("Please sign in to view your wishlist.");
        setLoading(false);
        return;
      }

      try {
        setLoading(true);
        setError("");

        const data = await getWishlist(userId);

        setWishlist(data || []);
      } catch (requestError) {
        setError(requestError.message);
      } finally {
        setLoading(false);
      }
    }

    loadWishlist();
  }, [userId]);

  async function handleRemove(shopId) {
    try {
      setRemovingShopId(shopId);
      setError("");
      setMessage("");

      await removeFromWishlist(userId, shopId);

      setWishlist((currentWishlist) =>
        currentWishlist.filter(
          (item) => item.shopId !== shopId
        )
      );

      setMessage(
        "Shop removed from wishlist successfully."
      );

      setTimeout(() => {
        setMessage("");
      }, 2500);
    } catch (requestError) {
      setError(requestError.message);
    } finally {
      setRemovingShopId(null);
    }
  }

  if (loading) {
    return <LoadingSpinner />;
  }

  if (error && wishlist.length === 0) {
    return (
      <div className="page-message">
        <Heart size={46} />
        <h2>Unable to load wishlist</h2>
        <p>{error}</p>

        <Link to="/home" className="primary-button">
          Return home
        </Link>
      </div>
    );
  }

  return (
    <div className="wishlist-page section-container">
      <div className="wishlist-header">
        <div>
          <p className="home-eyebrow">
            Saved for later
          </p>

          <h1>My wishlist</h1>

          <p>
            Keep track of the trusted shops you want to
            explore again.
          </p>
        </div>

        <div className="wishlist-count">
          <Heart size={20} />
          <span>
            {wishlist.length} saved{" "}
            {wishlist.length === 1 ? "shop" : "shops"}
          </span>
        </div>
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

      {wishlist.length === 0 ? (
        <div className="empty-wishlist-card">
          <div className="empty-wishlist-icon">
            <Heart size={38} />
          </div>

          <h2>Your wishlist is empty</h2>

          <p>
            Save restaurants and food vendors by clicking
            the heart icon.
          </p>

          <Link to="/home" className="primary-button">
            Explore trusted shops
          </Link>
        </div>
      ) : (
        <div className="wishlist-grid">
          {wishlist.map((item) => (
            <article
              className="wishlist-card"
              key={item.wishlistId}
            >
              <div className="wishlist-image-wrapper">
                {item.imageUrl ? (
                  <img
                    src={item.imageUrl}
                    alt={item.shopName}
                    className="wishlist-image"
                  />
                ) : (
                  <div className="wishlist-image-placeholder">
                    <Heart size={42} />
                  </div>
                )}

                <div className="wishlist-score-badge">
                  <ShieldCheck size={15} />

                  <span>GutTrust</span>

                  <strong>
                    {Number(
                      getShopTrustScore(item)
                    ).toFixed(1)}
                  </strong>
                </div>

                <button
                  type="button"
                  className="wishlist-remove-icon"
                  onClick={() =>
                    handleRemove(item.shopId)
                  }
                  disabled={
                    removingShopId === item.shopId
                  }
                  aria-label={`Remove ${item.shopName} from wishlist`}
                >
                  <Trash2 size={19} />
                </button>
              </div>

              <div className="wishlist-card-content">
                <span className="wishlist-category">
                  {item.category?.replaceAll("_", " ")}
                </span>

                <h2>{item.shopName}</h2>

                <div className="wishlist-location">
                  <MapPin size={16} />

                  <span>
                    {item.locality ||
                      "Location unavailable"}
                  </span>
                </div>

                <div className="wishlist-card-actions">
                  <Link
                    to={`/shops/${item.shopId}`}
                    className="wishlist-view-button"
                  >
                    View shop
                  </Link>

                  <button
                    type="button"
                    className="wishlist-remove-button"
                    onClick={() =>
                      handleRemove(item.shopId)
                    }
                    disabled={
                      removingShopId === item.shopId
                    }
                  >
                    <Trash2 size={17} />

                    {removingShopId === item.shopId
                      ? "Removing..."
                      : "Remove"}
                  </button>
                </div>
              </div>
            </article>
          ))}
        </div>
      )}
    </div>
  );
}

export default WishlistPage;