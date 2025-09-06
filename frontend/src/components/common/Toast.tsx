import { notification } from 'antd';
import { NotificationPlacement } from 'antd/es/notification/interface';

type NotificationType = 'success' | 'info' | 'warning' | 'error';

interface ToastOptions {
  message: string;
  description?: string;
  duration?: number;
  placement?: NotificationPlacement;
  onClick?: () => void;
}

const Toast = {
  success: (options: ToastOptions) => {
    notification.success({
      message: options.message,
      description: options.description,
      duration: options.duration || 4.5,
      placement: options.placement || 'topRight',
      onClick: options.onClick,
    });
  },
  
  error: (options: ToastOptions) => {
    notification.error({
      message: options.message,
      description: options.description,
      duration: options.duration || 4.5,
      placement: options.placement || 'topRight',
      onClick: options.onClick,
    });
  },
  
  warning: (options: ToastOptions) => {
    notification.warning({
      message: options.message,
      description: options.description,
      duration: options.duration || 4.5,
      placement: options.placement || 'topRight',
      onClick: options.onClick,
    });
  },
  
  info: (options: ToastOptions) => {
    notification.info({
      message: options.message,
      description: options.description,
      duration: options.duration || 4.5,
      placement: options.placement || 'topRight',
      onClick: options.onClick,
    });
  },
};

export default Toast;