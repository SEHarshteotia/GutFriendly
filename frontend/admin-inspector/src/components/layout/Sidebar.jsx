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
            path:"#"
        },
        {
            name:"Profile",
            icon:<FaStar/>,
            path:"#"
        },
        {
            name:"Settings",
            icon:<FaCog/>,
            path:"#"
        }
    ];

    return (

        <div className="w-72 h-screen bg-white text-slate-600 flex flex-col border-r border-gray-200">

            {/* Brand */}
            <div className="flex items-center gap-3 px-7 py-7 border-b border-gray-100">

                <div className="relative w-11 h-11 rounded-2xl bg-[#173F33] flex items-center justify-center shadow-sm">
                    <span className="text-lg font-extrabold text-white tracking-tight">G</span>
                    <span className="absolute -bottom-1 -right-1 w-4 h-4 rounded-full bg-white flex items-center justify-center border border-gray-100">
                        <span className="w-2.5 h-2.5 rounded-full bg-emerald-500" />
                    </span>
                </div>

                <div>
                    <div className="text-xl font-bold text-[#173F33] tracking-tight leading-none">
                        GutFriendly
                    </div>
                    <div className="text-[11px] uppercase tracking-widest text-gray-400 mt-1">
                        Admin Console
                    </div>
                </div>

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

        </div>

    )

}
