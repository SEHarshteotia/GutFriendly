# GutFriendly API Reference

**Base URL:** `http://localhost:8080`  
**CORS (vendor):** `http://localhost:5173`  
**Auth:** None — all endpoints trust path-parameter IDs. Store `userId` / `vendorId` / `inspectorId` client-side after login.

---

## Standard Error Response

Most user-module errors return `ErrorResponse`:

```json
{
  "timestamp": "2026-08-06T03:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Shop not found",
  "path": "/shops/99"
}
```

| Status | Source | When |
|--------|--------|------|
| 400 | `BadRequestException`, `ResponseStatusException` | Invalid input, business rule violation |
| 401 | `ResponseStatusException` (vendor login) | Invalid vendor credentials |
| 404 | `ResourceNotFoundException`, `ResponseStatusException` | Entity not found |
| 409 | `ConflictException`, `ResponseStatusException` | Duplicate data, invalid state transition |
| 500 | Unhandled `Exception` | Unexpected server error |

Admin/inspector services use the same exceptions (PASS 9). Vendor services use `ResponseStatusException` mapped by `GlobalExceptionHandler`.

---

## Pagination Convention

| Parameter | Default | Used by |
|-----------|---------|---------|
| `page` | `0` | Admin, inspector, foods, reviews |
| `size` | `10` (foods: `6`, reviews: `5`) | Paginated endpoints |
| `sortBy` | varies | Entity field name |
| `direction` | `DESC` or `asc` | `ASC` / `DESC` (case-insensitive) |

**Paginated response:** Spring `Page<T>` JSON (`content`, `totalElements`, `totalPages`, `size`, `number`, …).

**Non-paginated:** User shop listings return `List<ShopCardDTO>`.

---

# Vendor Module (`/vendor`)

## Authentication & Shops

### POST `/vendor/register`
| | |
|---|---|
| **Purpose** | Register a new vendor |
| **Request** | `VendorRegisterRequestDTO` |
| **Response** | `VendorRegisterResponseDTO` (`vendorId`, `message`) |
| **Success** | `201 Created` |
| **Errors** | `400` missing fields; `409` duplicate phone/email (DB) |
| **Validation** | fName, lName, phoneNo, password, aadharNo, panNo required |
| **Business** | Sets `isActive=true`, plaintext password |

### POST `/vendor/login`
| | |
|---|---|
| **Purpose** | Vendor login |
| **Request** | `VendorLoginDTO` (`phoneNo`, `password`) |
| **Response** | `VendorLoginResponseDTO` (`message`, `vendor`, `shops[]`) |
| **Success** | `200` |
| **Errors** | `401` invalid credentials or inactive account |

### GET `/vendor/{vendorId}/shops`
| | |
|---|---|
| **Purpose** | List vendor's shops |
| **Path vars** | `vendorId` |
| **Response** | `ShopListResponseDTO` (`shops: ShopDTO[]`) |
| **Success** | `200` |

### GET `/vendor/{vendorId}/shops/{shopId}`
| | |
|---|---|
| **Purpose** | Get single shop summary |
| **Path vars** | `vendorId`, `shopId` |
| **Response** | `ShopDTO` |
| **Errors** | `404` shop not found or not owned by vendor |

### POST `/vendor/{vendorId}/shops`
| | |
|---|---|
| **Purpose** | Create shop |
| **Request** | `CreateShopRequestDTO` (`shopName`) |
| **Response** | `ShopDTO` |
| **Success** | `201` |
| **Business** | `ShopStatus.PENDING`, `NOT_SERVICEABLE`, placeholder GST |

### PUT `/vendor/{vendorId}/shops/{shopId}`
| | |
|---|---|
| **Purpose** | Update shop basic info |
| **Request** | `UpdateShopRequestDTO` |
| **Response** | `ShopDTO` |

## Shop Settings & Profile

### GET `/vendor/{vendorId}/shops/{shopId}/settings`
**Response:** `ShopDetailsDTO` (hours, flags, address, operational fields)

### PUT `/vendor/{vendorId}/shops/{shopId}/settings`
**Request:** `UpdateShopRequestDTO` → **Response:** `ShopDetailsDTO`

### GET `/vendor/{vendorId}/shops/{shopId}/rating`
**Response:** `ShopRatingDTO`

### PUT `/vendor/{vendorId}/shops/{shopId}/rating`
**Request:** `UpdateShopRatingRequestDTO` → **Response:** `ShopRatingDTO`

### GET `/vendor/{vendorId}/settings/profile`
**Response:** `VendorProfileDTO`

### PUT `/vendor/{vendorId}/settings/profile`
**Request:** `UpdateVendorProfileRequestDTO` → **Response:** `VendorProfileDTO`

