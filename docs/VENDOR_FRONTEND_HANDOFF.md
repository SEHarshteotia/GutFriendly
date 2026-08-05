# GutFriendly Vendor Portal — Frontend Handoff Document

Use this document to build the **vendor-facing frontend** (e.g. React + Vite on `http://localhost:5173`) against the existing Spring Boot backend.

**Last updated:** August 2026 — reflects `Store*` naming for shop-scoped types.

---

## Table of contents

1. [Product overview](#1-product-overview)
2. [Naming conventions](#2-naming-conventions)
3. [Tech stack](#3-tech-stack-backend)
4. [Frontend pages & API mapping](#4-frontend-pages--api-mapping)
5. [Authentication flow](#5-authentication-flow)
6. [URL pattern convention](#6-url-pattern-convention)
7. [Complete API reference](#7-complete-api-reference)
8. [Key DTO shapes](#8-key-dto-shapes-typescript-friendly)
9. [Enums](#9-enums)
10. [Dashboard page — data wiring](#10-dashboard-page--data-wiring)
11. [Recommended frontend state](#11-recommended-frontend-state)
12. [UI components to build](#12-ui-components-to-build)
13. [Known limitations](#13-known-limitations-important-for-frontend)
14. [Suggested frontend stack](#14-suggested-frontend-stack)
15. [Example API service](#15-example-api-service-reference)
16. [Backend controllers](#16-backend-controllers)
17. [Database tables](#17-database-tables)

---

## 1. Product overview

**GutFriendly** is a food-ordering platform. This backend serves the **Vendor Portal** — a dashboard where restaurant owners manage one or more shops.

**Brand reference UI:** "QuickBite" vendor dashboard with sidebar navigation.

### Core concept: multi-shop per vendor

```
Vendor (account: login, KYC, profile)
  └── Shop 1, Shop 2, ... (operational units)
        ├── Address & serviceability
        ├── Menu items
        ├── Orders
        ├── Reviews
        ├── Payouts
        └── Rating (cached, synced later)
```

- **Vendor-level:** auth, profile, settings
- **Shop-level:** everything operational (dashboard, orders, menu, store, payouts, reviews)
- Frontend must maintain **`vendorId`** + **`shopId`** in app state after login
- User can **switch shops** via a shop picker (sidebar bottom + header)

### Sidebar pages (in scope)

| Page | Description |
|------|-------------|
| Dashboard | KPIs, order chart, active orders, top sellers, recent reviews, store status |
| Orders | List/filter orders, update status, sidebar badge count |
| Menu | CRUD menu items, categories, enable/disable items |
| Store | Shop details, hours, toggles, address, rating |
| Payouts | Earnings summary and payout history |
| Reviews | Customer reviews, stats, vendor replies |
| Settings | Vendor profile and password change |

---

## 2. Naming conventions

The backend uses two prefixes. **Use the same names in your TypeScript types.**

| Prefix | Scope | Examples |
|--------|-------|----------|
| **`Vendor*`** | Account-level: auth, profile, KYC, settings | `VendorProfileDTO`, `VendorLoginResponseDTO`, `VendorStatus` |
| **`Store*`** | Shop-level: operations tied to one shop | `StoreDTO`, `StoreOrderDTO`, `StoreAddressDTO`, `StoreDashboardResponseDTO` |

### Terminology

| Term in API / UI | Backend entity / DB | Notes |
|------------------|---------------------|-------|
| **shop** / `shopId` | `Store` / `store_id` | URL paths and JSON use `shopId`; DB column is `store_id` |
| **store** (page name) | Same as shop | The "Store" sidebar page edits shop details via `.../store` |
| **vendor** / `vendorId` | `VendorDetails` / `vendor_id` | One login account; owns many shops |

### What was renamed (for reference)

Legacy `VendorAddress`, `VendorStore`, `VendorOrder`, etc. are now `StoreAddress`, `Store`, `StoreOrder`, etc. **Do not use old names in the frontend.** All operational APIs are shop-scoped under `/vendor/{vendorId}/shops/{shopId}/...`.

---

## 3. Tech stack (backend)

| Layer | Technology |
|-------|------------|
| Framework | Spring Boot 4.1.0 |
| Language | Java 21 |
| Database | MySQL (JPA/Hibernate, `ddl-auto=update`) |
| API style | REST JSON |
| CORS | Allowed for `http://localhost:5173` |
| Auth | **No JWT/session yet** — login returns vendor profile; frontend stores `vendorId` and passes it in URL paths |
| Password | Plaintext comparison (no bcrypt yet) — never display or store password in UI state |

**Base URL:** `http://localhost:8080/vendor` (default Spring Boot port)

---

## 4. Frontend pages & API mapping

| Sidebar page | Purpose | Primary APIs |
|--------------|---------|--------------|
| **Dashboard** | KPIs, chart, active orders, top sellers, reviews, store status | `GET .../dashboard`, widget sub-endpoints |
| **Orders** | List/filter orders, update status | `GET .../orders`, `PATCH .../orders/{id}/status`, `GET .../orders/active-count` |
| **Menu** | CRUD menu items, categories, toggle active | `GET/POST/PUT/PATCH/DELETE .../menu` |
| **Store** | Shop details, hours, toggles, address | `GET/PUT .../store`, `POST .../location`, `GET .../rating` |
| **Payouts** | Earnings summary + history | `GET .../payouts/summary`, `GET .../payouts` |
| **Reviews** | List reviews, stats, reply | `GET .../reviews`, `GET .../reviews/stats`, `POST .../reviews/{id}/reply` |
| **Settings** | Vendor profile, change password, change phone | `GET/PUT .../settings/profile`, `POST .../settings/change-password`, `POST .../settings/change-phone` |

**Shop switcher:** `GET /vendor/{vendorId}/shops` or use `shops` array from login response.

---

## 5. Authentication flow

### Register

```
POST /vendor/register
Content-Type: application/json
```

**Request body (`VendorRegisterRequestDTO`):**

```json
{
  "fName": "Raj",
  "mName": "",
  "lName": "Kumar",
  "phoneNo": "9876543210",
  "password": "secret",
  "email": "raj@example.com",
  "aadharNo": "optional",
  "panNo": "optional"
}
```

| Field | Required |
|-------|----------|
| fName | Yes |
| lName | Yes |
| phoneNo | Yes — 10-digit Indian mobile; `+91`, spaces, and leading `0` are normalized server-side |
| password | Yes |
| mName, email, aadharNo, panNo | No |

**Response (`VendorRegisterResponseDTO`) — HTTP 201:**

```json
{
  "vendorId": 1,
  "message": "Vendor registered successfully"
}
```

### Login

```
POST /vendor/login
Content-Type: application/json
```

**Request body (`VendorLoginDTO`):**

```json
{
  "phoneNo": "9876543210",
  "password": "secret"
}
```

**Response (`VendorLoginResponseDTO`) — HTTP 200:**

```json
{
  "message": "Login successful",
  "vendor": {
    "vendorId": 1,
    "fName": "Raj",
    "mName": null,
    "lName": "Kumar",
    "phoneNo": "9876543210",
    "email": "raj@example.com",
    "aadharNo": null,
    "panNo": null,
    "active": true,
    "status": "PENDING",
    "joiningDate": "2026-08-04T10:00:00.000+00:00"
  },
  "shops": []
}
```

**On login, persist in frontend state:**

- `vendor.vendorId`
- `shops[]` for shop picker
- `selectedShopId` (first shop, or prompt user to create one)

**Phone normalization:** The backend accepts `9876543210`, `+91 98765 43210`, or `919876543210` and stores a 10-digit number. Use the same format on login as register.

**HTTP 401** on invalid credentials.

### Post-login onboarding (when `shops` is empty)

**Step 1 — Create shop:**

```
POST /vendor/{vendorId}/shops
```

```json
{
  "storeName": "Burger House",
  "imageUrl": "https://...",
  "openTime": "09:00:00",
  "estimatedPrepTimeMinutes": 15
}
```

**Step 2 — Set location:**

```
POST /vendor/{vendorId}/shops/{shopId}/location
```

```json
{
  "houseNo": "12",
  "street": "MG Road",
  "city": "Pune",
  "state": "MH",
  "pincode": "411001"
}
```

**Response (`StoreLocationResponseDTO`):**

```json
{
  "serviceable": true,
  "status": "UNDER_REVIEW",
  "message": "Your shop location is serviceable. The shop has been sent for review."
}
```

---

## 6. URL pattern convention

**Shop-scoped (most endpoints):**

```
/vendor/{vendorId}/shops/{shopId}/<resource>
```

**Vendor-scoped (settings only):**

```
/vendor/{vendorId}/settings/<resource>
```

**Auth (no IDs):**

```
/vendor/register
/vendor/login
```

---

## 7. Complete API reference

> **Path shorthand:** `...` = `/vendor/{vendorId}/shops/{shopId}`

### 7.1 Shops

| Method | Path | Request body | Response |
|--------|------|--------------|----------|
| GET | `/vendor/{vendorId}/shops` | — | `StoreListResponseDTO` |
| GET | `/vendor/{vendorId}/shops/{shopId}` | — | `StoreDTO` |
| POST | `/vendor/{vendorId}/shops` | `CreateShopRequestDTO` | `StoreDTO` (201) |
| PUT | `/vendor/{vendorId}/shops/{shopId}` | `UpdateShopRequestDTO` | `StoreDTO` |

**CreateShopRequestDTO:**

```json
{
  "storeName": "Burger House",
  "imageUrl": "optional",
  "openTime": "09:00:00",
  "estimatedPrepTimeMinutes": 15
}
```

### 7.2 Dashboard

| Method | Path | Response |
|--------|------|----------|
| GET | `.../dashboard` | `StoreDashboardResponseDTO` |
| GET | `.../dashboard/summary` | `StoreDashboardSummaryDTO` |
| GET | `.../dashboard/order-overview` | `OrderOverviewResponseDTO` |
| GET | `.../dashboard/active-orders` | `StoreActiveOrderListResponseDTO` |
| GET | `.../dashboard/top-selling-items` | `StoreTopSellingItemListResponseDTO` |
| GET | `.../dashboard/recent-reviews` | `StoreRecentReviewListResponseDTO` |
| GET | `.../dashboard/store-status` | `StoreStatusDTO` |
| POST | `.../serviceability/recheck` | `StoreDashboardResponseDTO` |

> **Note:** The full dashboard (`GET .../dashboard`) embeds `activeOrders`, `topSellingItems`, and `recentReviews` directly. Sub-endpoints below return the same data in **wrapper** objects with different property names (see [§10](#10-dashboard-page--data-wiring)).

### 7.3 Orders

| Method | Path | Query params | Request body | Response |
|--------|------|--------------|--------------|----------|
| GET | `.../orders/active-count` | — | — | `ActiveOrderCountResponseDTO` |
| GET | `.../orders` | `status` (optional) | — | `StoreOrderListResponseDTO` |
| GET | `.../orders/{orderId}` | — | — | `StoreOrderDTO` |
| PATCH | `.../orders/{orderId}/status` | — | `UpdateOrderStatusRequestDTO` | `StoreOrderDTO` |

**Order status filter values:**

- `active` — NEW, PREPARING, OUT_FOR_DELIVERY
- `NEW`, `PREPARING`, `OUT_FOR_DELIVERY`, `DELIVERED`, `CANCELLED`

**UpdateOrderStatusRequestDTO:**

```json
{ "status": "PREPARING" }
```

### 7.4 Menu

| Method | Path | Query params | Request body | Response |
|--------|------|--------------|--------------|----------|
| GET | `.../menu` | `activeOnly` (default false) | — | `MenuItemListResponseDTO` |
| GET | `.../menu/categories` | — | — | `MenuCategoriesResponseDTO` |
| GET | `.../menu/{itemId}` | — | — | `MenuItemDTO` |
| POST | `.../menu` | — | `CreateMenuItemRequestDTO` | `MenuItemDTO` (201) |
| PUT | `.../menu/{itemId}` | — | `UpdateMenuItemRequestDTO` | `MenuItemDTO` |
| PATCH | `.../menu/{itemId}/toggle` | — | — | `MenuItemDTO` |
| DELETE | `.../menu/{itemId}` | — | — | `MessageResponseDTO` |

**CreateMenuItemRequestDTO:**

```json
{
  "name": "Classic Burger",
  "category": "MAIN_COURSE",
  "description": "Beef patty with cheese",
  "price": 199.00,
  "imageUrl": "https://..."
}
```

**UpdateMenuItemRequestDTO** (all fields optional):

```json
{
  "name": "Classic Burger",
  "category": "MAIN_COURSE",
  "description": "...",
  "price": 219.00,
  "imageUrl": "https://...",
  "active": false
}
```

### 7.5 Store

| Method | Path | Request body | Response |
|--------|------|--------------|----------|
| GET | `.../store` | — | `StoreDetailsDTO` |
| PUT | `.../store` | `UpdateShopRequestDTO` | `StoreDetailsDTO` |
| POST | `.../location` | `StoreLocationRequestDTO` | `StoreLocationResponseDTO` |
| GET | `.../rating` | — | `StoreRatingDTO` |
| PUT | `.../rating` | `UpdateShopRatingRequestDTO` | `StoreRatingDTO` |

**StoreLocationRequestDTO** (address fields only — `vendorId` / `shopId` come from the URL):

```json
{
  "houseNo": "12",
  "street": "MG Road",
  "city": "Pune",
  "state": "MH",
  "pincode": "411001"
}
```

**UpdateShopRequestDTO** (all fields optional):

```json
{
  "storeName": "Burger House",
  "imageUrl": "https://...",
  "isOpen": true,
  "openTime": "09:00:00",
  "closeTime": "22:00:00",
  "onlineOrdersEnabled": true,
  "estimatedPrepTimeMinutes": 15
}
```

**UpdateShopRatingRequestDTO** (for future rating sync):

```json
{
  "rating": 4.6,
  "ratingCount": 128
}
```

### 7.6 Payouts

| Method | Path | Response |
|--------|------|----------|
| GET | `.../payouts/summary` | `StorePayoutSummaryDTO` |
| GET | `.../payouts` | `StorePayoutListResponseDTO` |

### 7.7 Reviews

| Method | Path | Request body | Response |
|--------|------|--------------|----------|
| GET | `.../reviews` | — | `StoreReviewListResponseDTO` |
| GET | `.../reviews/stats` | — | `StoreReviewStatsDTO` |
| POST | `.../reviews/{reviewId}/reply` | `StoreReviewReplyRequestDTO` | `StoreReviewDTO` |

**StoreReviewReplyRequestDTO:**

```json
{ "reply": "Thank you for your feedback!" }
```

### 7.8 Settings (vendor-level, no shopId)

| Method | Path | Request body | Response |
|--------|------|--------------|----------|
| GET | `/vendor/{vendorId}/settings/profile` | — | `VendorProfileDTO` |
| PUT | `/vendor/{vendorId}/settings/profile` | `UpdateVendorProfileRequestDTO` | `VendorProfileDTO` |
| POST | `/vendor/{vendorId}/settings/change-password` | `ChangePasswordRequestDTO` | `MessageResponseDTO` |
| POST | `/vendor/{vendorId}/settings/change-phone` | `ChangePhoneRequestDTO` | `VendorProfileDTO` |

**UpdateVendorProfileRequestDTO** (all optional):

```json
{
  "fName": "Raj",
  "mName": "",
  "lName": "Kumar",
  "email": "raj@example.com",
  "aadharNo": "123456789012",
  "panNo": "ABCDE1234F"
}
```

**ChangePasswordRequestDTO:**

```json
{
  "currentPassword": "oldsecret",
  "newPassword": "newsecret"
}
```

**ChangePhoneRequestDTO:**

```json
{
  "newPhoneNo": "9876543210",
  "password": "your-current-password"
}
```

Returns updated `VendorProfileDTO`. Phone is normalized server-side. Returns **409** if the number is already registered.

---

## 8. Key DTO shapes (TypeScript-friendly)

### Wrapper responses

List endpoints return wrapped objects, **not bare arrays**. Property names differ by endpoint:

| Response type | JSON property | Item type |
|---------------|---------------|-----------|
| `StoreListResponseDTO` | `shops` | `StoreDTO` |
| `StoreOrderListResponseDTO` | `orders` | `StoreOrderDTO` |
| `StoreActiveOrderListResponseDTO` | `orders` | `StoreActiveOrderDTO` |
| `StoreTopSellingItemListResponseDTO` | `items` | `StoreTopSellingItemDTO` |
| `StoreRecentReviewListResponseDTO` | `reviews` | `StoreRecentReviewDTO` |
| `StoreReviewListResponseDTO` | `reviews` | `StoreReviewDTO` |
| `StorePayoutListResponseDTO` | `payouts` | `StorePayoutDTO` |
| `MenuItemListResponseDTO` | `items` | `MenuItemDTO` |
| `MenuCategoriesResponseDTO` | `categories` | `string` |
| `OrderOverviewResponseDTO` | `points` | `OrderOverviewPointDTO` |
| `ActiveOrderCountResponseDTO` | `count` | `number` |
| `MessageResponseDTO` | `message` | `string` |
| `VendorLoginResponseDTO` | `shops` | `StoreDTO` |

```ts
// Examples
interface StoreListResponseDTO { shops: StoreDTO[] }
interface StoreOrderListResponseDTO { orders: StoreOrderDTO[] }
interface StoreActiveOrderListResponseDTO { orders: StoreActiveOrderDTO[] }
interface StoreTopSellingItemListResponseDTO { items: StoreTopSellingItemDTO[] }
interface StoreRecentReviewListResponseDTO { reviews: StoreRecentReviewDTO[] }
interface MenuItemListResponseDTO { items: MenuItemDTO[] }
interface OrderOverviewResponseDTO { points: OrderOverviewPointDTO[] }
interface ActiveOrderCountResponseDTO { count: number }
interface MessageResponseDTO { message: string }
```

### Auth DTOs

```ts
interface VendorRegisterRequestDTO {
  fName: string
  mName?: string
  lName: string
  phoneNo: string
  password: string
  email?: string
  aadharNo?: string
  panNo?: string
}

interface VendorRegisterResponseDTO {
  vendorId: number
  message: string
}

interface VendorLoginDTO {
  phoneNo: string
  password: string
}

interface VendorLoginResponseDTO {
  message: string
  vendor: VendorProfileDTO
  shops: StoreDTO[]
}
```

### VendorProfileDTO

```ts
interface VendorProfileDTO {
  vendorId: number
  fName: string
  mName: string | null
  lName: string
  phoneNo: string
  email: string | null
  aadharNo: string | null
  panNo: string | null
  active: boolean
  status: VendorStatus
  joiningDate: string  // ISO timestamp
}
```

### StoreDTO

```ts
interface StoreDTO {
  shopId: number
  storeName: string
  imageUrl: string | null
  open: boolean
  openTime: string           // "09:00:00"
  onlineOrdersEnabled: boolean
  estimatedPrepTimeMinutes: number
  status: VendorStatus
  rating: number | null      // e.g. 4.6; null until synced
  ratingCount: number
  address: StoreAddressDTO | null
}
```

### StoreDetailsDTO

Same as `StoreDTO` plus `closeTime: string`.

### StoreAddressDTO

```ts
interface StoreAddressDTO {
  addressId: number
  houseNo: string
  street: string
  city: string
  state: string
  pincode: string
  country: string
}
```

### StoreDashboardResponseDTO (full dashboard payload)

```ts
interface StoreDashboardResponseDTO {
  vendorId: number
  shopId: number
  shopName: string
  fullName: string
  phoneNo: string
  email: string | null
  active: boolean
  status: VendorStatus
  joiningDate: string
  address: StoreAddressDTO | null
  serviceableLocation: boolean
  profileCompletionPercentage: number
  nextAction: string
  pendingRequirements: string[]
  summary: StoreDashboardSummaryDTO
  activeOrders: StoreActiveOrderDTO[]
  topSellingItems: StoreTopSellingItemDTO[]
  recentReviews: StoreRecentReviewDTO[]
  storeStatus: StoreStatusDTO
}
```

### StoreDashboardSummaryDTO (KPI cards)

```ts
interface StoreDashboardSummaryDTO {
  todaysOrders: number
  todaysRevenue: number
  averageOrderValue: number
  averageRating: number
  reviewCount: number
  ordersChangePercent: number       // vs yesterday, e.g. 12.0
  revenueChangePercent: number
  avgOrderValueChangePercent: number
}
```

### OrderOverviewPointDTO (line chart)

```ts
interface OrderOverviewPointDTO {
  hour: number        // 0–23
  label: string       // "12 AM", "3 PM"
  orders: number
  revenue: number
}
```

### StoreActiveOrderDTO

```ts
interface StoreActiveOrderDTO {
  orderId: number
  orderNumber: string       // e.g. "#1247"
  itemsSummary: string      // "1 x Veg Burger, 1 x Fries"
  status: StoreOrderStatus
  statusLabel: string       // "Preparing", "New", etc.
  minutesAgo: number
}
```

### StoreTopSellingItemDTO

```ts
interface StoreTopSellingItemDTO {
  rank: number
  itemName: string
  quantitySold: number
}
```

### StoreRecentReviewDTO

```ts
interface StoreRecentReviewDTO {
  reviewId: number
  customerName: string
  customerImageUrl: string | null
  rating: number
  comment: string
  minutesAgo: number
}
```

### StoreOrderDTO

```ts
interface StoreOrderDTO {
  orderId: number
  orderNumber: string
  status: StoreOrderStatus
  statusLabel: string
  totalAmount: number
  createdAt: string
  minutesAgo: number
  items: StoreOrderItemDTO[]
}

interface StoreOrderItemDTO {
  orderItemId: number
  itemId: number | null
  itemName: string
  quantity: number
  unitPrice: number
}
```

### MenuItemDTO

```ts
interface MenuItemDTO {
  itemId: number
  name: string
  category: string | null
  description: string | null
  price: number
  imageUrl: string | null
  active: boolean
}
```

### StoreReviewDTO

```ts
interface StoreReviewDTO {
  reviewId: number
  customerName: string
  customerImageUrl: string | null
  rating: number
  comment: string
  vendorReply: string | null
  repliedAt: string | null
  createdAt: string
  minutesAgo: number
}
```

### StoreReviewStatsDTO

```ts
interface StoreReviewStatsDTO {
  averageRating: number
  totalReviews: number
  fiveStarCount: number
  fourStarCount: number
  threeStarCount: number
  twoStarCount: number
  oneStarCount: number
}
```

### StorePayoutSummaryDTO

```ts
interface StorePayoutSummaryDTO {
  pendingBalance: number
  totalEarned: number
  totalPaidOut: number
  completedPayouts: number
}
```

### StorePayoutDTO

```ts
interface StorePayoutDTO {
  payoutId: number
  amount: number
  status: PayoutStatus
  periodStart: string
  periodEnd: string
  paidAt: string | null
  referenceNumber: string | null
  description: string | null
  createdAt: string
}
```

### StoreStatusDTO

```ts
interface StoreStatusDTO {
  shopId: number
  storeName: string
  imageUrl: string | null
  open: boolean
  openTime: string
  onlineOrdersEnabled: boolean
  estimatedPrepTimeMinutes: number
  rating: number | null
  ratingCount: number
}
```

### StoreRatingDTO

```ts
interface StoreRatingDTO {
  shopId: number
  rating: number | null
  ratingCount: number
}
```

---

## 9. Enums

### VendorStatus

| Value | Meaning | Suggested UI |
|-------|---------|--------------|
| `PENDING` | Just created | Info badge |
| `NOT_SERVICEABLE` | Pincode not supported | Warning banner |
| `UNDER_REVIEW` | Awaiting admin approval | Pending badge |
| `SERVICEABLE` | In serviceable area | Success |
| `APPROVED` | Ready to operate | Green badge |
| `REJECTED` | Admin rejected | Error banner |
| `SUSPENDED` | Temporarily blocked | Error banner |

### StoreOrderStatus

```
NEW → PREPARING → OUT_FOR_DELIVERY → DELIVERED
                              ↘ CANCELLED
```

| Status | Suggested badge color |
|--------|----------------------|
| NEW | Blue |
| PREPARING | Orange |
| OUT_FOR_DELIVERY | Green |
| DELIVERED | Gray |
| CANCELLED | Red |

### PayoutStatus

`PENDING` | `PROCESSING` | `COMPLETED` | `FAILED`

### MenuItemCategory

Required when creating a menu item. Use enum name in JSON (`GET .../menu/categories` returns all valid values).

| Value | Suggested label |
|-------|-----------------|
| `APPETIZER` | Appetizer |
| `MAIN_COURSE` | Main Course |
| `BREADS` | Breads |
| `RICE` | Rice |
| `DESSERT` | Dessert |
| `BEVERAGE` | Beverage |
| `SNACK` | Snack |
| `SALAD` | Salad |
| `SOUP` | Soup |
| `COMBO` | Combo |

---

## 10. Dashboard page — data wiring

### Single-call initial load

```
GET /vendor/{vendorId}/shops/{shopId}/dashboard
```

Maps to UI sections:

| UI section | Response field |
|------------|----------------|
| Greeting ("Good morning, Burger House!") | `shopName`, `fullName` |
| KPI cards (orders, revenue, AOV, ratings) | `summary` |
| Order overview chart | `GET .../dashboard/order-overview` → `points` |
| Active orders panel | `activeOrders` (embedded) or `GET .../dashboard/active-orders` → `.orders` |
| Top selling items | `topSellingItems` (embedded) or `GET .../dashboard/top-selling-items` → `.items` |
| Recent reviews | `recentReviews` (embedded) or `GET .../dashboard/recent-reviews` → `.reviews` |
| Store status card | `storeStatus` |
| Onboarding banner | `pendingRequirements`, `nextAction`, `profileCompletionPercentage` |

### Orders sidebar badge

```
GET /vendor/{vendorId}/shops/{shopId}/orders/active-count
```

Response: `{ "count": 8 }`

Poll every 30–60 seconds or refresh on route change.

---

## 11. Recommended frontend state

```ts
interface AppState {
  vendor: VendorProfileDTO | null
  shops: StoreDTO[]
  selectedShopId: number | null
  isAuthenticated: boolean
}

function shopPath(vendorId: number, shopId: number, resource: string): string {
  return `/vendor/${vendorId}/shops/${shopId}/${resource}`
}
```

### Shop switch behavior

When user selects a different shop:

1. Update `selectedShopId` in state
2. Re-fetch current page data with new `shopId`
3. Update sidebar shop name and open/closed indicator

### Suggested routes

```
/login
/register
/dashboard
/orders
/menu
/store
/payouts
/reviews
/settings
```

Protect all routes except `/login` and `/register`.

---

## 12. UI components to build

| Component | Notes |
|-----------|-------|
| Login / Register forms | Phone + password; link between pages |
| Shop picker dropdown | Sidebar bottom + header; shows name, open status, rating |
| Create shop modal | Shown when `shops.length === 0` after login |
| KPI cards | 4 cards with trend arrows from `*ChangePercent` fields |
| Order overview chart | Dual-line: orders + revenue, 24 hourly points |
| Active orders list | Status badges, time ago, items summary |
| Top selling items | Ranked list with image, name, quantity |
| Recent reviews | Avatar, stars, comment, time ago |
| Store status card | Open badge, toggles for online orders / delivery, prep time, edit button |
| Orders table | Filter tabs: All, Active, New, Preparing, etc. |
| Order detail drawer | Line items, total, status update buttons |
| Menu grid/table | Category filter, add/edit modal, active toggle |
| Store settings form | Hours, toggles, address, image URL |
| Payouts summary + history table | Pending balance, earned, paid out |
| Reviews list + reply form | Star distribution chart from stats |
| Settings profile form | Name, email, KYC fields |
| Change password form | Current + new password |

### Layout (from mockup)

- Dark sidebar (fixed left)
- Light main content area
- Header: page title, notification bell, shop picker
- Responsive: collapse sidebar on mobile

---

## 13. Known limitations (important for frontend)

1. **No auth tokens** — `vendorId` is passed in URLs without verification; treat as dev-only
2. **Password never in responses** — `VendorProfileDTO` excludes password
3. **Currency** — format amounts as INR (`₹`); backend returns plain numbers
4. **Times** — `LocalTime` serializes as `"HH:mm:ss"` (e.g. `"09:00:00"`)
5. **Shop rating** — `rating` may be `null` until sync API runs; dashboard can use `summary.averageRating` as fallback
6. **CORS** — only `http://localhost:5173` is allowed
7. **Wrapped list responses** — always access `.orders`, `.items`, `.shops`, etc.
8. **Error responses** — Spring returns `{ "message": "...", "status": 404 }` style bodies on errors
9. **No vendor-level dashboard** — all dashboard/orders/menu APIs require `shopId`; pick a shop first
10. **DB migration** — if upgrading from older schema, table names changed (`vendor_store` → `store`, `vendor_address` → `store_address`, etc.); existing data is not auto-migrated

---

## 14. Suggested frontend stack

| Concern | Recommendation |
|---------|----------------|
| Framework | React 18+ with Vite |
| Routing | React Router v6 |
| Data fetching | TanStack Query (caching, polling for orders badge) |
| State | Zustand or React Context for auth + selected shop |
| HTTP | Axios or fetch with base URL `http://localhost:8080` |
| Charts | Recharts or Chart.js |
| Styling | Tailwind CSS |
| Forms | React Hook Form + Zod validation |
| Icons | Lucide React or Heroicons |

---

## 15. Example API service (reference)

```ts
const BASE = 'http://localhost:8080/vendor'

async function request<T>(url: string, options?: RequestInit): Promise<T> {
  const res = await fetch(`${BASE}${url}`, {
    headers: { 'Content-Type': 'application/json', ...options?.headers },
    ...options,
  })
  if (!res.ok) {
    const err = await res.json().catch(() => ({}))
    throw new Error(err.message ?? res.statusText)
  }
  return res.json()
}

export const vendorApi = {
  register: (body: VendorRegisterRequestDTO) =>
    request<VendorRegisterResponseDTO>('/register', { method: 'POST', body: JSON.stringify(body) }),

  login: (body: VendorLoginDTO) =>
    request<VendorLoginResponseDTO>('/login', { method: 'POST', body: JSON.stringify(body) }),

  listShops: (vendorId: number) =>
    request<StoreListResponseDTO>(`/${vendorId}/shops`),

  createShop: (vendorId: number, body: CreateShopRequestDTO) =>
    request<StoreDTO>(`/${vendorId}/shops`, { method: 'POST', body: JSON.stringify(body) }),

  getDashboard: (vendorId: number, shopId: number) =>
    request<StoreDashboardResponseDTO>(`/${vendorId}/shops/${shopId}/dashboard`),

  getOrderOverview: (vendorId: number, shopId: number) =>
    request<OrderOverviewResponseDTO>(`/${vendorId}/shops/${shopId}/dashboard/order-overview`),

  getDashboardSummary: (vendorId: number, shopId: number) =>
    request<StoreDashboardSummaryDTO>(`/${vendorId}/shops/${shopId}/dashboard/summary`),

  getActiveOrders: (vendorId: number, shopId: number) =>
    request<StoreActiveOrderListResponseDTO>(`/${vendorId}/shops/${shopId}/dashboard/active-orders`),

  getTopSellingItems: (vendorId: number, shopId: number) =>
    request<StoreTopSellingItemListResponseDTO>(`/${vendorId}/shops/${shopId}/dashboard/top-selling-items`),

  getRecentReviews: (vendorId: number, shopId: number) =>
    request<StoreRecentReviewListResponseDTO>(`/${vendorId}/shops/${shopId}/dashboard/recent-reviews`),

  recheckServiceability: (vendorId: number, shopId: number) =>
    request<StoreDashboardResponseDTO>(`/${vendorId}/shops/${shopId}/serviceability/recheck`, {
      method: 'POST',
    }),

  getActiveOrderCount: (vendorId: number, shopId: number) =>
    request<ActiveOrderCountResponseDTO>(`/${vendorId}/shops/${shopId}/orders/active-count`),

  listOrders: (vendorId: number, shopId: number, status?: string) =>
    request<StoreOrderListResponseDTO>(
      `/${vendorId}/shops/${shopId}/orders${status ? `?status=${status}` : ''}`
    ),

  updateOrderStatus: (vendorId: number, shopId: number, orderId: number, status: StoreOrderStatus) =>
    request<StoreOrderDTO>(`/${vendorId}/shops/${shopId}/orders/${orderId}/status`, {
      method: 'PATCH',
      body: JSON.stringify({ status }),
    }),

  listMenuItems: (vendorId: number, shopId: number, activeOnly = false) =>
    request<MenuItemListResponseDTO>(
      `/${vendorId}/shops/${shopId}/menu?activeOnly=${activeOnly}`
    ),

  listMenuCategories: (vendorId: number, shopId: number) =>
    request<MenuCategoriesResponseDTO>(
      `/${vendorId}/shops/${shopId}/menu/categories`
    ).then((r) => r.categories),

  createMenuItem: (vendorId: number, shopId: number, body: CreateMenuItemRequestDTO) =>
    request<MenuItemDTO>(`/${vendorId}/shops/${shopId}/menu`, {
      method: 'POST',
      body: JSON.stringify(body),
    }),

  getStoreDetails: (vendorId: number, shopId: number) =>
    request<StoreDetailsDTO>(`/${vendorId}/shops/${shopId}/store`),

  updateStore: (vendorId: number, shopId: number, body: UpdateShopRequestDTO) =>
    request<StoreDetailsDTO>(`/${vendorId}/shops/${shopId}/store`, {
      method: 'PUT',
      body: JSON.stringify(body),
    }),

  saveLocation: (vendorId: number, shopId: number, body: Omit<StoreLocationRequestDTO, 'vendorId' | 'shopId'>) =>
    request<StoreLocationResponseDTO>(`/${vendorId}/shops/${shopId}/location`, {
      method: 'POST',
      body: JSON.stringify(body),
    }),

  getPayoutSummary: (vendorId: number, shopId: number) =>
    request<StorePayoutSummaryDTO>(`/${vendorId}/shops/${shopId}/payouts/summary`),

  listPayouts: (vendorId: number, shopId: number) =>
    request<StorePayoutListResponseDTO>(`/${vendorId}/shops/${shopId}/payouts`),

  listReviews: (vendorId: number, shopId: number) =>
    request<StoreReviewListResponseDTO>(`/${vendorId}/shops/${shopId}/reviews`),

  getReviewStats: (vendorId: number, shopId: number) =>
    request<StoreReviewStatsDTO>(`/${vendorId}/shops/${shopId}/reviews/stats`),

  replyToReview: (vendorId: number, shopId: number, reviewId: number, reply: string) =>
    request<StoreReviewDTO>(`/${vendorId}/shops/${shopId}/reviews/${reviewId}/reply`, {
      method: 'POST',
      body: JSON.stringify({ reply }),
    }),

  getProfile: (vendorId: number) =>
    request<VendorProfileDTO>(`/${vendorId}/settings/profile`),

  updateProfile: (vendorId: number, body: UpdateVendorProfileRequestDTO) =>
    request<VendorProfileDTO>(`/${vendorId}/settings/profile`, {
      method: 'PUT',
      body: JSON.stringify(body),
    }),

  changePassword: (vendorId: number, body: ChangePasswordRequestDTO) =>
    request<MessageResponseDTO>(`/${vendorId}/settings/change-password`, {
      method: 'POST',
      body: JSON.stringify(body),
    }),

  changePhone: (vendorId: number, body: ChangePhoneRequestDTO) =>
    request<VendorProfileDTO>(`/${vendorId}/settings/change-phone`, {
      method: 'POST',
      body: JSON.stringify(body),
    }),
}
```

---

## 16. Backend controllers

All controllers are in `com.gutfriendly.app.vendor.controller`, base path `/vendor`.

| Controller | Responsibility |
|------------|----------------|
| `VendorController` | Register, login, shop CRUD (`/shops`) |
| `VendorSettingsController` | Profile and password (`/settings`) |
| `StoreController` | Shop details, rating (`.../store`, `.../rating`) |
| `StoreLocationController` | Address and serviceability (`.../location`) |
| `StoreDashboardController` | Dashboard and analytics (`.../dashboard`, `.../serviceability/recheck`) |
| `StoreOrderController` | Orders (`.../orders`) |
| `StoreMenuController` | Menu CRUD (`.../menu`) |
| `StorePayoutController` | Payouts (`.../payouts`) |
| `StoreReviewController` | Reviews (`.../reviews`) |

---

## 17. Database tables

Hibernate `ddl-auto=update` manages these tables:

| Table | Entity | Description |
|-------|--------|-------------|
| `vendor_details` | `VendorDetails` | Vendor account |
| `store` | `Store` | Shop / outlet (`store_id` → API `shopId`) |
| `store_address` | `StoreAddress` | One address per shop |
| `menu_item` | `MenuItem` | Menu items per shop |
| `store_order` | `StoreOrder` | Orders per shop |
| `store_order_item` | `StoreOrderItem` | Line items per order |
| `store_review` | `StoreReview` | Customer reviews per shop |
| `store_payout` | `StorePayout` | Payout records per shop |
| `serviceable_area` | `ServiceableArea` | Pincodes for location checks |

---

## Appendix: Entity relationship diagram

```
VendorDetails (vendor_details)
  ├── vendorId, fName, lName, phoneNo, password, email, KYC, status
  └── 1:N → Store (store)
              ├── storeId, storeName, hours, toggles, status, rating
              ├── 1:1 → StoreAddress (store_address)
              ├── 1:N → MenuItem (menu_item)
              ├── 1:N → StoreOrder (store_order)
              │           └── 1:N → StoreOrderItem (store_order_item)
              ├── 1:N → StoreReview (store_review)
              └── 1:N → StorePayout (store_payout)
```

---

*GutFriendly vendor portal frontend handoff. Backend package: `com.gutfriendly.app.vendor`. Shop-scoped types use `Store*`; account types use `Vendor*`.*
