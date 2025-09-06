import React from 'react';
import { Button, Result } from 'antd';
import { useNavigate } from 'react-router-dom';
import { ROUTES } from '../config/routes.config';

const NotFound: React.FC = () => {
  const navigate = useNavigate();

  return (
    <div className="flex items-center justify-center min-h-screen bg-gray-50">
      <Result
        status="404"
        title="404"
        subTitle="Xin lỗi, trang bạn đang tìm kiếm không tồn tại."
        extra={
          <Button 
            type="primary" 
            onClick={() => navigate(ROUTES.DASHBOARD)}
          >
            Quay lại trang chủ
          </Button>
        }
      />
    </div>
  );
};

export default NotFound;