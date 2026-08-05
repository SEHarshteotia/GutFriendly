export default function StatusBadge({ status }) {

    const styles = {

        SCHEDULED:
            "bg-yellow-100 text-yellow-700",

        WAITING:
            "bg-yellow-100 text-yellow-700",

        ASSIGNED:
            "bg-blue-100 text-blue-700",

        IN_PROGRESS:
            "bg-purple-100 text-purple-700",

        REPORT_SUBMITTED:
            "bg-indigo-100 text-indigo-700",

        UNDER_ADMIN_REVIEW:
            "bg-orange-100 text-orange-700",

        APPROVED:
            "bg-green-100 text-green-700",

        REJECTED:
            "bg-red-100 text-red-700",

        CLOSED_FOR_REINSPECTION:
            "bg-pink-100 text-pink-700"

    };

    return (

        <span
            className={`px-3 py-1 rounded-full text-sm font-semibold ${
                styles[status] || "bg-gray-100 text-gray-700"
            }`}
        >
            {status.replaceAll("_"," ")}
        </span>

    );

}