/**
 * Theme Configuration for Ant Design
 */

import type { ThemeConfig } from 'antd';

// Light theme configuration
export const lightTheme: ThemeConfig = {
  token: {
    colorPrimary: '#1677ff',
    colorSuccess: '#52c41a',
    colorWarning: '#faad14',
    colorError: '#ff4d4f',
    colorInfo: '#1677ff',
    borderRadius: 6,
  },
  components: {
    Button: {
      colorPrimary: '#1677ff',
      algorithm: true,
    },
    Input: {
      colorPrimary: '#1677ff',
    },
  },
};

// Dark theme configuration
export const darkTheme: ThemeConfig = {
  token: {
    colorPrimary: '#1668dc',
    colorSuccess: '#49aa19',
    colorWarning: '#d89614',
    colorError: '#dc4446',
    colorInfo: '#1668dc',
    borderRadius: 6,
  },
  components: {
    Button: {
      colorPrimary: '#1668dc',
      algorithm: true,
    },
    Input: {
      colorPrimary: '#1668dc',
    },
  },
};