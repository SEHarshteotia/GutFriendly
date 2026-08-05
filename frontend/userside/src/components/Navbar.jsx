import {
  ChevronDown,
  ClipboardCheck,
  Heart,
  LogOut,
  Package,
  ShieldCheck,
  ShoppingCart,
  Store,
  UserRound,
  UsersRound,
  X,
} from "lucide-react";

import {
  useEffect,
  useRef,
  useState,
} from "react";

import {
  Link,
  NavLink,
  useLocation,
  useNavigate,
} from "react-router-dom";

function Navbar() {
  const navigate = useNavigate();
  const location = useLocation();

  const profileDropdownRef = useRef(null);
  const roleDropdownRef = useRef(null);

  const [isLoggedIn, setIsLoggedIn] =
    useState(false);

  const [userName, setUserName] =
    useState("GutFriendly User");

  const [showProfileMenu, setShowProfileMenu] =
    useState(false);

  const [showRoleMenu, setShowRoleMenu] =
    useState(false);

  const [showLogoutModal, setShowLogoutModal] =
    useState(false);

  useEffect(() => {
    const storedUserId =
      localStorage.getItem("userId");

    const storedUserName =
      localStorage.getItem("userName");

    setIsLoggedIn(Boolean(storedUserId));

    setUserName(
      storedUserName || "GutFriendly User"
    );

    setShowProfileMenu(false);
    setShowRoleMenu(false);
  }, [location.pathname]);

  useEffect(() => {
    function handleOutsideClick(event) {
      if (
        profileDropdownRef.current &&
        !profileDropdownRef.current.contains(
          event.target
        )
      ) {
        setShowProfileMenu(false);
      }

      if (
        roleDropdownRef.current &&
        !roleDropdownRef.current.contains(
          event.target
        )
      ) {
        setShowRoleMenu(false);
      }
    }

    document.addEventListener(
      "mousedown",
      handleOutsideClick
    );

    return () => {
      document.removeEventListener(
        "mousedown",
        handleOutsideClick
      );
    };
  }, []);

  function handleLogout() {
    localStorage.removeItem("userId");
    localStorage.removeItem("userName");
    localStorage.removeItem("rewardPoints");

    setIsLoggedIn(false);
    setShowProfileMenu(false);
    setShowLogoutModal(false);

    navigate("/login");
  }

  function handleVendorClick(event) {
    event.preventDefault();
    setShowRoleMenu(false);

    window.alert(
      "Vendor portal is currently being integrated."
    );
  }

  const firstName =
    userName?.split(" ")[0] || "User";

  const avatarLetter =
    firstName.charAt(0).toUpperCase();

  return (
    <>
      <header className="navbar">
        <div className="navbar-container">
          <Link
            to={isLoggedIn ? "/home" : "/"}
            className="brand"
          >
            <span className="brand-icon">
              G
            </span>

            GutFriendly
          </Link>

          <nav className="nav-links">
            <NavLink
              to={isLoggedIn ? "/home" : "/"}
            >
              Discover
            </NavLink>

            <NavLink to="/trust-process">
              Trust Process
            </NavLink>

            <NavLink to="/health-hub">
              Health Hub
            </NavLink>
          </nav>

          <div className="nav-actions">
            {isLoggedIn && (
              <>
                <Link
                  to="/wishlist"
                  className="nav-icon-button"
                  title="Wishlist"
                >
                  <Heart size={21} />
                </Link>

                <Link
                  to="/cart"
                  className="nav-icon-button"
                  title="Cart"
                >
                  <ShoppingCart size={21} />
                </Link>
              </>
            )}

            {!isLoggedIn ? (
              <div
                className="navbar-role-area"
                ref={roleDropdownRef}
              >
                <button
                  type="button"
                  className={
                    showRoleMenu
                      ? "nav-login-button active"
                      : "nav-login-button"
                  }
                  onClick={() =>
                    setShowRoleMenu(
                      (currentValue) =>
                        !currentValue
                    )
                  }
                  aria-expanded={showRoleMenu}
                  aria-haspopup="menu"
                >
                  <UserRound size={18} />
                  Sign in

                  <ChevronDown
                    size={16}
                    className={
                      showRoleMenu
                        ? "navbar-chevron open"
                        : "navbar-chevron"
                    }
                  />
                </button>

                {showRoleMenu && (
                  <div
                    className="navbar-role-menu"
                    role="menu"
                  >
                    <div className="navbar-role-heading">
                      <strong>
                        Choose your portal
                      </strong>

                      <span>
                        Sign in according to your role
                      </span>
                    </div>

                    <div className="navbar-menu-divider" />

                    <Link
                      to="/login"
                      className="navbar-role-link"
                      onClick={() =>
                        setShowRoleMenu(false)
                      }
                    >
                      <span className="navbar-role-link-icon">
                        <UsersRound size={19} />
                      </span>

                      <div>
                        <strong>Customer</strong>
                        <span>
                          Order and review food
                        </span>
                      </div>
                    </Link>

                    <Link
                      to="/"
                      className="navbar-role-link"
                      onClick={handleVendorClick}
                    >
                      <span className="navbar-role-link-icon">
                        <Store size={19} />
                      </span>

                      <div>
                        <strong>Vendor</strong>
                        <span>
                          Manage shop and orders
                        </span>
                      </div>
                    </Link>

                    <Link
                      to="/inspector/dashboard"
                      className="navbar-role-link"
                      onClick={() =>
                        setShowRoleMenu(false)
                      }
                    >
                      <span className="navbar-role-link-icon">
                        <ClipboardCheck size={19} />
                      </span>

                      <div>
                        <strong>Inspector</strong>
                        <span>
                          Complete inspections
                        </span>
                      </div>
                    </Link>

                    <Link
                      to="/admin/dashboard"
                      className="navbar-role-link"
                      onClick={() =>
                        setShowRoleMenu(false)
                      }
                    >
                      <span className="navbar-role-link-icon">
                        <ShieldCheck size={19} />
                      </span>

                      <div>
                        <strong>Admin</strong>
                        <span>
                          Manage the platform
                        </span>
                      </div>
                    </Link>
                  </div>
                )}
              </div>
            ) : (
              <div
                className="navbar-profile-area"
                ref={profileDropdownRef}
              >
                <button
                  type="button"
                  className={
                    showProfileMenu
                      ? "navbar-profile-button active"
                      : "navbar-profile-button"
                  }
                  onClick={() =>
                    setShowProfileMenu(
                      (currentValue) =>
                        !currentValue
                    )
                  }
                  aria-expanded={showProfileMenu}
                  aria-haspopup="menu"
                >
                  <span className="navbar-avatar">
                    {avatarLetter}
                  </span>

                  <span className="navbar-user-name">
                    {firstName}
                  </span>

                  <ChevronDown
                    size={17}
                    className={
                      showProfileMenu
                        ? "navbar-chevron open"
                        : "navbar-chevron"
                    }
                  />
                </button>

                {showProfileMenu && (
                  <div
                    className="navbar-profile-menu"
                    role="menu"
                  >
                    <div className="navbar-profile-summary">
                      <span className="navbar-menu-avatar">
                        {avatarLetter}
                      </span>

                      <div>
                        <strong>{userName}</strong>

                        <span>
                          Customer account
                        </span>
                      </div>
                    </div>

                    <div className="navbar-menu-divider" />

                    <Link
                      to="/profile"
                      className="navbar-menu-link"
                      onClick={() =>
                        setShowProfileMenu(false)
                      }
                    >
                      <UserRound size={18} />
                      My profile
                    </Link>

                    <Link
                      to="/orders"
                      className="navbar-menu-link"
                      onClick={() =>
                        setShowProfileMenu(false)
                      }
                    >
                      <Package size={18} />
                      My orders
                    </Link>

                    <Link
                      to="/wishlist"
                      className="navbar-menu-link"
                      onClick={() =>
                        setShowProfileMenu(false)
                      }
                    >
                      <Heart size={18} />
                      Wishlist
                    </Link>

                    <Link
                      to="/cart"
                      className="navbar-menu-link"
                      onClick={() =>
                        setShowProfileMenu(false)
                      }
                    >
                      <ShoppingCart size={18} />
                      Cart
                    </Link>

                    <div className="navbar-menu-divider" />

                    <button
                      type="button"
                      className="navbar-menu-logout"
                      onClick={() => {
                        setShowProfileMenu(false);
                        setShowLogoutModal(true);
                      }}
                    >
                      <LogOut size={18} />
                      Logout
                    </button>
                  </div>
                )}
              </div>
            )}
          </div>
        </div>
      </header>

      {showLogoutModal && (
        <div className="logout-modal-backdrop">
          <div
            className="logout-modal"
            role="dialog"
            aria-modal="true"
            aria-labelledby="navbar-logout-title"
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

            <h2 id="navbar-logout-title">
              Goodbye {firstName} 👋
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

export default Navbar;