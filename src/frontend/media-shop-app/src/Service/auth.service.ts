import axios from "axios";

const API_URL = "http://localhost:8080/api/v1/auth";

const login = async (username: string, password: string) => {
    return axios.post(`${API_URL}/login`, { username, password })
        .then((response) => {
            if (response.data.accessToken) {
                localStorage.setItem("user", JSON.stringify(response.data));
            }
    });
}

const register = async (email: string, username: string, password: string, firstName: string, lastName: string) => {
    const response = await axios.post(`${API_URL}/signup`, { email, username, password, firstName, lastName });
    return response.data;
}

const logout = () => {
    localStorage.removeItem("user");
}


const AuthService = {
    login,
    register,
    logout
};

export default AuthService;