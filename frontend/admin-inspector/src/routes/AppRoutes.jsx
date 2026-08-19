import { Routes, Route } from "react-router-dom";

import AdminLayout from "../layouts/AdminLayout";
import InspectorLayout from "../layouts/InspectorLayout";

import Dashboard from "../pages/admin/Dashboard";
import Shops from "../pages/admin/Shops";
import ShopDetails from "../pages/admin/ShopDetails";
import Inspections from "../pages/admin/Inspections";
import InspectionDetails from "../pages/admin/InspectionDetails";
import Inspectors from "../pages/admin/Inspectors";
import ShopInspectionHistory from "../pages/admin/ShopInspectionHistory";
import InspectorDashboard from "../pages/inspector/InspectorDashboard";
import AssignedInspections from "../pages/inspector/AssignedInspections";
import InspectionForm from "../pages/inspector/InspectionForm";
import InspectionHistory from "../pages/inspector/InspectionHistory";
import Reviews from "../pages/admin/Reviews";
import Profile from "../pages/admin/Profile";
import Settings from "../pages/admin/Settings";

import NotFound from "../pages/common/NotFound";
import ProtectedRoute from "../components/ProtectedRoute";
import Login from "../pages/auth/Login";
import { Navigate } from "react-router-dom";
export default function AppRoutes() {

    return (

        <Routes>
          <Route path="/" element={<Navigate to="/login" />} />

          <Route path="/login" element={<Login />} />

            {/* ADMIN */}

            <Route path="/admin" element={<ProtectedRoute role="ADMIN">
            <AdminLayout />
        </ProtectedRoute>}>

                <Route path="dashboard" element={<Dashboard />} />

                <Route path="shops" element={<Shops />} />

                <Route path="shops/:shopId" element={<ShopDetails />} />

                

     <Route
    path="shops/:shopId/inspections"
    element={<ShopInspectionHistory />}
/>
                <Route path="inspections" element={<Inspections />} />
             <Route
                    path="inspections/:inspectionId"
                    element={<InspectionDetails />}
                />

                <Route
                    path="inspectors"
                    element={<Inspectors />}
                />


                                <Route
                                    path="reviews"
                                    element={<Reviews />}
                                />

                                <Route
                                    path="profile"
                                    element={<Profile />}
                                />

                                <Route
                                    path="settings"
                                    element={<Settings />}
                                />
                
                          

            </Route>

            

            {/* INSPECTOR */}

            <Route path="/inspector" element={   <ProtectedRoute role="INSPECTOR">
            <InspectorLayout />
        </ProtectedRoute>}>

                <Route
                    path="dashboard"
                    element={<InspectorDashboard />}
                />

                <Route
                    path="assigned"
                    element={<AssignedInspections />}
                />

                <Route
                    path="inspection/:inspectionId"
                    element={<InspectionForm />}
                />

                <Route
                    path="history"
                    element={<InspectionHistory />}
                />

            </Route>

            <Route path="*" element={<NotFound />} />

        </Routes>

    );

}