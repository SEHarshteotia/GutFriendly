import { useEffect, useState } from "react";
import {
    getAllShops,
    searchShops,
    getShopsByStatus,
    getShopsByAvailability,
    blockShop,
    unblockShop
} from "../../services/shopService";

import { useNavigate } from "react-router-dom";

const ICON_PALETTE = [
    "bg-blue-100 text-blue-700",
    "bg-emerald-100 text-emerald-700",
    "bg-rose-100 text-rose-700",
    "bg-indigo-100 text-indigo-700",
    "bg-amber-100 text-amber-700",
];

const STATUS_DOT = {
    VERIFIED: "bg-emerald-500",
    PENDING: "bg-amber-500",
    REJECTED: "bg-rose-500",
};

const STATUS_TEXT = {
    VERIFIED: "text-emerald-700",
    PENDING: "text-amber-700",
    REJECTED: "text-rose-700",
};

function initials(name) {
    if (!name) return "?";
    return name.trim().charAt(0).toUpperCase();
}

export default function Shops() {

    const [shops, setShops] = useState([]);
    const [loading, setLoading] = useState(true);

    const [search, setSearch] = useState("");
    const [status, setStatus] = useState("");
    const [availability, setAvailability] = useState("");
    const [page, setPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const [size] = useState(10);

    const navigate = useNavigate();

    // Load whenever filters change
    useEffect(() => {
        const timer = setTimeout(() => {
            loadShops();
        }, 500); // waits 500ms after typing

        return () => clearTimeout(timer);
    }, [search, status, availability, page]);

    async function loadShops() {

        setLoading(true);

        try {

            let data;

            if (search.trim() !== "") {

                data = await searchShops(search, page, size);

            } else if (status !== "") {

                data = await getShopsByStatus(status, page, size);

            } else if (availability !== "") {

                data = await getShopsByAvailability(availability, page, size);

            } else {

                data = await getAllShops(page, 10);

            }

            setShops(data.content);
            setTotalPages(data.totalPages);

        } catch (error) {

            console.log(error);

        } finally {

            setLoading(false);

        }
    }


    async function handleBlock(shopId) {

        const reason = prompt("Enter block reason");

        if (!reason) return;

        try {

            await blockShop(shopId, reason);

            loadShops();

        }

        catch (error) {

            console.log(error);

            alert("Unable to block shop");

        }

    }

    async function handleUnblock(shopId) {

        try {

            await unblockShop(shopId);

            loadShops();

        }

        catch (error) {

            console.log(error);

            alert("Unable to unblock shop");

        }

    }

    const blockedOnPage = shops.filter(s => s.blocked).length;
    const verifiedOnPage = shops.filter(s => s.status === "VERIFIED").length;
    const avgTrustOnPage = shops.length
        ? (shops.reduce((sum, s) => sum + (s.finalGutTrustScore || 0), 0) / shops.length).toFixed(1)
        : "—";

    return (
        <div className="p-6 bg-[#F7F8FA] min-h-screen">

            <div className="flex items-center justify-between mb-8">
                <div>
                    <h1 className="text-3xl font-bold text-[#173F33]">
                        Shop Management
                    </h1>
                    <p className="text-sm text-gray-500 mt-1">
                        Review shop status, availability, and moderate access
                    </p>
                </div>
            </div>

            <div className="flex flex-wrap gap-6 mb-6 items-end">

                <div>
                    <label className="block text-xs font-mono uppercase tracking-wider text-gray-500 mb-2">
                        Search
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
                            value={search}
                            onChange={(e) => { setSearch(e.target.value); setPage(0) }}
                            className="border border-gray-200 bg-white rounded-lg pl-9 pr-4 py-2 w-72 text-sm focus:outline-none focus:ring-2 focus:ring-[#173F33]/20 focus:border-[#173F33]"
                        />
                    </div>
                </div>

                <div>
                    <label className="block text-xs font-mono uppercase tracking-wider text-gray-500 mb-2">
                        Status
                    </label>
                    <select
                        value={status}
                        onChange={(e) => { setStatus(e.target.value); setPage(0); }}
                        className="border border-gray-200 bg-white rounded-lg px-4 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-[#173F33]/20 focus:border-[#173F33]"
                    >
                        <option value="">All Status</option>
                        <option value="VERIFIED">Verified</option>
                        <option value="PENDING">Pending</option>
                        <option value="REJECTED">Rejected</option>
                    </select>
                </div>

                <div>
                    <label className="block text-xs font-mono uppercase tracking-wider text-gray-500 mb-2">
                        Availability
                    </label>
                    <select
                        value={availability}
                        onChange={(e) => { setAvailability(e.target.value); setPage(0); }}
                        className="border border-gray-200 bg-white rounded-lg px-4 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-[#173F33]/20 focus:border-[#173F33]"
                    >
                        <option value="">All Availability</option>
                        <option value="SERVICEABLE">Serviceable</option>
                        <option value="COMING_SOON">Coming Soon</option>
                        <option value="NOT_SERVICEABLE">Not Serviceable</option>
                    </select>
                </div>

            </div>

            {loading ? (
                <h2 className="text-xl text-gray-500">Loading...</h2>
            ) : (
                <div className="bg-white shadow-sm border border-gray-100 rounded-xl overflow-hidden">

                    <table className="w-full">

                        <thead className="bg-gray-50">

                            <tr>
                                <th className="p-4 text-left text-xs font-mono uppercase tracking-wider text-gray-500">Shop</th>
                                <th className="p-4 text-left text-xs font-mono uppercase tracking-wider text-gray-500">Category</th>
                                <th className="p-4 text-left text-xs font-mono uppercase tracking-wider text-gray-500">Trust Score</th>
                                <th className="p-4 text-left text-xs font-mono uppercase tracking-wider text-gray-500">Status</th>
                                <th className="p-4 text-left text-xs font-mono uppercase tracking-wider text-gray-500">Blocked</th>
                                <th className="p-4 text-center text-xs font-mono uppercase tracking-wider text-gray-500">Actions</th>
                            </tr>

                        </thead>

                        <tbody>

                            {shops.length === 0 ? (
                                <tr>
                                    <td colSpan="6" className="text-center py-10 text-gray-400 text-sm">
                                        No shops match these filters.
                                    </td>
                                </tr>
                            ) : (

                            shops.map((shop, idx) => (

                                <tr key={shop.shopId} className="border-t border-gray-100 hover:bg-gray-50/70 transition-colors">

                                    <td className="p-4">
                                        <div className="flex items-center gap-3">
                                            <div className={`w-9 h-9 rounded-lg flex items-center justify-center text-sm font-semibold ${ICON_PALETTE[idx % ICON_PALETTE.length]}`}>
                                                {initials(shop.shopName)}
                                            </div>
                                            <div>
                                                <div className="font-medium text-gray-900">
                                                    {shop.shopName}
                                                </div>
                                                <div className="text-xs font-mono text-gray-400">
                                                    ID: {shop.shopId}
                                                </div>
                                            </div>
                                        </div>
                                    </td>

                                    <td className="p-4">
                                        <span className="px-3 py-1 rounded-full bg-gray-100 text-gray-600 text-xs font-mono uppercase tracking-wide">
                                            {shop.category}
                                        </span>
                                    </td>

                                    <td className="p-4 text-sm text-gray-700">
                                        ⭐ {shop.finalGutTrustScore.toFixed(1)}
                                    </td>

                                    <td className="p-4">
                                        <span className={`inline-flex items-center gap-2 text-sm font-medium ${STATUS_TEXT[shop.status] || "text-gray-600"}`}>
                                            <span className={`w-1.5 h-1.5 rounded-full ${STATUS_DOT[shop.status] || "bg-gray-400"}`} />
                                            {shop.status}
                                        </span>
                                    </td>

                                    <td className="p-4">
                                        <span
                                            className={`px-3 py-1 rounded-full text-xs font-medium
                                            ${
                                                shop.blocked
                                                    ? "bg-rose-50 text-rose-700"
                                                    : "bg-emerald-50 text-emerald-700"
                                            }`}
                                        >
                                            {shop.blocked ? "Blocked" : "Active"}
                                        </span>
                                    </td>

                                    <td className="p-4">
                                        <div className="flex flex-col items-center gap-2">
                                            <button
                                                onClick={() => navigate(`/admin/shops/${shop.shopId}`)}
                                                className="text-[#173F33] hover:text-[#0F2E25] text-sm font-medium underline-offset-2 hover:underline"
                                            >
                                                View Details
                                            </button>

                                            <button
                                                onClick={() =>
                                                    shop.blocked
                                                        ? handleUnblock(shop.shopId)
                                                        : handleBlock(shop.shopId)
                                                }
                                                className={`text-white text-xs font-medium px-4 py-1.5 rounded-lg transition-colors
                                                ${
                                                    shop.blocked
                                                        ? "bg-[#173F33] hover:bg-[#0F2E25]"
                                                        : "bg-rose-600 hover:bg-rose-700"
                                                }`}
                                            >
                                                {shop.blocked ? "Unblock" : "Block"}
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

            {!loading && shops.length > 0 && (
                <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mt-6">

                    <div className="bg-white border border-gray-100 rounded-xl p-5 flex items-center gap-4 shadow-sm">
                        <div className="w-11 h-11 rounded-lg bg-emerald-50 flex items-center justify-center text-emerald-600 text-lg">
                            ✓
                        </div>
                        <div>
                            <div className="text-xs font-mono uppercase tracking-wider text-gray-400">Verified (this page)</div>
                            <div className="text-2xl font-bold text-gray-900">{verifiedOnPage}</div>
                        </div>
                    </div>

                    <div className="bg-white border border-gray-100 rounded-xl p-5 flex items-center gap-4 shadow-sm">
                        <div className="w-11 h-11 rounded-lg bg-rose-50 flex items-center justify-center text-rose-600 text-lg">
                            ⛔
                        </div>
                        <div>
                            <div className="text-xs font-mono uppercase tracking-wider text-gray-400">Blocked (this page)</div>
                            <div className="text-2xl font-bold text-gray-900">{blockedOnPage}</div>
                        </div>
                    </div>

                    <div className="bg-white border border-gray-100 rounded-xl p-5 flex items-center gap-4 shadow-sm">
                        <div className="w-11 h-11 rounded-lg bg-amber-50 flex items-center justify-center text-amber-600 text-lg">
                            ⭐
                        </div>
                        <div>
                            <div className="text-xs font-mono uppercase tracking-wider text-gray-400">Avg. Trust Score (this page)</div>
                            <div className="text-2xl font-bold text-gray-900">{avgTrustOnPage}</div>
                        </div>
                    </div>

                </div>
            )}

        </div>
    );
}
