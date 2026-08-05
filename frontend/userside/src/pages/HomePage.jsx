import {
  ArrowRight,
  Gift,
  Search,
  ShieldCheck,
  Sparkles,
  X,
} from "lucide-react";

import { useEffect, useMemo, useState } from "react";

import LoadingSpinner from "../components/LoadingSpinner";
import ShopCard from "../components/ShopCard";

import { getHomePage } from "../services/homeService";

import {
  addToWishlist,
  getWishlist,
  removeFromWishlist,
} from "../services/wishlistService";

function HomePage() {
  const userId = localStorage.getItem("userId");

  const [homeData, setHomeData] = useState(null);

  const [wishlistedShopIds, setWishlistedShopIds] =
    useState([]);

  const [wishlistLoadingShopId, setWishlistLoadingShopId] =
    useState(null);

  const [searchText, setSearchText] = useState("");
  const [submittedSearch, setSubmittedSearch] =
    useState("");

  const [selectedCategory, setSelectedCategory] =
    useState("");

  const [error, setError] = useState("");
  const [message, setMessage] = useState("");
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function loadHomePage() {
      if (!userId) {
        setError("Please sign in to continue.");
        setLoading(false);
        return;
      }

      try {
        setLoading(true);
        setError("");

        const [homeResponse, wishlistResponse] =
          await Promise.all([
            getHomePage(userId),
            getWishlist(userId),
          ]);

        setHomeData(homeResponse);

        setWishlistedShopIds(
          (wishlistResponse || []).map(
            (wishlistItem) => wishlistItem.shopId
          )
        );
      } catch (requestError) {
        setError(requestError.message);
      } finally {
        setLoading(false);
      }
    }

    loadHomePage();
  }, [userId]);

  async function handleWishlistToggle(shopId) {
    if (!userId) {
      setError(
        "Please sign in before using the wishlist."
      );
      return;
    }

    const alreadyWishlisted =
      wishlistedShopIds.includes(shopId);

    try {
      setWishlistLoadingShopId(shopId);
      setError("");
      setMessage("");

      if (alreadyWishlisted) {
        await removeFromWishlist(userId, shopId);

        setWishlistedShopIds((currentIds) =>
          currentIds.filter(
            (currentShopId) =>
              currentShopId !== shopId
          )
        );

        setMessage("Shop removed from wishlist.");
      } else {
        await addToWishlist(userId, shopId);

        setWishlistedShopIds((currentIds) => [
          ...currentIds,
          shopId,
        ]);

        setMessage("Shop added to wishlist.");
      }

      setTimeout(() => {
        setMessage("");
      }, 2200);
    } catch (requestError) {
      setError(requestError.message);
    } finally {
      setWishlistLoadingShopId(null);
    }
  }

  function handleSearch(event) {
    event.preventDefault();

    setSubmittedSearch(searchText.trim());
  }

  function handleCategoryClick(category) {
    setSelectedCategory((currentCategory) =>
      currentCategory === category ? "" : category
    );
  }

  function clearFilters() {
    setSearchText("");
    setSubmittedSearch("");
    setSelectedCategory("");
  }

  const allShops = homeData?.allShops || [];

  const trustedVendors =
    homeData?.trustedVendors || [];

  const recommendedShops =
    homeData?.recommendedShops || [];

  const gutFriendlyPicks =
    homeData?.gutFriendlyPicks || [];

  const categories =
    homeData?.categories || [];

  const isFiltering =
    Boolean(submittedSearch) ||
    Boolean(selectedCategory);

  const filteredShops = useMemo(() => {
    const searchKeyword =
      submittedSearch.trim().toLowerCase();

    return allShops.filter((shop) => {
      const shopName =
        shop.shopName?.toLowerCase() || "";

      const category =
        shop.category?.toLowerCase() || "";

      const locality =
        shop.locality?.toLowerCase() || "";

      const matchesSearch =
        !searchKeyword ||
        shopName.includes(searchKeyword) ||
        category.includes(searchKeyword) ||
        locality.includes(searchKeyword);

      const matchesCategory =
        !selectedCategory ||
        shop.category === selectedCategory;

      return matchesSearch && matchesCategory;
    });
  }, [
    allShops,
    submittedSearch,
    selectedCategory,
  ]);

  if (loading) {
    return <LoadingSpinner />;
  }

  if (error && !homeData) {
    return (
      <div className="page-message">
        <h2>Unable to load homepage</h2>
        <p>{error}</p>
      </div>
    );
  }

  return (
    <div className="home-page">
      <section className="home-hero section-container">
        <div className="home-welcome">
          <div>
            <p className="home-eyebrow">
              Welcome back
            </p>

            <h1>
              Hi,{" "}
              {homeData?.userName || "Food lover"}!
            </h1>

            <p>
              Discover safer food choices from trusted
              shops around you.
            </p>
          </div>

          <div className="reward-summary-card">
            <div className="reward-icon">
              <Gift size={24} />
            </div>

            <div>
              <span>Your reward balance</span>

              <strong>
                {homeData?.rewardPoints || 0} points
              </strong>
            </div>
          </div>
        </div>

        <form
          className="home-search-bar"
          onSubmit={handleSearch}
        >
          <Search size={21} />

          <input
            type="text"
            value={searchText}
            onChange={(event) =>
              setSearchText(event.target.value)
            }
            placeholder="Search shops, categories or locations..."
          />

          {(searchText || submittedSearch) && (
            <button
              type="button"
              className="home-search-clear"
              onClick={() => {
                setSearchText("");
                setSubmittedSearch("");
              }}
              aria-label="Clear search"
            >
              <X size={16} />
              <span>Clear</span>
            </button>
          )}

          <button
            type="submit"
            className="home-search-submit"
          >
            Search
          </button>
        </form>

        {message && (
          <div className="success-message home-message">
            {message}
          </div>
        )}

        {error && (
          <div className="error-message home-message">
            {error}
          </div>
        )}
      </section>

      <section className="category-section section-container">
        <div className="home-section-heading">
          <div>
            <p>Browse quickly</p>
            <h2>Explore categories</h2>
          </div>
        </div>

        <div className="category-list">
          {categories.map((category) => (
            <button
              type="button"
              className={
                selectedCategory === category
                  ? "category-chip active"
                  : "category-chip"
              }
              key={category}
              onClick={() =>
                handleCategoryClick(category)
              }
            >
              {category.replaceAll("_", " ")}
            </button>
          ))}
        </div>
      </section>

      {isFiltering ? (
        <section className="shop-section section-container">
          <div className="filtered-results-header">
            <div className="home-section-heading">
              <div>
                <p className="section-title-icon">
                  <Search size={20} />
                  Search results
                </p>

                <h2>
                  {filteredShops.length} matching{" "}
                  {filteredShops.length === 1
                    ? "shop"
                    : "shops"}
                </h2>

                <span>
                  {submittedSearch &&
                    `Search: “${submittedSearch}”`}

                  {submittedSearch &&
                    selectedCategory &&
                    " • "}

                  {selectedCategory &&
                    `Category: ${selectedCategory.replaceAll(
                      "_",
                      " "
                    )}`}
                </span>
              </div>
            </div>

            <button
              type="button"
              className="clear-filters-button"
              onClick={clearFilters}
            >
              <X size={17} />
              Clear filters
            </button>
          </div>

          {filteredShops.length > 0 ? (
            <div className="shop-grid">
              {filteredShops.map((shop) => (
                <ShopCard
                  key={shop.shopId}
                  shop={shop}
                  isWishlisted={wishlistedShopIds.includes(
                    shop.shopId
                  )}
                  wishlistLoading={
                    wishlistLoadingShopId ===
                    shop.shopId
                  }
                  onWishlistToggle={
                    handleWishlistToggle
                  }
                />
              ))}
            </div>
          ) : (
            <div className="home-no-results">
              <Search size={38} />

              <h3>No matching shops found</h3>

              <p>
                Try another shop name, category or
                location.
              </p>

              <button
                type="button"
                className="primary-button"
                onClick={clearFilters}
              >
                View all shops
              </button>
            </div>
          )}
        </section>
      ) : (
        <>
          <ShopSection
            icon={<ShieldCheck size={21} />}
            title="Trusted vendors"
            subtitle="Top-rated shops based on GutTrust scores"
            shops={trustedVendors}
            wishlistedShopIds={wishlistedShopIds}
            wishlistLoadingShopId={
              wishlistLoadingShopId
            }
            onWishlistToggle={
              handleWishlistToggle
            }
          />

          <ShopSection
            icon={<Sparkles size={21} />}
            title="GutFriendly picks"
            subtitle="Curated choices for safer eating"
            shops={gutFriendlyPicks}
            wishlistedShopIds={wishlistedShopIds}
            wishlistLoadingShopId={
              wishlistLoadingShopId
            }
            onWishlistToggle={
              handleWishlistToggle
            }
          />

          <ShopSection
            icon={<ArrowRight size={21} />}
            title="Recommended for you"
            subtitle="Explore more trusted establishments"
            shops={recommendedShops}
            wishlistedShopIds={wishlistedShopIds}
            wishlistLoadingShopId={
              wishlistLoadingShopId
            }
            onWishlistToggle={
              handleWishlistToggle
            }
          />
        </>
      )}

      {!isFiltering && allShops.length === 0 && (
        <div className="page-message">
          <h2>No shops available yet</h2>

          <p>
            Shops will appear here once they are added.
          </p>
        </div>
      )}
    </div>
  );
}

function ShopSection({
  icon,
  title,
  subtitle,
  shops,
  wishlistedShopIds,
  wishlistLoadingShopId,
  onWishlistToggle,
}) {
  if (!shops || shops.length === 0) {
    return null;
  }

  return (
    <section className="shop-section section-container">
      <div className="home-section-heading">
        <div>
          <p className="section-title-icon">
            {icon}
            Featured collection
          </p>

          <h2>{title}</h2>
          <span>{subtitle}</span>
        </div>
      </div>

      <div className="shop-grid">
        {shops.slice(0, 6).map((shop) => (
          <ShopCard
            key={shop.shopId}
            shop={shop}
            isWishlisted={wishlistedShopIds.includes(
              shop.shopId
            )}
            wishlistLoading={
              wishlistLoadingShopId === shop.shopId
            }
            onWishlistToggle={onWishlistToggle}
          />
        ))}
      </div>
    </section>
  );
}

export default HomePage;