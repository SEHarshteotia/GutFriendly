import { Minus, Plus } from "lucide-react";

function FoodCard({
  food,
  quantity,
  loading,
  onAdd,
  onIncrease,
  onDecrease,
}) {
  const fallbackImage =
    "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?auto=format&fit=crop&w=800&q=80";

  const imageUrl =
    food.imageUrls && food.imageUrls.length > 0
      ? food.imageUrls[0]
      : fallbackImage;

  return (
    <article className="food-card">
      <img
        src={imageUrl}
        alt={food.foodName}
        className="food-card-image"
      />

      <div className="food-card-content">
        <div className="food-card-heading">
          <div>
            <h3>{food.foodName}</h3>

            <span>
              {food.foodCategory?.replaceAll(
                "_",
                " "
              )}
            </span>
          </div>

          <strong>
            ₹{Number(food.price).toFixed(2)}
          </strong>
        </div>

        <p className="food-description">
          {food.foodDesc ||
            "Freshly prepared food item."}
        </p>

        <div className="food-card-actions">
          {quantity === 0 ? (
            <button
              type="button"
              className="add-cart-button"
              onClick={() => onAdd(food)}
              disabled={loading}
            >
              {loading ? "Adding..." : "ADD"}
            </button>
          ) : (
            <div className="quantity-control active-quantity-control">
              <button
                type="button"
                onClick={() => onDecrease(food)}
                disabled={loading}
                aria-label="Decrease quantity"
              >
                <Minus size={16} />
              </button>

              <span>{quantity}</span>

              <button
                type="button"
                onClick={() => onIncrease(food)}
                disabled={loading}
                aria-label="Increase quantity"
              >
                <Plus size={16} />
              </button>
            </div>
          )}
        </div>
      </div>
    </article>
  );
}

export default FoodCard;