# GutFriendly Vendor Portal (Frontend)

React + Vite + JavaScript vendor dashboard for the GutFriendly Spring Boot backend.

## Prerequisites

- Node.js 18+
- Backend running at `http://localhost:8080` (see root `README` / `mvnw spring-boot:run`)

## Quick start

```bash
cd frontend
npm install
npm run dev
```

Open **http://localhost:5173**

## Pages

| Route | Page |
|-------|------|
| `/login` | Sign in |
| `/register` | Create vendor account |
| `/dashboard` | KPIs, chart, active orders, reviews |
| `/orders` | List/filter orders, update status |
| `/menu` | CRUD menu items |
| `/store` | Shop settings + location |
| `/payouts` | Earnings summary + history |
| `/reviews` | Reviews + replies |
| `/settings` | Profile + password |

## Create shop modal

- **First shop (required):** Shown automatically when `shops.length === 0` after login — cannot be dismissed.
- **Additional stores:** Click **Add new store** in the sidebar (below the shop picker) anytime.

## Stack

- React 19 + Vite 8
- JavaScript
- Tailwind CSS 4
- React Router 7
- TanStack Query
- Recharts
- Lucide icons

## API

All requests go to `http://localhost:8080/vendor` — see `src/api/vendorApi.js`.

## Build

```bash
npm run build
npm run preview
```
