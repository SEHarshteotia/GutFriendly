import {
  Route,
  Routes,
} from "react-router-dom";

import MainLayout from "./layouts/MainLayout";
import UserDashboardLayout from "./layouts/UserDashboardLayout";

/* User pages */
import CartPage from "./pages/CartPage";
import CheckoutPage from "./pages/CheckoutPage";
import HealthHubPage from "./pages/HealthHubPage";
import HomePage from "./pages/HomePage";
import LandingPage from "./pages/LandingPage";
import LoginPage from "./pages/LoginPage";
import NotFoundPage from "./pages/NotFoundPage";
import OrdersPage from "./pages/OrdersPage";
import ProfilePage from "./pages/ProfilePage";
import RegisterPage from "./pages/RegisterPage";
import ReviewPage from "./pages/ReviewPage";
import ShopDetailsPage from "./pages/ShopDetailsPage";
import TrustProcessPage from "./pages/TrustProcessPage";
import WishlistPage from "./pages/WishlistPage";

function App() {
  return (
    <Routes>
      {/* Public and User pages */}
      <Route element={<MainLayout />}>
        <Route
          path="/"
          element={<LandingPage />}
        />

        <Route element={<UserDashboardLayout />}>
          <Route
            path="/home"
            element={<HomePage />}
          />

          <Route
            path="/orders"
            element={<OrdersPage />}
          />

          <Route
            path="/cart"
            element={<CartPage />}
          />

          <Route
            path="/wishlist"
            element={<WishlistPage />}
          />

          <Route
            path="/profile"
            element={<ProfilePage />}
          />

          <Route
            path="/shops/:shopId"
            element={<ShopDetailsPage />}
          />

          <Route
            path="/checkout"
            element={<CheckoutPage />}
          />

          <Route
            path="/reviews/:orderId"
            element={<ReviewPage />}
          />

          <Route
            path="/trust-process"
            element={<TrustProcessPage />}
          />

          <Route
            path="/health-hub"
            element={<HealthHubPage />}
          />
        </Route>
      </Route>

      {/* User authentication */}
      <Route
        path="/login"
        element={<LoginPage />}
      />

      <Route
        path="/register"
        element={<RegisterPage />}
      />

      <Route
        path="*"
        element={<NotFoundPage />}
      />
    </Routes>
  );
}

export default App;
