import { apiRequest } from "./api";

export function getHomePage(userId) {
  return apiRequest(`/home/user/${userId}`);
}