import {
  ArrowRight,
  BadgeCheck,
  ClipboardCheck,
  LogIn,
  Search,
  ShieldCheck,
  Star,
  Store,
  UserPlus,
  UsersRound,
} from "lucide-react";

import { Link } from "react-router-dom";

import {
  ADMIN_LOGIN_URL,
  INSPECTOR_LOGIN_URL,
  VENDOR_LOGIN_URL,
  VENDOR_REGISTER_URL,
} from "../utils/constants";

const rolePortals = [
  {
    role: "Customer",
    icon: UsersRound,
    description:
      "Discover trusted food establishments, place orders and share verified reviews.",
    loginPath: "/login",
    loginText: "Customer login",
    registerPath: "/register",
    registerText: "Create account",
  },
  {
    role: "Vendor",
    icon: Store,
    description:
      "Register your food business, manage your menu and process customer orders.",
    loginPath: VENDOR_LOGIN_URL,
    loginText: "Vendor portal",
    registerPath: VENDOR_REGISTER_URL,
    registerText: "Register business",
  },
  {
    role: "Inspector",
    icon: ClipboardCheck,
    description:
      "View assigned establishments, complete checklists and submit inspection reports.",
    loginPath: INSPECTOR_LOGIN_URL,
    loginText: "Inspector portal",
  },
  {
    role: "Admin",
    icon: ShieldCheck,
    description:
      "Approve vendors, assign inspectors and manage GutFriendly operations.",
    loginPath: ADMIN_LOGIN_URL,
    loginText: "Admin portal",
  },
];

function LandingPage() {
  return (
    <>
      <section className="hero-section">
        <div className="hero-container">
          <div className="hero-content">
            <div className="hero-label">
              <ShieldCheck size={17} />
              Food safety and trust, simplified
            </div>

            <h1 className="hero-title">
              Eat with confidence.
              <span> Trust every bite.</span>
            </h1>

            <p className="hero-text">
              Discover restaurants and food vendors using
              transparent hygiene inspections, verified
              customer feedback and the GutTrust Score.
            </p>

            <div className="hero-actions">
              <Link
                to="/login"
                className="primary-button"
              >
                Explore trusted shops
                <ArrowRight size={19} />
              </Link>

              <a
                href="#role-portals"
                className="secondary-button"
              >
                <LogIn size={19} />
                Choose your portal
              </a>
            </div>

            <div className="hero-features">
              <div>
                <BadgeCheck size={20} />
                <span>Verified vendors</span>
              </div>

              <div>
                <ShieldCheck size={20} />
                <span>Transparent scores</span>
              </div>

              <div>
                <Star size={20} />
                <span>Rewarding reviews</span>
              </div>
            </div>
          </div>

          <div className="hero-visual">
            <div className="food-card-large">
              <div className="food-image-placeholder">
                <span>🥗</span>
              </div>

              <div className="floating-trust-card">
                <small>GUTTRUST™</small>

                <strong>4.7</strong>

                <span>
                  <Star
                    size={14}
                    fill="currentColor"
                  />
                  Excellent
                </span>
              </div>

              <div className="floating-search-result">
                <Search size={17} />
                120+ trusted food choices
              </div>
            </div>
          </div>
        </div>
      </section>

      <section
        id="role-portals"
        className="role-selection-section"
      >
        <div className="role-selection-heading">
          <p className="section-eyebrow">
            One platform, four roles
          </p>

          <h2>Choose your GutFriendly portal</h2>

          <p>
            Access the features and responsibilities
            designed specifically for your role.
          </p>
        </div>

        <div className="role-card-grid">
          {rolePortals.map((portal) => {
            const Icon = portal.icon;

            return (
              <article
                className="role-access-card"
                key={portal.role}
              >
                <div className="role-access-icon">
                  <Icon size={27} />
                </div>

                <h3>{portal.role}</h3>

                <p>{portal.description}</p>

                <div className="role-access-actions">
                  <a
                    href={portal.loginPath}
                    className="role-login-link"
                  >
                    <LogIn size={17} />
                    {portal.loginText}
                  </a>

                  {portal.registerPath && (
                    <a
                      href={portal.registerPath}
                      className="role-register-link"
                    >
                      <UserPlus size={17} />
                      {portal.registerText}
                    </a>
                  )}
                </div>
              </article>
            );
          })}
        </div>

        <div className="role-security-note">
          <ShieldCheck size={21} />

          <p>
            Inspector and administrator accounts are
            provisioned internally and cannot be publicly
            registered.
          </p>
        </div>
      </section>

      <section
        id="trust-process"
        className="intro-section"
      >
        <p className="section-eyebrow">
          Built around transparency
        </p>

        <h2>
          Safer food choices begin with better information.
        </h2>

        <p>
          GutFriendly combines inspection-based hygiene
          information with real customer feedback, helping
          users choose food with greater confidence.
        </p>
      </section>
    </>
  );
}

export default LandingPage;
