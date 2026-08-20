import { FaBell, FaSearch, FaQuestionCircle, FaBars } from "react-icons/fa";
import { useNavigate } from "react-router-dom";
import { clearStaffSession } from "../../services/session";

export default function Navbar({ onMenuClick }) {

    const navigate = useNavigate();

    const handleLogout = () => {

        // Drops the token as well, so the session really ends here.
        clearStaffSession();

        navigate("/login");

    };

    return (

        <header className="portal-topbar bg-white h-20 px-8 flex justify-between items-center">

            <div className="flex items-center gap-3 min-w-0">

                {/* Opens the slide-in menu; only visible on small screens. */}
                <button
                    type="button"
                    onClick={onMenuClick}
                    aria-label="Open menu"
                    className="gf-hamburger"
                >
                    <FaBars size={18} />
                </button>

            <div className="relative gf-topbar-search">
                <FaSearch
                    size={14}
                    className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-400"
                />
                <input
                    type="text"
                    placeholder="Search businesses..."
                    className="border border-gray-200 bg-gray-50 rounded-full pl-10 pr-4 py-2.5 w-96 text-sm focus:outline-none focus:ring-2 focus:ring-[#173F33]/20 focus:border-[#173F33] focus:bg-white"
                />
            </div>

            </div>

            <div className="flex items-center gap-5">

                <button className="w-10 h-10 rounded-full bg-gray-50 border border-gray-200 flex items-center justify-center text-gray-500 hover:text-[#173F33] hover:border-[#173F33]/30 transition-colors">
                    <FaBell size={16} />
                </button>

                <button className="w-10 h-10 rounded-full bg-gray-50 border border-gray-200 flex items-center justify-center text-gray-500 hover:text-[#173F33] hover:border-[#173F33]/30 transition-colors">
                    <FaQuestionCircle size={16} />
                </button>

                <div className="w-px h-8 bg-gray-200" />

                <div className="flex items-center gap-3">

                    <div className="w-11 h-11 rounded-full bg-[#173F33] text-white flex items-center justify-center font-bold">
                        A
                    </div>

                    <div>

                        <div className="font-semibold text-gray-900 text-sm">
                            Admin
                        </div>

                        <div className="text-xs text-gray-400">
                            Administrator
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
