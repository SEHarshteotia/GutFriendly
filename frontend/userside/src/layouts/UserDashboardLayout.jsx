import {
  Navigate,
  Outlet,
} from "react-router-dom";

import Sidebar from "../components/Sidebar";

function UserDashboardLayout() {
  const userId = localStorage.getItem("userId");

  if (!userId) {
    return (
      <Navigate
        to="/login"
        replace
      />
    );
  }

  return (
    <div className="dashboard-layout">
      <Sidebar />

      <main className="dashboard-content">
        <Outlet />
      </main>
    </div>
  );
}

export default UserDashboardLayout;