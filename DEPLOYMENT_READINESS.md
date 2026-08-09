# GutFriendly — Deployment Readiness Report

**Target:** Contabo Linux VPS  
**Inspected:** 2026-08-10  
**Scope:** Read-only analysis — **no code was modified** for this document.

---

## 1. Inspection Answers (Quick Reference)

| # | Question | Finding |
|---|----------|---------|
| 1 | Frontend framework & build | **React 19** + **Vite 7** + **Tailwind CSS 4**. Three SPAs (`userside`, `vendor`, `admin-inspector`) assembled into one `frontend/dist` and served by a Node static server (`frontend/server.js`). |
| 2 | Spring Boot version | **4.1.0** (`spring-boot-starter-parent`) |
| 3 | Java version | **Java 17** (`pom.xml` → `<java.version>17</java.version>`). JDK 17+ required on VPS. |
| 4 | Maven or Gradle | **Maven** (`pom.xml`, `mvnw` / `mvnw.cmd`) |
| 5 | Database | **MySQL 8** via JDBC; DB name `gutfriendly`; Hibernate `ddl-auto=update` |
| 6 | Env-specific config | Mostly **hardcoded** in `application.properties`. `.env.example` lists `DB_*` / `BACKEND_URL` / `PORT`, but Spring **does not read** those env vars today. |
| 7 | API base URLs | Frontends use `VITE_API_BASE_URL` (optional). Default: **relative paths** (`""` or `/vendor`) — works when Node server proxies API to backend. |
| 8 | CORS | `WebCorsConfig` + many `@CrossOrigin(origins = "http://localhost:5173")` on vendor controllers — **localhost only**. |
| 9 | File/image upload | **No multipart upload.** Images are **URL strings** in DB (`imageUrl`). Test catalog references placeholder paths like `/images/tests/...`. |
| 10 | JWT / Spring Security | **None.** No `spring-boot-starter-security`. Plaintext password compare in services. IDs in URL/localStorage. |
| 11 | localhost URLs | Backend CORS/origins; Vite proxies; `BACKEND_URL` default; error message in vendor client; sample `.env`. |
| 12 | Hardcoded ports | Backend default **8080**; unified frontend **5173**; Vite apps **5173 / 5174 / 5175**. |
| 13 | Secrets / passwords | **MySQL password in tracked `application.properties`.** Sample-data passwords are plaintext. No API keys found. |
| 14 | Separate deploy possible? | **Yes.** Backend JAR + MySQL independently; frontends as static files (or unified Node server). |
| 15 | Production changes needed? | **Yes** — see blockers below. Deployable after config/CORS/secret/env wiring changes. |

---

## 2. Current Architecture

```
┌──────────────────────────────────────────────────────────────┐
│  Browser                                                     │
│  /                  → userside (customer)                    │
│  /vendor-portal/    → vendor SPA                             │
│  /staff-portal/     → admin + inspector SPA                  │
└────────────────────────────┬─────────────────────────────────┘
                             │
                             ▼
┌──────────────────────────────────────────────────────────────┐
│  Node frontend server (frontend/server.js)  PORT=5173        │
│  - Serves frontend/dist                                      │
│  - Proxies /users,/shops,/vendor,/admin,/inspector,… → API   │
└────────────────────────────┬─────────────────────────────────┘
                             │ BACKEND_URL (default :8080)
                             ▼
┌──────────────────────────────────────────────────────────────┐
│  Spring Boot monolith  com.gutfriendly.app  PORT 8080        │
│  Modules: admin | inspector | vendor | user | orders | reviews│
└────────────────────────────┬─────────────────────────────────┘
                             │ JDBC
                             ▼
┌──────────────────────────────────────────────────────────────┐
│  MySQL 8  database: gutfriendly                              │
└──────────────────────────────────────────────────────────────┘
```

### Frontend layout

