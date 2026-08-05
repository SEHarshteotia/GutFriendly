import {
  Heart,
  MapPin,
  ShieldCheck,
} from "lucide-react";

import { Link } from "react-router-dom";

import { getShopTrustScore } from "../utils/shopMapper";

const fallbackShopImage =
  "/images/default-shop.jpg";

function ShopCard({
  shop,
  isWishlisted = false,
  wishlistLoading = false,
  onWishlistToggle,
}) {
  const gutTrustScore = getShopTrustScore(shop);

  const imageUrl =
    shop?.imageUrl ||
    shop?.imageUrls?.[0] ||
    fallbackShopImage;

  function handleWishlistClick(event) {
    event.preventDefault();
    event.stopPropagation();

    if (wishlistLoading) {
      return;
    }

    onWishlistToggle?.(shop?.shopId);
  }

  function handleImageError(event) {
    event.currentTarget.onerror = null;
    event.currentTarget.src = fallbackShopImage;
  }

  return (
    <article className="shop-card">
      <div className="shop-image-wrapper">
        <img
          src={imageUrl}
          alt={`${shop?.shopName || "Shop"} cover`}
          className="shop-image"
          onError={handleImageError}
        />

        <button
          type="button"
          className={
            isWishlisted
              ? "shop-heart-button active"
              : "shop-heart-button"
          }
          onClick={handleWishlistClick}
          disabled={wishlistLoading}
          aria-label={
            isWishlisted
              ? `Remove ${shop?.shopName || "shop"} from wishlist`
              : `Add ${shop?.shopName || "shop"} to wishlist`
          }
        >
          <Heart
            size={20}
            fill={
              isWishlisted
                ? "currentColor"
                : "none"
            }
          />
        </button>

        <div className="guttrust-badge">
          <ShieldCheck size={14} />

          <span>GutTrust</span>

          <strong>
            {Number(gutTrustScore).toFixed(1)}
          </strong>
        </div>
      </div>

      <div className="shop-card-content">
        <div className="shop-card-heading">
          <div>
            <h3>
              {shop?.shopName || "Unnamed shop"}
            </h3>

            <p>
              {shop?.category
                ?.replaceAll("_", " ") ||
                "Category unavailable"}
            </p>
          </div>
        </div>

        <div className="shop-location">
          <MapPin size={16} />

          <span>
            {shop?.locality ||
              "Location unavailable"}
          </span>
        </div>

        <Link
          to={`/shops/${shop?.shopId}`}
          className="view-shop-button"
        >
          View shop
        </Link>
      </div>
    </article>
  );
}

export default ShopCard;