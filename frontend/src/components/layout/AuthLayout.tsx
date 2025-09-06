import React from 'react';
import { Layout, theme } from 'antd';
import { Outlet } from 'react-router-dom';

const { Content } = Layout;

const AuthLayout: React.FC = () => {
  const {
    token: { colorBgContainer, borderRadiusLG },
  } = theme.useToken();

  return (
    <Layout className="min-h-screen bg-gray-100">
      <Content className="flex items-center justify-center p-4">
        <div
          className="w-full max-w-md p-8"
          style={{
            background: colorBgContainer,
            borderRadius: borderRadiusLG,
            boxShadow: '0 4px 12px rgba(0, 0, 0, 0.1)',
          }}
        >
          <div className="text-center mb-8">
            <h1 className="text-2xl font-bold text-blue-700">File Management System</h1>
            <p className="text-gray-500">Secure file storage and sharing</p>
          </div>
          <Outlet />
        </div>
      </Content>
    </Layout>
  );
};

export default AuthLayout;