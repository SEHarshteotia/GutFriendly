import { useNavigate } from "react-router-dom";
import { clearStaffSession } from "../../services/session";

/*
 * Shows the inspector account that is currently signed in.
 *
 * The details are the ones POST /inspector/login returns, which Login.jsx
 * stores under "inspector". Older sessions only have "inspectorId", so we
 * fall back to that and prompt for a fresh sign-in.
 */
function readInspector() {
    try {
        const stored = localStorage.getItem("inspector");

        if (stored) {
            return JSON.parse(stored);
        }
    } catch {
        // Corrupt JSON is treated the same as no stored profile.
    }

    const inspectorId = localStorage.getItem("inspectorId");

    return inspectorId ? { inspectorId } : null;
}

function Row({ label, value }) {
    return (
        <div className="flex justify-between border-b border-gray-100 py-3 last:border-0">
            <span className="text-sm text-gray-500">{label}</span>
            <span className="text-sm font-medium text-gray-900">
                {value || "\u2014"}
            </span>
        </div>
    );
}

export default function InspectorProfile() {

    const navigate = useNavigate();
    const inspector = readInspector();

    const fullName = inspector
        ? [inspector.firstName, inspector.lastName]
            .filter(Boolean)
            .join(" ")
        : "";

    const initials = fullName
        ? fullName
            .split(" ")
            .map((part) => part[0])
            .join("")
            .toUpperCase()
        : "IN";

    function handleSignOut() {
        // Drops the token as well, so the session really ends here.
        clearStaffSession();
        navigate("/login");
    }

    return (
        <div className="p-8">

            <h1 className="text-2xl font-bold text-gray-800">
                My Profile
            </h1>

            <p className="mt-1 text-gray-500">
                The inspector account currently signed in.
            </p>

            {!inspector && (
                <div className="mt-6 rounded-lg border border-amber-200 bg-amber-50 p-4 text-sm text-amber-800">
                    No profile details were found for this session.
                    Please sign out and sign in again.
                </div>
            )}

            {inspector && (
                <div className="mt-6 max-w-xl rounded-xl border border-gray-200 bg-white p-6">

                    <div className="flex items-center gap-4 border-b border-gray-100 pb-5">
                        <div className="flex h-14 w-14 items-center justify-center rounded-full bg-green-600 text-lg font-semibold text-white">
                            {initials}
                        </div>

                        <div>
                            <p className="text-lg font-semibold text-gray-900">
                                {fullName || "Inspector"}
                            </p>
                            <p className="text-sm text-gray-500">
                                Food Safety Inspector
                            </p>
                        </div>
                    </div>

                    <div className="mt-2">
                        <Row label="Inspector ID" value={inspector.inspectorId} />
                        <Row label="First name" value={inspector.firstName} />
                        <Row label="Last name" value={inspector.lastName} />
                        <Row label="Email" value={inspector.email} />
                        <Row label="Role" value="INSPECTOR" />
                    </div>

                </div>
            )}

            <div className="mt-6 max-w-xl rounded-xl border border-gray-200 bg-white p-6">
                <p className="font-semibold text-gray-900">Session</p>
                <p className="mt-1 text-sm text-gray-500">
                    Sign out to end this session on this device.
                </p>
                <button
                    type="button"
                    onClick={handleSignOut}
                    className="mt-4 rounded-lg bg-gray-900 px-4 py-2 text-sm font-medium text-white hover:bg-gray-800"
                >
                    Sign out
                </button>
            </div>

        </div>
    );
}