### POST `/vendor/{vendorId}/settings/change-password`
**Request:** `ChangePasswordRequestDTO` → **Response:** `MessageResponseDTO`

### POST `/vendor/{vendorId}/settings/change-phone`
**Request:** `ChangePhoneRequestDTO` → **Response:** `VendorProfileDTO`

## Location & Serviceability

### POST `/vendor/{vendorId}/shops/{shopId}/location`
| | |
|---|---|
| **Purpose** | Save shop address and validate pincode |
| **Request** | `ShopLocationRequestDTO` (`houseNo`, `street`, `city`, `state`, `pincode`) |
| **Response** | `ShopLocationResponseDTO` (`serviceable`, `status`, `message`) |
| **Validation** | `pincode` required; must exist in `pincode` table |
| **Business** | Sets `SERVICEABLE` on success; rejects unknown pincode with `400` |

### POST `/vendor/{vendorId}/shops/{shopId}/serviceability/recheck`
**Response:** `ShopDashboardResponseDTO` — re-validates saved pincode

## Inspection Booking

### POST `/vendor/{vendorId}/shops/{shopId}/inspections/book`
| | |
|---|---|
| **Purpose** | Book food-safety inspection |
| **Request** | `BookInspectionRequestDTO` (`inspectionDate`) |
| **Response** | `InspectionResponse` |
| **Business** | Shop must be `SERVICEABLE`; no active inspection; date must be future; creates `SCHEDULED` |

## Dashboard

### GET `/vendor/{vendorId}/shops/{shopId}/dashboard`
**Response:** `ShopDashboardResponseDTO` — full dashboard payload (summary, active orders, top items, reviews, onboarding)

### GET `.../dashboard/order-overview`
**Response:** `OrderOverviewResponseDTO` — hourly orders/revenue today

### GET `.../dashboard/summary`
**Response:** `ShopDashboardSummaryDTO` — today metrics + day-over-day %

### GET `.../dashboard/active-orders`
**Response:** `ShopActiveOrderListResponseDTO`

### GET `.../dashboard/top-selling-items`
**Response:** `ShopTopSellingItemListResponseDTO`

### GET `.../dashboard/recent-reviews`
**Response:** `ShopRecentReviewListResponseDTO`

### GET `.../dashboard/shop-status`
**Response:** `ShopStatusDTO`

## Menu

### GET `/vendor/{vendorId}/shops/{shopId}/menu`
**Query:** `activeOnly` (default `false`) → `MenuItemListResponseDTO`

### GET `.../menu/categories`
**Response:** `MenuCategoriesResponseDTO`

### GET `.../menu/{itemId}`
**Response:** `MenuItemDTO`

### POST `.../menu`
**Request:** `CreateMenuItemRequestDTO` → **Response:** `MenuItemDTO` (`201`)

### PUT `.../menu/{itemId}`
**Request:** `UpdateMenuItemRequestDTO` → **Response:** `MenuItemDTO`

### PATCH `.../menu/{itemId}/toggle`
**Response:** `MenuItemDTO` — toggles `available`

### DELETE `.../menu/{itemId}`
**Response:** `MessageResponseDTO`

## Orders

### GET `/vendor/{vendorId}/shops/{shopId}/orders/active-count`
**Response:** `ActiveOrderCountResponseDTO`

### GET `/vendor/{vendorId}/shops/{shopId}/orders`
**Query:** `status` — optional: `active`, or `NEW`/`ACCEPTED`/`PREPARING`/`OUT_FOR_DELIVERY`/`DELIVERED`/`CANCELLED`  
**Response:** `ShopOrderListResponseDTO`

### GET `.../orders/{orderId}`
**Response:** `ShopOrderDTO` with line items

### PATCH `.../orders/{orderId}/status`
| | |
|---|---|
| **Request** | `UpdateOrderStatusRequestDTO` (`status: ShopOrderStatus`) |
| **Response** | `ShopOrderDTO` |
| **Business** | `NEW→ACCEPTED→PREPARING→OUT_FOR_DELIVERY→DELIVERED`; no skip/cancel via this endpoint |

## Reviews (read-only)

### GET `/vendor/{vendorId}/shops/{shopId}/reviews`
**Query:** `rating` (optional 1–5) → `ShopReviewListResponseDTO`

### GET `.../reviews/stats`
**Response:** `ShopReviewStatsDTO` (avg + star breakdown)

## Payouts

### GET `/vendor/{vendorId}/shops/{shopId}/payouts/summary`
**Response:** `ShopPayoutSummaryDTO`

### GET `/vendor/{vendorId}/shops/{shopId}/payouts`
**Response:** `ShopPayoutListResponseDTO`

---

# User Module

## Users (`/users`)

