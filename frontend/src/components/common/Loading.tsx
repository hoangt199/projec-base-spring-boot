import React from 'react';
import { Spin } from 'antd';
import { LoadingOutlined } from '@ant-design/icons';

interface LoadingProps {
  tip?: string;
  size?: 'small' | 'default' | 'large';
  fullScreen?: boolean;
}

const Loading: React.FC<LoadingProps> = ({ 
  tip = 'Đang tải...', 
  size = 'large',
  fullScreen = false 
}) => {
  const antIcon = <LoadingOutlined style={{ fontSize: size === 'small' ? 24 : size === 'large' ? 40 : 32 }} spin />;
  
  if (fullScreen) {
    return (
      <div className="fixed inset-0 flex items-center justify-center bg-white bg-opacity-80 z-50">
        <Spin indicator={antIcon} tip={tip} size={size} />
      </div>
    );
  }
  
  return (
    <div className="flex items-center justify-center p-8">
      <Spin indicator={antIcon} tip={tip} size={size} />
    </div>
  );
};

export default Loading;