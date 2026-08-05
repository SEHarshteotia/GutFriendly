import api from "./adminApi";


export const getInspectionById = async (inspectionId) => {
    const response = await api.get(`/inspector/inspection/${inspectionId}`);
    return response.data;
};

export const getAllTests = async () => {
    const response = await api.get(`/inspector/tests`);
    return response.data;
};

export const getInspectionTestResults = async (inspectionId) => {
    const response = await api.get(
        `/inspector/inspection/${inspectionId}/test-results`
    );
    return response.data;
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
            recommendation
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

    const response = await api.get(
        `/inspector/${inspectorId}/inspections`,
        {
            params: {
                page,
                size,
                sortBy,
                direction
            }
        }
    );

    return response.data;
};

export const getInspectionHistory = async (
    inspectorId,
    page = 0,
    size = 10,
    sortBy = "inspectionDate",
    direction = "DESC"
) => {

    const response = await api.get(
        `/inspector/${inspectorId}/history`,
        {
            params: {
                page,
                size,
                sortBy,
                direction
            }
        }
    );

    return response.data;
};
export const getAllInspectors = async () => {

    const response = await api.get(
        "/admin/inspections/inspectors"
    );

    return response.data;
};