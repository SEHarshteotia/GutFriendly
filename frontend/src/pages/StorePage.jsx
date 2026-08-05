import { useState, useEffect } from 'react'
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { vendorApi } from '../api/vendorApi'
import { useAuth } from '../context/AuthContext'
import { Alert } from '../components/Alert'
import { getErrorMessage } from '../utils/errors'

const LOCATION_FIELDS = ['houseNo', 'street', 'city', 'state', 'pincode']

export function StorePage() {
  const { vendor, selectedShopId } = useAuth()
  const queryClient = useQueryClient()
  const [message, setMessage] = useState('')
  const [error, setError] = useState('')

  const { data: store, isLoading, isError, error: loadError } = useQuery({
    queryKey: ['store', vendor?.vendorId, selectedShopId],
    queryFn: () => vendorApi.getStoreDetails(vendor.vendorId, selectedShopId),
    enabled: !!vendor && !!selectedShopId,
  })

  const [form, setForm] = useState({
    storeName: '',
    isOpen: false,
    openTime: '09:00:00',
    closeTime: '22:00:00',
    onlineOrdersEnabled: true,
    estimatedPrepTimeMinutes: 15,
  })

  const [location, setLocation] = useState({
    houseNo: '',
    street: '',
    city: '',
    state: '',
    pincode: '',
  })

  useEffect(() => {
    if (store) {
      setForm({
        storeName: store.storeName,
        isOpen: store.open,
        openTime: store.openTime,
        closeTime: store.closeTime,
        onlineOrdersEnabled: store.onlineOrdersEnabled,
        estimatedPrepTimeMinutes: store.estimatedPrepTimeMinutes,
      })
      if (store.address) {
        setLocation({
          houseNo: store.address.houseNo,
          street: store.address.street,
          city: store.address.city,
          state: store.address.state,
          pincode: store.address.pincode,
        })
      }
    }
  }, [store])

  const updateMutation = useMutation({
    mutationFn: () => vendorApi.updateStore(vendor.vendorId, selectedShopId, form),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['store'] })
      setMessage('Store updated')
      setError('')
    },
    onError: (err) => {
      setMessage('')
      setError(getErrorMessage(err, 'store'))
    },
  })

  const locationMutation = useMutation({
    mutationFn: () => vendorApi.saveLocation(vendor.vendorId, selectedShopId, location),
    onSuccess: (res) => {
      queryClient.invalidateQueries({ queryKey: ['store'] })
      queryClient.invalidateQueries({ queryKey: ['dashboard'] })
      setMessage(res.message)
      setError('')
    },
    onError: (err) => {
      setMessage('')
      setError(getErrorMessage(err, 'store'))
    },
  })

  if (!selectedShopId) {
    return <div className="p-8 text-gray-500">Create a shop first.</div>
  }

  if (isLoading) return <div className="p-8 text-gray-500">Loading…</div>
  if (isError) {
    return (
      <div className="p-8">
        <Alert variant="error" message={getErrorMessage(loadError, 'store')} />
      </div>
    )
  }

  return (
    <div className="p-8">
      <h1 className="mb-6 text-2xl font-bold text-gray-900">Store settings</h1>

      {message && <Alert variant="success" message={message} onDismiss={() => setMessage('')} className="mb-4" />}
      {error && <Alert variant="error" message={error} onDismiss={() => setError('')} className="mb-4" />}

      <div className="grid gap-6 lg:grid-cols-2">
        <div className="rounded-xl border border-gray-200 bg-white p-6 shadow-sm">
          <h2 className="mb-4 font-semibold">Shop details</h2>
          <div className="space-y-3">
            <div>
              <label className="text-sm text-gray-600">Store name</label>
              <input
                value={form.storeName}
                onChange={(e) => setForm({ ...form, storeName: e.target.value })}
                className="mt-1 w-full rounded-lg border border-gray-300 px-3 py-2"
              />
            </div>
            <div className="grid grid-cols-2 gap-3">
              <div>
                <label className="text-sm text-gray-600">Open time</label>
                <input
                  type="time"
                  value={form.openTime.slice(0, 5)}
                  onChange={(e) => setForm({ ...form, openTime: e.target.value + ':00' })}
                  className="mt-1 w-full rounded-lg border border-gray-300 px-3 py-2"
                />
              </div>
              <div>
                <label className="text-sm text-gray-600">Close time</label>
                <input
                  type="time"
                  value={form.closeTime.slice(0, 5)}
                  onChange={(e) => setForm({ ...form, closeTime: e.target.value + ':00' })}
                  className="mt-1 w-full rounded-lg border border-gray-300 px-3 py-2"
                />
              </div>
            </div>
            <label className="flex items-center gap-2">
              <input
                type="checkbox"
                checked={form.isOpen}
                onChange={(e) => setForm({ ...form, isOpen: e.target.checked })}
              />
              <span className="text-sm">Shop is open</span>
            </label>
            <label className="flex items-center gap-2">
              <input
                type="checkbox"
                checked={form.onlineOrdersEnabled}
                onChange={(e) => setForm({ ...form, onlineOrdersEnabled: e.target.checked })}
              />
              <span className="text-sm">Online orders enabled</span>
            </label>
            <div>
              <label className="text-sm text-gray-600">Prep time (minutes)</label>
              <input
                type="number"
                value={form.estimatedPrepTimeMinutes}
                onChange={(e) =>
                  setForm({ ...form, estimatedPrepTimeMinutes: parseInt(e.target.value) || 15 })
                }
                className="mt-1 w-full rounded-lg border border-gray-300 px-3 py-2"
              />
            </div>
          </div>
          <button
            onClick={() => updateMutation.mutate()}
            className="mt-4 rounded-lg bg-brand-600 px-4 py-2 text-sm font-medium text-white hover:bg-brand-700"
          >
            Save changes
          </button>
        </div>

        <div className="rounded-xl border border-gray-200 bg-white p-6 shadow-sm">
          <h2 className="mb-4 font-semibold">Location</h2>
          <div className="space-y-3">
            {LOCATION_FIELDS.map((field) => (
              <div key={field}>
                <label className="text-sm capitalize text-gray-600">{field.replace(/([A-Z])/g, ' $1')}</label>
                <input
                  value={location[field]}
                  onChange={(e) => setLocation({ ...location, [field]: e.target.value })}
                  className="mt-1 w-full rounded-lg border border-gray-300 px-3 py-2"
                />
              </div>
            ))}
          </div>
          <button
            onClick={() => locationMutation.mutate()}
            className="mt-4 rounded-lg bg-brand-600 px-4 py-2 text-sm font-medium text-white hover:bg-brand-700"
          >
            Save location
          </button>
          {store && (
            <p className="mt-3 text-sm text-gray-500">
              Status: <span className="font-medium">{store.status}</span>
              {store.rating != null && ` · Rating: ${store.rating} (${store.ratingCount})`}
            </p>
          )}
        </div>
      </div>
    </div>
  )
}
