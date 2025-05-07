// src/routes/authRoute.js
import { Navigate, Outlet } from 'react-router-dom';
import authService from '../Components/services/AuthService';

const AuthRoute = () => {
    const user = authService.getCurrentUser();

    if (user) {
        // Redirect based on user role
        const redirectPath = user.role === 'RECRUITER' 
            ? '/recruiterProfile' 
            : '/candidateProfile';
        return <Navigate to={redirectPath} replace />;
    }

    return <Outlet />;
};

export default AuthRoute;