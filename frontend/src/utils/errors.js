import { ApiError } from '../api/client'

const CONTEXT_FALLBACKS = {
  login: 'Sign in failed. Please try again.',
  register: 'Registration failed. Please try again.',
  profile: 'Could not update your profile.',
  password: 'Could not change your password.',
  phone: 'Could not update your phone number.',
  shop: 'Could not create the shop.',
  store: 'Could not save store settings.',
  menu: 'Menu action failed.',
  orders: 'Order action failed.',
  reviews: 'Could not post your reply.',
  generic: 'Something went wrong. Please try again.',
}

const LOGIN_STATUS_MESSAGES = {
  401: 'Invalid phone number or password.',
  400: 'Please enter a valid 10-digit phone number.',
}

const REGISTER_STATUS_MESSAGES = {
  400: 'Please fill in all required fields correctly.',
  409: 'An account with this phone or email already exists.',
}

/**
 * Returns a user-friendly error message from API failures, network errors, or unknown throws.
 */
export function getErrorMessage(err, context = 'generic') {
  if (err instanceof ApiError) {
    if (err.message && !isGenericHttpLabel(err.message)) {
      return err.message
    }

    if (context === 'login' && LOGIN_STATUS_MESSAGES[err.status]) {
      return LOGIN_STATUS_MESSAGES[err.status]
    }

    if (context === 'register' && REGISTER_STATUS_MESSAGES[err.status]) {
      return REGISTER_STATUS_MESSAGES[err.status]
    }

    if (err.status === 401 && context === 'password') {
      return 'Current password is incorrect.'
    }

    if (err.status === 409 && context === 'phone') {
      return 'This phone number is already registered to another account.'
    }

    if (err.status === 0) {
      return err.message
    }

    return CONTEXT_FALLBACKS[context]
  }

  if (err instanceof Error && err.message) {
    return err.message
  }

  return CONTEXT_FALLBACKS[context]
}

function isGenericHttpLabel(message) {
  return ['Unauthorized', 'Bad Request', 'Not Found', 'Conflict', 'Internal Server Error'].includes(
    message,
  )
}
