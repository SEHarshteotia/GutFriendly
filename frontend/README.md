# GutFriendly Frontends

All client applications live under this folder. Each app is an independent Vite + React project with its own `package.json`.

| App | Path | Port (dev) | Role |
|-----|------|------------|------|
| **User (consumer)** | [`userside/`](userside/) | 5174 | Register, home, shops, cart, wishlist, orders, reviews |
| **Vendor** | [`vendor/`](vendor/) | 5173 | Vendor portal — shops, menu, orders, payouts |
| **Admin + Inspector** | [`admin-inspector/`](admin-inspector/) | 5175 | Admin dashboard, shops, inspections; inspector workflow |

## Shared UI

- [`shared/GutFriendlyLogo.jsx`](shared/GutFriendlyLogo.jsx) — brand mark used across all apps (import via `@shared/GutFriendlyLogo`)

## Quick start

```bash
# User app
cd frontend/userside && npm install && npm run dev

# Vendor app
cd frontend/vendor && npm install && npm run dev

# Admin + Inspector app
cd frontend/admin-inspector && npm install && npm run dev
```

## Production build

```bash
cd frontend/<app> && npm run build
```

Backend API base URL: `http://localhost:8080` (see each app's `vite.config.js` and `VITE_API_BASE_URL`).

## Integration reports

- PASS 11: [`docs/FRONTEND_INTEGRATION_REPORT.md`](../docs/FRONTEND_INTEGRATION_REPORT.md)
- PASS 12 (vendor): integrated in `vendor/`
- PASS 13 (admin): [`docs/ADMIN_INTEGRATION_REPORT.md`](../docs/ADMIN_INTEGRATION_REPORT.md)
- PASS 14 (inspector): [`docs/INSPECTOR_INTEGRATION_REPORT.md`](../docs/INSPECTOR_INTEGRATION_REPORT.md)
- PASS 15 (user): [`docs/USER_INTEGRATION_REPORT.md`](../docs/USER_INTEGRATION_REPORT.md)
