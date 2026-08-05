# GutFriendly Frontend API Guide

Maps each frontend page to the REST APIs it needs.  
**Base URL:** `http://localhost:8080`

Store session IDs client-side after login (`vendorId`, `userId`, `inspectorId`). No JWT is issued.

---

## Vendor App

### Register
| API | Method | URL |
|-----|--------|-----|
| Register vendor | POST | `/vendor/register` |

**Body:** `VendorRegisterRequestDTO`  
**On success:** Store `vendorId` from `VendorRegisterResponseDTO`; redirect to login.

---

### Login
| API | Method | URL |
|-----|--------|-----|
| Login | POST | `/vendor/login` |

**On success:** Store `vendorId` from `response.vendor.vendorId`; `response.shops` lists existing shops for shop picker.

---

### Dashboard
| API | Method | URL | When |
|-----|--------|-----|------|
| Full dashboard | GET | `/vendor/{vendorId}/shops/{shopId}/dashboard` | Initial load |
| Summary cards | GET | `/vendor/{vendorId}/shops/{shopId}/dashboard/summary` | Optional split load |
| Order chart | GET | `/vendor/{vendorId}/shops/{shopId}/dashboard/order-overview` | Chart widget |
| Active orders | GET | `/vendor/{vendorId}/shops/{shopId}/dashboard/active-orders` | Orders strip |
| Top items | GET | `/vendor/{vendorId}/shops/{shopId}/dashboard/top-selling-items` | Best sellers |
| Recent reviews | GET | `/vendor/{vendorId}/shops/{shopId}/dashboard/recent-reviews` | Reviews strip |
| Shop status flags | GET | `/vendor/{vendorId}/shops/{shopId}/dashboard/shop-status` | Open/online toggle UI |
| Active order count | GET | `/vendor/{vendorId}/shops/{shopId}/orders/active-count` | Badge |
| Recheck serviceability | POST | `/vendor/{vendorId}/shops/{shopId}/serviceability/recheck` | After address change |

**Gap:** No dedicated inspection-status endpoint — use `dashboard.nextAction` and `pendingRequirements` strings.

---

### Shop (create / list / settings)
| API | Method | URL |
|-----|--------|-----|
| List shops | GET | `/vendor/{vendorId}/shops` |
| Get shop | GET | `/vendor/{vendorId}/shops/{shopId}` |
| Create shop | POST | `/vendor/{vendorId}/shops` |
| Update shop | PUT | `/vendor/{vendorId}/shops/{shopId}` |
| Shop settings | GET | `/vendor/{vendorId}/shops/{shopId}/settings` |
| Update settings | PUT | `/vendor/{vendorId}/shops/{shopId}/settings` |
| Shop rating | GET | `/vendor/{vendorId}/shops/{shopId}/rating` |

---

### Shop Location
| API | Method | URL |
|-----|--------|-----|
| Save location | POST | `/vendor/{vendorId}/shops/{shopId}/location` |

**Body:** `ShopLocationRequestDTO` (`houseNo`, `street`, `city`, `state`, `pincode`)  
**Note:** Pincode validated against backend whitelist; no standalone validate endpoint.

---

### Inspection Booking
| API | Method | URL |
|-----|--------|-----|
| Book inspection | POST | `/vendor/{vendorId}/shops/{shopId}/inspections/book` |

**Body:** `BookInspectionRequestDTO` (`inspectionDate` — must be future)  
**Prerequisites:** Location saved; shop `SERVICEABLE`; no active inspection.

**Gap:** No `GET` inspection list for vendor — poll dashboard `nextAction` after booking.

---

