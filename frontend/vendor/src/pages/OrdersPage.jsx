import { useState } from 'react'
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { vendorApi } from '../api/vendorApi'
import { useAuth } from '../context/AuthContext'
import { Alert } from '../components/Alert'
import { formatCurrency, formatTimeAgo, orderStatusClass } from '../utils/format'
import { getErrorMessage } from '../utils/errors'

const FILTERS = [
  { value: '', label: 'All' },
  { value: 'active', label: 'Active' },
  { value: 'NEW', label: 'New' },
  { value: 'ACCEPTED', label: 'Accepted' },
  { value: 'PREPARING', label: 'Preparing' },
  { value: 'OUT_FOR_DELIVERY', label: 'Out for delivery' },
  { value: 'DELIVERED', label: 'Delivered' },
  { value: 'CANCELLED', label: 'Cancelled' },
]

const NEXT_STATUS = {
  NEW: 'ACCEPTED',
  ACCEPTED: 'PREPARING',
  PREPARING: 'OUT_FOR_DELIVERY',
  OUT_FOR_DELIVERY: 'DELIVERED',
}

export function OrdersPage() {
  const { vendor, selectedShopId } = useAuth()
  const queryClient = useQueryClient()
  const [filter, setFilter] = useState('')
  const [selectedId, setSelectedId] = useState(null)
  const [error, setError] = useState('')

  const { data: orders = [], isLoading, isError, error: loadError } = useQuery({
    queryKey: ['orders', vendor?.vendorId, selectedShopId, filter],
    queryFn: () => vendorApi.listOrders(vendor.vendorId, selectedShopId, filter || undefined),
    enabled: !!vendor && !!selectedShopId,
  })

  const updateMutation = useMutation({
    mutationFn: ({ orderId, status }) =>
      vendorApi.updateOrderStatus(vendor.vendorId, selectedShopId, orderId, status),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['orders'] })
      queryClient.invalidateQueries({ queryKey: ['activeOrderCount'] })
      queryClient.invalidateQueries({ queryKey: ['dashboard'] })
      setError('')
    },
    onError: (err) => setError(getErrorMessage(err, 'orders')),
  })

  const selected = orders.find((o) => o.orderId === selectedId)

  if (!selectedShopId) {
    return <div className="p-8 text-gray-500">Create a shop first.</div>
  }

  return (
    <div className="p-8">
      <h1 className="mb-6 text-2xl font-bold text-gray-900">Orders</h1>

      {error && <Alert variant="error" message={error} onDismiss={() => setError('')} className="mb-4" />}
      {isError && !error && (
        <Alert variant="error" message={getErrorMessage(loadError, 'orders')} className="mb-4" />
      )}

      <div className="mb-4 flex flex-wrap gap-2">
        {FILTERS.map((f) => (
          <button
            key={f.value}
            onClick={() => setFilter(f.value)}
            className={`rounded-full px-4 py-1.5 text-sm font-medium ${
              filter === f.value
                ? 'bg-brand-600 text-white'
                : 'bg-white text-gray-600 ring-1 ring-gray-200 hover:bg-gray-50'
            }`}
          >
            {f.label}
          </button>
        ))}
      </div>

      {isLoading ? (
        <p className="text-gray-500">Loading…</p>
      ) : orders.length === 0 ? (
        <p className="text-gray-500">No orders found.</p>
      ) : (
        <div className="overflow-hidden rounded-xl border border-gray-200 bg-white shadow-sm">
          <table className="w-full text-left text-sm">
            <thead className="border-b border-gray-200 bg-gray-50">
              <tr>
                <th className="px-4 py-3 font-medium text-gray-600">Order</th>
                <th className="px-4 py-3 font-medium text-gray-600">Status</th>
                <th className="px-4 py-3 font-medium text-gray-600">Total</th>
                <th className="px-4 py-3 font-medium text-gray-600">Time</th>
                <th className="px-4 py-3 font-medium text-gray-600">Actions</th>
              </tr>
            </thead>
            <tbody>
              {orders.map((order) => {
                const nextStatus = NEXT_STATUS[order.status]
                return (
                  <tr
                    key={order.orderId}
                    className="border-b border-gray-100 hover:bg-gray-50 cursor-pointer"
                    onClick={() => setSelectedId(order.orderId)}
                  >
                    <td className="px-4 py-3 font-medium">{order.orderNumber}</td>
                    <td className="px-4 py-3">
                      <span className={`rounded-full px-2 py-0.5 text-xs font-medium ${orderStatusClass(order.status)}`}>
                        {order.statusLabel}
                      </span>
                    </td>
                    <td className="px-4 py-3">{formatCurrency(order.totalAmount)}</td>
                    <td className="px-4 py-3 text-gray-500">{formatTimeAgo(order.minutesAgo)}</td>
                    <td className="px-4 py-3" onClick={(e) => e.stopPropagation()}>
                      {nextStatus && (
                        <button
                          onClick={() =>
                            updateMutation.mutate({
                              orderId: order.orderId,
                              status: nextStatus,
                            })
                          }
                          className="text-xs font-medium text-brand-600 hover:text-brand-700"
                        >
                          → {nextStatus.replace(/_/g, ' ')}
                        </button>
                      )}
                    </td>
                  </tr>
                )
              })}
            </tbody>
          </table>
        </div>
      )}

      {selected && (
        <div className="fixed inset-0 z-40 flex justify-end bg-black/30" onClick={() => setSelectedId(null)}>
          <div
            className="h-full w-full max-w-md overflow-auto bg-white p-6 shadow-xl"
            onClick={(e) => e.stopPropagation()}
          >
            <h2 className="text-lg font-bold">{selected.orderNumber}</h2>
            <p className="text-sm text-gray-500">{formatTimeAgo(selected.minutesAgo)}</p>
            <ul className="mt-4 space-y-2">
              {selected.items.map((item) => (
                <li key={item.orderItemId} className="flex justify-between text-sm">
                  <span>
                    {item.quantity}× {item.itemName}
                  </span>
                  <span>{formatCurrency(item.unitPrice * item.quantity)}</span>
                </li>
              ))}
            </ul>
            <p className="mt-4 border-t pt-4 font-bold">
              Total: {formatCurrency(selected.totalAmount)}
            </p>
          </div>
        </div>
      )}
    </div>
  )
}
