import axios from 'axios';
import {Navigate} from "react-router-dom";
const API_BASE_URL = process.env.REACT_APP_API_URL;

class AuthService{

    registerCandidat(candidate){
        return axios.post(`${API_BASE_URL}/auth/register/candidat`,  candidate);
    }

    registerRecruiter(recruiter){
        return axios.post(`${API_BASE_URL}/auth/register/recruteur`, recruiter);
    }

    login(credentials){
        try {
            const response = axios.post(`${API_BASE_URL}/auth/login`, credentials);
            return response;
        } catch(error){
            console.error("Full error :",error);
        }
    }

    logout(){
        localStorage.removeItem('user');
    }

    // No need for this anymore since we used the axios interceptors
    // getAuthHeader(){
    //     const user = JSON.parse(localStorage.getItem('user'));
    //     if (user && user.token){
    //         return {
    //              Authorization: 'Bearer' + user.token
    //         };} else {
    //             return {};
    //         }
    // }

    getCurrentUser(){
        return JSON.parse(localStorage.getItem('user'));
    }

    updatePassword(credentials){
        return axios.patch(`${API_BASE_URL}/auth/password`, credentials)
    }

    emailExist(email){
        return axios.get(`${API_BASE_URL}/auth/exists/email`, {params:{email:email}}).then(response=>
            response
        ).catch(error=>{
            const errorMessage = error.response?.data?.message || error.message;
            throw new Error(errorMessage);
        });
    }


   
}

export default new AuthService();