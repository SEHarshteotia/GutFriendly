# GutFriendly Module Overview

## Architecture

Single Spring Boot application (`com.gutfriendly.app.GutFriendlyApplication`) with six logical modules sharing one MySQL database (`gutfriendly`).

```
┌─────────────────────────────────────────────────────────────┐
│                    GutFriendlyApplication                    │
├──────────┬──────────┬──────────┬──────────┬──────────┬───────┤
│  admin   │ inspector│  vendor  │   user   │  orders  │reviews│
└──────────┴──────────┴──────────┴──────────┴──────────┴───────┘
                              │
                    ┌─────────▼─────────┐
                    │   MySQL (JPA)     │
                    └───────────────────┘
```

**No authentication layer** — endpoints identify actors via path parameters.

---

## Module Responsibilities

### admin
Government/platform operations: shop oversight, inspection lifecycle, dashboard analytics.

| Layer | Package | Count |
|-------|---------|-------|
| Controllers | `admin.controller` | 4 (`Admin`, `AdminDashboardController`, `AdminShopController`, `AdminInspectionController`) |
| Services | `admin.service` | 5 |
| Repositories | `admin.repository` | 11 |
| Entities | `admin.model` | 10 (shared catalog + admin) |
| DTOs | `admin.dto` | request + response |
| Enums | `admin.enums` | ShopStatus, InspectionStatus, Category, etc. |

**Owns (canonical):** `ShopDetails`, `FoodItemsDetails`, `VendorDetails`, `Pincode`, `Certificate`, `AdminDetails`, `UserPaymentStatus`, `VendorShopAddress`

---

### inspector
Field inspection execution: test results, report submission.

| Layer | Package |
|-------|---------|
| Controllers | `inspector.controller` (1) |
| Services | `inspector.service` (1) |
| Entities | `inspector.model` (5) |
| Mappers | `inspector.mapper` (3) |
| Repositories | Uses `admin.repository` |

**Owns (canonical):** `InspectionDetails`, `InspectionTestResult`, `InspectionImages`, `InspectorDetails`, `TestCatalog`

---

### vendor
Restaurant partner onboarding, menu, orders, payouts.

| Layer | Package |
|-------|---------|
| Controllers | `vendor.controller` (10) |
| Services | `vendor.service` (10) |
| DTOs | `vendor.dto` (40+) |
| Mappers | `vendor.mapper` (4) |
| Enums/Status | `vendor.status`, `vendor.enums` |
| Entities | `vendor.model` (`ShopPayout` only) |

**Uses:** `admin.model.ShopDetails`, `admin.model.FoodItemsDetails`, `orders.model.UserOrders`, `reviews.model.ShopReview`

---

### user
Consumer-facing browse, cart, wishlist, profile.

| Layer | Package |
|-------|---------|
| Controllers | `vendor.controller` (6) |
| Services | `user.service` (6) |
| Entities | `user.model` (5) |
| Repositories | `user.repository` (5) |
| Exceptions | `user.exception` (global handler) |

**Owns (canonical):** `UserDetails`, `UserAddress`, `Cart`, `CartItem`, `Wishlist`

---

### orders
Order placement, cancellation, status.

| Layer | Package |
|-------|---------|
| Controllers | `orders.controller` (1) |
| Services | `orders.service` (1) |
| Entities | `orders.model` (2) |
| Enums | `orders.enums` (Status, OrderStatus, PaymentMethod, PaymentStatus) |
| Repositories | `orders.repository` (2) |

**Owns (canonical):** `UserOrders`, `OrderItems`

---

### reviews
Post-delivery shop reviews and trust score updates.

| Layer | Package |
|-------|---------|
| Controllers | `reviews.controller` (1) |
| Services | `reviews.service` (1) |
| Entities | `reviews.model` (2) |
| Repositories | `reviews.repository` (2) |

**Owns (canonical):** `ShopReview` (order reviews), `UserReviews` (legacy dining reviews — admin dashboard only)

---

## Cross-Module Dependencies

```
vendor ──► admin.model.ShopDetails, FoodItemsDetails
vendor ──► orders.model.UserOrders (read/update status)
vendor ──► reviews.model.ShopReview (read)

user   ──► admin.model.ShopDetails, FoodItemsDetails, Pincode
user   ──► orders (via OrderController)
user   ──► reviews (via ReviewController)

orders ──► user.model.UserDetails, Cart
orders ──► admin.model.ShopDetails, FoodItemsDetails

reviews ──► orders.model.UserOrders
reviews ──► user.model.UserDetails
reviews ──► admin.model.ShopDetails
reviews ──► user.service.GutTrustScoreService

admin  ──► inspector.model.InspectionDetails
admin  ──► user.service.GutTrustScoreService (on inspection approve)

inspector ──► admin.repository.* (shared repos)
```

---

## Canonical Entity Ownership

| Table | Entity | Module |
|-------|--------|--------|
| `vendor_details` | `VendorDetails` | admin |
| `shop_details` | `ShopDetails` | admin |
| `food_items_details` | `FoodItemsDetails` | admin |
| `vendor_shop_address` | `VendorShopAddress` | admin |
| `pincode` | `Pincode` | admin |
| `user_details` | `UserDetails` | user |
| `user_address` | `UserAddress` | user |
| `cart` / `cart_item` | `Cart` / `CartItem` | user |
| `wishlist` | `Wishlist` | user |
| `user_orders` / `order_items` | `UserOrders` / `OrderItems` | orders |
| `shop_reviews` | `ShopReview` | reviews |
| `user_reviews` | `UserReviews` | reviews (legacy) |
| `inspection_details` | `InspectionDetails` | inspector |
| `inspector_details` | `InspectorDetails` | inspector |
| `test_catalog` | `TestCatalog` | inspector |
| `inspection_test_result` | `InspectionTestResult` | inspector |
| `shop_payout` | `ShopPayout` | vendor |
| `admin_details` | `AdminDetails` | admin |
| `certificate` | `Certificate` | admin |
| `user_payment_status` | `UserPaymentStatus` | admin |

Duplicate `@Entity` mappings were removed in PASS 9.

---

## API Surface by Module

| Module | Base paths | Endpoints |
|--------|------------|-----------|
| vendor | `/vendor` | ~39 |
| user | `/users`, `/shops`, `/foods`, `/cart`, `/wishlist`, `/home` | ~26 |
| orders | `/orders` | 5 |
| reviews | `/reviews` | 6 |
| admin | `/admin`, `/admin/dashboard`, `/admin/inspections` | ~24 |
| inspector | `/inspector` | 7 |
| **Total** | | **~107** |

---

## Exception Handling

`GlobalExceptionHandler` (`user.exception`) applies application-wide:

- `ResourceNotFoundException` → 404
- `BadRequestException` → 400
- `ConflictException` → 409
- `ResponseStatusException` → mapped status
- `Exception` → 500

---

## Configuration

| Property | Value |
|----------|-------|
| `server.port` | 8080 (default) |
| `spring.datasource.url` | `jdbc:mysql://localhost:3306/gutfriendly` |
| `spring.jpa.hibernate.ddl-auto` | `update` |

---

## Out of Scope (by design)

- JWT / Spring Security
- Payment gateway
- Push notifications / WebSockets
- Certificate REST API
- Inspector list REST API
