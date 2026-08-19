import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

/**
 * Admin settings.
 *
 * Only session-level options are offered here. The backend does not yet
 * expose endpoints for changing admin credentials or platform configuration,
 * so nothing on this page silently pretends to persist server-side state.
 */
const Settings = () => {

    const navigate = useNavigate();
    const [admin, setAdmin] = useState(null);

    useEffect(() => {
        try {
            const raw = localStorage.getItem("admin");
            if (raw) setAdmin(JSON.parse(raw));
        } catch (err) {
            console.log(err);
        }
    }, []);

    const handleSignOut = () => {
        localStorage.removeItem("role");
        localStorage.removeItem("admin");
        localStorage.removeItem("inspectorId");
        navigate("/login");
    };

    return (

        <div>

            <div className="mb-8">
                <h1 className="text-3xl font-bold text-[#173F33]">Settings</h1>
                <p className="text-sm text-gray-500 mt-1">
                    Manage your admin session
                </p>
            </div>

            <div className="space-y-6 max-w-2xl">

                <div className="bg-white border border-gray-200 rounded-xl p-6">

                    <h2 className="text-lg font-semibold text-[#173F33] mb-1">
                        Session
                    </h2>
                    <p className="text-sm text-gray-500 mb-5">
                        You are signed in as{" "}
                        <span className="font-medium text-gray-700">
                            {admin?.email || "an administrator"}
                        </span>.
                    </p>

                    <button
                        onClick={handleSignOut}
                        className="px-4 py-2 text-sm font-medium text-white bg-[#173F33] rounded-lg hover:bg-[#0f2c24]"
                    >
                        Sign out
                    </button>

                </div>

                <div className="bg-white border border-gray-200 rounded-xl p-6">

                    <h2 className="text-lg font-semibold text-[#173F33] mb-1">
                        Account
                    </h2>
                    <p className="text-sm text-gray-500 mb-5">
                        Review the account details recorded for this admin.
                    </p>

                    <button
                        onClick={() => navigate("/admin/profile")}
                        className="px-4 py-2 text-sm border border-gray-200 rounded-lg hover:bg-[#F7F8F7]"
                    >
                        View Profile
                    </button>

                </div>

                <div className="bg-white border border-gray-200 rounded-xl p-6">

                    <h2 className="text-lg font-semibold text-[#173F33] mb-1">
                        Password
                    </h2>
                    <p className="text-sm text-gray-500">
                        Changing your password is not available from the console yet.
                        Contact the platform administrator to have it updated.
                    </p>

                </div>

            </div>

        </div>

    );

};

export default Settings;
