# GutFriendly — Project Context

> **Purpose:** Single reference for developers and AI assistants working on this repo.  
> Summarizes the full backend, frontend, how they connect, and what is / isn't built yet.

**Last updated:** August 2026

---

## Table of contents

1. [What is GutFriendly?](#1-what-is-gutfriendly)
2. [Repository layout](#2-repository-layout)
3. [Architecture](#3-architecture)
4. [Tech stack](#4-tech-stack)
5. [Domain model](#5-domain-model)
6. [Naming conventions](#6-naming-conventions)
7. [Backend](#7-backend)
8. [Frontend](#8-frontend)
9. [Authentication & security](#9-authentication--security)
10. [Key user flows](#10-key-user-flows)
11. [API reference (all endpoints)](#11-api-reference-all-endpoints)
12. [Running locally](#12-running-locally)
13. [What's not built yet](#13-whats-not-built-yet)
14. [Related documents](#14-related-documents)

---

## 1. What is GutFriendly?

**GutFriendly** is a food-ordering platform. This repository currently implements the **Vendor Portal** — a dashboard where restaurant owners:

- Register and log in
- Own **one or more shops** (stores)
- Manage menu, orders, reviews, payouts, and shop settings
- View analytics on a per-shop dashboard

There is **no customer app**, **no admin portal**, and **no inspector module** in this repo today. Orders and reviews are read/updated by vendors; creation of those records is expected from future services or seed data.

**Brand UI reference:** QuickBite-style vendor dashboard (dark sidebar, light content area).

---

## 2. Repository layout

```
GutFriendly/
├── src/main/java/com/gutfriendly/app/
│   ├── GutFriendlyApplication.java      # Spring Boot entry
│   └── vendor/                          # Entire backend API
│       ├── controller/   (9 controllers)
│       ├── service/      (10 services)
│       ├── repository/   (9 repos)
│       ├── model/        (9 entities)
│       ├── dto/          (request/response types)
│       ├── enums/        (VendorStatus, ShopOrderStatus, PayoutStatus, MenuItemCategory)
│       └── util/         (PhoneNumberUtil)
├── src/main/resources/
│   ├── application.properties
│   └── application-example.properties
├── frontend/                            # React vendor portal
│   └── src/
│       ├── api/          vendorApi + HTTP client
│       ├── context/      AuthContext (localStorage)
│       ├── components/   Layout, modals, Alert
│       ├── pages/        9 route pages
│       └── utils/        format, errors
├── docs/
│   ├── PROJECT_CONTEXT.md               # ← this file
│   ├── BACKEND_OVERVIEW.md              # Detailed backend reference
│   └── VENDOR_FRONTEND_HANDOFF.md       # Frontend API + DTO shapes
├── pom.xml
└── .cursor/rules/vendor-handoff.mdc     # Keep handoff in sync on API changes
```

---

## 3. Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│  React Vendor Portal (frontend/)                                 │
│  http://localhost:5173                                           │
│  • Auth in localStorage (vendorId, shops, selectedShopId)        │
│  • TanStack Query for server state                               │
└────────────────────────────┬────────────────────────────────────┘
                             │ REST JSON  →  http://localhost:8080/vendor
                             ▼
┌─────────────────────────────────────────────────────────────────┐
│  Spring Boot 4.1 / Java 21 (src/main/java/.../vendor)            │
│  9 controllers → 10 services → 9 JPA repositories              │
│  VendorContextService validates vendor ↔ shop ownership          │
└────────────────────────────┬────────────────────────────────────┘
                             │ JPA/Hibernate (ddl-auto=update)
                             ▼
┌─────────────────────────────────────────────────────────────────┐
│  MySQL                                                           │
│  vendor_details, store, store_address, menu_item, store_order,   │
│  store_order_item, store_review, store_payout, serviceable_area    │
└─────────────────────────────────────────────────────────────────┘
```

### Multi-shop model

```
VendorDetails (account: login, KYC, profile)
  └── Store × N (shops — operational units)
        ├── StoreAddress (1:1)
        ├── MenuItem × N
        ├── StoreOrder × N → StoreOrderItem × N
        ├── StoreReview × N
        └── StorePayout × N
```

- **Vendor-scoped APIs:** auth, settings (`/vendor/{vendorId}/settings/...`)
- **Shop-scoped APIs:** everything operational (`/vendor/{vendorId}/shops/{shopId}/...`)

---

## 4. Tech stack

| Layer | Technology |
|-------|------------|
| **Backend framework** | Spring Boot 4.1.0 |
| **Language** | Java 21 |
| **Persistence** | Spring Data JPA, Hibernate |
| **Database** | MySQL |
| **API** | REST JSON, no GraphQL |
| **Security** | None (Spring Security removed) |
| **Frontend** | React 19, Vite 8, JavaScript |
| **Styling** | Tailwind CSS 4 |
| **Routing** | React Router 7 |
| **Data fetching** | TanStack Query 5 |
| **Charts** | Recharts |
| **Icons** | Lucide React |

---

## 5. Domain model

| Table | Entity | Description |
|-------|--------|-------------|
| `vendor_details` | `VendorDetails` | Vendor account (phone, password, KYC) |
| `store` | `Store` | Shop/outlet (`store_id` → API `shopId`) |
| `store_address` | `StoreAddress` | One address per shop |
| `menu_item` | `MenuItem` | Menu items per shop |
| `store_order` | `StoreOrder` | Orders per shop |
| `store_order_item` | `StoreOrderItem` | Line items |
| `store_review` | `StoreReview` | Customer reviews + vendor reply |
| `store_payout` | `StorePayout` | Payout records |
| `serviceable_area` | `ServiceableArea` | Serviceable pincodes |

### API ↔ DB field mapping

| API / JSON | Database |
|------------|----------|
| `vendorId` | `vendor_details.vendor_id` |
| `shopId` | `store.store_id` |
| `orderId` | `store_order.order_id` |
| `itemId` | `menu_item.item_id` |

---

## 6. Naming conventions

| Prefix | Scope | Examples |
|--------|-------|----------|
| **`Vendor*`** | Account-level | `VendorDetails`, `VendorService`, `VendorProfileDTO` |
| **`Store*`** | Shop-level operations | `Store`, `StoreOrder`, `StoreAddressDTO` |

URLs use **shop** (`/shops/{shopId}`); Java entities use **Store**; DB table is **`store`**.

---

## 7. Backend

**Package:** `com.gutfriendly.app.vendor`  
**Base path:** `/vendor`  
**CORS:** `http://localhost:5173`  
**Total endpoints:** 39

### Controllers

| Controller | Responsibility |
|------------|----------------|
| `VendorController` | Register, login, shop CRUD |
| `VendorSettingsController` | Profile, password, phone |
| `StoreController` | Store details, rating |
| `StoreLocationController` | Address + serviceability |
| `StoreDashboardController` | Dashboard analytics |
| `StoreOrderController` | Orders |
| `StoreMenuController` | Menu CRUD |
| `StorePayoutController` | Payouts |
| `StoreReviewController` | Reviews |

### Services

| Service | Role |
|---------|------|
| `VendorService` | Register, login, phone normalization |
| `VendorSettingsService` | Profile, password, change phone |
| `StoreService` | Shop CRUD, store details, rating |
| `StoreLocationService` | Save address, pincode check |
| `StoreDashboardService` | KPIs, charts, onboarding hints |
| `StoreOrderService` | List/filter orders, status updates |
| `StoreMenuService` | Menu CRUD, categories, toggle |
| `StorePayoutService` | Payout summary + history |
| `StoreReviewService` | Reviews, stats, replies |
| `VendorContextService` | `findVendor`, `findShop` with ownership check |

### Enums

**VendorStatus** (vendor + shop onboarding):  
`PENDING` → `UNDER_REVIEW` / `NOT_SERVICEABLE` → `APPROVED` / `REJECTED` / `SUSPENDED` / `SERVICEABLE`

**StoreOrderStatus:**  
`NEW` → `PREPARING` → `OUT_FOR_DELIVERY` → `DELIVERED` (or `CANCELLED`)

**PayoutStatus:**  
`PENDING` | `PROCESSING` | `COMPLETED` | `FAILED`

### Phone normalization (`PhoneNumberUtil`)

Used on register, login, and change-phone. Accepts `9876543210`, `+91 98765 43210`, `919876543210` → stores 10 digits.

### Error responses

Spring `ResponseStatusException` → JSON with `message` (and sometimes `detail`).  
Typical statuses: 400 validation, 401 auth, 404 not found, 409 conflict.

---

## 8. Frontend

**Directory:** `frontend/`  
**Dev URL:** `http://localhost:5173`  
**API base:** `http://localhost:8080/vendor` (hardcoded in `src/api/client.js`)

### Routes

| Path | Page | Auth |
|------|------|------|
| `/login` | Login | Guest |
| `/register` | Register | Guest |
| `/dashboard` | Dashboard (KPIs, chart, widgets) | Protected |
| `/orders` | Order list + status updates | Protected |
| `/menu` | Menu CRUD | Protected |
| `/store` | Shop settings + location | Protected |
| `/payouts` | Payout summary + history | Protected |
| `/reviews` | Reviews + replies | Protected |
| `/settings` | Profile, phone, password | Protected |

### App state (`AuthContext`)

Persisted in `localStorage` key `gutfriendly_vendor_auth`:

```js
{
  vendor: { vendorId, fName, lName, phoneNo, email, ... } | null
  shops: [{ shopId, shopName, ... }]
  selectedShopId: number | null
}
```

After login, frontend passes `vendorId` and `selectedShopId` in every API URL. **No JWT.**

### Key UI behaviors

| Feature | Behavior |
|---------|----------|
| **First shop** | Modal auto-opens when `shops.length === 0` (cannot dismiss) |
| **Add store** | Sidebar → "Add new store" (optional modal, dismissible) |
| **Shop switcher** | Sidebar dropdown; changing shop refetches page data |
| **Orders badge** | Sidebar polls `active-count` every 60s |
| **Errors** | `Alert` component + `getErrorMessage()` with context (login, register, etc.) |
| **Network down** | Shows message to start backend on :8080 |

### Frontend structure

```
frontend/src/
├── api/
│   ├── client.js       # fetch wrapper, ApiError, Spring error parsing
│   └── vendorApi.js    # All 39 API methods
├── context/
│   └── AuthContext.jsx
├── components/
│   ├── Alert.jsx
│   ├── CreateShopModal.jsx
│   ├── ProtectedRoute.jsx
│   └── layout/
│       ├── AppLayout.jsx
│       └── Sidebar.jsx
├── pages/              # One file per route (.jsx)
└── utils/
    ├── errors.js       # getErrorMessage(context)
    └── format.js       # INR, time ago, status badges
```

### Page → API mapping

| Page | Primary APIs |
|------|----------------|
| Dashboard | `GET .../dashboard`, `.../dashboard/order-overview` |
| Orders | `GET .../orders`, `PATCH .../orders/{id}/status`, `GET .../orders/active-count` |
| Menu | `GET/POST/PUT/PATCH/DELETE .../menu` |
| Store | `GET/PUT .../store`, `POST .../location` |
| Payouts | `GET .../payouts/summary`, `GET .../payouts` |
| Reviews | `GET .../reviews`, `GET .../reviews/stats`, `POST .../reviews/{id}/reply` |
| Settings | `GET/PUT .../settings/profile`, `POST .../change-password`, `POST .../change-phone` |

---

## 9. Authentication & security

| Aspect | Current state |
|--------|----------------|
| Login | Phone + plaintext password |
| Session | None — `vendorId` in URL paths |
| Authorization | `VendorContextService` checks shop ownership; **no caller identity verification** |
| Password storage | Plaintext in DB (dev only) |
| CORS | `localhost:5173` only |

**Treat as development-only.** Production needs JWT/session, bcrypt, and server-side auth middleware.

---

## 10. Key user flows

### Register → first shop

```
1. POST /vendor/register
2. POST /vendor/login  →  shops: []
3. Frontend: required Create Shop modal
4. POST /vendor/{vendorId}/shops
5. POST /vendor/{vendorId}/shops/{shopId}/location
   → serviceable pincode → UNDER_REVIEW
   → not serviceable     → NOT_SERVICEABLE
6. Vendor adds menu, configures store
```

### Vendor daily use

```
Login → select shop (sidebar) → Dashboard / Orders / Menu / etc.
Orders: filter by status, advance NEW → PREPARING → OUT_FOR_DELIVERY → DELIVERED
```

### Change phone (Settings)

```
POST /vendor/{vendorId}/settings/change-phone
{ newPhoneNo, password }
→ validates password, normalizes phone, checks uniqueness (409)
→ returns updated VendorProfileDTO
```

### Add another store

```
Sidebar → "Add new store" → POST /vendor/{vendorId}/shops
→ new shop auto-selected
```

---

## 11. API reference (all endpoints)

> `...` = `/vendor/{vendorId}/shops/{shopId}`

### Auth & shops (`VendorController`)

| Method | Path | Description |
|--------|------|-------------|
| POST | `/vendor/register` | Create vendor account |
| POST | `/vendor/login` | Login → profile + shops |
| GET | `/vendor/{vendorId}/shops` | List shops |
| GET | `/vendor/{vendorId}/shops/{shopId}` | Get shop |
| POST | `/vendor/{vendorId}/shops` | Create shop |
| PUT | `/vendor/{vendorId}/shops/{shopId}` | Update shop |

### Settings (`VendorSettingsController`)

| Method | Path | Description |
|--------|------|-------------|
| GET | `/vendor/{vendorId}/settings/profile` | Get profile |
| PUT | `/vendor/{vendorId}/settings/profile` | Update profile |
| POST | `/vendor/{vendorId}/settings/change-password` | Change password |
| POST | `/vendor/{vendorId}/settings/change-phone` | Change phone |

### Store & location

| Method | Path | Description |
|--------|------|-------------|
| GET | `.../store` | Store details |
| PUT | `.../store` | Update store |
| GET | `.../rating` | Cached rating |
| PUT | `.../rating` | Update rating (sync hook) |
| POST | `.../location` | Save address + serviceability |

### Dashboard

| Method | Path | Description |
|--------|------|-------------|
| GET | `.../dashboard` | Full dashboard payload |
| GET | `.../dashboard/summary` | KPI cards |
| GET | `.../dashboard/order-overview` | 24h chart data |
| GET | `.../dashboard/active-orders` | Active orders list |
| GET | `.../dashboard/top-selling-items` | Top sellers today |
| GET | `.../dashboard/recent-reviews` | Recent reviews |
| GET | `.../dashboard/store-status` | Open/toggles status |
| POST | `.../serviceability/recheck` | Re-check pincode |

### Orders

| Method | Path | Description |
|--------|------|-------------|
| GET | `.../orders/active-count` | Badge count |
| GET | `.../orders?status=` | List orders |
| GET | `.../orders/{orderId}` | Order detail |
| PATCH | `.../orders/{orderId}/status` | Update status |

### Menu

| Method | Path | Description |
|--------|------|-------------|
| GET | `.../menu` | List items |
| GET | `.../menu/categories` | Categories |
| GET | `.../menu/{itemId}` | Single item |
| POST | `.../menu` | Create item |
| PUT | `.../menu/{itemId}` | Update item |
| PATCH | `.../menu/{itemId}/toggle` | Toggle active |
| DELETE | `.../menu/{itemId}` | Delete item |

### Payouts & reviews

| Method | Path | Description |
|--------|------|-------------|
| GET | `.../payouts/summary` | Payout aggregates |
| GET | `.../payouts` | Payout history |
| GET | `.../reviews` | All reviews |
| GET | `.../reviews/stats` | Rating breakdown |
| POST | `.../reviews/{reviewId}/reply` | Vendor reply |

### List response wrappers

Endpoints return wrapped objects, not bare arrays:

| Wrapper property | Content |
|------------------|---------|
| `shops` | `StoreDTO[]` |
| `orders` | `StoreOrderDTO[]` |
| `items` | `MenuItemDTO[]` |
| `reviews` | `StoreReviewDTO[]` |
| `payouts` | `StorePayoutDTO[]` |
| `points` | chart hourly data |

---

## 12. Running locally

### Prerequisites

- Java 21, Maven (or `./mvnw`)
- Node.js 18+
- MySQL with credentials in `application.properties`

### Backend

```bash
# Copy and configure DB credentials
cp src/main/resources/application-example.properties src/main/resources/application.properties

./mvnw spring-boot:run
# → http://localhost:8080
```

### Frontend

```bash
cd frontend
npm install
npm run dev
# → http://localhost:5173
```

### Build for production

```bash
./mvnw clean package
cd frontend && npm run build   # output in frontend/dist/
```

---

## 13. What's not built yet

| Area | Status |
|------|--------|
| Customer ordering app | Not in repo |
| Admin portal (approve shops) | Not in repo — status fields exist, no API to change them |
| Inspector module | Removed / not in repo |
| JWT / session auth | Not implemented |
| Password hashing (bcrypt) | Not implemented |
| Order creation API | Vendor can only read/update orders |
| Review creation API | Vendor can only read/reply |
| Payout creation API | Read-only for vendor |
| File/image upload | URLs are strings only |
| Pagination | Lists return all records |
| Email / SMS / notifications | Not implemented |
| Production deployment config | Not in repo |

---

## 14. Related documents

| Document | Use when |
|----------|----------|
| **[PROJECT_CONTEXT.md](./PROJECT_CONTEXT.md)** | Onboarding, AI context, big picture |
| **[BACKEND_OVERVIEW.md](./BACKEND_OVERVIEW.md)** | Deep dive on backend services, flows, errors |
| **[VENDOR_FRONTEND_HANDOFF.md](./VENDOR_FRONTEND_HANDOFF.md)** | DTO shapes, UI wiring, example API client |
| **[frontend/README.md](../frontend/README.md)** | Frontend quick start |
| **`.cursor/rules/vendor-handoff.mdc`** | Rule: update handoff when changing vendor APIs |

### Maintenance rule

When changing vendor APIs or DTOs, update **`VENDOR_FRONTEND_HANDOFF.md`** in the same task. Update **`PROJECT_CONTEXT.md`** and **`BACKEND_OVERVIEW.md`** when architecture or endpoint counts change.

---

*GutFriendly — Vendor Portal. Backend: `com.gutfriendly.app.vendor`. Frontend: `frontend/`.*
