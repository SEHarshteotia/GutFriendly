import { useQuery } from '@tanstack/react-query'
import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer,
  Legend,
} from 'recharts'
import { TrendingUp, TrendingDown, ShoppingBag, IndianRupee, Star } from 'lucide-react'
import { vendorApi } from '../api/vendorApi'
import { useAuth } from '../context/AuthContext'
import { Alert } from '../components/Alert'
import { formatCurrency, formatTimeAgo, greeting, orderStatusClass } from '../utils/format'
import { getErrorMessage } from '../utils/errors'

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

  const { data: dashboard, isLoading, isError, error } = useQuery({
    queryKey: ['dashboard', vendor?.vendorId, selectedShopId],
    queryFn: () => vendorApi.getDashboard(vendor.vendorId, selectedShopId),
    enabled: !!vendor && !!selectedShopId,
  })

  const { data: chartData = [] } = useQuery({
    queryKey: ['orderOverview', vendor?.vendorId, selectedShopId],
    queryFn: () => vendorApi.getOrderOverview(vendor.vendorId, selectedShopId),
    enabled: !!vendor && !!selectedShopId,
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

      <div className="mt-6 rounded-xl border border-gray-200 bg-white p-5 shadow-sm">
        <h2 className="mb-4 font-semibold text-gray-900">Order overview (today)</h2>
        <ResponsiveContainer width="100%" height={260}>
          <LineChart data={chartData}>
            <CartesianGrid strokeDasharray="3 3" />
            <XAxis dataKey="label" tick={{ fontSize: 11 }} />
            <YAxis yAxisId="left" tick={{ fontSize: 11 }} />
            <YAxis yAxisId="right" orientation="right" tick={{ fontSize: 11 }} />
            <Tooltip />
            <Legend />
            <Line yAxisId="left" type="monotone" dataKey="orders" stroke="#16a34a" name="Orders" />
            <Line yAxisId="right" type="monotone" dataKey="revenue" stroke="#2563eb" name="Revenue" />
          </LineChart>
        </ResponsiveContainer>
      </div>

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
