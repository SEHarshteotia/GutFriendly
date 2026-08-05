
import { useEffect, useState } from "react";
import { Link } from "react-router-dom";

import {
    FaClipboardCheck,
    FaHourglassHalf,
    FaCheckCircle,
    FaHistory
} from "react-icons/fa";

import { getAssignedInspections } from "../../services/inspectorService";

export default function InspectorDashboard() {

   const inspectorId = localStorage.getItem("inspectorId"); // Temporary until login

    const [inspections, setInspections] = useState([]);

  
    useEffect(() => {

        loadDashboard();

    }, []);


    async function loadDashboard() {

        try {

            const response = await getAssignedInspections(
                inspectorId,
                0,
                20
            );

            setInspections(response.content);

        }

        catch (error) {

            console.log(error);

        }

    }

    const assigned = inspections.filter(
        i => i.status === "ASSIGNED"
    ).length;

    const inProgress = inspections.filter(
        i => i.status === "IN_PROGRESS"
    ).length;

    const submitted = inspections.filter(
        i => i.status === "REPORT_SUBMITTED"
    ).length;

    return (

        <div className="space-y-8">

            <div>

                <h1 className="text-3xl font-bold">

                    Inspector Dashboard

                </h1>

                <p className="text-gray-600 mt-2">

                    Welcome to GutFriendly Inspector Portal

                </p>

            </div>

            {/* Statistics */}

            <div className="grid grid-cols-1 md:grid-cols-3 gap-6">

                <div className="bg-white shadow rounded-lg p-6">

                    <FaClipboardCheck
                        size={32}
                        className="text-blue-600 mb-4"
                    />

                    <h2 className="text-3xl font-bold">

                        {assigned}

                    </h2>

                    <p className="text-gray-600">

                        Assigned Inspections

                    </p>

                </div>

                <div className="bg-white shadow rounded-lg p-6">

                    <FaHourglassHalf
                        size={32}
                        className="text-yellow-500 mb-4"
                    />

                    <h2 className="text-3xl font-bold">

                        {inProgress}

                    </h2>

                    <p className="text-gray-600">

                        In Progress

                    </p>

                </div>

                <div className="bg-white shadow rounded-lg p-6">

                    <FaCheckCircle
                        size={32}
                        className="text-green-600 mb-4"
                    />

                    <h2 className="text-3xl font-bold">

                        {submitted}

                    </h2>

                    <p className="text-gray-600">

                        Reports Submitted

                    </p>

                </div>

            </div>

            {/* Quick Actions */}

            <div className="bg-white shadow rounded-lg p-6">

                <h2 className="text-xl font-bold mb-5">

                    Quick Actions

                </h2>

                <div className="flex gap-4">

                    <Link

                        to="/inspector/assigned"

                        className="bg-blue-600 hover:bg-blue-700 text-white px-5 py-3 rounded"

                    >

                        Assigned Inspections

                    </Link>

                    <Link

                        to="/inspector/history"

                        className="bg-green-600 hover:bg-green-700 text-white px-5 py-3 rounded"

                    >

                        Inspection History

                    </Link>

                </div>

            </div>

            {/* Recent Inspections */}

            <div className="bg-white shadow rounded-lg p-6">

                <div className="flex items-center gap-3 mb-5">

                    <FaHistory />

                    <h2 className="text-xl font-bold">

                        Recent Assigned Inspections

                    </h2>

                </div>

                <table className="w-full">

                    <thead>

                        <tr className="border-b">

                            <th className="text-left py-3">

                                Shop

                            </th>

                            <th className="text-left">

                                Vendor

                            </th>

                            <th className="text-left">

                                Status

                            </th>

                            <th className="text-left">

                                Action

                            </th>

                        </tr>

                    </thead>

                    <tbody>

                        {

                            inspections.slice(0,5).map(inspection => (

                                <tr
                                    key={inspection.inspectionId}
                                    className="border-b"
                                >

                                    <td className="py-3">

                                        {inspection.shopName}

                                    </td>

                                    <td>

                                        {inspection.vendorName}

                                    </td>

                                    <td>

                                        <span className="bg-gray-100 px-3 py-1 rounded">

                                            {inspection.status}

                                        </span>

                                    </td>

                                    <td>

                                        <Link

                                            to={`/inspector/inspection/${inspection.inspectionId}`}

                                            className="text-blue-600 font-semibold"

                                        >

                                            Open

                                        </Link>

                                    </td>

                                </tr>

                            ))

                        }

                    </tbody>

                </table>

            </div>

        </div>

    );

}

