const vendorPortalBase =
  import.meta.env.VITE_VENDOR_PORTAL_URL ?? "/vendor-portal";

const adminInspectorPortalBase =
  import.meta.env.VITE_ADMIN_INSPECTOR_PORTAL_URL ?? "/staff-portal";

const userPortalBase = import.meta.env.VITE_USER_PORTAL_URL ?? "";

export const USER_LANDING_URL = `${userPortalBase}/`;
export const VENDOR_LOGIN_URL = `${vendorPortalBase}/login`;
export const VENDOR_REGISTER_URL = `${vendorPortalBase}/register`;
export const INSPECTOR_LOGIN_URL = `${adminInspectorPortalBase}/login`;
export const ADMIN_LOGIN_URL = `${adminInspectorPortalBase}/login`;
