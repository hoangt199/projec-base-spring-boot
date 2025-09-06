/**
 * API Configuration
 */

export const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api';

export const API_ENDPOINTS = {
  // Auth endpoints
  AUTH: {
    LOGIN: '/auth/login',
    REGISTER: '/auth/register',
    REFRESH_TOKEN: '/auth/refresh-token',
    LOGOUT: '/auth/logout',
    FORGOT_PASSWORD: '/auth/forgot-password',
    RESET_PASSWORD: '/auth/reset-password',
  },
  
  // User endpoints
  USER: {
    PROFILE: '/users/profile',
    UPDATE_PROFILE: '/users/profile',
    CHANGE_PASSWORD: '/users/change-password',
    GET_ALL: '/users',
    GET_BY_ID: (id: string) => `/users/${id}`,
    CREATE: '/users',
    UPDATE: (id: string) => `/users/${id}`,
    DELETE: (id: string) => `/users/${id}`,
  },
  
  // File endpoints
  FILE: {
    GET_ALL: '/files',
    GET_BY_ID: (id: string) => `/files/${id}`,
    UPLOAD: '/files/upload',
    DOWNLOAD: (id: string) => `/files/${id}/download`,
    DELETE: (id: string) => `/files/${id}`,
    UPDATE: (id: string) => `/files/${id}`,
    GET_INFO: (id: string) => `/files/${id}/info`,
    SEARCH: '/files/search',
    
    // Folder endpoints
    FOLDER: {
      CREATE: '/folders',
      GET_ALL: '/folders',
      GET_BY_ID: (id: string) => `/folders/${id}`,
      UPDATE: (id: string) => `/folders/${id}`,
      DELETE: (id: string) => `/folders/${id}`,
      GET_CONTENT: (id: string) => `/folders/${id}/content`,
    },
    
    // Permission endpoints
    PERMISSION: {
      GET: (fileId: string) => `/files/${fileId}/permissions`,
      ADD: (fileId: string) => `/files/${fileId}/permissions`,
      UPDATE: (fileId: string, permissionId: string) => `/files/${fileId}/permissions/${permissionId}`,
      DELETE: (fileId: string, permissionId: string) => `/files/${fileId}/permissions/${permissionId}`,
    },
  },
  
  // Notification endpoints
  NOTIFICATION: {
    GET_ALL: '/notifications',
    GET_UNREAD: '/notifications/unread',
    MARK_AS_READ: (id: string) => `/notifications/${id}/read`,
    MARK_ALL_AS_READ: '/notifications/read-all',
    DELETE: (id: string) => `/notifications/${id}`,
  },
};