import { clearVendorToken, getVendorToken } from './session'

const BASE =
  import.meta.env.VITE_API_BASE_URL
    ? `${import.meta.env.VITE_API_BASE_URL}/vendor`
    : '/vendor'

export class ApiError extends Error {
  constructor(message, status, title) {
    super(message)
    this.status = status
    this.title = title
  }
}

function parseErrorBody(body, status) {
  const message =
    body.message?.trim() ||
    body.detail?.trim() ||
    (typeof body.error === 'string' &&
    body.error !== 'Unauthorized' &&
    body.error !== 'Bad Request' &&
    body.error !== 'Not Found' &&
    body.error !== 'Conflict' &&
    body.error !== 'Internal Server Error'
      ? body.error
      : '') ||
    statusTextFallback(status)

  return new ApiError(message, status, body.title)
}

function statusTextFallback(status) {
  switch (status) {
    case 400:
      return 'Invalid request. Please check your input.'
    case 401:
      return 'Authentication failed. Please sign in again.'
    case 403:
      return 'You do not have permission to perform this action.'
    case 404:
      return 'The requested resource was not found.'
    case 409:
      return 'This action conflicts with existing data.'
    case 500:
      return 'Something went wrong on the server. Please try again later.'
    default:
      return `Request failed (${status}).`
  }
}

export async function request(url, options) {
  let res

  try {
    const token = getVendorToken()

    res = await fetch(`${BASE}${url}`, {
      headers: {
        'Content-Type': 'application/json',
        // Without this every /vendor call comes back 401.
        ...(token ? { Authorization: `Bearer ${token}` } : {}),
        ...options?.headers,
      },
      ...options,
    })
  } catch {
    throw new ApiError(
      'Unable to reach the server. Make sure the backend is running at http://localhost:8080.',
      0,
    )
  }

  if (!res.ok) {
    if (res.status === 401) {
      // Expired or invalid token: clear it so the app falls back to the login
      // screen instead of looping on failed requests.
      clearVendorToken()
      localStorage.removeItem('gutfriendly_vendor_auth')
    }

    const err = await res.json().catch(() => ({}))
    throw parseErrorBody(err, res.status)
  }

  if (res.status === 204) {
    return undefined
  }

  return res.json()
}

export function shopPath(vendorId, shopId, resource) {
  return `/${vendorId}/shops/${shopId}/${resource}`
}
