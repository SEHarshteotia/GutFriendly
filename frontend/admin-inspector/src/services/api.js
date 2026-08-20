import axios from "axios";

import { clearStaffSession, getStaffToken } from "./session";

const API_BASE_URL =
    import.meta.env.VITE_API_BASE_URL ?? "";

const api = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        "Content-Type": "application/json",
    },
});

// Every /admin and /inspector call is rejected by the backend without this
// header. Attaching it here means no individual service has to remember to.
api.interceptors.request.use(
    (config) => {
        const token = getStaffToken();

        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }

        return config;
    },
    (error) => Promise.reject(error)
);

api.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error?.response?.status === 401) {
            // Expired or forged token: drop it and ask for a fresh login.
            clearStaffSession();

            if (window.location.pathname !== "/") {
                window.location.replace("/");
            }
        }

        return Promise.reject(error);
    }
);

export default api;
