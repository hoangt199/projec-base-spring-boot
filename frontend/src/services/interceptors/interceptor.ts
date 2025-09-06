import axios from 'axios';
import type { AxiosRequestConfig, AxiosResponse, AxiosError, AxiosRequestHeaders, RawAxiosRequestHeaders } from 'axios';
import { store } from '../../store';
import { clearCredentials, setCredentials } from '../../store/slices/authSlice';
import axiosInstance from '../api/axios';
import { API_ENDPOINTS } from '../../config/api.config';
import { isTokenExpired } from '../../utils';

// Request interceptor
export const requestInterceptor = (config: AxiosRequestConfig): AxiosRequestConfig => {
  const { auth } = store.getState();
  const { accessToken } = auth;

  if (accessToken) {
    if (!config.headers) {
      config.headers = {} as RawAxiosRequestHeaders;
    }
    if (config.headers && typeof config.headers === 'object') {
      (config.headers as RawAxiosRequestHeaders).Authorization = `Bearer ${accessToken}`;
    }
  }

  return config;
};

// Response interceptor
export const responseInterceptor = (response: AxiosResponse): AxiosResponse => {
  return response;
};

// Error interceptor
export const errorInterceptor = async (error: AxiosError) => {
  const originalRequest = error.config as AxiosRequestConfig & { _retry?: boolean };
  const { auth } = store.getState();
  const { refreshToken } = auth;

  // If error is 401 Unauthorized and we have a refresh token
  if (
    error.response?.status === 401 &&
    refreshToken &&
    !originalRequest._retry &&
    // Don't retry for refresh token endpoint to avoid infinite loop
    !originalRequest.url?.includes('refresh-token')
  ) {
    originalRequest._retry = true;

    try {
      // Try to refresh the token
      const response = await axios.post(
        API_ENDPOINTS.AUTH.REFRESH_TOKEN,
        { refreshToken },
        { baseURL: axiosInstance.defaults.baseURL }
      );

      const { accessToken: newAccessToken, refreshToken: newRefreshToken } = response.data;

      // Update tokens in store
      store.dispatch(
        setCredentials({
          accessToken: newAccessToken,
          refreshToken: newRefreshToken,
          user: auth.user || undefined,
        })
      );

      // Retry the original request with new token
      if (!originalRequest.headers) {
        originalRequest.headers = {};
      }
      if (originalRequest.headers && typeof originalRequest.headers === 'object') {
        originalRequest.headers.Authorization = `Bearer ${newAccessToken}`;
      }
      return axiosInstance(originalRequest);
    } catch (refreshError) {
      // If refresh token is invalid, logout the user
      store.dispatch(clearCredentials());
      return Promise.reject(refreshError);
    }
  }

  // Check if token is expired
  if (error.response?.status === 401 && auth.accessToken && isTokenExpired(auth.accessToken)) {
    // If token is expired and we don't have a refresh token, logout the user
    store.dispatch(clearCredentials());
  }

  return Promise.reject(error);
};