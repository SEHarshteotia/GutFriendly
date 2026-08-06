const userPortalBase =
  import.meta.env.VITE_USER_PORTAL_URL ?? 'http://localhost:5174'

export const USER_LANDING_URL = `${userPortalBase}/`
