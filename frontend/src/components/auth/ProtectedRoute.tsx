import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import { useSelector } from 'react-redux';
import { RootState } from '../../store';
import { ROUTES } from '../../config/routes.config';

interface ProtectedRouteProps {
  redirectPath?: string;
}

const ProtectedRoute: React.FC<ProtectedRouteProps> = ({
  redirectPath = ROUTES.AUTH.LOGIN,
}) => {
  const { accessToken, user } = useSelector((state: RootState) => state.auth);
  
  // Check if user is authenticated
  const isAuthenticated = !!accessToken && !!user;
  
  // If not authenticated, redirect to login page
  if (!isAuthenticated) {
    return <Navigate to={redirectPath} replace />;
  }
  
  // If authenticated, render the child routes
  return <Outlet />;
};

export default ProtectedRoute;