### POST `/users/register`
**Request:** `UserDetails` entity body → **Response:** plain string `"User Registered Successfully!"` (`200`)

### POST `/users/login`
**Request:** `UserLoginDTO` → **Response:** `{ message, userId, fname, rewardPoints }`

### GET `/users/profile/{id}`
**Response:** `ProfileResponseDTO`

### PUT `/users/profile/{id}`
**Request:** `UpdateProfileDTO` → **Response:** `ProfileResponseDTO`

### POST `/users/address/{id}`
**Request:** `UserAddress` → **Response:** `UserAddress` entity

### GET `/users/address/{id}`
**Response:** `List<UserAddress>`

### DELETE `/users/address/{id}`
**Response:** `{ message }` — deletes by address ID only (no user check)

### DELETE `/users/{userId}/address/{addressId}`
**Response:** `{ message }` — ownership verified

### DELETE `/users/{id}`
**Response:** `{ message }` — delete account

## Home (`/home`)

### GET `/home/user/{userId}`
**Response:** `HomePageDTO` (shops, trusted, recommended, categories, userName, rewardPoints)

## Shops (`/shops`)

### GET `/shops`
**Response:** `List<ShopCardDTO>` — VERIFIED, non-blocked only

### GET `/shops/trusted-vendors`
**Response:** `List<ShopCardDTO>` — sorted by `finalGutTrustScore` desc

### GET `/shops/search?keyword=`
**Response:** `List<ShopCardDTO>`

### GET `/shops/category/{category}`
**Path:** `category` = `Category` enum → `List<ShopCardDTO>`

### GET `/shops/{id}`
**Response:** `ShopDetailsDTO`

## Foods (`/foods`)

### GET `/foods/shop/{shopId}`
**Query:** `page`, `size`, `sortBy`, `direction` → `Page<FoodItemDTO>`  
**Business:** Shop must be VERIFIED and not blocked

### GET `/foods/{foodId}`
**Response:** `FoodItemDTO`

### GET `/foods/search?keyword=`
**Query:** pagination params → `Page<FoodItemDTO>`

## Cart (`/cart`)

### POST `/cart/user/{userId}/items`
**Request:** `AddToCartDTO` (`foodId`, `quantity`) → **Response:** `CartDTO`

### GET `/cart/user/{userId}`
**Response:** `CartDTO`

### PUT `/cart/user/{userId}/items/{cartItemId}`
**Request:** `UpdateCartQuantityDTO` → **Response:** `CartDTO`

### DELETE `/cart/user/{userId}/items/{cartItemId}`
**Response:** `CartDTO`

### DELETE `/cart/user/{userId}/clear`
**Response:** `CartDTO`

**Business:** Single-shop cart; food must be `available=true`

## Wishlist (`/wishlist`)

### POST `/wishlist/user/{userId}/shop/{shopId}`
**Response:** `WishlistDTO`

### GET `/wishlist/user/{userId}`
**Response:** `List<WishlistDTO>`

### DELETE `/wishlist/user/{userId}/shop/{shopId}`
**Response:** plain string

### GET `/wishlist/user/{userId}/shop/{shopId}/status`
**Response:** `Boolean`

---

# Orders Module (`/orders`)

### POST `/orders/user/{userId}`
| | |
|---|---|
| **Request** | `PlaceOrderDTO` (`deliveryAddress`, `paymentMethod`) |
| **Response** | `OrderDTO` |
| **Business** | Cart non-empty; shop VERIFIED, not blocked, online, open; clears cart; creates `OrderItems` |

### GET `/orders/user/{userId}`
**Response:** `List<OrderDTO>`

### GET `/orders/user/{userId}/{orderId}`
**Response:** `OrderDTO` (ownership checked)

### PUT `/orders/user/{userId}/{orderId}/cancel`
**Business:** Only when status `PLACED` → `CANCELLED`

### PUT `/orders/{orderId}/status?status=`
**Query:** `OrderStatus` enum — **no ownership check** (internal/admin use)

---

# Reviews Module (`/reviews`)

### POST `/reviews/user/{userId}`
**Request:** `ReviewRequestDTO` → **Response:** `ReviewDTO`  
**Business:** Order `DELIVERED`; one review per order; updates trust scores

### GET `/reviews/shop/{shopId}`
**Query:** pagination → `Page<ReviewDTO>`

### GET `/reviews/shop/{shopId}/summary`
**Response:** `ReviewSummaryDTO`

### GET `/reviews/{reviewId}`
**Response:** `ReviewDTO`

### PUT `/reviews/user/{userId}/{reviewId}`
**Request:** `ReviewRequestDTO` → **Response:** `ReviewDTO`

### DELETE `/reviews/user/{userId}/{reviewId}`
**Response:** plain string

---

