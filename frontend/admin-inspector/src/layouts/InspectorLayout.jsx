import { Outlet } from "react-router-dom";

import InspectorSidebar from "../components/layout/InspectorSidebar";
import InspectorNavbar from "../components/layout/InspectorNavbar";

export default function InspectorLayout() {

    return (

        <div className="gf-shell flex h-screen">

            <InspectorSidebar />

            <div className="flex flex-col flex-1">

                <InspectorNavbar />

                <main className="gf-main flex-1 overflow-auto p-8">

                    <Outlet />

                </main>

            </div>

        </div>

    );

}
