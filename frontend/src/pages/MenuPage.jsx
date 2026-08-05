import { useState } from 'react'
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { Plus, Trash2, ToggleLeft, ToggleRight } from 'lucide-react'
import { vendorApi } from '../api/vendorApi'
import { useAuth } from '../context/AuthContext'
import { Alert } from '../components/Alert'
import { formatCurrency, formatMenuCategory } from '../utils/format'
import { getErrorMessage } from '../utils/errors'

export function MenuPage() {
  const { vendor, selectedShopId } = useAuth()
  const queryClient = useQueryClient()
  const [showForm, setShowForm] = useState(false)
  const [error, setError] = useState('')
  const [form, setForm] = useState({ name: '', category: '', price: '', description: '' })

  const { data: items = [], isLoading, isError, error: loadError } = useQuery({
    queryKey: ['menu', vendor?.vendorId, selectedShopId],
    queryFn: () => vendorApi.listMenuItems(vendor.vendorId, selectedShopId),
    enabled: !!vendor && !!selectedShopId,
  })

  const { data: categories = [] } = useQuery({
    queryKey: ['menuCategories', vendor?.vendorId, selectedShopId],
    queryFn: () => vendorApi.listMenuCategories(vendor.vendorId, selectedShopId),
    enabled: !!vendor && !!selectedShopId,
  })

  const createMutation = useMutation({
    mutationFn: () => {
      const price = parseFloat(form.price)
      if (!form.name.trim()) throw new Error('Item name is required.')
      if (!form.category) throw new Error('Category is required.')
      if (!form.price || Number.isNaN(price) || price <= 0) {
        throw new Error('Enter a valid price greater than zero.')
      }
      return vendorApi.createMenuItem(vendor.vendorId, selectedShopId, {
        name: form.name,
        category: form.category,
        description: form.description || undefined,
        price,
      })
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['menu'] })
      setShowForm(false)
      setForm({ name: '', category: '', price: '', description: '' })
      setError('')
    },
    onError: (err) => setError(getErrorMessage(err, 'menu')),
  })

  const toggleMutation = useMutation({
    mutationFn: (itemId) =>
      vendorApi.toggleMenuItem(vendor.vendorId, selectedShopId, itemId),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['menu'] }),
    onError: (err) => setError(getErrorMessage(err, 'menu')),
  })

  const deleteMutation = useMutation({
    mutationFn: (itemId) =>
      vendorApi.deleteMenuItem(vendor.vendorId, selectedShopId, itemId),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['menu'] }),
    onError: (err) => setError(getErrorMessage(err, 'menu')),
  })

  if (!selectedShopId) {
    return <div className="p-8 text-gray-500">Create a shop first.</div>
  }

  return (
    <div className="p-8">
      <div className="mb-6 flex items-center justify-between">
        <h1 className="text-2xl font-bold text-gray-900">Menu</h1>
        <button
          onClick={() => setShowForm(true)}
          className="flex items-center gap-2 rounded-lg bg-brand-600 px-4 py-2 text-sm font-medium text-white hover:bg-brand-700"
        >
          <Plus className="h-4 w-4" />
          Add item
        </button>
      </div>

      {error && <Alert variant="error" message={error} onDismiss={() => setError('')} className="mb-4" />}
      {isError && !error && (
        <Alert variant="error" message={getErrorMessage(loadError, 'menu')} className="mb-4" />
      )}

      {showForm && (
        <div className="mb-6 rounded-xl border border-gray-200 bg-white p-5 shadow-sm">
          <h2 className="mb-4 font-semibold">New menu item</h2>
          <div className="grid grid-cols-2 gap-3">
            <input
              placeholder="Name *"
              value={form.name}
              onChange={(e) => setForm({ ...form, name: e.target.value })}
              className="rounded-lg border border-gray-300 px-3 py-2"
            />
            <select
              value={form.category}
              onChange={(e) => setForm({ ...form, category: e.target.value })}
              className="rounded-lg border border-gray-300 px-3 py-2"
            >
              <option value="">Select category *</option>
              {categories.map((category) => (
                <option key={category} value={category}>
                  {formatMenuCategory(category)}
                </option>
              ))}
            </select>
            <input
              placeholder="Price *"
              type="number"
              value={form.price}
              onChange={(e) => setForm({ ...form, price: e.target.value })}
              className="rounded-lg border border-gray-300 px-3 py-2"
            />
            <input
              placeholder="Description"
              value={form.description}
              onChange={(e) => setForm({ ...form, description: e.target.value })}
              className="rounded-lg border border-gray-300 px-3 py-2"
            />
          </div>
          <div className="mt-3 flex gap-2">
            <button
              onClick={() => createMutation.mutate()}
              className="rounded-lg bg-brand-600 px-4 py-2 text-sm text-white"
            >
              Save
            </button>
            <button onClick={() => setShowForm(false)} className="rounded-lg px-4 py-2 text-sm text-gray-600">
              Cancel
            </button>
          </div>
        </div>
      )}

      {isLoading ? (
        <p className="text-gray-500">Loading…</p>
      ) : items.length === 0 ? (
        <p className="text-gray-500">No menu items yet.</p>
      ) : (
        <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
          {items.map((item) => (
            <div
              key={item.itemId}
              className={`rounded-xl border bg-white p-4 shadow-sm ${!item.active ? 'opacity-60' : ''}`}
            >
              <div className="flex items-start justify-between">
                <div>
                  <h3 className="font-semibold text-gray-900">{item.name}</h3>
                  {item.category && (
                    <p className="text-xs text-gray-500">{formatMenuCategory(item.category)}</p>
                  )}
                </div>
                <p className="font-bold text-brand-600">{formatCurrency(item.price)}</p>
              </div>
              {item.description && <p className="mt-2 text-sm text-gray-600">{item.description}</p>}
              <div className="mt-3 flex items-center gap-2">
                <button
                  onClick={() => toggleMutation.mutate(item.itemId)}
                  className="text-gray-500 hover:text-brand-600"
                  title={item.active ? 'Disable' : 'Enable'}
                >
                  {item.active ? <ToggleRight className="h-6 w-6 text-brand-600" /> : <ToggleLeft className="h-6 w-6" />}
                </button>
                <button
                  onClick={() => deleteMutation.mutate(item.itemId)}
                  className="text-gray-400 hover:text-red-600"
                >
                  <Trash2 className="h-4 w-4" />
                </button>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  )
}