### Menu
| API | Method | URL |
|-----|--------|-----|
| List items | GET | `/vendor/{vendorId}/shops/{shopId}/menu?activeOnly=false` |
| Categories | GET | `/vendor/{vendorId}/shops/{shopId}/menu/categories` |
| Get item | GET | `/vendor/{vendorId}/shops/{shopId}/menu/{itemId}` |
| Create | POST | `/vendor/{vendorId}/shops/{shopId}/menu` |
| Update | PUT | `/vendor/{vendorId}/shops/{shopId}/menu/{itemId}` |
| Toggle active | PATCH | `/vendor/{vendorId}/shops/{shopId}/menu/{itemId}/toggle` |
| Delete | DELETE | `/vendor/{vendorId}/shops/{shopId}/menu/{itemId}` |

---

### Orders
| API | Method | URL |
|-----|--------|-----|
| List orders | GET | `/vendor/{vendorId}/shops/{shopId}/orders?status=active` |
| Filter by status | GET | `/vendor/{vendorId}/shops/{shopId}/orders?status=NEW` |
| Order detail | GET | `/vendor/{vendorId}/shops/{shopId}/orders/{orderId}` |
| Update status | PATCH | `/vendor/{vendorId}/shops/{shopId}/orders/{orderId}/status` |

**Status flow:** `NEW → ACCEPTED → PREPARING → OUT_FOR_DELIVERY → DELIVERED`

---

### Reviews
| API | Method | URL |
|-----|--------|-----|
| List reviews | GET | `/vendor/{vendorId}/shops/{shopId}/reviews?rating=` |
| Review stats | GET | `/vendor/{vendorId}/shops/{shopId}/reviews/stats` |

Read-only — no vendor reply endpoint.

---

### Profile
| API | Method | URL |
|-----|--------|-----|
| Get profile | GET | `/vendor/{vendorId}/settings/profile` |
| Update profile | PUT | `/vendor/{vendorId}/settings/profile` |
| Change password | POST | `/vendor/{vendorId}/settings/change-password` |
| Change phone | POST | `/vendor/{vendorId}/settings/change-phone` |

---

### Payouts (optional page)
| API | Method | URL |
|-----|--------|-----|
| Summary | GET | `/vendor/{vendorId}/shops/{shopId}/payouts/summary` |
| History | GET | `/vendor/{vendorId}/shops/{shopId}/payouts` |

---

## User App

### Register
| API | Method | URL |
|-----|--------|-----|
| Register | POST | `/users/register` |

**Body:** `UserDetails` JSON (`fname`, `lname`, `phoneNo`, `email`, `password`, …)

---

### Login
| API | Method | URL |
|-----|--------|-----|
| Login | POST | `/users/login` |

**On success:** Store `userId`, `fname`, `rewardPoints` from response map.

---

### Home
| API | Method | URL |
|-----|--------|-----|
| Home feed | GET | `/home/user/{userId}` |

Returns `allShops`, `trustedVendors`, `recommendedShops`, `gutFriendlyPicks`, `categories`.

**Alternative granular calls:**
- `GET /shops`
- `GET /shops/trusted-vendors`

---

### Shop Details
| API | Method | URL |
|-----|--------|-----|
| Shop detail | GET | `/shops/{id}` |
| Shop menu | GET | `/foods/shop/{shopId}?page=0&size=12` |
| Review summary | GET | `/reviews/shop/{shopId}/summary` |
| Reviews list | GET | `/reviews/shop/{shopId}?page=0&size=5` |
| Wishlist status | GET | `/wishlist/user/{userId}/shop/{shopId}/status` |
| Add to wishlist | POST | `/wishlist/user/{userId}/shop/{shopId}` |

---

### Food Details
| API | Method | URL |
|-----|--------|-----|
| Food detail | GET | `/foods/{foodId}` |
| Add to cart | POST | `/cart/user/{userId}/items` |

---

### Cart
| API | Method | URL |
|-----|--------|-----|
| View cart | GET | `/cart/user/{userId}` |
| Add item | POST | `/cart/user/{userId}/items` |
| Update qty | PUT | `/cart/user/{userId}/items/{cartItemId}` |
| Remove item | DELETE | `/cart/user/{userId}/items/{cartItemId}` |
| Clear cart | DELETE | `/cart/user/{userId}/clear` |
| Place order | POST | `/orders/user/{userId}` |

