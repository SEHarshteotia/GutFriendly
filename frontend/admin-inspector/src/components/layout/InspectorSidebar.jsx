import {
    FaClipboardCheck,
    FaHistory,
    FaChartBar,
    FaUser
} from "react-icons/fa";

import { NavLink } from "react-router-dom";
import GutFriendlyLogo from "@shared/GutFriendlyLogo";

export default function InspectorSidebar({ isOpen = false, onClose }) {

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
            path: "/inspector/profile"
        }

    ];

    return (

        <aside
            className={`portal-sidebar w-72 h-screen ${
                isOpen ? "is-open" : ""
            }`}
        >

            <div className="border-b p-7 flex items-center justify-between gap-3">
                <GutFriendlyLogo
                    size="md"
                    subtitle="Inspector Portal"
                />

                {/* Only shown on a phone, where the sidebar is a drawer. */}
                <button
                    type="button"
                    onClick={onClose}
                    aria-label="Close menu"
                    className="gf-drawer-close text-gray-400 hover:text-[#087454] text-xl leading-none px-2"
                >
                    &times;
                </button>
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
