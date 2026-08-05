import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import { getInspectionHistory } from "../../services/inspectorService";
import { formatDate } from "../../utils/dateFormatter";

export default function InspectionHistory() {

    const navigate = useNavigate();

    const inspectorId = localStorage.getItem("inspectorId");

    const [history, setHistory] = useState([]);

    const [page, setPage] = useState(0);

    const [totalPages, setTotalPages] = useState(0);

    useEffect(() => {

        loadHistory();

    }, [page]);

    async function loadHistory() {

        try {

            const response = await getInspectionHistory(
                inspectorId,
                page
            );

            setHistory(response.content);

            setTotalPages(response.totalPages);

        }
        catch (error) {

            console.log(error);

        }

    }

    return (

        <div className="p-6">

            <h1 className="text-3xl font-bold mb-6">

                Inspection History

            </h1>

            <table className="w-full border">

                <thead>

                    <tr className="bg-gray-100">

                        <th className="border p-2">ID</th>

                        <th className="border p-2">Shop</th>

                        <th className="border p-2">Vendor</th>

                        <th className="border p-2">Inspection Date</th>

                        <th className="border p-2">Status</th>

                        <th className="border p-2">Score</th>

                        <th className="border p-2">Recommendation</th>

                        <th className="border p-2">Action</th>

                    </tr>

                </thead>

                <tbody>

                    {

                        history.length === 0 ?

                            (

                                <tr>

                                    <td
                                        colSpan="8"
                                        className="text-center p-5"
                                    >

                                        No Inspection History

                                    </td>

                                </tr>

                            )

                            :

                            history.map(inspection => (

                                <tr key={inspection.inspectionId}>

                                    <td className="border p-2">

                                        {inspection.inspectionId}

                                    </td>

                                    <td className="border p-2">

                                        {inspection.shopName}

                                    </td>

                                    <td className="border p-2">

                                        {inspection.vendorName}

                                    </td>

                                    <td className="border p-2">

                                        {formatDate(inspection.inspectionDate)}

                                    </td>

                                    <td className="border p-2">

                                        {inspection.status}

                                    </td>

                                    <td className="border p-2">

                                        {inspection.overallInspectionScore}

                                    </td>

                                    <td className="border p-2">

                                        {inspection.recommendation}

                                    </td>

                                    <td className="border p-2">

                                        <button

                                            onClick={() =>
                                                navigate(
                                                    `/inspector/inspection/${inspection.inspectionId}`
                                                )
                                            }

                                            className="bg-blue-600 hover:bg-blue-700 text-white px-3 py-1 rounded"

                                        >

                                            View

                                        </button>

                                    </td>

                                </tr>

                            ))

                    }

                </tbody>

            </table>

            <div className="flex justify-between mt-5">

                <button

                    disabled={page === 0}

                    onClick={() => setPage(page - 1)}

                    className="bg-gray-300 px-4 py-2 rounded disabled:opacity-50"

                >

                    Previous

                </button>

                <span>

                    Page {page + 1} of {totalPages}

                </span>

                <button

                    disabled={page + 1 >= totalPages}

                    onClick={() => setPage(page + 1)}

                    className="bg-gray-300 px-4 py-2 rounded disabled:opacity-50"

                >

                    Next

                </button>

            </div>

        </div>

    );

}