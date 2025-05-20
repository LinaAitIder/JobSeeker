import axios from 'axios';
import AuthService from '../services/AuthService';
import { Navigate } from 'react-router-dom';

const API_BASE_URL = "http://localhost:8080/api";

//Creating an axios instance
const api = axios.create({
    baseURL: API_BASE_URL,
})

// Handling the request interceptor instead of having to add the tokeneahc time in requests that are sent to the back-end
api.interceptors.request.use(
    (config)=> {
        const token = AuthService.getCurrentUser()?.token;
        if(token){
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    }, (error)=>{
        return Promise.reject(error);
    }
)

//Handling token expiration  // By refreshing it 
api.interceptors.response.use(
    (response) => response,
    (error) => {
        if(error.response?.status === 401){
            AuthService.logout();
            Navigate('/login');
        }
        return Promise.reject(error);
    }
);

export default api;

// Using this method, there might be bugs 
// Possibility to get 401 errors that are not related to 
// Need o refresh the token instead