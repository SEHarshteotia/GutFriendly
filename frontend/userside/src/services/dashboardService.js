import api from "./adminApi";

export const getDashboardSummary = async () => {
    const response = await api.get("/admin/dashboard/summary");
    return response.data;
};

export const getMonthlyTrends = async () => {
    const response = await api.get("/admin/dashboard/monthly-trends");
    return response.data;
};

export const getCategoryPerformance = async () => {
    const response = await api.get("/admin/dashboard/category-performance");
    return response.data;
};

export const getRecentActivities = async () => {
    const response = await api.get("/admin/dashboard/recent-activities");
    return response.data;
};

export async function getUpcomingInspections() {

    const response = await api.get("/admin/dashboard/upcoming-inspections");

    return response.data;

}