| App | Dev port | Production path (unified) | Build tool |
|-----|----------|---------------------------|------------|
| `frontend/userside` | 5174 | `/` | Vite |
| `frontend/vendor` | 5173 | `/vendor-portal/` (`base` in vite.config) | Vite |
| `frontend/admin-inspector` | 5175 | `/staff-portal/` | Vite |
| Unified | 5173 | `frontend/server.js` → `dist/` | `npm run build` + assemble |

### Backend layout

- Entry: `GutFriendlyApplication.java`
- Build: Maven → `target/GutFriendly-1.0.0.jar` (executable Spring Boot JAR)
- Schema: Hibernate `spring.jpa.hibernate.ddl-auto=update` (no Flyway/Liquibase)

---

## 3. Deployment Requirements (Contabo VPS)

| Component | Requirement |
|-----------|-------------|
| OS | Ubuntu 22.04/24.04 LTS (or similar) |
| Java | **JDK 17** (Temurin/OpenJDK 17+) |
| Maven | 3.9+ (or use `./mvnw`) |
| Node.js | **18+** (20 LTS recommended) for build + optional static server |
| MySQL | **8.0+** |
| RAM | ≥ 2 GB recommended (4 GB better for JAR + MySQL + Node) |
| Disk | ≥ 10 GB free |
| Network | Open **80/443** (public); optionally firewall 8080/5173 to localhost only |
| DNS | Domain or Contabo IP pointing to VPS |

Optional but recommended: **Nginx** as reverse proxy + **Certbot** (HTTPS), **systemd** units for JAR and Node.

---

## 4. Required Environment Variables

### Today (actually used)

| Variable | Used by | Default | Notes |
|----------|---------|---------|-------|
| `PORT` | `frontend/server.js` | `5173` | Frontend HTTP port |
| `BACKEND_URL` | `frontend/server.js` / `backend-proxy.js` | `http://localhost:8080` | Upstream API |
| `VITE_API_BASE_URL` | Frontend builds (optional) | `""` (relative) | Bake in at **build time** if frontends call API on another origin |
| `VITE_VENDOR_PORTAL_URL` | userside links | `/vendor-portal` | |
| `VITE_ADMIN_INSPECTOR_PORTAL_URL` | userside links | `/staff-portal` | |
| `VITE_USER_PORTAL_URL` | cross-links | `""` | |

### Documented in `.env.example` but **NOT wired in Spring**

| Variable | Status |
|----------|--------|
| `DB_URL` | **Unused** — Spring reads `spring.datasource.url` from properties file only |
| `DB_USERNAME` | **Unused** |
| `DB_PASSWORD` | **Unused** |

### Spring properties that must be set for production (currently file-based)

```properties
spring.datasource.url=jdbc:mysql://HOST:3306/gutfriendly
spring.datasource.username=...
spring.datasource.password=...
spring.jpa.hibernate.ddl-auto=update   # or validate after schema stable
spring.jpa.show-sql=false              # recommended in prod
server.port=8080                       # optional override
```

These can be overridden at runtime without code changes via:

```bash
java -jar GutFriendly-1.0.0.jar \
  --spring.datasource.url=jdbc:mysql://127.0.0.1:3306/gutfriendly \
  --spring.datasource.username=gutfriendly \
  --spring.datasource.password='STRONG_PASSWORD' \
  --spring.jpa.show-sql=false
```

---

## 5. Production Issues Found

### BLOCKERS (will prevent or break public deploy)

