import {
  ArrowLeft,
  Heart,
  MapPin,
  Search,
  ShieldCheck,
} from "lucide-react";

import {
  useEffect,
  useMemo,
  useState,
} from "react";

import {
  Link,
  useParams,
} from "react-router-dom";

import FoodCard from "../components/FoodCard";
import GutTrustBadge from "../components/GutTrustBadge";
import LoadingSpinner from "../components/LoadingSpinner";
import ShopReviewSummary from "../components/ShopReviewSummary";
import ShopReviews from "../components/ShopReviews";

import {
  addToCart,
  getCart,
  removeCartItem,
  updateCartQuantity,
} from "../services/cartService";

import { getMenuByShop } from "../services/foodService";
import { getShopById } from "../services/shopService";

import {
  addToWishlist,
  checkWishlistStatus,
  removeFromWishlist,
} from "../services/wishlistService";

function ShopDetailsPage() {
  const { shopId } = useParams();

  const userId =
    localStorage.getItem("userId");

  const [shop, setShop] = useState(null);
  const [foods, setFoods] = useState([]);

  const [searchText, setSearchText] =
    useState("");

  const [sortBy, setSortBy] =
    useState("foodName");

  const [direction, setDirection] =
    useState("asc");

  const [loading, setLoading] =
    useState(true);

  const [pageError, setPageError] =
    useState("");

  const [actionError, setActionError] =
    useState("");

  const [message, setMessage] =
    useState("");

  const [cart, setCart] = useState({
    cartId: 0,
    totalItems: 0,
    totalAmount: 0,
    items: [],
  });

  const [changingFoodId, setChangingFoodId] =
    useState(null);

  const [isWishlisted, setIsWishlisted] =
    useState(false);

  const [wishlistLoading, setWishlistLoading] =
    useState(false);

  useEffect(() => {
    async function loadShopData() {
      try {
        setLoading(true);
        setPageError("");

        const requests = [
          getShopById(shopId),

          getMenuByShop(
            shopId,
            0,
            50,
            sortBy,
            direction
          ),
        ];

        if (userId) {
          requests.push(
            getCart(userId)
          );

          requests.push(
            checkWishlistStatus(
              userId,
              shopId
            )
          );
        }

        const results =
          await Promise.all(requests);

        const shopData = results[0];
        const menuData = results[1];
        const cartData = results[2];
        const wishlistStatus = results[3];

        setShop(shopData);

        setFoods(
          menuData?.content || []
        );

        if (cartData) {
          setCart(cartData);
        }

        if (
          typeof wishlistStatus ===
          "boolean"
        ) {
          setIsWishlisted(
            wishlistStatus
          );
        }
      } catch (requestError) {
        setPageError(
          requestError.message
        );
      } finally {
        setLoading(false);
      }
    }

    loadShopData();
  }, [
    shopId,
    sortBy,
    direction,
    userId,
  ]);

  const filteredFoods =
    useMemo(() => {
      const keyword =
        searchText
          .trim()
          .toLowerCase();

      if (!keyword) {
        return foods;
      }

      return foods.filter((food) =>
        food.foodName
          ?.toLowerCase()
          .includes(keyword)
      );
    }, [foods, searchText]);

  function getCartItem(foodId) {
    return cart.items?.find(
      (item) =>
        item.foodId === foodId
    );
  }

  function getFoodQuantity(foodId) {
    return (
      getCartItem(foodId)
        ?.quantity || 0
    );
  }

  function showSuccessMessage(text) {
    setMessage(text);

    setTimeout(() => {
      setMessage("");
    }, 2500);
  }

  async function handleWishlistToggle() {
    if (!userId) {
      setActionError(
        "Please sign in before saving this shop."
      );

      return;
    }

    try {
      setWishlistLoading(true);
      setActionError("");
      setMessage("");

      if (isWishlisted) {
        await removeFromWishlist(
          userId,
          shopId
        );

        setIsWishlisted(false);

        showSuccessMessage(
          `${shop?.shopName} removed from wishlist.`
        );
      } else {
        await addToWishlist(
          userId,
          shopId
        );

        setIsWishlisted(true);

        showSuccessMessage(
          `${shop?.shopName} added to wishlist.`
        );
      }
    } catch (requestError) {
      setActionError(
        requestError.message
      );
    } finally {
      setWishlistLoading(false);
    }
  }

  async function handleAdd(food) {
    if (!userId) {
      setActionError(
        "Please sign in before adding food."
      );

      return;
    }

    try {
      setChangingFoodId(
        food.foodId
      );

      setMessage("");
      setActionError("");

      const updatedCart =
        await addToCart(
          userId,
          food.foodId,
          1
        );

      setCart(updatedCart);

      showSuccessMessage(
        `${food.foodName} added to cart.`
      );
    } catch (requestError) {
      setActionError(
        requestError.message
      );
    } finally {
      setChangingFoodId(null);
    }
  }

  async function handleIncrease(food) {
    const cartItem =
      getCartItem(food.foodId);

    if (!cartItem) {
      await handleAdd(food);
      return;
    }

    try {
      setChangingFoodId(
        food.foodId
      );

      setMessage("");
      setActionError("");

      const updatedCart =
        await updateCartQuantity(
          userId,
          cartItem.cartItemId,
          cartItem.quantity + 1
        );

      setCart(updatedCart);
    } catch (requestError) {
      setActionError(
        requestError.message
      );
    } finally {
      setChangingFoodId(null);
    }
  }

  async function handleDecrease(food) {
    const cartItem =
      getCartItem(food.foodId);

    if (!cartItem) {
      return;
    }

    try {
      setChangingFoodId(
        food.foodId
      );

      setMessage("");
      setActionError("");

      let updatedCart;

      if (cartItem.quantity === 1) {
        updatedCart =
          await removeCartItem(
            userId,
            cartItem.cartItemId
          );
      } else {
        updatedCart =
          await updateCartQuantity(
            userId,
            cartItem.cartItemId,
            cartItem.quantity - 1
          );
      }

      setCart(updatedCart);
    } catch (requestError) {
      setActionError(
        requestError.message
      );
    } finally {
      setChangingFoodId(null);
    }
  }

  if (loading) {
    return <LoadingSpinner />;
  }

  if (pageError) {
    return (
      <div className="page-message">
        <h2>Unable to load shop</h2>

        <p>{pageError}</p>

        <Link
          to="/home"
          className="primary-button"
        >
          Return home
        </Link>
      </div>
    );
  }

  const fallbackBanner =
    "https://images.unsplash.com/photo-1515003197210-e0cd71810b5f?auto=format&fit=crop&w=1400&q=80";

  const bannerImage =
    shop?.imageUrls?.length > 0
      ? shop.imageUrls[0]
      : fallbackBanner;

  return (
    <div className="shop-details-page">
      <section className="shop-banner">
        <img
          src={bannerImage}
          alt={
            shop?.shopName ||
            "Shop banner"
          }
        />

        <div className="shop-banner-overlay" />

        <div className="shop-banner-content section-container">
          <Link
            to="/home"
            className="back-link-light"
          >
            <ArrowLeft size={18} />
            Back to shops
          </Link>

          <div className="shop-banner-main">
            <div>
              <p className="shop-category-label">
                {shop?.category
                  ?.replaceAll(
                    "_",
                    " "
                  )}
              </p>

              <h1>
                {shop?.shopName}
              </h1>

              <div className="shop-meta-row">
                <span>
                  <MapPin size={17} />

                  {shop?.locality ||
                    "Location unavailable"}
                </span>

                {shop?.pincode && (
                  <span>
                    {shop.pincode}
                  </span>
                )}
              </div>
            </div>

            <button
              type="button"
              className={
                isWishlisted
                  ? "save-shop-button active"
                  : "save-shop-button"
              }
              onClick={
                handleWishlistToggle
              }
              disabled={
                wishlistLoading
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

              {wishlistLoading
                ? "Updating..."
                : isWishlisted
                  ? "Saved"
                  : "Save shop"}
            </button>
          </div>
        </div>
      </section>

      <section className="shop-score-section section-container">
        <div className="score-summary-card">
          <ShieldCheck size={26} />

          <div>
            <span>
              Final GutTrust Score
            </span>

            <strong>
              {Number(
                shop?.finalGutTrustScore ||
                  0
              ).toFixed(2)}
            </strong>
          </div>
        </div>

        <div className="score-breakdown-card">
          <span>
            Customer rating
          </span>

          <strong>
            {Number(
              shop?.userTrustScore ||
                0
            ).toFixed(2)}
          </strong>
        </div>

        <div className="score-breakdown-card">
          <span>
            Inspection score
          </span>

          <strong>
            {Number(
              shop?.inspectionTrustScore ||
                0
            ).toFixed(2)}
          </strong>
        </div>
      </section>

      <section className="menu-section section-container">
        <div className="menu-header">
          <div>
            <p className="home-eyebrow">
              Fresh choices
            </p>

            <h2>
              Explore the menu
            </h2>

            <span>
              {filteredFoods.length}{" "}
              available items
            </span>
          </div>

          <GutTrustBadge
            score={
              shop?.finalGutTrustScore
            }
          />
        </div>

        <div className="menu-tools">
          <div className="menu-search">
            <Search size={19} />

            <input
              type="text"
              value={searchText}
              onChange={(event) =>
                setSearchText(
                  event.target.value
                )
              }
              placeholder="Search this menu..."
            />
          </div>

          <select
            value={`${sortBy}-${direction}`}
            onChange={(event) => {
              const [
                newSortBy,
                newDirection,
              ] =
                event.target.value.split(
                  "-"
                );

              setSortBy(newSortBy);

              setDirection(
                newDirection
              );
            }}
          >
            <option value="foodName-asc">
              Name: A to Z
            </option>

            <option value="foodName-desc">
              Name: Z to A
            </option>

            <option value="price-asc">
              Price: Low to high
            </option>

            <option value="price-desc">
              Price: High to low
            </option>
          </select>
        </div>

        {message && (
          <div className="success-message menu-message">
            {message}
          </div>
        )}

        {actionError && (
          <div className="error-message menu-message">
            {actionError}
          </div>
        )}

        {cart.totalItems > 0 && (
          <div className="shop-cart-summary">
            <div>
              <span>
                {cart.totalItems}{" "}
                item
                {cart.totalItems !== 1
                  ? "s"
                  : ""}
              </span>

              <strong>
                ₹
                {Number(
                  cart.totalAmount ||
                    0
                ).toFixed(2)}
              </strong>
            </div>

            <Link
              to="/cart"
              className="view-cart-link"
            >
              View cart
            </Link>
          </div>
        )}

        {filteredFoods.length > 0 ? (
          <div className="food-grid">
            {filteredFoods.map(
              (food) => (
                <FoodCard
                  key={food.foodId}
                  food={food}
                  quantity={getFoodQuantity(
                    food.foodId
                  )}
                  loading={
                    changingFoodId ===
                    food.foodId
                  }
                  onAdd={handleAdd}
                  onIncrease={
                    handleIncrease
                  }
                  onDecrease={
                    handleDecrease
                  }
                />
              )
            )}
          </div>
        ) : (
          <div className="page-message small-message">
            <h3>
              No matching food found
            </h3>

            <p>
              Try another search term.
            </p>
          </div>
        )}
      </section>

      <ShopReviewSummary shopId={shopId} />

      <ShopReviews shopId={shopId} />
    </div>
  );
}

export default ShopDetailsPage;