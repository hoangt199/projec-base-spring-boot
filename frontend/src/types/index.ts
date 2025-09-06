/**
 * Common Types
 */

// User types
export interface User {
  id: string;
  username: string;
  email: string;
  fullName: string;
  avatar?: string;
  role: string;
  createdAt: string;
  updatedAt: string;
}

// Auth types
export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  accessToken: string;
  refreshToken: string;
  user: User;
}

export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
  fullName: string;
}

// File types
export interface FileItem {
  id: string;
  name: string;
  type: string;
  size: number;
  path: string;
  isFolder: boolean;
  parentId?: string;
  createdBy: string;
  createdAt: string;
  updatedAt: string;
  isPublic: boolean;
}

export interface Folder extends FileItem {
  isFolder: true;
}

export interface File extends FileItem {
  isFolder: false;
  extension: string;
  mimeType: string;
  downloadUrl: string;
}

export interface FilePermission {
  id: string;
  fileId: string;
  userId: string;
  permissionType: 'READ' | 'WRITE' | 'OWNER';
  user?: User;
}

// Notification types
export interface Notification {
  id: string;
  userId: string;
  title: string;
  message: string;
  isRead: boolean;
  type: 'INFO' | 'WARNING' | 'ERROR' | 'SUCCESS';
  createdAt: string;
}

// Pagination types
export interface PaginationParams {
  page: number;
  size: number;
  sort?: string;
}

export interface PaginatedResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
}

// API Response types
export interface ApiResponse<T> {
  data: T;
  message: string;
  status: number;
}