| ID | Severity | Issue |
|----|----------|-------|
| B1 | **CRITICAL** | **CORS locked to localhost** (`WebCorsConfig` + vendor `@CrossOrigin(...5173)`). Browser calls from `https://yourdomain.com` will fail unless same-origin proxy is used **and** CORS is not still rejecting, or CORS is updated. |
| B2 | **CRITICAL** | **DB password committed** in `src/main/resources/application.properties` (`Sk782378`). Must rotate and stop shipping secrets in the JAR/repo. |
| B3 | **HIGH** | **`.env.example` DB vars unused.** Operators may think setting `DB_PASSWORD` env is enough — it is not. |
| B4 | **HIGH** | **No authentication tokens.** Anyone who knows/guesses `vendorId` / `userId` / `inspectorId` can call APIs. Acceptable for demo; **not acceptable** for a public VPS without network lockdown or adding auth. |
| B5 | **HIGH** | **Passwords stored and compared in plaintext** (users, vendors, inspectors, admins). |

### Serious (should fix before public traffic)

| ID | Severity | Issue |
|----|----------|-------|
| S1 | HIGH | `spring.jpa.show-sql=true` → log noise / possible data leakage in logs |
| S2 | MEDIUM | `ddl-auto=update` on production is risky for schema drift; fine for first bring-up, then switch to `validate` |
| S3 | MEDIUM | No HTTPS / HSTS story in app — must terminate TLS at Nginx |
| S4 | MEDIUM | Vendor client error text hardcodes `http://localhost:8080` (UX only) |
| S5 | LOW | Test catalog / sample image paths (`/images/tests/...`) are placeholders — broken images unless assets added |
| S6 | LOW | `.gitignore` ignores `/docs/` — docs may not be in git; unrelated to runtime |

### Non-blockers if using recommended architecture

| Item | Why OK |
|------|--------|
| Relative `VITE_API_BASE_URL` | Works when Node/`Nginx` proxies `/api` or API prefixes to Spring on same host |
| No file upload API | No disk mount needed for uploads; only external image URLs |
| Three frontends | Already assemblable via `npm run build` in `frontend/` |

---

## 6. Required Code Changes (Before / For Production)

> Not applied yet — listed for planning.

1. **Externalize datasource**  
   - Use `spring.datasource.url=${DB_URL:...}` (or Spring Boot’s standard env mapping)  
   - Remove real password from repo; keep only `application-example.properties`  
   - Prefer `application-prod.properties` + `SPRING_PROFILES_ACTIVE=prod`

2. **Fix CORS**  
   - Replace localhost origins with env-driven list, e.g. `APP_CORS_ORIGINS=https://yourdomain.com`  
   - Remove or align per-controller `@CrossOrigin(localhost:5173)` so they don’t override/conflict

3. **Production profile**  
   - `show-sql=false`  
   - Optional `ddl-auto=validate` after first successful schema create  
   - Explicit `server.port` / `server.forward-headers-strategy=framework` behind Nginx

4. **Security (strongly recommended for public VPS)**  
   - Add Spring Security + JWT (or session)  
   - Hash passwords (BCrypt)  
   - Stop trusting path `vendorId`/`userId` alone

5. **Frontend build for VPS**  
   - If same-origin via Nginx/Node proxy: leave `VITE_API_BASE_URL` empty  
   - If API on another host/port: set `VITE_API_BASE_URL=https://api.yourdomain.com` at **build** time

6. **Secrets hygiene**  
   - Rotate MySQL password already exposed in git history  
   - Ensure `application.properties` with secrets is not deployed from git as-is

---

## 7. Recommended Deployment Architecture (Contabo)

**Recommended (simple, matches current code):**

```
Internet → Nginx :443 (TLS)
              ├─ /                  → Node frontend :5173  OR  static files from frontend/dist
              ├─ /vendor-portal/    → same
              ├─ /staff-portal/     → same
              └─ /users,/shops,/vendor,/admin,/inspector,/orders,... → Spring Boot :8080
                    └─ MySQL :3306 (localhost only)
```

**Option A — Use existing Node server (least app change)**  
- Build frontends → `frontend/dist`  
- Run `node server.js` with `BACKEND_URL=http://127.0.0.1:8080`  
- Nginx proxies `80/443` → `127.0.0.1:5173`  
- Spring only listens on `127.0.0.1:8080`

