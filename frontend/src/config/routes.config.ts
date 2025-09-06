/**
 * Routes Configuration
 */

export const ROUTES = {
  // Auth routes
  AUTH: {
    LOGIN: '/login',
    REGISTER: '/register',
    FORGOT_PASSWORD: '/forgot-password',
    RESET_PASSWORD: '/reset-password',
  },
  
  // Dashboard routes
  DASHBOARD: '/',
  
  // User routes
  USER: {
    LIST: '/users',
    DETAIL: (id: string = ':id') => `/users/${id}`,
    CREATE: '/users/create',
    EDIT: (id: string = ':id') => `/users/${id}/edit`,
    PROFILE: '/profile',
  },
  
  // File routes
  FILE: {
    LIST: '/files',
    DETAIL: (id: string = ':id') => `/files/${id}`,
    FOLDER: (id: string = ':id') => `/folders/${id}`,
  },
  
  // Error routes
  ERROR: {
    NOT_FOUND: '/404',
    FORBIDDEN: '/403',
    SERVER_ERROR: '/500',
  },
};