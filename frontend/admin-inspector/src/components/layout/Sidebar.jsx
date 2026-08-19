import {
  FaStore,
  FaClipboardCheck,
  FaUsers,
  FaAward,
  FaStar,
  FaCog,
  FaChartBar
} from "react-icons/fa";

import { NavLink } from "react-router-dom";
import GutFriendlyLogo from "@shared/GutFriendlyLogo";

export default function Sidebar() {

    const menu = [
        {
            name:"Dashboard",
            icon:<FaChartBar/>,
            path:"/admin/dashboard"
        },
        {
            name:"Shops",
            icon:<FaStore/>,
            path:"/admin/shops"
        },
        {
            name:"Inspections",
            icon:<FaClipboardCheck/>,
            path:"/admin/inspections"
        },
        {
            name:"Inspectors",
            icon:<FaUsers/>,
            path:"/admin/inspectors"
        },
        {
            name:"Reviews",
            icon:<FaAward/>,
            path:"/admin/reviews"
        },
        {
            name:"Profile",
            icon:<FaStar/>,
            path:"/admin/profile"
        },
        {
            name:"Settings",
            icon:<FaCog/>,
            path:"/admin/settings"
        }
    ];

    return (

        <aside className="portal-sidebar w-72 h-screen flex flex-col">

            {/* Brand */}
            <div className="border-b border-gray-100 px-7 py-7">
                <GutFriendlyLogo
                    size="md"
                    subtitle="Admin Console"
                />
            </div>

            {/* Nav */}
            <div className="flex-1 mt-6 px-4 overflow-y-auto">

                <div className="px-3 mb-2 text-[11px] font-semibold uppercase tracking-widest text-gray-400">
                    Menu
                </div>

                <div className="flex flex-col gap-1">

                    {
                        menu.map((item)=>(
                            <NavLink
                                key={item.name}
                                to={item.path}
                                className={({isActive})=>

                                    `group relative flex items-center gap-3 px-3.5 py-3 rounded-xl text-[15px] font-medium transition-all duration-200

                                    ${
                                        isActive
                                        ?
                                        "bg-[#173F33] text-white shadow-sm"
                                        :
                                        "text-gray-500 hover:bg-gray-50 hover:text-[#173F33]"
                                    }
                                    `
                                }
                            >
                                {({isActive}) => (
                                    <>
                                        <span
                                            className={`absolute left-0 top-1/2 -translate-y-1/2 h-6 w-[3px] rounded-full bg-emerald-400 transition-opacity duration-200 ${
                                                isActive ? "opacity-100" : "opacity-0"
                                            }`}
                                        />

                                        <span
                                            className={`flex items-center justify-center w-9 h-9 rounded-lg text-base transition-colors duration-200 ${
                                                isActive
                                                ? "bg-white/15 text-white"
                                                : "bg-gray-100 text-gray-400 group-hover:text-[#173F33]"
                                            }`}
                                        >
                                            {item.icon}
                                        </span>

                                        {item.name}
                                    </>
                                )}

                            </NavLink>
                        ))
                    }

                </div>

            </div>

            {/* Footer */}
            <div className="px-7 py-5 border-t border-gray-100">
                <div className="text-[11px] text-gray-400">
                    GutFriendly Admin &middot; v1.0
                </div>
            </div>

        </aside>

    )

}