**Option B — Nginx serves static + proxies API (no Node runtime)**  
- `root` = `frontend/dist`  
- SPA fallbacks for `/`, `/vendor-portal/`, `/staff-portal/`  
- `location` blocks for API prefixes → `http://127.0.0.1:8080`  
- Requires careful SPA `try_files` for each portal base path

**Firewall:** allow 22, 80, 443 only. Do **not** expose MySQL or 8080 publicly.

---

## 8. Exact Build Commands

### Backend (on VPS or CI)

```bash
cd /opt/gutfriendly   # or your clone path
chmod +x mvnw
./mvnw -DskipTests clean package
# Artifact: target/GutFriendly-1.0.0.jar
```

### Frontend (unified)

```bash
cd frontend
npm run install:all
# Optional same-origin (default):
npm run build
# Or absolute API host at build time:
# VITE_API_BASE_URL=https://yourdomain.com npm run build
# Output: frontend/dist/
```

### Database

```bash
sudo mysql -e "CREATE DATABASE IF NOT EXISTS gutfriendly CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
sudo mysql -e "CREATE USER IF NOT EXISTS 'gutfriendly'@'localhost' IDENTIFIED BY 'STRONG_PASSWORD';"
sudo mysql -e "GRANT ALL ON gutfriendly.* TO 'gutfriendly'@'localhost'; FLUSH PRIVILEGES;"
```

---

## 9. Exact Run Commands

### First-time schema

1. Start Spring Boot once with correct JDBC URL (Hibernate creates/updates tables).  
2. Optionally seed:

```bash
mysql -u gutfriendly -p gutfriendly < scripts/sample-data.sql
```

### Backend

```bash
java -jar target/GutFriendly-1.0.0.jar \
  --spring.datasource.url=jdbc:mysql://127.0.0.1:3306/gutfriendly \
  --spring.datasource.username=gutfriendly \
  --spring.datasource.password='STRONG_PASSWORD' \
  --spring.jpa.show-sql=false \
  --server.address=127.0.0.1 \
  --server.port=8080
```

### Frontend (unified Node)

```bash
cd frontend
PORT=5173 BACKEND_URL=http://127.0.0.1:8080 node server.js
```

### systemd (illustrative)

```ini
# /etc/systemd/system/gutfriendly-api.service
[Service]
WorkingDirectory=/opt/gutfriendly
ExecStart=/usr/bin/java -jar /opt/gutfriendly/target/GutFriendly-1.0.0.jar --spring.datasource.password=...
Restart=always

# /etc/systemd/system/gutfriendly-web.service
[Service]
WorkingDirectory=/opt/gutfriendly/frontend
Environment=PORT=5173
Environment=BACKEND_URL=http://127.0.0.1:8080
ExecStart=/usr/bin/node server.js
Restart=always
```

---

## 10. Database Migration Requirements

| Mechanism | Present? |
|-----------|----------|
| Flyway | **No** |
| Liquibase | **No** |
| Hibernate `ddl-auto` | **Yes** — `update` |

### Production approach

1. **First deploy:** keep `ddl-auto=update` (or run once), start app → tables created.  
2. **Seed (optional):** `scripts/sample-data.sql` (dev/demo data — **do not** use sample passwords in production).  
3. **Optional migrations already in repo:**  
   - `scripts/migrations/make-aadhar-pan-optional.sql` (if column still `NOT NULL`)  
4. **After stable:** set `ddl-auto=validate` (or `none`) and manage future schema with SQL/Flyway.

### Manual checks after first start

```sql
SHOW TABLES;
SELECT COUNT(*) FROM test_catalog;  -- expect 44 if sample DART seed loaded
```

No automatic migration pipeline exists — **schema depends on Hibernate + manual SQL scripts**.

---

## 11. Separate vs Combined Deploy

