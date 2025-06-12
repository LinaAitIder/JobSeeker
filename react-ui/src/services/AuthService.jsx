import axios from 'axios';
const API_BASE_URL = process.env.REACT_APP_API_URL;
const user = JSON.parse(localStorage.getItem('user'));

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
        console.log(user);
        if(user.role === 'CANDIDAT'){
            localStorage.removeItem('candidat');
        } else if (user.role === 'RECRUTEUR'){
            localStorage.removeItem('recruiter');
        }
        localStorage.removeItem('user');

    }

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