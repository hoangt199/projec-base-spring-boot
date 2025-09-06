import axios from 'axios';
import { API_BASE_URL } from '../../config/api.config';
import { requestInterceptor, responseInterceptor, errorInterceptor } from '../interceptors/interceptor';

// Create axios instance with default config
const axiosInstance = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
    Accept: 'application/json',
  },
  timeout: 30000, // 30 seconds
});

// Add request interceptor
axiosInstance.interceptors.request.use(requestInterceptor, (error) => Promise.reject(error));

// Add response interceptor
axiosInstance.interceptors.response.use(responseInterceptor, errorInterceptor);

export default axiosInstance;