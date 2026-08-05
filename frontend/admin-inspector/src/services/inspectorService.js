import api from "./api";

function resolveInspectorId(inspectorId) {
    if (inspectorId !== undefined && inspectorId !== null && inspectorId !== "") {
        return inspectorId;
    }

    const storedId = localStorage.getItem("inspectorId");

    if (!storedId) {
        throw new Error("Inspector session not found. Please sign in again.");
    }

    return storedId;
}

export function getApiErrorMessage(error, fallback = "Request failed") {
    return error?.response?.data?.message ?? fallback;
}

export const getInspectionById = async (inspectionId) => {
    const response = await api.get(`/inspector/inspection/${inspectionId}`);
    return response.data;
};

export const getAllTests = async () => {
    const response = await api.get("/inspector/tests");
    return response.data;
};

export const getInspectionTestResults = async (inspectionId) => {
    const response = await api.get(
        `/inspector/inspection/${inspectionId}/test-results`
    );
    return response.data ?? [];
};

export const startInspection = async (inspectionId) => {
    const response = await api.patch(
        `/inspector/inspection/${inspectionId}/start`
    );
    return response.data;
};

export const saveTestResult = async (inspectionId, data) => {
    const response = await api.post(
        `/inspector/inspection/${inspectionId}/test-results`,
        data
    );
    return response.data;
};

export const submitInspection = async (
    inspectionId,
    inspectorRemarks,
    recommendation
) => {
    const response = await api.patch(
        `/inspector/inspection/${inspectionId}/submit`,
        {
            inspectorRemarks,
            recommendation,
        }
    );

    return response.data;
};

export const getAssignedInspections = async (
    inspectorId,
    page = 0,
    size = 10,
    sortBy = "inspectionDate",
    direction = "DESC"
) => {
    const id = resolveInspectorId(inspectorId);

    const response = await api.get(`/inspector/${id}/inspections`, {
        params: {
            page,
            size,
            sortBy,
            direction,
        },
    });

    return response.data;
};

/**
 * Inspector history: backend has no GET /inspector/{id}/history.
 * Uses admin endpoint GET /admin/inspections/inspector/{inspectorId}.
 */
export const getInspectionHistory = async (
    inspectorId,
    page = 0,
    size = 10,
    sortBy = "inspectionDate",
    direction = "DESC"
) => {
    const id = resolveInspectorId(inspectorId);

    const response = await api.get(`/admin/inspections/inspector/${id}`, {
        params: {
            page,
            size,
            sortBy,
            direction,
        },
    });

    return response.data;
};

/**
 * Lists inspectors available for admin assignment.
 */
export const getAllInspectors = async () => {
    const response = await api.get("/admin/inspections/inspectors");
    return response.data;
};
