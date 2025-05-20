// src/routes/authRoute.js
import { Navigate, Outlet } from 'react-router-dom';
import authService from '../services/AuthService';

const AuthRoute = () => {
    const user = authService.getCurrentUser();

    if (user) {
        // Redirect based on user role
        const redirectPath = user.role === 'RECRUTEUR'
            ? '/recruiterProfile' 
            : '/candidateProfile';
        return <Navigate to={redirectPath} replace />;
    }

    return <Outlet />;
};

export default AuthRoute;