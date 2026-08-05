import {
  Gift,
  Heart,
  Home,
  LogOut,
  Package,
  ShoppingCart,
  UserRound,
  X,
} from "lucide-react";

import { useState } from "react";

import {
  NavLink,
  useNavigate,
} from "react-router-dom";

function Sidebar() {
  const navigate = useNavigate();

  const [showLogoutModal, setShowLogoutModal] =
    useState(false);

  const userName =
    localStorage.getItem("userName") ||
    "GutFriendly User";

  const rewardPoints =
    localStorage.getItem("rewardPoints") || 0;

  function handleLogout() {
    localStorage.removeItem("userId");
    localStorage.removeItem("userName");
    localStorage.removeItem("rewardPoints");

    setShowLogoutModal(false);

    navigate("/login");
  }

  const sidebarLinks = [
    {
      to: "/home",
      label: "Home",
      icon: Home,
    },
    {
      to: "/orders",
      label: "My Orders",
      icon: Package,
    },
    {
      to: "/cart",
      label: "Cart",
      icon: ShoppingCart,
    },
    {
      to: "/wishlist",
      label: "Wishlist",
      icon: Heart,
    },
    {
      to: "/profile",
      label: "Profile",
      icon: UserRound,
    },
  ];

  return (
    <>
      <aside className="user-sidebar">
        <div className="sidebar-user-card">
          <div className="sidebar-avatar">
            {userName.charAt(0).toUpperCase()}
          </div>

          <div className="sidebar-user-details">
            <span>Welcome</span>
            <strong>{userName}</strong>
          </div>
        </div>

        <div className="sidebar-reward-card">
          <div className="sidebar-reward-icon">
            <Gift size={20} />
          </div>

          <div>
            <span>Reward balance</span>
            <strong>{rewardPoints} points</strong>
          </div>
        </div>

        <nav className="sidebar-navigation">
          {sidebarLinks.map((link) => {
            const Icon = link.icon;

            return (
              <NavLink
                key={link.to}
                to={link.to}
                className={({ isActive }) =>
                  isActive
                    ? "sidebar-link active"
                    : "sidebar-link"
                }
              >
                <Icon size={20} />
                <span>{link.label}</span>
              </NavLink>
            );
          })}
        </nav>

        <button
          type="button"
          className="sidebar-logout-button"
          onClick={() =>
            setShowLogoutModal(true)
          }
        >
          <LogOut size={20} />
          <span>Logout</span>
        </button>
      </aside>

      {showLogoutModal && (
        <div className="logout-modal-backdrop">
          <div
            className="logout-modal"
            role="dialog"
            aria-modal="true"
            aria-labelledby="logout-title"
          >
            <button
              type="button"
              className="logout-modal-close"
              onClick={() =>
                setShowLogoutModal(false)
              }
              aria-label="Close logout popup"
            >
              <X size={20} />
            </button>

            <div className="logout-modal-icon">
              <LogOut size={27} />
            </div>

            <p className="home-eyebrow">
              Logout
            </p>

            <h2 id="logout-title">
              Goodbye {userName} 👋
            </h2>

            <p className="logout-modal-message">
              We&apos;ll keep your cart and wishlist
              safe.
            </p>

            <p className="logout-modal-question">
              Are you sure you want to logout?
            </p>

            <div className="logout-modal-actions">
              <button
                type="button"
                className="logout-cancel-button"
                onClick={() =>
                  setShowLogoutModal(false)
                }
              >
                Cancel
              </button>

              <button
                type="button"
                className="logout-confirm-button"
                onClick={handleLogout}
              >
                <LogOut size={18} />
                Logout
              </button>
            </div>
          </div>
        </div>
      )}
    </>
  );
}

export default Sidebar;