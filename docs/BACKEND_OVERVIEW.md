# GutFriendly Backend — Complete Overview

This document describes the **entire current backend**: architecture, data model, business flows, and every REST API.

**Scope today:** The codebase implements the **Vendor Portal** only (`com.gutfriendly.app.vendor`). There are no customer, admin, or inspector APIs in the repo at present.

**Base URL:** `http://localhost:8080`  
**Vendor API prefix:** `/vendor`  
**Frontend CORS:** `http://localhost:5173`

---

## Table of contents

1. [Architecture](#1-architecture)
2. [Tech stack](#2-tech-stack)
3. [Package structure](#3-package-structure)
4. [Data model](#4-data-model)
5. [Naming conventions](#5-naming-conventions)
6. [Authentication & security](#6-authentication--security)
7. [Business flows](#7-business-flows)
8. [Complete API catalog](#8-complete-api-catalog)
9. [Service layer](#9-service-layer)
10. [Enums & status machines](#10-enums--status-machines)
11. [Error handling](#11-error-handling)
12. [Configuration](#12-configuration)
13. [What's not implemented](#13-whats-not-implemented)

---

## 1. Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    React Vendor Portal                       │
│                  (localhost:5173)                            │
└──────────────────────────┬──────────────────────────────────┘
                           │ REST JSON (no auth tokens)
                           ▼
┌─────────────────────────────────────────────────────────────┐
│              Spring Boot 4.1.0 (port 8080)                   │
│  ┌─────────────────────────────────────────────────────┐    │
│  │  Controllers (9)  →  Services (10)  →  Repos (9)    │    │
│  │  VendorContextService validates vendor ↔ shop       │    │
│  └─────────────────────────────────────────────────────┘    │
└──────────────────────────┬──────────────────────────────────┘
                           │ JPA/Hibernate
                           ▼
┌─────────────────────────────────────────────────────────────┐
│                         MySQL                                │
│  vendor_details, store, store_address, menu_item,            │
│  store_order, store_order_item, store_review,                  │
│  store_payout, serviceable_area                                │
└─────────────────────────────────────────────────────────────┘
```

### Multi-shop model

One **vendor account** owns many **shops** (stores). Operational data — menu, orders, reviews, payouts, address — belongs to a **shop**, not directly to the vendor.

```
VendorDetails (account)
  └── Store × N (shops)
        ├── StoreAddress (1:1)
        ├── MenuItem × N
        ├── StoreOrder × N → StoreOrderItem × N
        ├── StoreReview × N
        └── StorePayout × N
```

### URL scoping

| Scope | Pattern | Example |
|-------|---------|---------|
| Public auth | `/vendor/register`, `/vendor/login` | No IDs |
| Vendor account | `/vendor/{vendorId}/settings/...` | Profile, password |
| Shop operations | `/vendor/{vendorId}/shops/{shopId}/...` | Orders, menu, dashboard |

---

## 2. Tech stack

| Layer | Technology |
|-------|------------|
| Framework | Spring Boot 4.1.0 |
| Language | Java 21 |
| Web | Spring Web MVC (`spring-boot-starter-webmvc`) |
| Persistence | Spring Data JPA + Hibernate |
| Database | MySQL (`mysql-connector-j`) |
| Boilerplate | Lombok |
| Security | **None** (Spring Security removed) |
| Schema management | `spring.jpa.hibernate.ddl-auto=update` |

---

## 3. Package structure

```
com.gutfriendly.app
├── GutFriendlyApplication.java          # Entry point
└── vendor
    ├── controller/     # 9 REST controllers
    ├── service/        # 10 business services
    ├── repository/     # 9 JPA repositories
    ├── model/          # 9 JPA entities
    ├── dto/            # Request/response DTOs
    └── status/         # Enums (VendorStatus, StoreOrderStatus, PayoutStatus)
```

### Controllers

| Controller | Responsibility |
|------------|----------------|
| `VendorController` | Register, login, shop CRUD |
| `VendorSettingsController` | Profile, change password |
| `StoreController` | Store details, rating |
| `StoreLocationController` | Address + serviceability |
| `StoreDashboardController` | Dashboard analytics |
| `StoreOrderController` | Order list, status updates |
| `StoreMenuController` | Menu CRUD |
| `StorePayoutController` | Payout summary + history |
| `StoreReviewController` | Reviews, stats, replies |

---

## 4. Data model

### Entity relationship

```
vendor_details
  vendor_id (PK), fName, mName, lName, phoneNo, password, email,
  aadharNo, panNo, joining_date, isActive, status

store
  store_id (PK), vendor_id (FK), storeName, imageUrl, isOpen,
  openTime, closeTime, onlineOrdersEnabled,
  estimatedPrepTimeMinutes, status, rating, ratingCount

store_address
  address_id (PK), store_id (FK), houseNo, street, city, state,
  pincode, country

menu_item
  item_id (PK), store_id (FK), name, category, description,
  price, imageUrl, active

store_order
  order_id (PK), store_id (FK), orderNumber, status, totalAmount, createdAt

store_order_item
  order_item_id (PK), order_id (FK), item_id (FK, optional), itemName,
  quantity, unitPrice

store_review
  review_id (PK), store_id (FK), customerName, customerImageUrl,
  rating, comment, vendorReply, repliedAt, createdAt

store_payout
  payout_id (PK), store_id (FK), amount, status, periodStart, periodEnd,
  paidAt, referenceNumber, description, createdAt

serviceable_area
  pincode (lookup for location checks)
```

### API field mapping

| API / DTO | Database |
|-----------|----------|
| `vendorId` | `vendor_details.vendor_id` |
| `shopId` | `store.store_id` |
| `orderId` | `store_order.order_id` |
| `itemId` | `menu_item.item_id` |

---

## 5. Naming conventions

| Prefix | Scope | Examples |
|--------|-------|----------|
| `Vendor*` | Account-level | `VendorDetails`, `VendorService`, `VendorProfileDTO` |
| `Store*` | Shop-level operations | `Store`, `StoreOrder`, `StoreAddressDTO` |

The word **shop** in URLs (`/shops/{shopId}`) maps to the `Store` entity and `store` table.

---

## 6. Authentication & security

### Current state (dev-only)

- **No JWT, session, or API keys**
- Login validates phone + password; frontend stores `vendorId` and passes it in every URL
- **No server-side check** that the caller owns the `vendorId` in the path
- Passwords stored and compared as **plaintext** (no bcrypt)
- CORS allows only `http://localhost:5173`

### Phone normalization (`VendorService`)

On register and login, phone numbers are normalized to 10 digits:

- Strips non-digits
- Removes `+91` prefix (12-digit input)
- Removes leading `0` (11-digit input)
- Rejects if result is not exactly 10 digits

### Ownership validation (`VendorContextService`)

Shop-scoped services call `findShop(vendorId, shopId)` which:

1. Loads vendor by ID → 404 if missing
2. Loads store where `store_id = shopId AND vendor_id = vendorId` → 404 if not owned

---

## 7. Business flows

### Flow 1: Vendor registration

```
POST /vendor/register
  → Validate fName, lName, phoneNo, password
  → Normalize phone to 10 digits
  → Create VendorDetails (status=PENDING, isActive=true)
  → Return vendorId
```

### Flow 2: Login

```
POST /vendor/login
  → Normalize phone
  → findByPhoneNoAndPassword (plaintext match)
  → 401 if invalid
  → Return vendor profile + list of shops (StoreDTO[])
```

### Flow 3: Shop onboarding (new vendor)

```
1. POST /vendor/{vendorId}/shops          → Create shop (status=PENDING)
2. POST /vendor/{vendorId}/shops/{shopId}/location
     → Save StoreAddress
     → Check pincode in serviceable_area
     → If serviceable: status=UNDER_REVIEW
     → If not: status=NOT_SERVICEABLE
3. (Future) Admin approves → status=APPROVED
4. Vendor configures store via PUT .../store (hours, toggles, etc.)
5. Vendor adds menu via POST .../menu
```

### Flow 4: Serviceability recheck

```
POST /vendor/{vendorId}/shops/{shopId}/serviceability/recheck
  → Load saved address (400 if none)
  → Re-check pincode against serviceable_area
  → Update shop status:
      - Not serviceable → NOT_SERVICEABLE
      - Serviceable + was PENDING/NOT_SERVICEABLE → UNDER_REVIEW
      - Otherwise keep current status
  → Return full dashboard payload
```

### Flow 5: Order lifecycle (vendor side)

```
Customer places order (not implemented in this backend)
  → StoreOrder created with status=NEW

Vendor:
  GET  .../orders?status=active     → NEW, PREPARING, OUT_FOR_DELIVERY
  PATCH .../orders/{id}/status      → Update to PREPARING, OUT_FOR_DELIVERY, DELIVERED, CANCELLED
  GET  .../orders/active-count      → Badge count for sidebar
```

Status progression:

```
NEW → PREPARING → OUT_FOR_DELIVERY → DELIVERED
                              ↘ CANCELLED
```

### Flow 6: Menu management

```
GET    .../menu                     → List items (optional activeOnly=true)
GET    .../menu/categories          → Distinct categories
POST   .../menu                     → Create (name + positive price required)
PUT    .../menu/{itemId}            → Partial update
PATCH  .../menu/{itemId}/toggle     → Flip active flag
DELETE .../menu/{itemId}            → Permanent delete
```

### Flow 7: Reviews

```
GET  .../reviews              → All reviews (newest first)
GET  .../reviews/stats        → Average + star distribution
POST .../reviews/{id}/reply   → Vendor reply (sets repliedAt)
```

Reviews and orders are **read/update only** from the vendor API — no endpoint creates orders or reviews (expected from customer app).

### Flow 8: Payouts

```
GET .../payouts/summary
  → pendingBalance  = sum(PENDING payouts)
  → totalPaidOut    = sum(COMPLETED payouts)
  → totalEarned     = pending + paid out
  → completedPayouts = count(COMPLETED)

GET .../payouts → Full history, newest first
```

Payout records are not created via vendor API (expected from admin/finance system).

### Flow 9: Dashboard analytics

Single call `GET .../dashboard` returns everything, or use widget endpoints:

| Widget | Source |
|--------|--------|
| KPI cards | Today's orders, revenue, AOV, rating + % change vs yesterday |
| Order chart | 24 hourly points (orders + revenue) for today |
| Active orders | Top 5 active (NEW/PREPARING/OUT_FOR_DELIVERY) |
| Top sellers | Top 5 items by quantity sold today |
| Recent reviews | Latest 3 reviews |
| Store status | Open flag, toggles, prep time, rating |
| Onboarding | profileCompletion%, pendingRequirements[], nextAction |

### Flow 10: Settings

```
GET  /vendor/{vendorId}/settings/profile
PUT  /vendor/{vendorId}/settings/profile     → Partial update (name, email, KYC)
POST /vendor/{vendorId}/settings/change-password
  → Validate current password (401 if wrong)
  → New password min 6 chars
```

---

## 8. Complete API catalog

**Total: 39 endpoints** across 9 controllers.

### 8.1 Authentication & shops (`VendorController`)

| # | Method | Path | Request | Response | Status |
|---|--------|------|---------|----------|--------|
| 1 | POST | `/vendor/register` | `VendorRegisterRequestDTO` | `VendorRegisterResponseDTO` | 201 |
| 2 | POST | `/vendor/login` | `VendorLoginDTO` | `VendorLoginResponseDTO` | 200 / 401 |
| 3 | GET | `/vendor/{vendorId}/shops` | — | `StoreListResponseDTO` | 200 |
| 4 | GET | `/vendor/{vendorId}/shops/{shopId}` | — | `StoreDTO` | 200 |
| 5 | POST | `/vendor/{vendorId}/shops` | `CreateShopRequestDTO` | `StoreDTO` | 201 |
| 6 | PUT | `/vendor/{vendorId}/shops/{shopId}` | `UpdateShopRequestDTO` | `StoreDTO` | 200 |

### 8.2 Settings (`VendorSettingsController`)

| # | Method | Path | Request | Response |
|---|--------|------|---------|----------|
| 7 | GET | `/vendor/{vendorId}/settings/profile` | — | `VendorProfileDTO` |
| 8 | PUT | `/vendor/{vendorId}/settings/profile` | `UpdateVendorProfileRequestDTO` | `VendorProfileDTO` |
| 9 | POST | `/vendor/{vendorId}/settings/change-password` | `ChangePasswordRequestDTO` | `MessageResponseDTO` |
| 10 | POST | `/vendor/{vendorId}/settings/change-phone` | `ChangePhoneRequestDTO` | `VendorProfileDTO` |

### 8.3 Store details (`StoreController`)

| # | Method | Path | Request | Response |
|---|--------|------|---------|----------|
| 10 | GET | `.../store` | — | `StoreDetailsDTO` |
| 11 | PUT | `.../store` | `UpdateShopRequestDTO` | `StoreDetailsDTO` |
| 12 | GET | `.../rating` | — | `StoreRatingDTO` |
| 13 | PUT | `.../rating` | `UpdateShopRatingRequestDTO` | `StoreRatingDTO` |

### 8.4 Location (`StoreLocationController`)

| # | Method | Path | Request | Response |
|---|--------|------|---------|----------|
| 14 | POST | `.../location` | `StoreLocationRequestDTO` | `StoreLocationResponseDTO` |

### 8.5 Dashboard (`StoreDashboardController`)

| # | Method | Path | Response |
|---|--------|------|----------|
| 15 | GET | `.../dashboard` | `StoreDashboardResponseDTO` |
| 16 | GET | `.../dashboard/order-overview` | `OrderOverviewResponseDTO` |
| 17 | GET | `.../dashboard/summary` | `StoreDashboardSummaryDTO` |
| 18 | GET | `.../dashboard/active-orders` | `StoreActiveOrderListResponseDTO` |
| 19 | GET | `.../dashboard/top-selling-items` | `StoreTopSellingItemListResponseDTO` |
| 20 | GET | `.../dashboard/recent-reviews` | `StoreRecentReviewListResponseDTO` |
| 21 | GET | `.../dashboard/store-status` | `StoreStatusDTO` |
| 22 | POST | `.../serviceability/recheck` | `StoreDashboardResponseDTO` |

### 8.6 Orders (`StoreOrderController`)

| # | Method | Path | Query / Body | Response |
|---|--------|------|--------------|----------|
| 23 | GET | `.../orders/active-count` | — | `ActiveOrderCountResponseDTO` |
| 24 | GET | `.../orders` | `?status=` (optional) | `StoreOrderListResponseDTO` |
| 25 | GET | `.../orders/{orderId}` | — | `StoreOrderDTO` |
| 26 | PATCH | `.../orders/{orderId}/status` | `UpdateOrderStatusRequestDTO` | `StoreOrderDTO` |

**Order filter values:** `active`, `NEW`, `PREPARING`, `OUT_FOR_DELIVERY`, `DELIVERED`, `CANCELLED`, or omit for all.

### 8.7 Menu (`StoreMenuController`)

| # | Method | Path | Query / Body | Response |
|---|--------|------|--------------|----------|
| 27 | GET | `.../menu` | `?activeOnly=false` | `MenuItemListResponseDTO` |
| 28 | GET | `.../menu/categories` | — | `MenuCategoriesResponseDTO` |
| 29 | GET | `.../menu/{itemId}` | — | `MenuItemDTO` |
| 30 | POST | `.../menu` | `CreateMenuItemRequestDTO` | `MenuItemDTO` (201) |
| 31 | PUT | `.../menu/{itemId}` | `UpdateMenuItemRequestDTO` | `MenuItemDTO` |
| 32 | PATCH | `.../menu/{itemId}/toggle` | — | `MenuItemDTO` |
| 33 | DELETE | `.../menu/{itemId}` | — | `MessageResponseDTO` |

### 8.8 Payouts (`StorePayoutController`)

| # | Method | Path | Response |
|---|--------|------|----------|
| 34 | GET | `.../payouts/summary` | `StorePayoutSummaryDTO` |
| 35 | GET | `.../payouts` | `StorePayoutListResponseDTO` |

### 8.9 Reviews (`StoreReviewController`)

| # | Method | Path | Request | Response |
|---|--------|------|---------|----------|
| 36 | GET | `.../reviews` | — | `StoreReviewListResponseDTO` |
| 37 | GET | `.../reviews/stats` | — | `StoreReviewStatsDTO` |
| 38 | POST | `.../reviews/{reviewId}/reply` | `StoreReviewReplyRequestDTO` | `StoreReviewDTO` |

> `...` = `/vendor/{vendorId}/shops/{shopId}`

---

## 9. Service layer

| Service | Used by | Key logic |
|---------|---------|-----------|
| `VendorService` | Auth | Register, login, phone normalization |
| `StoreService` | Shops, store page | CRUD shops, store details, rating cache |
| `StoreLocationService` | Location | Save address, pincode serviceability check |
| `StoreDashboardService` | Dashboard | Analytics aggregation, onboarding hints, recheck |
| `StoreOrderService` | Orders | List/filter, status update, active count |
| `StoreMenuService` | Menu | CRUD, toggle, categories |
| `StorePayoutService` | Payouts | Summary aggregates, history |
| `StoreReviewService` | Reviews | List, stats, vendor reply |
| `VendorSettingsService` | Settings | Profile update, password change |
| `VendorContextService` | Most shop services | `findVendor`, `findShop` with ownership check |

### List response wrappers

Endpoints return wrapped objects, not raw arrays:

| Wrapper | Property | Item type |
|---------|----------|-----------|
| `StoreListResponseDTO` | `shops` | `StoreDTO` |
| `StoreOrderListResponseDTO` | `orders` | `StoreOrderDTO` |
| `StoreActiveOrderListResponseDTO` | `orders` | `StoreActiveOrderDTO` |
| `StoreTopSellingItemListResponseDTO` | `items` | `StoreTopSellingItemDTO` |
| `StoreRecentReviewListResponseDTO` | `reviews` | `StoreRecentReviewDTO` |
| `StoreReviewListResponseDTO` | `reviews` | `StoreReviewDTO` |
| `StorePayoutListResponseDTO` | `payouts` | `StorePayoutDTO` |
| `MenuItemListResponseDTO` | `items` | `MenuItemDTO` |
| `OrderOverviewResponseDTO` | `points` | `OrderOverviewPointDTO` |

The full dashboard (`StoreDashboardResponseDTO`) embeds `activeOrders`, `topSellingItems`, and `recentReviews` directly — sub-endpoints wrap them in `orders` / `items` / `reviews`.

---

## 10. Enums & status machines

### VendorStatus (vendor account + shop onboarding)

| Value | Meaning |
|-------|---------|
| `PENDING` | Just created |
| `NOT_SERVICEABLE` | Pincode outside serviceable areas |
| `UNDER_REVIEW` | Serviceable; awaiting admin approval |
| `SERVICEABLE` | In a serviceable area |
| `APPROVED` | Admin approved; ready to operate |
| `REJECTED` | Admin rejected |
| `SUSPENDED` | Temporarily blocked |

**Location save transitions:**

```
Save address → pincode in serviceable_area?
  YES → UNDER_REVIEW
  NO  → NOT_SERVICEABLE
```

### StoreOrderStatus

```
NEW → PREPARING → OUT_FOR_DELIVERY → DELIVERED
                              ↘ CANCELLED
```

### PayoutStatus

`PENDING` | `PROCESSING` | `COMPLETED` | `FAILED`

---

## 11. Error handling

Spring returns standard HTTP status codes via `ResponseStatusException`:

| Status | When |
|--------|------|
| 400 | Validation failure (missing fields, invalid filter, no address for recheck) |
| 401 | Invalid login credentials; wrong current password |
| 404 | Vendor, shop, order, menu item, or review not found |
| 201 | Resource created (register, shop, menu item) |

Error body shape (typical):

```json
{
  "status": 404,
  "message": "Shop not found"
}
```

---

## 12. Configuration

`src/main/resources/application.properties` (copy from `application-example.properties`):

```properties
spring.application.name=GutFriendly
spring.datasource.url=jdbc:mysql://...
spring.datasource.username=...
spring.datasource.password=...
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

**Run:**

```bash
./mvnw spring-boot:run
```

Default port: **8080**

---

## 13. What's not implemented

| Area | Status |
|------|--------|
| Customer ordering app | No APIs — orders/reviews must be seeded or added later |
| Admin portal | Removed from codebase |
| Inspector module | Removed from codebase |
| JWT / session auth | Frontend passes `vendorId` in URLs |
| Password hashing | Plaintext storage |
| Order creation API | Read + status update only |
| Review creation API | Read + reply only |
| Payout creation API | Read only |
| Admin approval workflow | Status fields exist; no admin API to change them |
| Rating sync job | `PUT .../rating` exists for manual/sync updates |
| File upload | Image URLs are strings only |
| Webhooks / notifications | Not implemented |
| Pagination | All list endpoints return full lists |

---

## Related documents

| Document | Audience |
|----------|----------|
| [VENDOR_FRONTEND_HANDOFF.md](./VENDOR_FRONTEND_HANDOFF.md) | Frontend team — DTO shapes, UI wiring, example API client |
| `.cursor/rules/vendor-handoff.mdc` | Rule to keep handoff in sync with API changes |

---

*Last updated: August 2026. Package: `com.gutfriendly.app.vendor`.*
