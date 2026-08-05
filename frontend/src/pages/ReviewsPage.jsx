import { useState } from 'react'
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { vendorApi } from '../api/vendorApi'
import { useAuth } from '../context/AuthContext'
import { Alert } from '../components/Alert'
import { formatTimeAgo } from '../utils/format'
import { getErrorMessage } from '../utils/errors'

export function ReviewsPage() {
  const { vendor, selectedShopId } = useAuth()
  const queryClient = useQueryClient()
  const [replyingTo, setReplyingTo] = useState(null)
  const [replyText, setReplyText] = useState('')
  const [error, setError] = useState('')

  const { data: stats } = useQuery({
    queryKey: ['reviewStats', vendor?.vendorId, selectedShopId],
    queryFn: () => vendorApi.getReviewStats(vendor.vendorId, selectedShopId),
    enabled: !!vendor && !!selectedShopId,
  })

  const { data: reviews = [], isLoading, isError, error: loadError } = useQuery({
    queryKey: ['reviews', vendor?.vendorId, selectedShopId],
    queryFn: () => vendorApi.listReviews(vendor.vendorId, selectedShopId),
    enabled: !!vendor && !!selectedShopId,
  })

  const replyMutation = useMutation({
    mutationFn: ({ reviewId, reply }) =>
      vendorApi.replyToReview(vendor.vendorId, selectedShopId, reviewId, reply),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['reviews'] })
      queryClient.invalidateQueries({ queryKey: ['reviewStats'] })
      setReplyingTo(null)
      setReplyText('')
      setError('')
    },
    onError: (err) => setError(getErrorMessage(err, 'reviews')),
  })

  if (!selectedShopId) {
    return <div className="p-8 text-gray-500">Create a shop first.</div>
  }

  return (
    <div className="p-8">
      <h1 className="mb-6 text-2xl font-bold text-gray-900">Reviews</h1>

      {error && <Alert variant="error" message={error} onDismiss={() => setError('')} className="mb-4" />}
      {isError && !error && (
        <Alert variant="error" message={getErrorMessage(loadError, 'reviews')} className="mb-4" />
      )}

      {stats && (
        <div className="mb-6 flex flex-wrap gap-6 rounded-xl border border-gray-200 bg-white p-5 shadow-sm">
          <div>
            <p className="text-3xl font-bold text-gray-900">{stats.averageRating.toFixed(1)}</p>
            <p className="text-sm text-gray-500">{stats.totalReviews} reviews</p>
          </div>
          {[5, 4, 3, 2, 1].map((star) => {
            const count = [stats.fiveStarCount, stats.fourStarCount, stats.threeStarCount, stats.twoStarCount, stats.oneStarCount][5 - star]
            return (
              <div key={star} className="text-sm">
                <span className="text-amber-500">{'★'.repeat(star)}</span>
                <span className="ml-2 text-gray-600">{count}</span>
              </div>
            )
          })}
        </div>
      )}

      {isLoading ? (
        <p className="text-gray-500">Loading…</p>
      ) : reviews.length === 0 ? (
        <p className="text-gray-500">No reviews yet.</p>
      ) : (
        <ul className="space-y-4">
          {reviews.map((review) => (
            <li key={review.reviewId} className="rounded-xl border border-gray-200 bg-white p-5 shadow-sm">
              <div className="flex items-center justify-between">
                <span className="font-semibold">{review.customerName}</span>
                <span className="text-amber-500">{'★'.repeat(review.rating)}</span>
              </div>
              <p className="mt-2 text-gray-700">{review.comment}</p>
              <p className="mt-1 text-xs text-gray-400">{formatTimeAgo(review.minutesAgo)}</p>
              {review.vendorReply && (
                <p className="mt-3 rounded-lg bg-gray-50 p-3 text-sm text-gray-600">
                  <strong>Your reply:</strong> {review.vendorReply}
                </p>
              )}
              {replyingTo === review.reviewId ? (
                <div className="mt-3">
                  <textarea
                    value={replyText}
                    onChange={(e) => setReplyText(e.target.value)}
                    className="w-full rounded-lg border border-gray-300 p-2 text-sm"
                    rows={2}
                    placeholder="Write a reply…"
                  />
                  <div className="mt-2 flex gap-2">
                    <button
                      onClick={() => replyMutation.mutate({ reviewId: review.reviewId, reply: replyText })}
                      className="rounded-lg bg-brand-600 px-3 py-1.5 text-sm text-white"
                    >
                      Post reply
                    </button>
                    <button onClick={() => setReplyingTo(null)} className="text-sm text-gray-500">
                      Cancel
                    </button>
                  </div>
                </div>
              ) : (
                !review.vendorReply && (
                  <button
                    onClick={() => setReplyingTo(review.reviewId)}
                    className="mt-3 text-sm font-medium text-brand-600 hover:text-brand-700"
                  >
                    Reply
                  </button>
                )
              )}
            </li>
          ))}
        </ul>
      )}
    </div>
  )
}
