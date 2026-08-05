import api from "./api";

/**
 * Normalizes backend dashboard summary fields for the admin UI.
 * Backend typo: totalVerfiedVendors
 */
function normalizeSummary(data) {
    return {
        ...data,
        totalVerifiedVendors: data.totalVerfiedVendors ?? data.totalVerifiedVendors ?? 0,
        activeInspections: data.activeInspections ?? 0,
        pendingVendorApprovals: data.pendingVendorApprovals ?? 0,
        averageGutTrustScore: data.averageGutTrustScore ?? 0,
        pendingComplaints: data.pendingComplaints ?? 0,
        expiringCertificates: data.expiringCertificates ?? 0,
    };
}

export const getDashboardSummary = async () => {
    const response = await api.get("/admin/dashboard/summary");
    return normalizeSummary(response.data);
};

export const getMonthlyTrends = async () => {
    const response = await api.get("/admin/dashboard/monthly-trends");
    return response.data.map((row) => ({
        ...row,
        score: row.averageScore ?? row.score ?? 0,
    }));
};

export const getCategoryPerformance = async () => {
    const response = await api.get("/admin/dashboard/category-performance");
    return response.data;
};

export const getRecentActivities = async () => {
    const response = await api.get("/admin/dashboard/recent-activities");
    return response.data.map((activity) => ({
        ...activity,
        activityType: activity.activityType ?? activity.actitvityType ?? "",
    }));
};

export async function getUpcomingInspections() {
    const response = await api.get("/admin/dashboard/upcoming-inspections");
    return response.data;
}
