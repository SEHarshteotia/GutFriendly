import { useEffect, useState } from "react";
import {
    getAllReviews,
    getReviewsSummary,
    hideReview,
    restoreReview,
} from "../../services/reviewService";
import { formatDate } from "../../utils/dateFormatter";

const ICON_PALETTE = [
    "bg-blue-100 text-blue-700",
    "bg-emerald-100 text-emerald-700",
    "bg-rose-100 text-rose-700",
    "bg-indigo-100 text-indigo-700",
    "bg-amber-100 text-amber-700",
];

function initials(name) {
    if (!name) return "?";
    return name.trim().charAt(0).toUpperCase();
}

function Stars({ rating }) {
    return (
        <span className="text-amber-500 tracking-tight" aria-label={`${rating} out of 5 stars`}>
            {"★".repeat(rating)}
            <span className="text-gray-200">{"★".repeat(5 - rating)}</span>
        </span>
    );
}

export default function Reviews() {

    const [reviews, setReviews] = useState([]);
    const [summary, setSummary] = useState(null);
    const [loading, setLoading] = useState(true);

    const [shopName, setShopName] = useState("");
    const [rating, setRating] = useState("");
    const [page, setPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const [size] = useState(10);

    useEffect(() => {
        getReviewsSummary()
            .then(setSummary)
            .catch((error) => console.log(error));
    }, []);

    useEffect(() => {
        const timer = setTimeout(() => {
            loadReviews();
        }, 400);

        return () => clearTimeout(timer);
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [shopName, rating, page]);

    async function loadReviews() {
        setLoading(true);

        try {
            const data = await getAllReviews({
                page,
                size,
                shopName: shopName.trim() || undefined,
                rating: rating || undefined,
            });

            setReviews(data.content);
            setTotalPages(data.totalPages);
        } catch (error) {
            console.log(error);
        } finally {
            setLoading(false);
        }
    }

    async function refreshAll() {
        await Promise.all([loadReviews(), getReviewsSummary().then(setSummary)]);
    }

    async function handleHide(reviewId) {
        const reason = prompt("Reason for hiding this review (optional):") || "";

        try {
            await hideReview(reviewId, reason);
            refreshAll();
        } catch (error) {
            console.log(error);
            alert("Unable to hide review");
        }
    }

    async function handleRestore(reviewId) {
        try {
            await restoreReview(reviewId);
            refreshAll();
        } catch (error) {
            console.log(error);
            alert("Unable to restore review");
        }
    }

    return (
        <div>

            <div className="flex items-center justify-between mb-8">
                <div>
                    <h1 className="text-3xl font-bold text-[#173F33]">
                        Customer Reviews
                    </h1>
                    <p className="text-sm text-gray-500 mt-1">
                        Every review submitted by users across all shops, in one place
                    </p>
                </div>
            </div>

            {summary && (
                <div className="grid grid-cols-1 md:grid-cols-4 gap-4 mb-6">

                    <div className="bg-white border border-gray-100 rounded-xl p-5 flex items-center gap-4 shadow-sm">
                        <div className="w-11 h-11 rounded-lg bg-amber-50 flex items-center justify-center text-amber-600 text-lg">
                            ⭐
                        </div>
                        <div>
                            <div className="text-xs font-mono uppercase tracking-wider text-gray-400">Average Rating</div>
                            <div className="text-2xl font-bold text-gray-900">{summary.averageRating.toFixed(1)}</div>
                        </div>
                    </div>

                    <div className="bg-white border border-gray-100 rounded-xl p-5 flex items-center gap-4 shadow-sm">
                        <div className="w-11 h-11 rounded-lg bg-blue-50 flex items-center justify-center text-blue-600 text-lg">
                            💬
                        </div>
                        <div>
                            <div className="text-xs font-mono uppercase tracking-wider text-gray-400">Total Reviews</div>
                            <div className="text-2xl font-bold text-gray-900">{summary.totalReviews}</div>
                        </div>
                    </div>

                    <div className="bg-white border border-gray-100 rounded-xl p-5 flex items-center gap-4 shadow-sm">
                        <div className="w-11 h-11 rounded-lg bg-emerald-50 flex items-center justify-center text-emerald-600 text-lg">
                            🏪
                        </div>
                        <div>
                            <div className="text-xs font-mono uppercase tracking-wider text-gray-400">Shops Reviewed</div>
                            <div className="text-2xl font-bold text-gray-900">{summary.shopsReviewed}</div>
                        </div>
                    </div>

                    <div className="bg-white border border-gray-100 rounded-xl p-5 flex items-center gap-4 shadow-sm">
                        <div className="w-11 h-11 rounded-lg bg-rose-50 flex items-center justify-center text-rose-600 text-lg">
                            👎
                        </div>
                        <div>
                            <div className="text-xs font-mono uppercase tracking-wider text-gray-400">1–2 Star Reviews</div>
                            <div className="text-2xl font-bold text-gray-900">{summary.oneStar + summary.twoStar}</div>
                        </div>
                    </div>

                </div>
            )}

            <div className="flex flex-wrap gap-6 mb-6 items-end">

                <div>
                    <label className="block text-xs font-mono uppercase tracking-wider text-gray-500 mb-2">
                        Search by shop
                    </label>
                    <div className="relative">
                        <svg
                            className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-400"
                            fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}
                        >
                            <path strokeLinecap="round" strokeLinejoin="round" d="M21 21l-4.35-4.35m0 0A7.5 7.5 0 104.5 4.5a7.5 7.5 0 0012.15 12.15z" />
                        </svg>
                        <input
                            type="text"
                            placeholder="Search shop name..."
                            value={shopName}
                            onChange={(e) => { setShopName(e.target.value); setPage(0); }}
                            className="border border-gray-200 bg-white rounded-lg pl-9 pr-4 py-2 w-72 text-sm focus:outline-none focus:ring-2 focus:ring-[#173F33]/20 focus:border-[#173F33]"
                        />
                    </div>
                </div>

                <div>
                    <label className="block text-xs font-mono uppercase tracking-wider text-gray-500 mb-2">
                        Rating
                    </label>
                    <select
                        value={rating}
                        onChange={(e) => { setRating(e.target.value); setPage(0); }}
                        className="border border-gray-200 bg-white rounded-lg px-4 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-[#173F33]/20 focus:border-[#173F33]"
                    >
                        <option value="">All Ratings</option>
                        <option value="5">5 Stars</option>
                        <option value="4">4 Stars</option>
                        <option value="3">3 Stars</option>
                        <option value="2">2 Stars</option>
                        <option value="1">1 Star</option>
                    </select>
                </div>

                <div className="ml-auto text-sm text-gray-500 self-center">
                    {!loading && `Showing ${reviews.length} review${reviews.length === 1 ? "" : "s"} on this page`}
                </div>

            </div>

            {loading ? (
                <h2 className="text-xl text-gray-500">Loading...</h2>
            ) : (
                <div className="bg-white shadow-sm border border-gray-100 rounded-xl overflow-hidden">

                    <table className="w-full">

                        <thead className="bg-gray-50">
                            <tr>
                                <th className="p-4 text-left text-xs font-mono uppercase tracking-wider text-gray-500">Customer</th>
                                <th className="p-4 text-left text-xs font-mono uppercase tracking-wider text-gray-500">Shop</th>
                                <th className="p-4 text-left text-xs font-mono uppercase tracking-wider text-gray-500">Rating</th>
                                <th className="p-4 text-left text-xs font-mono uppercase tracking-wider text-gray-500">Comment</th>
                                <th className="p-4 text-left text-xs font-mono uppercase tracking-wider text-gray-500">Submitted</th>
                                <th className="p-4 text-center text-xs font-mono uppercase tracking-wider text-gray-500">Actions</th>
                            </tr>
                        </thead>

                        <tbody>

                            {reviews.length === 0 ? (
                                <tr>
                                    <td colSpan="6" className="text-center py-10 text-gray-400 text-sm">
                                        No reviews match these filters.
                                    </td>
                                </tr>
                            ) : (

                                reviews.map((review, idx) => (

                                    <tr key={review.reviewId} className="border-t border-gray-100 hover:bg-gray-50/70 transition-colors align-top">

                                        <td className="p-4">
                                            <div className="flex items-center gap-3">
                                                <div className={`w-9 h-9 rounded-lg flex items-center justify-center text-sm font-semibold ${ICON_PALETTE[idx % ICON_PALETTE.length]}`}>
                                                    {initials(review.userName)}
                                                </div>
                                                <div>
                                                    <div className="font-medium text-gray-900">
                                                        {review.userName}
                                                    </div>
                                                    <div className="text-xs font-mono text-gray-400">
                                                        Order #{review.orderId}
                                                    </div>
                                                </div>
                                            </div>
                                        </td>

                                        <td className="p-4">
                                            <span className="px-3 py-1 rounded-full bg-gray-100 text-gray-600 text-xs font-mono uppercase tracking-wide">
                                                {review.shopName}
                                            </span>
                                        </td>

                                        <td className="p-4 text-sm">
                                            <Stars rating={review.rating} />
                                        </td>

                                        <td className="p-4 text-sm text-gray-700 max-w-sm">
                                            {review.comment ? (
                                                <p className="line-clamp-3">{review.comment}</p>
                                            ) : (
                                                <span className="text-gray-400 italic">No comment</span>
                                            )}
                                            {review.keywords?.length > 0 && (
                                                <div className="flex flex-wrap gap-1 mt-2">
                                                    {review.keywords.map((k) => (
                                                        <span key={k} className="px-2 py-0.5 rounded-full bg-emerald-50 text-emerald-700 text-[11px] font-medium">
                                                            {k.replaceAll("_", " ")}
                                                        </span>
                                                    ))}
                                                </div>
                                            )}
                                        </td>

                                        <td className="p-4 text-sm text-gray-500 whitespace-nowrap">
                                            {formatDate(review.createdAt)}
                                        </td>

                                        <td className="p-4">
                                            <div className="flex flex-col items-center gap-2">
                                                <button
                                                    onClick={() => handleHide(review.reviewId)}
                                                    className="bg-rose-600 hover:bg-rose-700 text-white text-xs font-medium px-4 py-1.5 rounded-lg transition-colors"
                                                >
                                                    Hide
                                                </button>
                                                <button
                                                    onClick={() => handleRestore(review.reviewId)}
                                                    className="text-[#173F33] hover:text-[#0F2E25] text-xs font-medium underline-offset-2 hover:underline"
                                                >
                                                    Restore
                                                </button>
                                            </div>
                                        </td>

                                    </tr>

                                )))}

                        </tbody>

                    </table>

                    <div className="flex justify-between items-center px-4 py-4 border-t border-gray-100">

                        <button
                            disabled={page === 0}
                            onClick={() => setPage(page - 1)}
                            className="bg-gray-100 text-gray-700 px-4 py-2 rounded-lg text-sm font-medium disabled:opacity-40 disabled:cursor-not-allowed hover:bg-gray-200"
                        >
                            Previous
                        </button>

                        <span className="text-sm font-mono text-gray-500">
                            Page {page + 1} of {totalPages || 1}
                        </span>

                        <button
                            disabled={page + 1 >= totalPages}
                            onClick={() => setPage(page + 1)}
                            className="bg-[#173F33] text-white px-4 py-2 rounded-lg text-sm font-medium disabled:opacity-40 disabled:cursor-not-allowed hover:bg-[#0F2E25]"
                        >
                            Next
                        </button>

                    </div>

                </div>
            )}

        </div>
    );
}
