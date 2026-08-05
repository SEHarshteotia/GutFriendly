import { useEffect, useState } from 'react'
import { X } from 'lucide-react'
import { vendorApi } from '../api/vendorApi'
import { useAuth } from '../context/AuthContext'
import { Alert } from './Alert'
import { getErrorMessage } from '../utils/errors'

export function CreateShopModal({ open, required = false, onClose }) {
  const { vendor, addShop } = useAuth()
  const [storeName, setStoreName] = useState('')
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  useEffect(() => {
    if (open) {
      setStoreName('')
      setError('')
    }
  }, [open])

  if (!open || !vendor) return null

  function handleClose() {
    if (required) return
    onClose?.()
  }

  async function handleSubmit(e) {
    e.preventDefault()
    if (!storeName.trim()) {
      setError('Store name is required')
      return
    }
    setLoading(true)
    setError('')
    try {
      const shop = await vendorApi.createShop(vendor.vendorId, {
        shopName: storeName.trim(),
        openTime: '09:00:00',
        estimatedPrepTimeMinutes: 15,
      })
      addShop(shop)
      if (!required) {
        onClose?.()
      }
    } catch (err) {
      setError(getErrorMessage(err, 'shop'))
    } finally {
      setLoading(false)
    }
  }

  return (
    <div
      className="fixed inset-0 z-50 flex items-center justify-center bg-black/50 p-4"
      onClick={required ? undefined : handleClose}
    >
      <div
        className="relative w-full max-w-md rounded-2xl bg-white p-6 shadow-xl"
        onClick={(e) => e.stopPropagation()}
      >
        {!required && (
          <button
            type="button"
            onClick={handleClose}
            className="absolute right-4 top-4 rounded-lg p-1 text-gray-400 hover:bg-gray-100 hover:text-gray-600"
            aria-label="Close"
          >
            <X className="h-5 w-5" />
          </button>
        )}

        <h2 className="text-xl font-semibold text-gray-900">
          {required ? 'Create your first shop' : 'Add a new store'}
        </h2>
        <p className="mt-1 text-sm text-gray-500">
          {required
            ? 'You need at least one shop to use the vendor portal.'
            : 'Set up another location under your account.'}
        </p>

        <form onSubmit={handleSubmit} className="mt-6 space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700">Store name</label>
            <input
              type="text"
              value={storeName}
              onChange={(e) => setStoreName(e.target.value)}
              placeholder="e.g. Burger House"
              className="mt-1 w-full rounded-lg border border-gray-300 px-3 py-2 focus:border-brand-500 focus:outline-none focus:ring-1 focus:ring-brand-500"
              autoFocus
            />
          </div>
          {error && <Alert variant="error" message={error} onDismiss={() => setError('')} />}
          <div className="flex gap-2">
            {!required && (
              <button
                type="button"
                onClick={handleClose}
                className="flex-1 rounded-lg border border-gray-300 py-2.5 font-medium text-gray-700 hover:bg-gray-50"
              >
                Cancel
              </button>
            )}
            <button
              type="submit"
              disabled={loading}
              className={`rounded-lg bg-brand-600 py-2.5 font-medium text-white hover:bg-brand-700 disabled:opacity-50 ${
                required ? 'w-full' : 'flex-1'
              }`}
            >
              {loading ? 'Creating…' : required ? 'Create shop' : 'Add store'}
            </button>
          </div>
        </form>
      </div>
    </div>
  )
}
