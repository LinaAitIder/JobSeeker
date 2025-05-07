// src/routes/ProtectedRoutes.js
import { Navigate, Outlet } from 'react-router-dom';
import authService from '../Components/services/AuthService';

const ProtectedRoute = (allowedRole) => {
    const user = authService.getCurrentUser();

    if (!user) {
        return <Navigate to="/login" replace />;
    }

    if (allowedRole && !allowedRole===user.role) {
        return <Navigate to="/unauthorized" replace />; // What is the replace for
    }

    return <Outlet />;
};

export default ProtectedRoute;