// One place that knows how the signed in customer is remembered.
//
// The token is issued by POST /users/login and must ride along on every
// protected call; without it the API answers 401. Everything that touches
// localStorage for auth purposes goes through here so a future change (for
// example moving to a cookie) is a single edit.

const TOKEN_KEY = "token";

export function saveSession({ token, userId, fname, rewardPoints }) {
  if (token) {
    localStorage.setItem(TOKEN_KEY, token);
  }

  if (userId !== undefined && userId !== null) {
    localStorage.setItem("userId", String(userId));
  }

  if (fname !== undefined) {
    localStorage.setItem("userName", fname);
  }

  if (rewardPoints !== undefined && rewardPoints !== null) {
    localStorage.setItem("rewardPoints", String(rewardPoints));
  }
}

export function getToken() {
  return localStorage.getItem(TOKEN_KEY);
}

export function authHeader() {
  const token = getToken();

  return token ? { Authorization: `Bearer ${token}` } : {};
}

export function clearSession() {
  localStorage.removeItem(TOKEN_KEY);
  localStorage.removeItem("userId");
  localStorage.removeItem("userName");
  localStorage.removeItem("rewardPoints");
}

/**
 * Called when the API rejects our token. Sends the visitor back to the login
 * screen instead of leaving them on a page that silently fails to load.
 */
export function handleUnauthorized() {
  clearSession();

  if (!window.location.pathname.startsWith("/login")) {
    window.location.replace("/login");
  }
}
