import { apiRequest } from "./api";

export function registerUser(userData) {
  return apiRequest("/users/register", {
    method: "POST",
    body: JSON.stringify(userData),
  });
}

export function loginUser(loginData) {
  return apiRequest("/users/login", {
    method: "POST",
    body: JSON.stringify(loginData),
  });
}