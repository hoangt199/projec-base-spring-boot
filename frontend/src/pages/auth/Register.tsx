import React, { useState } from 'react';
import { Form, Input, Button, message, Card, Typography, Divider, Alert } from 'antd';
import { UserOutlined, LockOutlined, MailOutlined, UserAddOutlined } from '@ant-design/icons';
import { Link, useNavigate } from 'react-router-dom';
import { useAppDispatch } from '../../store';
import { register } from '../../store/slices/authSlice';
import type { RegisterRequest } from '../../types';
import { ROUTES } from '../../config/routes.config';

const { Title, Text } = Typography;

const Register: React.FC = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const [form] = Form.useForm();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const onFinish = async (values: RegisterRequest) => {
    setLoading(true);
    setError(null);
    try {
      const resultAction = await dispatch(register(values));
      if (register.fulfilled.match(resultAction)) {
        message.success('Đăng ký thành công! Vui lòng đăng nhập.');
        navigate(ROUTES.AUTH.LOGIN);
      } else if (register.rejected.match(resultAction)) {
        setError(resultAction.payload as string || 'Đăng ký thất bại. Vui lòng thử lại.');
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
          <Title level={2} className="mb-2">Đăng ký tài khoản</Title>
          <Text type="secondary">Tạo tài khoản mới để sử dụng hệ thống</Text>
        </div>
        
        {error && (
          <Alert
            message="Lỗi đăng ký"
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
          name="register"
          onFinish={onFinish}
          layout="vertical"
          size="large"
        >
          <Form.Item
            name="fullName"
            rules={[{ required: true, message: 'Vui lòng nhập họ tên đầy đủ!' }]}
          >
            <Input 
              prefix={<UserAddOutlined className="text-gray-400" />} 
              placeholder="Họ và tên" 
              className="rounded-md"
            />
          </Form.Item>

          <Form.Item
            name="email"
            rules={[
              { required: true, message: 'Vui lòng nhập email!' },
            { type: 'email', message: 'Vui lòng nhập đúng định dạng email!' },
          ]}
        >
          <Input 
            prefix={<MailOutlined className="text-gray-400" />} 
            placeholder="Email" 
            className="rounded-md"
          />
        </Form.Item>

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
            { min: 6, message: 'Mật khẩu phải có ít nhất 6 ký tự!' },
          ]}
        >
          <Input.Password 
            prefix={<LockOutlined className="text-gray-400" />} 
            placeholder="Mật khẩu" 
            className="rounded-md"
          />
        </Form.Item>

        <Form.Item
          name="confirmPassword"
          dependencies={['password']}
          rules={[
            { required: true, message: 'Vui lòng xác nhận mật khẩu!' },
            ({ getFieldValue }) => ({
              validator(_, value) {
                if (!value || getFieldValue('password') === value) {
                  return Promise.resolve();
                }
                return Promise.reject(new Error('Hai mật khẩu không khớp nhau!'));
              },
            }),
          ]}
        >
          <Input.Password 
            prefix={<LockOutlined className="text-gray-400" />} 
            placeholder="Xác nhận mật khẩu" 
            className="rounded-md"
          />
        </Form.Item>

        <Form.Item>
          <Button 
            type="primary" 
            htmlType="submit" 
            className="w-full h-10 rounded-md flex items-center justify-center"
            icon={<UserAddOutlined />}
            loading={loading}
          >
            Đăng ký
          </Button>
        </Form.Item>
        
        <Divider plain>Hoặc</Divider>
        
        <div className="text-center">
          <Text type="secondary">Đã có tài khoản? </Text>
          <Link to={ROUTES.AUTH.LOGIN} className="text-blue-500 hover:text-blue-700 font-medium">
            Đăng nhập
          </Link>
        </div>
      </Form>
    </Card>
  </div>
  );
};

export default Register;