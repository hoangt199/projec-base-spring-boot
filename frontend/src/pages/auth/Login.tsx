import React, { useState } from 'react';
import { Form, Input, Button, Checkbox, message, Card, Typography, Divider, Alert } from 'antd';
import { UserOutlined, LockOutlined, LoginOutlined } from '@ant-design/icons';
import { Link, useNavigate } from 'react-router-dom';
import { useAppDispatch } from '../../store';
import { login } from '../../store/slices/authSlice';
import type { LoginRequest } from '../../types';
import { ROUTES } from '../../config/routes.config';

const { Title, Text } = Typography;

const Login: React.FC = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const [form] = Form.useForm();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const onFinish = async (values: LoginRequest) => {
    setLoading(true);
    setError(null);
    try {
      const resultAction = await dispatch(login(values));
      if (login.fulfilled.match(resultAction)) {
        message.success('Đăng nhập thành công!');
        navigate(ROUTES.DASHBOARD);
      } else if (login.rejected.match(resultAction)) {
        setError(resultAction.payload as string || 'Đăng nhập thất bại. Vui lòng kiểm tra lại thông tin đăng nhập.');
      }
    } catch (error) {
      setError('Đã xảy ra lỗi không mong muốn. Vui lòng thử lại sau.');
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="flex justify-center items-center min-h-full">
      <Card className="w-full max-w-md shadow-lg rounded-lg p-2">
        <div className="text-center mb-6">
          <Title level={2} className="mb-2">Đăng nhập</Title>
          <Text type="secondary">Nhập thông tin đăng nhập của bạn để tiếp tục</Text>
        </div>
        
        {error && (
          <Alert
            message="Lỗi đăng nhập"
            description={error}
            type="error"
            showIcon
            className="mb-4"
            closable
            onClose={() => setError(null)}
          />
        )}
        
        <Form
          form={form}
          name="login"
          initialValues={{ remember: true }}
          onFinish={onFinish}
          layout="vertical"
          size="large"
        >
          <Form.Item
            name="username"
            rules={[
              { required: true, message: 'Vui lòng nhập tên đăng nhập!' },
              { min: 3, message: 'Tên đăng nhập phải có ít nhất 3 ký tự!' }
            ]}
          >
            <Input 
              prefix={<UserOutlined className="text-gray-400" />} 
              placeholder="Tên đăng nhập" 
              className="rounded-md"
            />
          </Form.Item>

          <Form.Item
            name="password"
            rules={[
              { required: true, message: 'Vui lòng nhập mật khẩu!' },
              { min: 6, message: 'Mật khẩu phải có ít nhất 6 ký tự!' }
            ]}
        >
          <Input.Password 
            prefix={<LockOutlined className="text-gray-400" />} 
            placeholder="Mật khẩu" 
            className="rounded-md"
          />
        </Form.Item>

        <div className="flex justify-between items-center mb-4">
          <Form.Item name="remember" valuePropName="checked" noStyle>
            <Checkbox>Ghi nhớ đăng nhập</Checkbox>
          </Form.Item>

          <Link to={ROUTES.AUTH.FORGOT_PASSWORD} className="text-blue-500 hover:text-blue-700 text-sm">
            Quên mật khẩu?
          </Link>
        </div>

        <Form.Item>
          <Button 
            type="primary" 
            htmlType="submit" 
            className="w-full h-10 rounded-md flex items-center justify-center"
            icon={<LoginOutlined />}
            loading={loading}
          >
            Đăng nhập
          </Button>
        </Form.Item>
        
        <Divider plain>Hoặc</Divider>
        
        <div className="text-center">
          <Text type="secondary">Chưa có tài khoản? </Text>
          <Link to={ROUTES.AUTH.REGISTER} className="text-blue-500 hover:text-blue-700 font-medium">
            Đăng ký ngay
          </Link>
        </div>
      </Form>
    </Card>
  </div>
  );
};

export default Login;