| Mode | Feasible? | Notes |
|------|-----------|-------|
| Backend alone | Yes | JAR + MySQL |
| Frontend alone | Yes | Static `dist` or Node server; needs API reachable |
| Combined on one VPS | **Recommended** | Nginx → Node or static + JAR + MySQL |
| Frontends on CDN, API on VPS | Yes | Requires CORS + `VITE_API_BASE_URL` at build |

---

## 12. Hardcoded Localhost / Ports Inventory

| Location | Value | Impact |
|----------|-------|--------|
| `application.properties` | `localhost:3306` | Must override on VPS |
| `WebCorsConfig.java` | `localhost:5173/5174/5175` | **CORS blocker** for public domain |
| Vendor `@CrossOrigin` | `localhost:5173` | Same |
| Vite configs | proxy → `localhost:8080` | Dev only |
| `backend-proxy.js` | default `http://localhost:8080` | Override with `BACKEND_URL` |
| `server.js` | port `5173` | Override with `PORT` |
| `vendor/.../client.js` | error string mentions localhost:8080 | UX only |
| `.env.example` | localhost DB / 8080 | Docs only |

---

## 13. Secrets Inventory

| Secret | Where | Risk |
|--------|-------|------|
| MySQL password `Sk782378` | Tracked `application.properties` | **Exposed in repo/JAR** — rotate |
| Sample user/vendor/inspector passwords | `scripts/sample-data.sql` | Demo only — change or omit in prod |
| No cloud API keys | — | None found |

---

## 14. What Could Prevent Deployment (Checklist)

Mark each before go-live:

- [ ] **B1 CORS** — still localhost-only → browsers blocked from domain  
- [ ] **B2 committed DB password** — security/compliance failure  
- [ ] **B3 env vars not bound** — wrong DB config if operator relies on `.env` alone  
- [ ] **B4 no JWT/auth** — open APIs if 8080 exposed  
- [ ] **MySQL not installed / wrong credentials** — app fails to start  
- [ ] **Java &lt; 17** — build/run fails  
- [ ] **Frontend not built** — `server.js` returns 503  
- [ ] **Port 80/443 not open** on Contabo firewall  
- [ ] **8080 exposed publicly** without auth → data risk  
- [ ] Using sample-data on production without password rotation  

**With Option A (Nginx → Node proxy → local Spring) and CLI JDBC overrides, the app can be brought up for a private/demo Contabo deploy even before full CORS/auth refactors — but B1–B4 must be addressed before treating it as a secure public product.**

---

## 15. Suggested Contabo Bring-Up Order

1. Install JDK 17, MySQL 8, Node 20, Nginx, Certbot  
2. Create DB + user  
3. Clone repo; build JAR; run with CLI datasource overrides (ignore file password)  
4. Confirm `curl http://127.0.0.1:8080/shops` (or health endpoint)  
5. `cd frontend && npm run install:all && npm run build && PORT=5173 BACKEND_URL=http://127.0.0.1:8080 node server.js`  
6. Point Nginx to `:5173`, obtain TLS certificate  
7. Seed pincodes / admin user carefully (not full demo dump if production)  
8. Schedule code changes: CORS, env config, remove secrets, auth hardening  

---

## 16. Related Project Files

| File | Role |
|------|------|
| `pom.xml` | Spring Boot 4.1.0, Java 17, Maven |
| `src/main/resources/application.properties` | Live DB config (contains secret) |
| `src/main/resources/application-example.properties` | Template |
| `.env.example` | Frontend/server + unused DB vars |
| `src/main/java/.../config/WebCorsConfig.java` | Global CORS |
| `frontend/package.json` | Unified build/start scripts |
| `frontend/server.js` | Production static + API proxy |
| `frontend/scripts/assemble.js` | Merges three SPA builds |
| `frontend/scripts/backend-proxy.js` | API prefix list + proxy |
| `scripts/sample-data.sql` | Demo seed |

---

*End of DEPLOYMENT_READINESS.md — inspection only; no application code changed.*
