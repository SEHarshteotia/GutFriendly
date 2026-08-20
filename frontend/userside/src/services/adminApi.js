import axios from "axios";
import { handleUnauthorized } from "./session";

const adminApi = axios.create({
  baseURL:
    import.meta.env.VITE_API_BASE_URL ?? "",

  headers: {
    "Content-Type": "application/json",
  },
});

// Signed in callers must present the token issued at login.
adminApi.interceptors.request.use(
  (config) => {
    const token =
      localStorage.getItem("token");

    if (token) {
      config.headers.Authorization =
        `Bearer ${token}`;
    }

    return config;
  },
  (error) => Promise.reject(error)
);

adminApi.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error?.response?.status === 401) {
      handleUnauthorized();
    }

    return Promise.reject(error);
  }
);

export default adminApi;
