import {
  ArrowLeft,
  Building2,
  ClipboardCheck,
  ShieldCheck,
  Store,
  UserRound,
} from "lucide-react";

import { Link, useParams } from "react-router-dom";

const portalInformation = {
  vendor: {
    name: "Vendor",
    icon: Store,
    eyebrow: "Business portal",
    description:
      "Register your food business, manage your shop, menu and customer orders.",
    loginText: "Vendor login",
    loginPath: "/vendor/login",
    registerText: "Register business",
    registerPath: "/vendor/register",
    registrationAvailable: true,
  },

  inspector: {
    name: "Inspector",
    icon: ClipboardCheck,
    eyebrow: "Inspection portal",
    description:
      "View assigned establishments, complete hygiene inspections and submit reports.",
    loginText: "Inspector login",
    loginPath: "/inspector/login",
    registrationAvailable: false,
  },

  admin: {
    name: "Admin",
    icon: ShieldCheck,
    eyebrow: "Administration portal",
    description:
      "Manage vendors, inspector assignments, certifications and platform operations.",
    loginText: "Admin login",
    loginPath: "/admin/login",
    registrationAvailable: false,
  },
};

function RolePortalPage() {
  const { role } = useParams();

  const portal = portalInformation[role];

  if (!portal) {
    return (
      <div className="page-message">
        <h2>Portal not found</h2>

        <Link to="/" className="primary-button">
          Return home
        </Link>
      </div>
    );
  }

  const Icon = portal.icon;

  return (
    <div className="role-portal-page">
      <div className="role-portal-card">
        <Link
          to="/"
          className="role-portal-back"
        >
          <ArrowLeft size={18} />
          Back to GutFriendly
        </Link>

        <div className="role-portal-icon">
          <Icon size={34} />
        </div>

        <p className="home-eyebrow">
          {portal.eyebrow}
        </p>

        <h1>{portal.name} portal</h1>

        <p className="role-portal-description">
          {portal.description}
        </p>

        <div className="role-portal-actions">
          <Link
            to={portal.loginPath}
            className="primary-button"
          >
            <UserRound size={19} />
            {portal.loginText}
          </Link>

          {portal.registrationAvailable && (
            <Link
              to={portal.registerPath}
              className="secondary-button"
            >
              <Building2 size={19} />
              {portal.registerText}
            </Link>
          )}
        </div>

        {!portal.registrationAvailable && (
          <div className="restricted-registration-note">
            <ShieldCheck size={18} />

            <span>
              {portal.name} accounts are created and
              authorised by the GutFriendly administration.
            </span>
          </div>
        )}
      </div>
    </div>
  );
}

export default RolePortalPage;