**Place order body:** `PlaceOrderDTO` (`deliveryAddress`, `paymentMethod`: `COD` | `ONLINE`)

---

### Wishlist
| API | Method | URL |
|-----|--------|-----|
| List | GET | `/wishlist/user/{userId}` |
| Add | POST | `/wishlist/user/{userId}/shop/{shopId}` |
| Remove | DELETE | `/wishlist/user/{userId}/shop/{shopId}` |
| Check status | GET | `/wishlist/user/{userId}/shop/{shopId}/status` |

---

### Orders
| API | Method | URL |
|-----|--------|-----|
| My orders | GET | `/orders/user/{userId}` |
| Order detail | GET | `/orders/user/{userId}/{orderId}` |
| Cancel | PUT | `/orders/user/{userId}/{orderId}/cancel` |

**Track status:** Poll `GET /orders/user/{userId}/{orderId}` — `orderStatus` field.

---

### Reviews
| API | Method | URL |
|-----|--------|-----|
| Submit review | POST | `/reviews/user/{userId}` |
| Update review | PUT | `/reviews/user/{userId}/{reviewId}` |
| Delete review | DELETE | `/reviews/user/{userId}/{reviewId}` |

**Submit body:** `ReviewRequestDTO` (`orderId`, `rating` 1–5, `comment`, `keywords[]`)  
**Rule:** Order must be `DELIVERED`; one review per order.

---

### Profile
| API | Method | URL |
|-----|--------|-----|
| Get profile | GET | `/users/profile/{id}` |
| Update profile | PUT | `/users/profile/{id}` |
| List addresses | GET | `/users/address/{id}` |
| Add address | POST | `/users/address/{id}` |
| Delete address | DELETE | `/users/{userId}/address/{addressId}` |
| Delete account | DELETE | `/users/{id}` |

---

### Search (global)
| API | Method | URL |
|-----|--------|-----|
| Search shops | GET | `/shops/search?keyword=` |
| Search food | GET | `/foods/search?keyword=` |
| By category | GET | `/shops/category/{category}` |

---

## Admin App

### Dashboard
| API | Method | URL |
|-----|--------|-----|
| Summary cards | GET | `/admin/dashboard/summary` |
| Monthly trends | GET | `/admin/dashboard/monthly-trends` |
| Category performance | GET | `/admin/dashboard/category-performance` |
| Recent activity | GET | `/admin/dashboard/recent-activities` |
| Upcoming inspections | GET | `/admin/dashboard/upcoming-inspections` |

**Note:** `averageGutTrustScore`, `pendingVendorApprovals`, `pendingComplaints`, `expiringCertificates` may return `0` (stubbed).

---

### Shops
| API | Method | URL |
|-----|--------|-----|
| All shops | GET | `/admin/shops?page=0&size=10` |
| By status | GET | `/admin/shops/status/{status}` |
| By availability | GET | `/admin/shops/availability-Status/{status}` |
| Search | GET | `/admin/shops/search?shopName=` |
| Detail | GET | `/admin/shops/{shopId}` |
| Block | PATCH | `/admin/shops/{shopId}/block` |
| Unblock | PATCH | `/admin/shops/{shopId}/unblock` |

---

### Inspections
| API | Method | URL |
|-----|--------|-----|
| All | GET | `/admin/inspections?page=0&size=10` |
| By status | GET | `/admin/inspections/status/{status}` |
| Detail | GET | `/admin/inspections/{inspectionId}` |
| By shop | GET | `/admin/inspections/shop/{shopId}` |
| Assign inspector | PATCH | `/admin/inspections/{inspectionId}/assign/{inspectorId}` |
| Mark under review | PATCH | `/admin/inspections/{inspectionId}/review` |
| Approve | PATCH | `/admin/inspections/{inspectionId}/approve` |
| Reject | PATCH | `/admin/inspections/{inspectionId}/reject` |
| Re-inspection | PATCH | `/admin/inspections/{inspectionId}/reinspection` |
| Test results | GET | `/inspector/inspection/{inspectionId}/test-results` |