# Admin Module (`/admin`)

## Auth

### POST `/admin/registration`
**Request:** `AdminRegisterRequest` → **Response:** `ResponseDto`  
**Validation:** email, password ≥8 with uppercase+digit, phone, name, password match

### POST `/admin/login`
**Request:** `AdminDetails` body → **Response:** `ResponseDto` (stub — always succeeds)

## Dashboard (`/admin/dashboard`)

| Method | URL | Response |
|--------|-----|----------|
| GET | `/summary` | `DashboardSummaryDto` |
| GET | `/monthly-trends` | `List<MonthlyTrendResponse>` |
| GET | `/category-performance` | `List<CategoryPerformanceResponse>` |
| GET | `/recent-activities` | `List<RecentActivityResponse>` |
| GET | `/upcoming-inspections` | `List<UpcomingInspectionResponse>` |

## Shops (`/admin/shops`)

| Method | URL | Notes |
|--------|-----|-------|
| GET | `/shops` | Paginated all shops |
| GET | `/shops/status/{status}` | Filter by `ShopStatus` |
| GET | `/shops/availability-Status/{status}` | Filter by `ServiceAvailabilityStatus` |
| GET | `/shops/search?shopName=` | Name search |
| GET | `/shops/{shopId}` | Single shop |
| PATCH | `/shops/{shopId}/block` | Body: `BlockShopRequest` |
| PATCH | `/shops/{shopId}/unblock` | Unblock shop |

**Pagination:** `page`, `size`, `sortBy` (default `createdAt`), `direction` (default `DESC`)

## Inspections (`/admin/inspections`)

| Method | URL | Purpose |
|--------|-----|---------|
| GET | `/` | All inspections (paginated) |
| GET | `/{inspectionId}` | Single inspection |
| GET | `/status/{status}` | Filter by `InspectionStatus` |
| GET | `/shop/{shopId}` | By shop |
| GET | `/inspector/{inspectorId}` | By inspector |
| PATCH | `/{inspectionId}/assign/{inspectorId}` | `SCHEDULED→ASSIGNED` |
| PATCH | `/{inspectionId}/review` | `REPORT_SUBMITTED→UNDER_ADMIN_REVIEW` |
| PATCH | `/{inspectionId}/approve` | `UNDER_ADMIN_REVIEW→APPROVED`; shop `VERIFIED` |
| PATCH | `/{inspectionId}/reject` | Body: `InspectionRejectionRequest` |
| PATCH | `/{inspectionId}/reinspection` | Body: `ReInspectionRequest`; creates new `SCHEDULED` |

---

# Inspector Module (`/inspector`)

| Method | URL | Purpose |
|--------|-----|---------|
| GET | `/{inspectorId}/inspections` | Assigned inspections (paginated) |
| GET | `/inspection/{inspectionId}` | Inspection detail |
| PATCH | `/inspection/{inspectionId}/start` | `ASSIGNED→IN_PROGRESS` |
| GET | `/tests` | Active test catalog |
| POST | `/inspection/{inspectionId}/test-results` | Body: `InspectionTestResultRequest` |
| GET | `/inspection/{inspectionId}/test-results` | List test results |
| PATCH | `/inspection/{inspectionId}/submit` | Body: `SubmitInspectionRequest`; `IN_PROGRESS→REPORT_SUBMITTED` |

---

# API Consistency Notes

| Topic | Finding |
|-------|---------|
| **Naming** | Vendor uses `Shop*` paths; user uses plural nouns (`/shops`, `/foods`) |
| **Status codes** | Vendor register/shop create return `201`; most others `200` |
| **Response wrappers** | Vendor wraps lists (`ShopListResponseDTO`); user shops return raw `List` |
| **Login responses** | Inconsistent: vendor DTO, user Map, admin `ResponseDto` |
| **Entity in API** | `POST /users/register` accepts raw `UserDetails` entity |
| **Pagination** | Admin/inspector/foods/reviews paginated; user shop browse not paginated |
| **Certificates** | **No REST API** — `Certificate` entity exists; dashboard reads only |
| **Inspectors list** | **No REST API** for listing inspectors (assign uses ID in path) |

---

# DTO Review Summary

| Issue | Detail |
|-------|--------|
| **Missing fields** | Vendor dashboard lacks structured `inspectionId`/`inspectionStatus`; admin dashboard `averageGutTrustScore` always `0` |
| **Redundant fields** | `ShopDashboardResponseDTO` duplicates vendor profile + shop status already in nested DTOs |
| **Large payloads** | `GET .../dashboard` returns full dashboard in one call (by design) |
| **Entity leakage** | `POST /users/register` and address endpoints return JPA entities |
| **No circular refs** | All APIs return DTOs (except user register/address) |
