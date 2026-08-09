# GutFriendly Unified Frontend

All three client apps run from **one origin** on port `5173`:

| Path | App |
|------|-----|
| `/` | Customer site and landing |
| `/vendor-portal/` | Vendor portal |
| `/staff-portal/` | Admin and inspector portal |

API calls on the same origin (`/users`, `/vendor`, `/admin`, `/inspector`, …) are
proxied to Spring Boot, so the browser does not depend on CORS or separate
frontend ports.

## Run locally (recommended)

1. Start MySQL and create the `gutfriendly` database.
2. Set `DB_PASSWORD` (plus `DB_URL`/`DB_USERNAME` if needed).
3. From the repository root, start Spring Boot with `mvnw.cmd spring-boot:run`.
4. In this directory:

```bash
npm run install:all
npm run dev
```

Open:

- Customer / landing: http://localhost:5173/
- Vendor login: http://localhost:5173/vendor-portal/login
- Staff login: http://localhost:5173/staff-portal/login

Override the backend with `BACKEND_URL` and the listen port with `PORT` if needed.

## Production-style serve

```bash
npm run build
npm start
```

`npm run build` builds each Vite app and assembles them into `dist/`.
`npm start` serves that folder and proxies API traffic to
`http://localhost:8080` by default.

## Source applications

Each app remains an independent Vite + React project:

| App | Path | Role |
|-----|------|------|
| **User (consumer)** | [`userside/`](userside/) | Register, home, shops, cart, wishlist, orders, reviews |
| **Vendor** | [`vendor/`](vendor/) | Shops, menu, orders, payouts |
| **Admin + Inspector** | [`admin-inspector/`](admin-inspector/) | Admin dashboard and inspector workflow |

Isolated per-app `npm run dev` still works for focused work, but the default
workflow is the unified server above.

## Shared UI

- [`shared/GutFriendlyLogo.jsx`](shared/GutFriendlyLogo.jsx) — brand mark used across all apps (import via `@shared/GutFriendlyLogo`)

## Integration reports

- PASS 11: [`docs/FRONTEND_INTEGRATION_REPORT.md`](../docs/FRONTEND_INTEGRATION_REPORT.md)
- PASS 12 (vendor): integrated in `vendor/`
- PASS 13 (admin): [`docs/ADMIN_INTEGRATION_REPORT.md`](../docs/ADMIN_INTEGRATION_REPORT.md)
- PASS 14 (inspector): [`docs/INSPECTOR_INTEGRATION_REPORT.md`](../docs/INSPECTOR_INTEGRATION_REPORT.md)
- PASS 15 (user): [`docs/USER_INTEGRATION_REPORT.md`](../docs/USER_INTEGRATION_REPORT.md)
