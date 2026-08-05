import { apiRequest } from "./api";

export function getProfile(userId) {
  return apiRequest(`/users/profile/${userId}`);
}

export function updateProfile(userId, profileData) {
  return apiRequest(`/users/profile/${userId}`, {
    method: "PUT",
    body: JSON.stringify(profileData),
  });
}

export function getAddresses(userId) {
  return apiRequest(`/users/address/${userId}`);
}

export function addAddress(userId, addressData) {
  return apiRequest(`/users/address/${userId}`, {
    method: "POST",
    body: JSON.stringify(addressData),
  });
}

export function deleteAddress(userId, addressId) {
  return apiRequest(
    `/users/${userId}/address/${addressId}`,
    {
      method: "DELETE",
    }
  );
}

export function deleteAccount(userId) {
  return apiRequest(`/users/${userId}`, {
    method: "DELETE",
  });
}