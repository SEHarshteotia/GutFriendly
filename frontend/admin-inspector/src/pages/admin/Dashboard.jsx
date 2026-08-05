import { useEffect, useState } from "react";

import {
    FaStore,
    FaClipboardCheck,
    FaAward,
    FaStar,
    FaUserCheck,
    FaExclamationTriangle
} from "react-icons/fa";

import {
    LineChart,
    Line,
    XAxis,
    YAxis,
    CartesianGrid,
    Tooltip,
    ResponsiveContainer
} from "recharts";

import {
    BarChart,
    Bar,
} from "recharts";

import StatCard from "../../components/ui/StatCard";

import { getDashboardSummary, getMonthlyTrends, getCategoryPerformance, getRecentActivities, getUpcomingInspections } from "../../services/dashboardService";

export default function Dashboard() {

    const [summary, setSummary] = useState(null);
    const [monthlyTrends, setMonthlyTrends] = useState([]);
    const [categoryPerformance, setCategoryPerformance] = useState([]);
    const [recentActivities, setRecentActivities] = useState([]);
    const [upcomingInspections, setUpcomingInspections] = useState([]);

    useEffect(() => {

        loadSummary();
        loadMonthlyTrends();
        loadCategoryPerformance();
        loadRecentActivities();
        loadUpcomingInspections();

    }, []);

    async function loadSummary() {

        try {

            const data = await getDashboardSummary();

            setSummary(data);

        }

        catch (error) {

            console.error("Dashboard Error:", error);

        }

    }

    async function loadMonthlyTrends() {

        try {

            const data = await getMonthlyTrends();

            setMonthlyTrends(data);

        }
        catch (error) {

            console.log(error);

        }

    }

    async function loadCategoryPerformance() {

        try {

            const data = await getCategoryPerformance();

            setCategoryPerformance(data);

        }
        catch (error) {

            console.log(error);

        }

    }

    async function loadRecentActivities() {

        try {

            const data = await getRecentActivities();

            setRecentActivities(data);

        }
        catch (error) {

            console.log(error);

        }

    }

    async function loadUpcomingInspections() {

        try {

            const data = await getUpcomingInspections();

            setUpcomingInspections(data);

        }

        catch (error) {

            console.log(error);

        }

    }

    if (summary == null) {

        return (
            <div className="flex flex-col items-center justify-center h-[70vh] gap-3">
                <div className="w-10 h-10 rounded-full border-[3px] border-emerald-200 border-t-[#173F33] animate-spin" />
                <h2 className="text-sm font-medium text-gray-400">
                    Loading dashboard...
                </h2>
            </div>
        );

    }

    return (

        <div>

            <div className="mb-8">
                <h1 className="text-3xl font-bold text-[#173F33]">
                    Welcome Back, Admin 👋
                </h1>

                <p className="text-gray-500 mt-1">
                    Here's what's happening in GutFriendly today.
                </p>

                <p className="text-gray-400 text-sm mt-1">
                    An overview of shops, inspections and trust scores
                </p>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">

                <StatCard
                    title="Verified Shops"
                    value={summary.totalVerifiedVendors}
                    icon={<FaUserCheck />}
                    tone="emerald"
                />

                <StatCard
                    title="Active Inspections"
                    value={summary.activeInspections}
                    icon={<FaClipboardCheck />}
                    tone="blue"
                />

                <StatCard
                    title="Pending Shop Approvals"
                    value={summary.pendingVendorApprovals}
                    icon={<FaStore />}
                    tone="amber"
                />

                


                <StatCard
                    title="Average Gut Trust Score"
                    value={summary.averageGutTrustScore}
                    icon={<FaStar />}
                    tone="pink"
                />

            </div>



            <div className="bg-white rounded-xl shadow-sm border border-gray-100 mt-8 p-6">

                <div className="flex items-center gap-3 mb-6">
                    <span className="w-9 h-9 rounded-lg bg-blue-50 text-blue-600 flex items-center justify-center">
                        <FaClipboardCheck size={15} />
                    </span>
                    <h2 className="text-lg font-semibold text-[#173F33]">
                        Monthly Trust Trends
                    </h2>
                </div>

                <ResponsiveContainer width="100%" height={300}>

                    <LineChart data={monthlyTrends}>

                        <CartesianGrid strokeDasharray="3 3" stroke="#eef0f2" vertical={false} />

                        <XAxis dataKey="month" tick={{ fill: "#9ca3af", fontSize: 12 }} axisLine={false} tickLine={false} />

                        <YAxis tick={{ fill: "#9ca3af", fontSize: 12 }} axisLine={false} tickLine={false} />

                        <Tooltip
                            contentStyle={{
                                borderRadius: 12,
                                border: "1px solid #eef0f2",
                                boxShadow: "0 8px 24px rgba(15,23,42,0.08)"
                            }}
                        />

                        <Line
                            type="monotone"
                            dataKey="score"
                            stroke="#173F33"
                            strokeWidth={3}
                            dot={{ r: 4, fill: "#173F33", strokeWidth: 0 }}
                            activeDot={{ r: 6 }}
                        />

                    </LineChart>

                </ResponsiveContainer>

            </div>

            <div className="bg-white rounded-xl shadow-sm border border-gray-100 mt-8 p-6">

                <div className="flex items-center gap-3 mb-6">
                    <span className="w-9 h-9 rounded-lg bg-emerald-50 text-emerald-600 flex items-center justify-center">
                        <FaStore size={15} />
                    </span>
                    <h2 className="text-lg font-semibold text-[#173F33]">
                        Category Performance
                    </h2>
                </div>

                {
                    categoryPerformance.length === 0 ?

                        <p className="text-sm text-gray-400 py-10 text-center">
                            No category data available.
                        </p>

                        :

                        <ResponsiveContainer width="100%" height={300}>

                            <BarChart data={categoryPerformance}>

                                <CartesianGrid strokeDasharray="3 3" stroke="#eef0f2" vertical={false} />

                                <XAxis dataKey="category" tick={{ fill: "#9ca3af", fontSize: 12 }} axisLine={false} tickLine={false} />

                                <YAxis tick={{ fill: "#9ca3af", fontSize: 12 }} axisLine={false} tickLine={false} />

                                <Tooltip
                                    cursor={{ fill: "#f8fafc" }}
                                    contentStyle={{
                                        borderRadius: 12,
                                        border: "1px solid #eef0f2",
                                        boxShadow: "0 8px 24px rgba(15,23,42,0.08)"
                                    }}
                                />

                                <Bar
                                    dataKey="averageScore"
                                    fill="#22c55e"
                                    radius={[6, 6, 0, 0]}
                                />

                            </BarChart>

                        </ResponsiveContainer>

                }

            </div>

            <div className="bg-white rounded-xl shadow-sm border border-gray-100 mt-8 p-6">

                <div className="flex items-center gap-3 mb-6">
                    <span className="w-9 h-9 rounded-lg bg-amber-50 text-amber-600 flex items-center justify-center">
                        <FaExclamationTriangle size={15} />
                    </span>
                    <h2 className="text-lg font-semibold text-[#173F33]">
                        Recent Activities
                    </h2>
                </div>

                {
                    recentActivities.length === 0 ?

                        (
                            <p className="text-sm text-gray-400 py-10 text-center">
                                No recent activities.
                            </p>
                        )

                        :

                        (
                            <div className="divide-y divide-gray-100">

                                {
                                    recentActivities.map((activity, index) => (

                                        <div
                                            key={index}
                                            className="py-4 first:pt-0 last:pb-0 flex items-start gap-3"
                                        >

                                            <span className="mt-1.5 w-2 h-2 rounded-full bg-emerald-400 shrink-0" />

                                            <div>
                                                <p className="font-semibold text-gray-900 text-sm">
                                                    {activity.activityType}
                                                </p>

                                                <p className="text-gray-500 text-sm mt-0.5">
                                                    {activity.message}
                                                </p>

                                                <p className="text-xs font-mono text-gray-400 mt-1">
                                                    {activity.time}
                                                </p>
                                            </div>

                                        </div>

                                    ))
                                }

                            </div>
                        )
                }

            </div>

            <div className="bg-white rounded-xl shadow-sm border border-gray-100 mt-8 p-6">

                <div className="flex items-center gap-3 mb-6">
                    <span className="w-9 h-9 rounded-lg bg-purple-50 text-purple-600 flex items-center justify-center">
                        <FaAward size={15} />
                    </span>
                    <h2 className="text-lg font-semibold text-[#173F33]">
                        Upcoming Inspections
                    </h2>
                </div>

                {
                    upcomingInspections.length === 0 ?

                        (
                            <p className="text-sm text-gray-400 py-10 text-center">
                                No upcoming inspections.
                            </p>
                        )

                        :

                        (
                            <div className="divide-y divide-gray-100">

                                {
                                    upcomingInspections.map((inspection, index) => (

                                        <div
                                            key={index}
                                            className="py-4 first:pt-0 last:pb-0 flex items-center justify-between gap-4"
                                        >

                                            <div>
                                                <p className="font-semibold text-gray-900 text-sm">
                                                    {inspection.shopName}
                                                </p>

                                                <p className="text-gray-500 text-sm mt-0.5">
                                                    Vendor: {inspection.vendorName}
                                                </p>

                                                <p className="text-xs font-mono text-gray-400 mt-1">
                                                    {inspection.inspectionDate}
                                                </p>
                                            </div>

                                            <span className="text-xs font-medium px-2.5 py-1 rounded-full bg-blue-50 text-blue-700 whitespace-nowrap">
                                                {inspection.status}
                                            </span>

                                        </div>

                                    ))
                                }

                            </div>
                        )
                }

            </div>

        </div>

    );

}