**Workflow:** `SCHEDULED → assign → ASSIGNED → (inspector) → REPORT_SUBMITTED → review → UNDER_ADMIN_REVIEW → approve/reject/reinspection`

---

### Inspectors
| API | Method | URL |
|-----|--------|-----|
| Inspections by inspector | GET | `/admin/inspections/inspector/{inspectorId}` |

**Gap:** No `GET /admin/inspectors` list endpoint — inspector IDs must be known out-of-band for assignment UI.

---

### Certificates
**No REST API exists.** `Certificate` entity is read only in dashboard recent-activities. Certificate management page cannot be wired without a new backend endpoint.

---

### Admin Auth
| API | Method | URL |
|-----|--------|-----|
| Register | POST | `/admin/registration` |
| Login | POST | `/admin/login` |

Login is a stub (always succeeds).

---

## Inspector App

### Dashboard
| API | Method | URL |
|-----|--------|-----|
| Assigned inspections | GET | `/inspector/{inspectorId}/inspections?page=0&size=10` |

Use `status` field on each `InspectionResponse` to group by lifecycle stage.

---

### Assigned Inspections
| API | Method | URL |
|-----|--------|-----|
| List | GET | `/inspector/{inspectorId}/inspections` |
| Filter client-side | — | By `status`: `ASSIGNED`, `IN_PROGRESS`, etc. |

---

### Inspection Details
| API | Method | URL |
|-----|--------|-----|
| Detail | GET | `/inspector/inspection/{inspectionId}` |
| Start | PATCH | `/inspector/inspection/{inspectionId}/start` |

---

### Test Results
| API | Method | URL |
|-----|--------|-----|
| Test catalog | GET | `/inspector/tests` |
| List results | GET | `/inspector/inspection/{inspectionId}/test-results` |
| Save result | POST | `/inspector/inspection/{inspectionId}/test-results` |

**Body:** `InspectionTestResultRequest` (`testId`, `sampleType`, `outcome`, `scoreAwarded`, …)

---

### Reports (Submit)
| API | Method | URL |
|-----|--------|-----|
| Submit report | PATCH | `/inspector/inspection/{inspectionId}/submit` |

**Body:** `SubmitInspectionRequest` (`inspectorRemarks`, `recommendation`)  
**Requires:** At least one test result; status `IN_PROGRESS`.

---

## Frontend Integration Checklist

| Check | Status |
|-------|--------|
| CORS configured for vendor (`localhost:5173`) | Yes (vendor controllers only) |
| User/admin CORS | Not configured — add proxy or `@CrossOrigin` |
| Auth tokens | Not implemented — use localStorage for IDs |
| Error handling | Parse `ErrorResponse` for user module; vendor uses same after PASS 9 |
| Pagination | Use Spring `Page` metadata for admin/inspector/foods/reviews |
| Shop picker (vendor) | Use shops from login response or `GET /vendor/{id}/shops` |
| Order status mapping | User sees `OrderStatus`; vendor sees `ShopOrderStatus` (`NEW` = `PLACED`) |
| Payment | `COD` and `ONLINE` enum only — no gateway integration |

---

## Known API Gaps for Frontend

1. **No vendor inspection GET** — use dashboard `nextAction`
2. **No admin inspector list** — hardcode or add endpoint later
3. **No certificate CRUD API**
4. **No inspector login** — pass `inspectorId` manually
5. **Admin login stub** — no real credential check
6. **User register returns string**, not structured DTO
7. **Food search/detail** does not filter by shop verification (ordering does)
