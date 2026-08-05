import { Outlet } from "react-router-dom";

import InspectorSidebar from "../components/layout/InspectorSidebar";
import InspectorNavbar from "../components/layout/InspectorNavbar";

export default function InspectorLayout() {

    return (

        <div className="flex h-screen bg-gray-100">

            <InspectorSidebar />

            <div className="flex flex-col flex-1">

                <InspectorNavbar />

                <main className="flex-1 overflow-auto p-6">

                    <Outlet />

                </main>

            </div>

        </div>

    );

}