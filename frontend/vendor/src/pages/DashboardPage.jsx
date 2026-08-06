import { useState } from 'react'
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { Link } from 'react-router-dom'
import {
  TrendingUp,
  TrendingDown,
  ShoppingBag,
  IndianRupee,
  Star,
  ClipboardCheck,
} from 'lucide-react'
import { vendorApi } from '../api/vendorApi'
import { useAuth } from '../context/AuthContext'
import { Alert } from '../components/Alert'
import { formatCurrency, formatTimeAgo, greeting, orderStatusClass } from '../utils/format'
import { getErrorMessage } from '../utils/errors'

function canBookInspection(dashboard) {
  if (dashboard.status === 'APPROVED') {
    return false
  }

  if (dashboard.nextAction === 'Book inspection.') {
    return true
  }

  return dashboard.pendingRequirements?.some((requirement) =>
    requirement.toLowerCase().includes('book an inspection'),
  )
}

function KpiCard({ label, value, change, icon: Icon }) {
  const positive = change >= 0
  return (
    <div className="rounded-xl border border-gray-200 bg-white p-5 shadow-sm">
      <div className="flex items-center justify-between">
        <span className="text-sm text-gray-500">{label}</span>
        <Icon className="h-5 w-5 text-brand-600" />
      </div>
      <p className="mt-2 text-2xl font-bold text-gray-900">{value}</p>
      <div className={`mt-1 flex items-center gap-1 text-xs ${positive ? 'text-green-600' : 'text-red-600'}`}>
        {positive ? <TrendingUp className="h-3 w-3" /> : <TrendingDown className="h-3 w-3" />}
        {Math.abs(change).toFixed(1)}% vs yesterday
      </div>
    </div>
  )
}

