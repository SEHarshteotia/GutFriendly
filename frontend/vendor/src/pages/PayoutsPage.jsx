import { useQuery } from '@tanstack/react-query'
import { vendorApi } from '../api/vendorApi'
import { useAuth } from '../context/AuthContext'
import { Alert } from '../components/Alert'
import { formatCurrency } from '../utils/format'
import { getErrorMessage } from '../utils/errors'

export function PayoutsPage() {
  const { vendor, selectedShopId } = useAuth()

  const { data: summary, isError: summaryError, error: summaryErr } = useQuery({
    queryKey: ['payoutSummary', vendor?.vendorId, selectedShopId],
    queryFn: () => vendorApi.getPayoutSummary(vendor.vendorId, selectedShopId),
    enabled: !!vendor && !!selectedShopId,
  })

  const { data: payouts = [], isLoading, isError, error: loadError } = useQuery({
    queryKey: ['payouts', vendor?.vendorId, selectedShopId],
    queryFn: () => vendorApi.listPayouts(vendor.vendorId, selectedShopId),
    enabled: !!vendor && !!selectedShopId,
  })

  if (!selectedShopId) {
    return <div className="p-8 text-gray-500">Create a shop first.</div>
  }

  return (
    <div className="p-8">
      <h1 className="mb-6 text-2xl font-bold text-gray-900">Payouts</h1>

      {(summaryError || isError) && (
        <Alert
          variant="error"
          message={getErrorMessage(summaryErr ?? loadError, 'generic')}
          className="mb-4"
        />
      )}

      {summary && (
        <div className="mb-6 grid grid-cols-2 gap-4 lg:grid-cols-4">
          {[
            { label: 'Pending balance', value: formatCurrency(summary.pendingBalance) },
            { label: 'Total earned', value: formatCurrency(summary.totalEarned) },
            { label: 'Total paid out', value: formatCurrency(summary.totalPaidOut) },
            { label: 'Completed payouts', value: String(summary.completedPayouts) },
          ].map((card) => (
            <div key={card.label} className="rounded-xl border border-gray-200 bg-white p-5 shadow-sm">
              <p className="text-sm text-gray-500">{card.label}</p>
              <p className="mt-1 text-xl font-bold text-gray-900">{card.value}</p>
            </div>
          ))}
        </div>
      )}

      <h2 className="mb-4 font-semibold text-gray-900">Payout history</h2>
      {isLoading ? (
        <p className="text-gray-500">Loading…</p>
      ) : payouts.length === 0 ? (
        <p className="text-gray-500">No payouts yet.</p>
      ) : (
        <div className="overflow-hidden rounded-xl border border-gray-200 bg-white shadow-sm">
          <table className="w-full text-left text-sm">
            <thead className="border-b bg-gray-50">
              <tr>
                <th className="px-4 py-3 font-medium text-gray-600">Amount</th>
                <th className="px-4 py-3 font-medium text-gray-600">Status</th>
                <th className="px-4 py-3 font-medium text-gray-600">Period</th>
                <th className="px-4 py-3 font-medium text-gray-600">Reference</th>
              </tr>
            </thead>
            <tbody>
              {payouts.map((p) => (
                <tr key={p.payoutId} className="border-b border-gray-100">
                  <td className="px-4 py-3 font-medium">{formatCurrency(p.amount)}</td>
                  <td className="px-4 py-3">
                    <span className="rounded-full bg-gray-100 px-2 py-0.5 text-xs">{p.status}</span>
                  </td>
                  <td className="px-4 py-3 text-gray-500">
                    {p.periodStart} – {p.periodEnd}
                  </td>
                  <td className="px-4 py-3 text-gray-500">{p.referenceNumber ?? '—'}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  )
}
