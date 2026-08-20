// Where the admin and inspector portals keep their signed in state.
//
// The token is issued by POST /admin/login or POST /inspector/login. The role
// stored next to it only decides which menu to draw - the backend decides what
// the token is actually allowed to reach, so editing it by hand achieves
// nothing.

const TOKEN_KEY = "staffToken";

export function saveStaffSession(role, token) {
    localStorage.setItem("role", role);

    if (token) {
        localStorage.setItem(TOKEN_KEY, token);
    }
}

export function getStaffToken() {
    return localStorage.getItem(TOKEN_KEY);
}

export function clearStaffSession() {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem("role");
    localStorage.removeItem("admin");
    localStorage.removeItem("inspector");
    localStorage.removeItem("inspectorId");
}
