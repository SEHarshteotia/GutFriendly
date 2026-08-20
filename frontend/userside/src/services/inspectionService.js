// Uses the shared instance so every call carries the login token.
import axios from "./adminApi";


const API_URL = `${import.meta.env.VITE_API_BASE_URL ?? ""}/admin/inspections`;


export const getAllInspections = async (
    page = 0,
    size = 10,
    sortBy = "inspectionDate",
    direction = "DESC"
) => {

    const response = await axios.get(API_URL, {
        params:{
            page,
            size,
            sortBy,
            direction
        }
    });

    return response.data;
};



export const getInspectionById = async (inspectionId)=>{

    const response = await axios.get(
        `${API_URL}/${inspectionId}`
    );

    return response.data;
};



export const getAllInspectors = async ()=>{

    const response = await axios.get(
        `${API_URL}/inspectors`
    );

    return response.data;
};



export const assignInspector = async (
    inspectionId,
    inspectorId
)=>{

    const response = await axios.patch(
        `${API_URL}/${inspectionId}/assign/${inspectorId}`
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

    const response = await axios.get(
        `${API_URL}/status/${status}`,
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

export const getInspectionsByShop = async (
    shopId,
    page = 0,
    size = 10,
    sortBy = "inspectionDate",
    direction = "DESC"
) => {

    const response = await axios.get(
        `${API_URL}/shop/${shopId}`,
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

export const reviewInspection = async (inspectionId) => {

    const response = await axios.patch(
        `${API_URL}/${inspectionId}/review`
    );

    return response.data;
};

export const approveInspection = async (inspectionId) => {

    const response = await axios.patch(
        `${API_URL}/${inspectionId}/approve`
    );

    return response.data;
};

export const rejectInspection = async (
    inspectionId,
    inspectionRejectionReason
) => {

    const response = await axios.patch(
        `${API_URL}/${inspectionId}/reject`,
        {
            inspectionRejectionReason
        }
    );

    return response.data;
};

export const sendForReInspection = async (
    inspectionId,
    reInspectionRequestReason
) => {

    const response = await axios.patch(
        `${API_URL}/${inspectionId}/reinspection`,
        {
            reInspectionRequestReason
        }
    );

    return response.data;
};

export const getInspectionDetails = async (inspectionId) => {

    const response = await axios.get(
        `${API_URL}/${inspectionId}/details`
    );

    return response.data;
};
