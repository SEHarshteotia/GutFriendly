import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { getInspectionsByShop } from "../../services/inspectionService";
import { getShopById } from "../../services/shopService";
import StatusBadge from "../../components/StatusBadge";
import { formatDate } from "../../utils/dateFormatter";

const ShopInspectionHistory = () => {

    const { shopId } = useParams();
    const navigate = useNavigate();

    const [shop, setShop] = useState(null);
    const [inspections, setInspections] = useState([]);
    const [page, setPage] = useState(0);
    const [size] = useState(10);
    const [totalPages, setTotalPages] = useState(0);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        loadShop();
    }, [shopId]);

    useEffect(() => {
        loadInspections();
    }, [shopId, page]);

    const loadShop = async () => {
        try {
            const data = await getShopById(shopId);
            setShop(data);
        } catch (err) {
            console.log(err);
        }
    };

    const loadInspections = async () => {
        setLoading(true);
        setError("");
        try {
            const data = await getInspectionsByShop(shopId, page, size);
            setInspections(data?.content ?? []);
            setTotalPages(data?.totalPages ?? 0);
        } catch (err) {
            console.log(err);
            setError("Could not load inspection history for this shop.");
            setInspections([]);
        } finally {
            setLoading(false);
        }
    };

    return (

        <div>

            <button
                onClick={() => navigate(`/admin/shops/${shopId}`)}
                className="text-sm text-gray-500 hover:text-[#173F33] mb-4"
            >
                &larr; Back to Shop Details
            </button>

            <div className="mb-8">
                <h1 className="text-3xl font-bold text-[#173F33]">
                    Inspection History
                </h1>
                <p className="text-sm text-gray-500 mt-1">
                    {shop?.shopName
                        ? `All inspections recorded for ${shop.shopName}`
                        : `All inspections recorded for shop #${shopId}`}
                </p>
            </div>

            {error && (
                <div className="mb-6 rounded-lg border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">
                    {error}
                </div>
            )}

            <div className="bg-white border border-gray-200 rounded-xl overflow-hidden">

                <table className="w-full text-sm">

                    <thead className="bg-[#F7F8F7] border-b border-gray-200">
                        <tr>
                            <th className="text-left px-6 py-3 text-xs font-mono uppercase tracking-wider text-gray-500">Inspection ID</th>
                            <th className="text-left px-6 py-3 text-xs font-mono uppercase tracking-wider text-gray-500">Inspector</th>
                            <th className="text-left px-6 py-3 text-xs font-mono uppercase tracking-wider text-gray-500">Inspection Date</th>
                            <th className="text-left px-6 py-3 text-xs font-mono uppercase tracking-wider text-gray-500">Completed</th>
                            <th className="text-left px-6 py-3 text-xs font-mono uppercase tracking-wider text-gray-500">Status</th>
                            <th className="text-left px-6 py-3 text-xs font-mono uppercase tracking-wider text-gray-500">Action</th>
                        </tr>
                    </thead>

                    <tbody>

                        {loading && (
                            <tr>
                                <td colSpan="6" className="px-6 py-10 text-center text-gray-400">
                                    Loading inspections...
                                </td>
                            </tr>
                        )}

                        {!loading && inspections.length === 0 && (
                            <tr>
                                <td colSpan="6" className="px-6 py-10 text-center text-gray-400">
                                    No inspections have been recorded for this shop yet.
                                </td>
                            </tr>
                        )}

                        {!loading && inspections.map((inspection) => (

                            <tr
                                key={inspection.inspectionId}
                                className="border-b border-gray-100 last:border-0 hover:bg-[#F7F8F7]"
                            >
                                <td className="px-6 py-4 font-mono text-gray-700">
                                    #{inspection.inspectionId}
                                </td>
                                <td className="px-6 py-4 text-gray-700">
                                    {inspection.inspectorName || "Unassigned"}
                                </td>
                                <td className="px-6 py-4 text-gray-700">
                                    {formatDate(inspection.inspectionDate)}
                                </td>
                                <td className="px-6 py-4 text-gray-700">
                                    {formatDate(inspection.completedAt)}
                                </td>
                                <td className="px-6 py-4">
                                    <StatusBadge status={inspection.status} />
                                </td>
                                <td className="px-6 py-4">
                                    <button
                                        onClick={() => navigate(`/admin/inspections/${inspection.inspectionId}`)}
                                        className="text-[#173F33] hover:underline font-medium"
                                    >
                                        View
                                    </button>
                                </td>
                            </tr>

                        ))}

                    </tbody>

                </table>

            </div>

            {totalPages > 1 && (

                <div className="flex items-center justify-between mt-6">

                    <button
                        disabled={page === 0}
                        onClick={() => setPage(page - 1)}
                        className="px-4 py-2 text-sm border border-gray-200 rounded-lg disabled:opacity-40 hover:bg-[#F7F8F7]"
                    >
                        Previous
                    </button>

                    <span className="text-sm text-gray-500">
                        Page {page + 1} of {totalPages}
                    </span>

                    <button
                        disabled={page + 1 >= totalPages}
                        onClick={() => setPage(page + 1)}
                        className="px-4 py-2 text-sm border border-gray-200 rounded-lg disabled:opacity-40 hover:bg-[#F7F8F7]"
                    >
                        Next
                    </button>

                </div>

            )}

        </div>

    );

};

export default ShopInspectionHistory;
