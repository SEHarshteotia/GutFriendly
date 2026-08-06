import {
    FaClipboardCheck,
    FaHistory,
    FaChartBar,
    FaUser
} from "react-icons/fa";

import { NavLink } from "react-router-dom";
import GutFriendlyLogo from "@shared/GutFriendlyLogo";

export default function InspectorSidebar() {

    const menu = [

        {
            name: "Dashboard",
            icon: <FaChartBar />,
            path: "/inspector/dashboard"
        },

        {
            name: "Assigned Inspections",
            icon: <FaClipboardCheck />,
            path: "/inspector/assigned"
        },

        {
            name: "Inspection History",
            icon: <FaHistory />,
            path: "/inspector/history"
        },

        {
            name: "Profile",
            icon: <FaUser />,
            path: "#"
        }

    ];

    return (

        <div className="w-72 bg-[#0F172A] text-white h-screen">

            <div className="border-b border-slate-700 p-8">
                <GutFriendlyLogo
                    size="md"
                    theme="dark"
                    subtitle="Inspector Portal"
                />
            </div>

            <div className="mt-6">

                {

                    menu.map(item => (

                        <NavLink

                            key={item.name}

                            to={item.path}

                            className={({ isActive }) =>

                                `flex items-center gap-4 px-8 py-4 transition

                                ${

                                    isActive

                                        ? "bg-green-600"

                                        : "hover:bg-slate-800"

                                }`

                            }

                        >

                            {item.icon}

                            {item.name}

                        </NavLink>

                    ))

                }

            </div>

        </div>

    );

}