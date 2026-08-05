import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { getShopById } from "../../services/shopService";

const STATUS_STYLE = {
    VERIFIED: "bg-emerald-50 text-emerald-700",
    PENDING: "bg-amber-50 text-amber-700",
    REJECTED: "bg-rose-50 text-rose-700",
};

const AVAILABILITY_STYLE = {
    SERVICEABLE: "bg-emerald-50 text-emerald-700",
    COMING_SOON: "bg-amber-50 text-amber-700",
    NOT_SERVICEABLE: "bg-rose-50 text-rose-700",
};

function initials(name) {
    if (!name) return "?";
    return name.trim().charAt(0).toUpperCase();
}

export default function ShopDetails() {

    const navigate = useNavigate();
    const { shopId } = useParams();

    const [shop, setShop] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {

        loadShop();

    }, []);

    async function loadShop() {

        try {

            const data = await getShopById(shopId);

            setShop(data);

        }

        catch (error) {

            console.log(error);

        }

        finally {

            setLoading(false);

        }

    }

    if (loading) {

        return (
            <div className="p-6 bg-[#F7F8FA] min-h-screen">
                <h2 className="text-xl text-gray-500">Loading...</h2>
            </div>
        );

    }

    if (!shop) {

        return (
            <div className="p-6 bg-[#F7F8FA] min-h-screen">
                <h2 className="text-xl text-gray-500">Shop not found.</h2>
            </div>
        );

    }

    return (

        <div className="p-6 bg-[#F7F8FA] min-h-screen">

            <div className="flex justify-between items-start mb-8">

                <div>
                    <button
                        onClick={() => navigate(-1)}
                        className="text-sm text-gray-500 hover:text-[#173F33] mb-3 inline-flex items-center gap-1"
                    >
                        ← Back to Shops
                    </button>

                    <div className="flex items-center gap-4">
                        <div className="w-14 h-14 rounded-xl bg-emerald-100 text-emerald-700 flex items-center justify-center text-xl font-bold">
                            {initials(shop.shopName)}
                        </div>
                        <div>
                            <h1 className="text-3xl font-bold text-[#173F33]">
                                {shop.shopName}
                            </h1>
                            <p className="text-xs font-mono text-gray-400 mt-1">
                                ID: {shop.shopId}
                            </p>
                        </div>
                    </div>
                </div>

                <button
                    onClick={() => navigate(`/admin/shops/${shop.shopId}/inspections`)}
                    className="bg-[#173F33] hover:bg-[#0F2E25] text-white px-5 py-2.5 rounded-lg text-sm font-medium transition-colors"
                >
                    View Inspection History
                </button>

            </div>

            <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-6">

                <div className="bg-white border border-gray-100 rounded-xl p-5 shadow-sm">
                    <div className="text-xs font-mono uppercase tracking-wider text-gray-400 mb-2">Status</div>
                    <span className={`inline-block px-3 py-1 rounded-full text-sm font-medium ${STATUS_STYLE[shop.status] || "bg-gray-100 text-gray-600"}`}>
                        {shop.status}
                    </span>
                </div>

                <div className="bg-white border border-gray-100 rounded-xl p-5 shadow-sm">
                    <div className="text-xs font-mono uppercase tracking-wider text-gray-400 mb-2">Availability</div>
                    <span className={`inline-block px-3 py-1 rounded-full text-sm font-medium ${AVAILABILITY_STYLE[shop.serviceAvailabilityStatus] || "bg-gray-100 text-gray-600"}`}>
                        {shop.serviceAvailabilityStatus}
                    </span>
                </div>

                <div className="bg-white border border-gray-100 rounded-xl p-5 shadow-sm">
                    <div className="text-xs font-mono uppercase tracking-wider text-gray-400 mb-2">Access</div>
                    <span className={`inline-block px-3 py-1 rounded-full text-sm font-medium ${shop.blocked ? "bg-rose-50 text-rose-700" : "bg-emerald-50 text-emerald-700"}`}>
                        {shop.blocked ? "Blocked" : "Active"}
                    </span>
                </div>

            </div>

            <div className="bg-white shadow-sm border border-gray-100 rounded-xl overflow-hidden">

                <div className="px-8 py-5 border-b border-gray-100">
                    <h2 className="text-lg font-semibold text-[#173F33]">Shop Information</h2>
                </div>

                <div className="p-8 grid grid-cols-1 md:grid-cols-2 gap-x-12 gap-y-6">

                    <div>
                        <div className="text-xs font-mono uppercase tracking-wider text-gray-400 mb-1">GST Number</div>
                        <div className="text-gray-900 font-medium">{shop.gstNo}</div>
                    </div>

                    <div>
                        <div className="text-xs font-mono uppercase tracking-wider text-gray-400 mb-1">Category</div>
                        <span className="inline-block px-3 py-1 rounded-full bg-gray-100 text-gray-600 text-xs font-mono uppercase tracking-wide">
                            {shop.category}
                        </span>
                    </div>

                    <div>
                        <div className="text-xs font-mono uppercase tracking-wider text-gray-400 mb-1">GutTrust Score</div>
                        <div className="text-gray-900 font-medium">⭐ {shop.finalGutTrustScore}</div>
                    </div>

                    <div>
                        <div className="text-xs font-mono uppercase tracking-wider text-gray-400 mb-1">Created At</div>
                        <div className="text-gray-900 font-medium">{shop.createdAt}</div>
                    </div>

                    <div className="md:col-span-2">
                        <div className="text-xs font-mono uppercase tracking-wider text-gray-400 mb-1">Admin Remarks</div>
                        <div className="text-gray-700">
                            {shop.adminRemarks || <span className="text-gray-400">No remarks</span>}
                        </div>
                    </div>

                </div>

            </div>

        </div>

    );

}
