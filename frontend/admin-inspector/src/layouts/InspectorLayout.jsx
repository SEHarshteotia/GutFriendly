import { useEffect, useState } from "react";
import { Outlet, useLocation } from "react-router-dom";

import InspectorSidebar from "../components/layout/InspectorSidebar";
import InspectorNavbar from "../components/layout/InspectorNavbar";

export default function InspectorLayout() {

    // Same drawer behaviour as the admin console: off-canvas on a phone,
    // always visible on a larger screen.
    const [menuOpen, setMenuOpen] = useState(false);
    const location = useLocation();

    useEffect(() => {
        setMenuOpen(false);
    }, [location.pathname]);

    return (

        <div className="gf-shell flex h-screen">

            <InspectorSidebar
                isOpen={menuOpen}
                onClose={() => setMenuOpen(false)}
            />

            {menuOpen && (
                <div
                    className="gf-backdrop"
                    onClick={() => setMenuOpen(false)}
                />
            )}

            <div className="flex flex-col flex-1 min-w-0">

                <InspectorNavbar onMenuClick={() => setMenuOpen(true)} />

                <main className="gf-main flex-1 overflow-auto p-8">

                    <Outlet />

                </main>

            </div>

        </div>

    );

}