export function DashboardPage() {
  const { vendor, selectedShopId } = useAuth()
  const queryClient = useQueryClient()
  const [inspectionDate, setInspectionDate] = useState('')
  const [inspectionTime, setInspectionTime] = useState('10:00')
  const [bookingMessage, setBookingMessage] = useState('')
  const [bookingError, setBookingError] = useState('')

  const { data: dashboard, isLoading, isError, error } = useQuery({
    queryKey: ['dashboard', vendor?.vendorId, selectedShopId],
    queryFn: () => vendorApi.getDashboard(vendor.vendorId, selectedShopId),
    enabled: !!vendor && !!selectedShopId,
  })

  const bookInspectionMutation = useMutation({
    mutationFn: () => {
      const timeValue = inspectionTime.length === 5 ? `${inspectionTime}:00` : inspectionTime
      const inspectionDateTime = `${inspectionDate}T${timeValue}`
      return vendorApi.bookInspection(vendor.vendorId, selectedShopId, inspectionDateTime)
    },
    onSuccess: () => {
      setBookingMessage('Inspection booked successfully.')
      setBookingError('')
      setInspectionDate('')
      setInspectionTime('10:00')
      queryClient.invalidateQueries({ queryKey: ['dashboard'] })
    },
    onError: (err) => {
      setBookingMessage('')
      setBookingError(getErrorMessage(err, 'generic'))
    },
  })

  if (!selectedShopId) {
    return (
      <div className="flex h-full items-center justify-center p-8 text-gray-500">
        Create a shop to view the dashboard.
      </div>
    )
  }

  if (isLoading) {
    return <div className="p-8 text-gray-500">Loading dashboard…</div>
  }

  if (isError || !dashboard) {
    return (
      <div className="p-8">
        <Alert variant="error" message={getErrorMessage(error, 'generic')} />
      </div>
    )
  }

  const { summary } = dashboard
  const showInspectionBooking = canBookInspection(dashboard)
  const needsLocationFirst =
    !dashboard.serviceableLocation ||
    dashboard.nextAction?.includes('location') ||
    dashboard.nextAction?.includes('serviceable pincode')
  const today = (() => {
    const now = new Date()
    const month = String(now.getMonth() + 1).padStart(2, '0')
    const day = String(now.getDate()).padStart(2, '0')
    return `${now.getFullYear()}-${month}-${day}`
  })()
  const canSubmitInspection = Boolean(inspectionDate && inspectionTime)

  return (
    <div className="p-8">
      <div className="mb-8">
        <h1 className="text-2xl font-bold text-gray-900">
          {greeting()}, {dashboard.shopName}!
        </h1>
        {dashboard.pendingRequirements.length > 0 && (
          <div className="mt-3 rounded-lg border border-amber-200 bg-amber-50 p-4">
            <p className="text-sm font-medium text-amber-800">{dashboard.nextAction}</p>
            <ul className="mt-2 list-inside list-disc text-sm text-amber-700">
              {dashboard.pendingRequirements.map((req) => (
                <li key={req}>{req}</li>
              ))}
            </ul>
          </div>
        )}
      </div>

      <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-4">
        <KpiCard
          label="Today's orders"
          value={String(summary.todaysOrders)}
          change={summary.ordersChangePercent}
          icon={ShoppingBag}
        />
        <KpiCard
          label="Today's revenue"
          value={formatCurrency(summary.todaysRevenue)}
          change={summary.revenueChangePercent}
          icon={IndianRupee}
        />
        <KpiCard
          label="Avg order value"
          value={formatCurrency(summary.averageOrderValue)}
          change={summary.avgOrderValueChangePercent}
          icon={IndianRupee}
        />
        <KpiCard
          label="Rating"
          value={`${summary.averageRating.toFixed(1)} (${summary.reviewCount})`}
          change={0}
          icon={Star}
        />
      </div>

      {dashboard.status !== 'APPROVED' && (
        <div className="mt-6 rounded-xl border border-gray-200 bg-white p-5 shadow-sm">
          <div className="flex items-start gap-3">
            <div className="rounded-lg bg-brand-50 p-2 text-brand-600">
              <ClipboardCheck className="h-5 w-5" />
            </div>
            <div className="flex-1">
              <h2 className="font-semibold text-gray-900">Food safety inspection</h2>
              <p className="mt-1 text-sm text-gray-600">{dashboard.nextAction}</p>

              {showInspectionBooking && (
                <div className="mt-4 flex flex-wrap items-end gap-3">
                  <div>
                    <label className="block text-sm font-medium text-gray-700">
                      Inspection date
                    </label>
                    <input
                      type="date"
                      value={inspectionDate}
                      min={today}
                      onChange={(e) => {
                        setInspectionDate(e.target.value)
                        setBookingMessage('')
                        setBookingError('')
                      }}
                      className="mt-1 rounded-lg border border-gray-300 px-3 py-2 text-sm text-gray-900"
                    />
                  </div>
                  <div>
                    <label className="block text-sm font-medium text-gray-700">
                      Inspection time
                    </label>
                    <input
                      type="time"
                      value={inspectionTime}
                      step={900}
                      onChange={(e) => {
                        setInspectionTime(e.target.value)
                        setBookingMessage('')
                        setBookingError('')
                      }}
                      className="mt-1 rounded-lg border border-gray-300 px-3 py-2 text-sm text-gray-900"
                    />
                  </div>
                  <button
                    type="button"
                    onClick={() => bookInspectionMutation.mutate()}
                    disabled={!canSubmitInspection || bookInspectionMutation.isPending}
                    className="rounded-lg bg-brand-600 px-4 py-2 text-sm font-medium text-white hover:bg-brand-700 disabled:opacity-50"
                  >
                    {bookInspectionMutation.isPending ? 'Booking…' : 'Book inspection'}
                  </button>
                </div>
              )}

              {needsLocationFirst && !showInspectionBooking && (
                <Link
                  to="/store"
                  className="mt-4 inline-flex rounded-lg bg-brand-600 px-4 py-2 text-sm font-medium text-white hover:bg-brand-700"
                >
                  Add shop location
                </Link>
              )}

              {bookingMessage && <p className="mt-3 text-sm text-green-700">{bookingMessage}</p>}
              {bookingError && <p className="mt-3 text-sm text-red-700">{bookingError}</p>}
            </div>
          </div>
        </div>
      )}

      <div className="mt-6 grid grid-cols-1 gap-6 lg:grid-cols-2">
        <div className="rounded-xl border border-gray-200 bg-white p-5 shadow-sm">
          <h2 className="mb-4 font-semibold text-gray-900">Active orders</h2>
          {dashboard.activeOrders.length === 0 ? (
            <p className="text-sm text-gray-500">No active orders</p>
          ) : (
            <ul className="space-y-3">
              {dashboard.activeOrders.map((order) => (
                <li key={order.orderId} className="flex items-start justify-between border-b border-gray-100 pb-3">
                  <div>
                    <p className="font-medium text-gray-900">{order.orderNumber}</p>
                    <p className="text-sm text-gray-500">{order.itemsSummary}</p>
                  </div>
                  <div className="text-right">
                    <span className={`rounded-full px-2 py-0.5 text-xs font-medium ${orderStatusClass(order.status)}`}>
                      {order.statusLabel}
                    </span>
                    <p className="mt-1 text-xs text-gray-400">{formatTimeAgo(order.minutesAgo)}</p>
                  </div>
                </li>
              ))}
            </ul>
          )}
        </div>

        <div className="rounded-xl border border-gray-200 bg-white p-5 shadow-sm">
          <h2 className="mb-4 font-semibold text-gray-900">Top selling items</h2>
          {dashboard.topSellingItems.length === 0 ? (
            <p className="text-sm text-gray-500">No sales today</p>
          ) : (
            <ul className="space-y-2">
              {dashboard.topSellingItems.map((item) => (
                <li key={item.rank} className="flex items-center justify-between text-sm">
                  <span>
                    <span className="mr-2 font-bold text-brand-600">#{item.rank}</span>
                    {item.itemName}
                  </span>
                  <span className="text-gray-500">{item.quantitySold} sold</span>
                </li>
              ))}
            </ul>
          )}
        </div>

        <div className="rounded-xl border border-gray-200 bg-white p-5 shadow-sm lg:col-span-2">
          <h2 className="mb-4 font-semibold text-gray-900">Recent reviews</h2>
          {dashboard.recentReviews.length === 0 ? (
            <p className="text-sm text-gray-500">No reviews yet</p>
          ) : (
            <ul className="space-y-4">
              {dashboard.recentReviews.map((review) => (
                <li key={review.reviewId} className="border-b border-gray-100 pb-3">
                  <div className="flex items-center justify-between">
                    <span className="font-medium">{review.customerName}</span>
                    <span className="text-amber-500">{'★'.repeat(review.rating)}</span>
                  </div>
                  <p className="mt-1 text-sm text-gray-600">{review.comment}</p>
                  <p className="mt-1 text-xs text-gray-400">{formatTimeAgo(review.minutesAgo)}</p>
                </li>
              ))}
            </ul>
          )}
        </div>
      </div>
    </div>
  )
}
