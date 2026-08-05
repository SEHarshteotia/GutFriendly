import React, { useEffect, useState } from "react";
import { getAllInspections, getInspectionsByStatus } from "../../services/inspectionService";
import { useNavigate } from "react-router-dom";
import StatusBadge from "../../components/StatusBadge";

const Inspections = () => {

    const [inspections, setInspections] = useState([]);
    const [page, setPage] = useState(0);

    const [size] = useState(10);

    const [totalPages, setTotalPages] = useState(0);
    const [selectedStatus, setSelectedStatus] = useState("ALL");
    const navigate = useNavigate();

    useEffect(() => {

        loadInspections();

    }, [selectedStatus, page]);

    const loadInspections = async () => {

        try {

            let data;

            if (selectedStatus === "ALL") {

                data = await getAllInspections(
                    page,
                    size
                );

            } else {

                data = await getInspectionsByStatus(
                    selectedStatus,
                    page,
                    size
                );

            }

            setInspections(data.content);

            setTotalPages(data.totalPages);

        } catch (error) {

            console.log(error);

        }

    };

    return (

        <div>

            <div className="mb-8">
                <h1 className="text-3xl font-bold text-[#173F33]">
                    Inspection Management
                </h1>
                <p className="text-sm text-gray-500 mt-1">
                    Track inspection progress from assignment through admin review
                </p>
            </div>

            <div className="mb-6">

                <label className="block text-xs font-mono uppercase tracking-wider text-gray-500 mb-2">
                    Status
                </label>

                <select
                    className="border border-gray-200 bg-white rounded-lg px-4 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-[#173F33]/20 focus:border-[#173F33]"
                    value={selectedStatus}
                    onChange={(e) => { setSelectedStatus(e.target.value); setPage(0); }}
                >

                    <option value="ALL">All</option>

                    <option value="SCHEDULED">Waiting</option>

                    <option value="ASSIGNED">Assigned</option>

                    <option value="IN_PROGRESS">In Progress</option>

                    <option value="REPORT_SUBMITTED">
                        Report Submitted
                    </option>

                    <option value="UNDER_ADMIN_REVIEW">
                        Under Admin Review
                    </option>

                    <option value="APPROVED">
                        Approved
                    </option>

                    <option value="REJECTED">
                        Rejected
                    </option>

                    <option value="CLOSED_FOR_REINSPECTION">
                        Closed For Reinspection
                    </option>

                </select>

            </div>

            <div className="bg-white shadow-sm border border-gray-100 rounded-xl overflow-hidden">

                <table className="w-full">

                    <thead className="bg-gray-50">

                        <tr>

                            <th className="p-4 text-left text-xs font-mono uppercase tracking-wider text-gray-500">
                                ID
                            </th>

                            <th className="p-4 text-left text-xs font-mono uppercase tracking-wider text-gray-500">
                                Shop
                            </th>

                            <th className="p-4 text-left text-xs font-mono uppercase tracking-wider text-gray-500">
                                Vendor
                            </th>

                            <th className="p-4 text-left text-xs font-mono uppercase tracking-wider text-gray-500">
                                Inspector
                            </th>

                            <th className="p-4 text-left text-xs font-mono uppercase tracking-wider text-gray-500">
                                Status
                            </th>

                            <th className="p-4 text-center text-xs font-mono uppercase tracking-wider text-gray-500">
                                Action
                            </th>

                        </tr>

                    </thead>

                    <tbody>

                        {inspections.length === 0 ? (

                            <tr>
                                <td colSpan="6" className="text-center py-10 text-gray-400 text-sm">
                                    No inspections found.
                                </td>
                            </tr>

                        ) : (

                            inspections.map((inspection) => (

                                <tr key={inspection.inspectionId} className="border-t border-gray-100 hover:bg-gray-50/70 transition-colors">

                                    <td className="p-4 font-mono text-sm text-gray-900 font-medium">
                                        #{inspection.inspectionId}
                                    </td>

                                    <td className="p-4 text-sm text-gray-900 font-medium">
                                        {inspection.shopName}
                                    </td>

                                    <td className="p-4 text-sm text-gray-700">
                                        {inspection.vendorName}
                                    </td>

                                    <td className="p-4 text-sm text-gray-700">
                                        {inspection.inspectorName || "Not Assigned"}
                                    </td>

                                    <td className="p-4">
                                        <StatusBadge status={inspection.status} />
                                    </td>

                                    <td className="p-4 text-center">

                                        <button
                                            onClick={() => navigate(
                                                `/admin/inspections/${inspection.inspectionId}`
                                            )}
                                            className="bg-[#173F33] hover:bg-[#0F2E25] text-white px-4 py-1.5 rounded-lg text-xs font-medium transition-colors"
                                        >
                                            View
                                        </button>

                                    </td>

                                </tr>

                            ))

                        )}

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

        </div>

    );

};

export default Inspections;
