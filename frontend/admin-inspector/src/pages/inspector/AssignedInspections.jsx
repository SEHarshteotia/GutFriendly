import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import { getAssignedInspections } from "../../services/inspectorService";
import { formatDate } from "../../utils/dateFormatter";

export default function AssignedInspections() {

    const inspectorId = localStorage.getItem("inspectorId");

    const navigate = useNavigate();

    const [inspections, setInspections] = useState([]);

    const [loading, setLoading] = useState(true);

    useEffect(() => {

        loadAssignedInspections();

    }, []);

    async function loadAssignedInspections() {

        try {

            const response =
                await getAssignedInspections(inspectorId);

            setInspections(response.content);

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
            <div className="p-6">
                Loading...
            </div>
        );

    }

    return (

        <div className="p-6">

            <h1 className="text-3xl font-bold mb-6">
                Assigned Inspections
            </h1>

            {
                inspections.length === 0 ?

                    <div className="bg-white shadow rounded-lg p-8 text-center">

                        <h2 className="text-xl font-semibold">
                            No Assigned Inspections
                        </h2>

                    </div>

                    :

                    <table className="w-full border">

                        <thead>

                            <tr className="bg-gray-100">

                                <th className="border p-3">
                                    ID
                                </th>

                                <th className="border p-3">
                                    Shop
                                </th>

                                <th className="border p-3">
                                    Vendor
                                </th>

                                <th className="border p-3">
                                    Date
                                </th>

                                <th className="border p-3">
                                    Status
                                </th>

                                <th className="border p-3">
                                    Action
                                </th>

                            </tr>

                        </thead>

                        <tbody>

                            {
                                inspections.map((inspection) => (

                                    <tr
                                        key={inspection.inspectionId}
                                    >

                                        <td className="border p-3">
                                            {inspection.inspectionId}
                                        </td>

                                        <td className="border p-3">
                                            {inspection.shopName}
                                        </td>

                                        <td className="border p-3">
                                            {inspection.vendorName}
                                        </td>

                                        <td className="border p-3">
                                            {formatDate(inspection.inspectionDate)}
                                        </td>

                                        <td className="border p-3">
                                            {inspection.status}
                                        </td>

                                        <td className="border p-3">

                                            <button

                                                onClick={() =>
                                                    navigate(
                                                        `/inspector/inspection/${inspection.inspectionId}`
                                                    )
                                                }

                                                className="bg-green-600 hover:bg-green-700 text-white px-4 py-2 rounded"

                                            >

                                                {
                                                    inspection.status === "ASSIGNED"
                                                        ? "Start Inspection"
                                                        : "Continue"
                                                }

                                            </button>

                                        </td>

                                    </tr>

                                ))
                            }

                        </tbody>

                    </table>

            }

        </div>

    );

}