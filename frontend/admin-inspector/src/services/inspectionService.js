import api from "./api";

const INSPECTIONS_BASE = "/admin/inspections";

export const getAllInspections = async (
    page = 0,
    size = 10,
    sortBy = "inspectionDate",
    direction = "DESC"
) => {
    const response = await api.get(INSPECTIONS_BASE, {
        params: {
            page,
            size,
            sortBy,
            direction,
        },
    });

    return response.data;
};

export const getInspectionById = async (inspectionId) => {
    const response = await api.get(`${INSPECTIONS_BASE}/${inspectionId}`);
    return response.data;
};

/**
 * Lists inspectors available for admin assignment.
 */
export const getAllInspectors = async () => {
    const response = await api.get("/admin/inspections/inspectors");
    return response.data;
};

export const assignInspector = async (inspectionId, inspectorId) => {
    const response = await api.patch(
        `${INSPECTIONS_BASE}/${inspectionId}/assign/${inspectorId}`
    );
    return response.data;
};

export const getInspectionsByStatus = async (
    status,
    page = 0,
    size = 10,
    sortBy = "inspectionDate",
    direction = "DESC"
) => {
    const response = await api.get(`${INSPECTIONS_BASE}/status/${status}`, {
        params: {
            page,
            size,
            sortBy,
            direction,
        },
    });

    return response.data;
};

export const getInspectionsByShop = async (
    shopId,
    page = 0,
    size = 10,
    sortBy = "inspectionDate",
    direction = "DESC"
) => {
    const response = await api.get(`${INSPECTIONS_BASE}/shop/${shopId}`, {
        params: {
            page,
            size,
            sortBy,
            direction,
        },
    });

    return response.data;
};

export const reviewInspection = async (inspectionId) => {
    const response = await api.patch(`${INSPECTIONS_BASE}/${inspectionId}/review`);
    return response.data;
};

export const approveInspection = async (inspectionId) => {
    const response = await api.patch(`${INSPECTIONS_BASE}/${inspectionId}/approve`);
    return response.data;
};

export const rejectInspection = async (inspectionId, inspectionRejectionReason) => {
    const response = await api.patch(`${INSPECTIONS_BASE}/${inspectionId}/reject`, {
        inspectionRejectionReason,
    });
    return response.data;
};

export const sendForReInspection = async (inspectionId, reInspectionRequestReason) => {
    const response = await api.patch(`${INSPECTIONS_BASE}/${inspectionId}/reinspection`, {
        reInspectionRequestReason,
    });
    return response.data;
};

/**
 * Composite load: inspection header from admin API + test results from inspector API.
 */
export const getInspectionDetails = async (inspectionId) => {
    const [inspectionResponse, testResultsResponse] = await Promise.all([
        api.get(`${INSPECTIONS_BASE}/${inspectionId}`),
        api.get(`/inspector/inspection/${inspectionId}/test-results`),
    ]);

    return {
        inspection: inspectionResponse.data,
        testResults: testResultsResponse.data ?? [],
    };
};
