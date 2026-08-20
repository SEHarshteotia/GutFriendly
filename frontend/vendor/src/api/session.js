// Token storage for the vendor portal.
//
// Kept separate from AuthContext because api/client.js needs the token during
// a request, which is outside React's render cycle.

const TOKEN_KEY = 'gutfriendly_vendor_token'

export function saveVendorToken(token) {
  if (token) {
    localStorage.setItem(TOKEN_KEY, token)
  }
}

export function getVendorToken() {
  return localStorage.getItem(TOKEN_KEY)
}

export function clearVendorToken() {
  localStorage.removeItem(TOKEN_KEY)
}
