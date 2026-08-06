const vendorPortalBase =
  import.meta.env.VITE_VENDOR_PORTAL_URL ?? "http://localhost:5173";

const adminInspectorPortalBase =
  import.meta.env.VITE_ADMIN_INSPECTOR_PORTAL_URL ??
  "http://localhost:5175";

const userPortalBase =
  import.meta.env.VITE_USER_PORTAL_URL ?? "http://localhost:5174";

export const USER_LANDING_URL = `${userPortalBase}/`;
export const VENDOR_LOGIN_URL = `${vendorPortalBase}/login`;
export const INSPECTOR_LOGIN_URL = `${adminInspectorPortalBase}/login`;
export const ADMIN_LOGIN_URL = `${adminInspectorPortalBase}/login`;
