import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

/**
 * Shows the details of the currently signed-in admin.
 *
 * The backend does not expose an authenticated "current admin" endpoint yet,
 * so the values shown here are the ones returned by POST /admin/login and
 * stored in localStorage at sign-in time.
 */
const Profile = () => {

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

    const fullName = admin
        ? `${admin.firstName ?? ""} ${admin.lastName ?? ""}`.trim()
        : "";

    const initials = fullName
        ? fullName.split(" ").filter(Boolean).map((p) => p[0]).join("").slice(0, 2).toUpperCase()
        : "AD";

    const rows = [
        { label: "Admin ID", value: admin?.adminId != null ? `#${admin.adminId}` : "-" },
        { label: "First Name", value: admin?.firstName || "-" },
        { label: "Last Name", value: admin?.lastName || "-" },
        { label: "Email", value: admin?.email || "-" },
        { label: "Role", value: localStorage.getItem("role") || "-" },
    ];

    return (

        <div>

            <div className="mb-8">
                <h1 className="text-3xl font-bold text-[#173F33]">Profile</h1>
                <p className="text-sm text-gray-500 mt-1">
                    Your GutFriendly admin account details
                </p>
            </div>

            {!admin && (
                <div className="mb-6 rounded-lg border border-amber-200 bg-amber-50 px-4 py-3 text-sm text-amber-800">
                    No profile details were found for this session. Sign out and sign in
                    again to refresh them.
                </div>
            )}

            <div className="bg-white border border-gray-200 rounded-xl overflow-hidden max-w-2xl">

                <div className="flex items-center gap-5 px-6 py-6 border-b border-gray-100 bg-[#F7F8F7]">

                    <div className="flex items-center justify-center w-16 h-16 rounded-full bg-[#173F33] text-white text-xl font-semibold">
                        {initials}
                    </div>

                    <div>
                        <div className="text-lg font-semibold text-[#173F33]">
                            {fullName || "GutFriendly Admin"}
                        </div>
                        <div className="text-sm text-gray-500">
                            {admin?.email || "-"}
                        </div>
                    </div>

                </div>

                <div className="divide-y divide-gray-100">

                    {rows.map((row) => (
                        <div key={row.label} className="flex items-center justify-between px-6 py-4">
                            <span className="text-xs font-mono uppercase tracking-wider text-gray-500">
                                {row.label}
                            </span>
                            <span className="text-sm text-gray-800">
                                {row.value}
                            </span>
                        </div>
                    ))}

                </div>

            </div>

            <button
                onClick={() => navigate("/admin/settings")}
                className="mt-6 px-4 py-2 text-sm border border-gray-200 rounded-lg hover:bg-[#F7F8F7]"
            >
                Go to Settings
            </button>

        </div>

    );

};

export default Profile;
