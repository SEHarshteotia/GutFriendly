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

        <aside className="portal-sidebar w-72 h-screen">

            <div className="border-b p-7">
                <GutFriendlyLogo
                    size="md"
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

                                        ? "portal-link-active"

                                        : "portal-link"

                                }`

                            }

                        >

                            {item.icon}

                            {item.name}

                        </NavLink>

                    ))

                }

            </div>

        </aside>

    );

}
