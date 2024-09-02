import axios from "axios";

const API_URL = "http://localhost:8080/api/v1/products";

const getProductsByCategoryNameFilter = async (category: any, filters: any) => {
    const params:{[key:string]: any} = new URLSearchParams({
        brandName: filters.brandName,
        minPrice: filters.minPrice,
        maxPrice: filters.maxPrice,
        rating: filters.rating,
        isAvailable: filters.isAvailable,
        page: filters.page || 0,
        size: filters.size || 10,
        sortedBy: filters.sortedBy || 'name',
        sortDirection: filters.sortDirection || 'ASC',
        ...filters.attributes,
    });

    const queryParams = new URLSearchParams();
    Object.keys(params).forEach(key => {
        if (params[key] !== undefined && params[key] !== null) {
            queryParams.append(key, params[key]);
        }
    });

    const response = await axios.get(`${API_URL}/category/${category}?${queryParams}`);

    return response;
};

const getProductByDiscount = async (discountCode: string)=> {
    const response = await axios.get(`${API_URL}/discountCode/${discountCode}`);

    return response;
}

const apiProductService = {
    getProductsByCategoryNameFilter,
    getProductByDiscount
};

export default apiProductService;