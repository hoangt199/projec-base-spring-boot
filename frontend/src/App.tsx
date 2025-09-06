import { useState } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';

import { ConfigProvider, App as AntApp, theme } from 'antd';
import { Provider } from 'react-redux';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { store, useAppSelector } from './store';
import { lightTheme } from './config/theme.config';
import { ROUTES } from './config/routes.config';
import './App.css'

// Components
import { MainLayout, AuthLayout } from './components/layout';
import { ErrorBoundary } from './components/common';
import { ProtectedRoute } from './components/auth';

// Pages
import Login from './pages/auth/Login';
import Register from './pages/auth/Register';
import Dashboard from './pages/Dashboard';
import FileList from './pages/file/FileList';
import UserList from './pages/user/UserList'
import UserProfile from './pages/user/UserProfile';
import NotFound from './pages/NotFound';

// Create a client for React Query
const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      refetchOnWindowFocus: false,
      retry: 1,
    },
  },
});



function App() {
  const [isDarkMode, setIsDarkMode] = useState(false);

  return (
    <ErrorBoundary>
      <Provider store={store}>
        <QueryClientProvider client={queryClient}>
          <ConfigProvider theme={lightTheme}>
            <AntApp>
              <Router>
              <div className="min-h-screen bg-gray-50">
                <Routes>
                  {/* Auth routes */}
                  <Route element={<AuthLayout />}>
                    <Route path={ROUTES.AUTH.LOGIN} element={<Login />} />
                    <Route path={ROUTES.AUTH.REGISTER} element={<Register />} />
                  </Route>
                   
                   {/* Protected routes */}
                   <Route path="/" element={<ProtectedRoute />}>
                    <Route element={<MainLayout />}>
                      <Route index element={<Dashboard />} />
                      <Route path={ROUTES.DASHBOARD} element={<Dashboard />} />
                      <Route path={ROUTES.FILE.LIST} element={<FileList />} />
                      <Route path={ROUTES.USER.LIST} element={<UserList />} />
                      <Route path={ROUTES.USER.PROFILE} element={<UserProfile />} />
                  </Route>
                  </Route>
                  
                  {/* Redirect to dashboard if authenticated, otherwise to login */}
                  <Route
                    path="/"
                    element={<Navigate to={ROUTES.DASHBOARD} replace />}
                  />
                  
                  {/* 404 Not Found route */}
                  <Route path="*" element={<NotFound />} />
                </Routes>
              </div>
              </Router>
            </AntApp>
          </ConfigProvider>
        </QueryClientProvider>
      </Provider>
    </ErrorBoundary>
  )
}

export default App
