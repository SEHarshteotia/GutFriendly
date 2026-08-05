export function formatCurrency(amount) {
  return new Intl.NumberFormat('en-IN', {
    style: 'currency',
    currency: 'INR',
    maximumFractionDigits: 0,
  }).format(amount)
}

export function formatTimeAgo(minutes) {
  if (minutes < 1) return 'Just now'
  if (minutes < 60) return `${minutes}m ago`
  const hours = Math.floor(minutes / 60)
  if (hours < 24) return `${hours}h ago`
  return `${Math.floor(hours / 24)}d ago`
}

export function greeting() {
  const hour = new Date().getHours()
  if (hour < 12) return 'Good morning'
  if (hour < 17) return 'Good afternoon'
  return 'Good evening'
}

export function minutesSince(isoDateTime) {
  if (!isoDateTime) return 0
  const then = new Date(isoDateTime)
  if (Number.isNaN(then.getTime())) return 0
  return Math.max(0, Math.floor((Date.now() - then.getTime()) / 60_000))
}

const ORDER_STATUS_COLORS = {
  NEW: 'bg-blue-100 text-blue-800',
  ACCEPTED: 'bg-indigo-100 text-indigo-800',
  PREPARING: 'bg-orange-100 text-orange-800',
  OUT_FOR_DELIVERY: 'bg-green-100 text-green-800',
  DELIVERED: 'bg-gray-100 text-gray-600',
  CANCELLED: 'bg-red-100 text-red-800',
}

export function orderStatusClass(status) {
  return ORDER_STATUS_COLORS[status] ?? 'bg-gray-100 text-gray-600'
}

export function renderStars(rating) {
  return '★'.repeat(Math.round(rating)) + '☆'.repeat(5 - Math.round(rating))
}

export function formatMenuCategory(value) {
  if (!value) return ''
  return value
    .split('_')
    .map((word) => word.charAt(0) + word.slice(1).toLowerCase())
    .join(' ')
}
