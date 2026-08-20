import { authHeader, handleUnauthorized } from "./session";

const API_BASE_URL =
  import.meta.env.VITE_API_BASE_URL ?? "";

export async function apiRequest(
  endpoint,
  options = {}
) {
  const response = await fetch(
    `${API_BASE_URL}${endpoint}`,
    {
      ...options,
      headers: {
        "Content-Type": "application/json",
        ...authHeader(),
        ...options.headers,
      },
    }
  );

  const contentType =
    response.headers.get("content-type");

  const data = contentType?.includes(
    "application/json"
  )
    ? await response.json()
    : await response.text();

  if (!response.ok) {
    if (response.status === 401) {
      // The token is missing, expired or tampered with: there is nothing this
      // page can do except send the visitor back to the login screen.
      handleUnauthorized();
    }

    let message = "Something went wrong";

    if (typeof data === "object" && data !== null) {
      message =
        data.message ||
        data.error ||
        (Array.isArray(data.errors) && data.errors[0]) ||
        message;
    } else if (typeof data === "string" && data.trim()) {
      message = data;
    }

    throw new Error(message);
  }

  return data;
}
