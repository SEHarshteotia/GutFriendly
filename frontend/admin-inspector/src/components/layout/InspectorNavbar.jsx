import { FaBell } from "react-icons/fa";
import { useNavigate } from "react-router-dom";

export default function InspectorNavbar() {

    const navigate = useNavigate();

    const handleLogout = () => {
        localStorage.removeItem("role");
        localStorage.removeItem("inspectorId");
        navigate("/login");
    };

    return (

        <header className="portal-topbar bg-white h-20 px-8 flex justify-between items-center">

            <input

                type="text"

                placeholder="Search Inspections..."

                className="border rounded-lg px-4 py-2 w-96"

            />

            <div className="flex items-center gap-6">

                <FaBell

                    size={22}

                    className="cursor-pointer"

                />

                <div className="flex items-center gap-3">

                    <div className="w-11 h-11 rounded-full bg-[#087454] text-white flex items-center justify-center font-bold">

                        I

                    </div>

                    <div>

                        <div className="font-semibold">

                            Inspector

                        </div>

                        <div className="text-sm text-gray-500">

                            Food Safety Inspector

                        </div>

                    </div>

                </div>

                <button
                    onClick={handleLogout}
                    className="bg-rose-600 hover:bg-rose-700 text-white px-4 py-2 rounded-lg text-sm font-medium transition-colors"
                >
                    Logout
                </button>

            </div>

        </header>

    );

}
