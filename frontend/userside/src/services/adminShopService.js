import api from "./adminApi";


export const getAllShops = async (
    page = 0,
    size = 10,
    sortBy = "createdAt",
    direction = "DESC"
) => {

    const response = await api.get("/admin/shops", {
        params: {
            page,
            size,
            sortBy,
            direction
        }
    });

    return response.data;
};

export async function searchShops(shopName, page = 0, size = 10) {
    const response = await api.get("/admin/shops/search", {
        params: {
            shopName,
            page,
            size
        }
    });

    return response.data;
}

export async function getShopsByStatus(status,  page = 0,
    size = 10) {

    const response = await api.get(
        `/admin/shops/status/${status}`, { params: {
                page,
                size
            }
        }
    );

    return response.data;

};
export async function getShopsByAvailability(status,   page = 0,
    size = 10) {

    const response = await api.get(
        `/admin/shops/availability-Status/${status}`,  {
            params: {
                page,
                size
            }
        }
    );

    return response.data;

};

export async function getShopById(shopId) {

    const response = await api.get(`/admin/shops/${shopId}`);

    return response.data;

};

export async function blockShop(shopId, reason) {

    const response = await api.patch(
        `/admin/shops/${shopId}/block`,
        {
            blockShopRequestReason: reason
        }
    );

    return response.data;

};

export async function unblockShop(shopId) {

    const response = await api.patch(
        `/admin/shops/${shopId}/unblock`
    );

    return response.data;

};
