import { useEffect, useState } from "react";
import { getAllInspectors } from "../../services/inspectorService";

export default function Inspectors() {

    const [inspectors, setInspectors] = useState([]);
    const [loading, setLoading] = useState(true);
    const [search, setSearch] = useState("");

    useEffect(() => {
        loadInspectors();
    }, []);

    async function loadInspectors() {

        try {

            const data = await getAllInspectors();
            setInspectors(data);

        } catch (error) {

            console.log(error);

        } finally {

            setLoading(false);

        }
    }

    const filteredInspectors = inspectors.filter(inspector =>

        `${inspector.firstName} ${inspector.lastName}`
            .toLowerCase()
            .includes(search.toLowerCase())

    );

    return (

        <div className="p-6">

            <div className="flex justify-between items-center mb-6">

                <div>

                    <h1 className="text-3xl font-bold">
                        Inspector Management
                    </h1>

                    <p className="text-gray-500 mt-1">
                        View all registered inspectors
                    </p>

                </div>

            </div>

            <input
                type="text"
                placeholder="Search Inspector..."
                value={search}
                onChange={(e) => setSearch(e.target.value)}
                className="border rounded-lg px-4 py-2 w-80 mb-6"
            />

            {loading ?

                (
                    <h2>Loading...</h2>
                )

                :

                (

                    <div className="bg-white rounded-xl shadow overflow-hidden">

                        <table className="w-full">

                            <thead className="bg-gray-100">

                                <tr>

                                    <th className="p-4 text-left">ID</th>

                                    <th className="p-4 text-left">
                                        Name
                                    </th>

                                    <th className="p-4 text-left">
                                        Email
                                    </th>

                                    <th className="p-4 text-left">
                                        Phone
                                    </th>

                                    <th className="p-4 text-center">
                                        Status
                                    </th>

                                </tr>

                            </thead>

                            <tbody>

                                {

                                    filteredInspectors.map(inspector => (

                                        <tr
                                            key={inspector.inspectorId}
                                            className="border-t hover:bg-gray-50"
                                        >

                                            <td className="p-4">
                                                #{inspector.inspectorId}
                                            </td>

                                            <td className="p-4 font-medium">

                                                {inspector.firstName}{" "}
                                                {inspector.lastName}

                                            </td>

                                            <td className="p-4">
                                                {inspector.email}
                                            </td>

                                            <td className="p-4">
                                                {inspector.phoneNo}
                                            </td>

                                            <td className="p-4 text-center">

                                                <span className="bg-green-100 text-green-700 px-3 py-1 rounded-full text-sm">

                                                    Active

                                                </span>

                                            </td>

                                        </tr>

                                    ))

                                }

                            </tbody>

                        </table>

                    </div>

                )

            }

        </div>

    );

}