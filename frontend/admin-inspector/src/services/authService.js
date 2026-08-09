
import axios from "axios";

const API_BASE_URL =
    import.meta.env.VITE_API_BASE_URL ?? "";

export const adminLogin = async (email, password) => {

    const response = await axios.post(
        `${API_BASE_URL}/admin/login`,
        {
            email,
            password
        }
    );

    return response.data;
};

export const inspectorLogin = async (email, password) => {
    const response = await axios.post(
        `${API_BASE_URL}/inspector/login`,
        {
            email,
            password
        }
    );

    return response.data;
};
