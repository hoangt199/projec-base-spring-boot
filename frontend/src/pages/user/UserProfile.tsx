import React, { useState, useEffect } from 'react';
import {
  Card,
  Avatar,
  Tabs,
  Form,
  Input,
  Button,
  message,
  Upload,
} from 'antd';
import {
  UserOutlined,
  MailOutlined,
  LockOutlined,
  UploadOutlined,
} from '@ant-design/icons';
import { useAppSelector, useAppDispatch } from '../../store';
import { setUser } from '../../store/slices/authSlice';
// import axiosInstance from '../../services/api/axios';
import { API_ENDPOINTS } from '../../config/api.config';
import { getInitials } from '../../utils';

const { TabPane } = Tabs;

const UserProfile: React.FC = () => {
  const dispatch = useAppDispatch();
  const { user } = useAppSelector((state) => state.auth);
  const [profileForm] = Form.useForm();
  const [passwordForm] = Form.useForm();
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (user) {
      profileForm.setFieldsValue({
        fullName: user.fullName,
        email: user.email,
        username: user.username,
      });
    }
  }, [user, profileForm]);

  const handleUpdateProfile = async (values: any) => {
    try {
      setLoading(true);
      // In a real application, this would be an actual API call
      // For now, we'll simulate updating the user profile
      
      // Simulate API call delay
      await new Promise(resolve => setTimeout(resolve, 1000));
      
      // Update user in Redux store
      if (user) {
        const updatedUser = {
          ...user,
          fullName: values.fullName,
          email: values.email,
        };
        dispatch(setUser(updatedUser));
      }
      
      message.success('Profile updated successfully');
      setLoading(false);
    } catch (error) {
      console.error('Error updating profile:', error);
      message.error('Failed to update profile');
      setLoading(false);
    }
  };

  const handleChangePassword = async (values: any) => {
    try {
      setLoading(true);
      // In a real application, this would be an actual API call
      // For now, we'll simulate changing the password
      
      // Simulate API call delay
      await new Promise(resolve => setTimeout(resolve, 1000));
      
      message.success('Password changed successfully');
      passwordForm.resetFields();
      setLoading(false);
    } catch (error) {
      console.error('Error changing password:', error);
      message.error('Failed to change password');
      setLoading(false);
    }
  };

  return (
    <div className="max-w-4xl mx-auto">
      <Card>
        <div className="flex flex-col md:flex-row items-center md:items-start gap-8 mb-6">
          <div className="flex flex-col items-center">
            <Avatar
              size={100}
              icon={<UserOutlined />}
              style={{ backgroundColor: '#1890ff' }}
            >
              {user?.fullName ? getInitials(user.fullName) : ''}
            </Avatar>
            <Upload
              name="avatar"
              showUploadList={false}
              action="/api/user/avatar"
              beforeUpload={(file) => {
                const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png';
                if (!isJpgOrPng) {
                  message.error('You can only upload JPG/PNG file!');
                }
                const isLt2M = file.size / 1024 / 1024 < 2;
                if (!isLt2M) {
                  message.error('Image must smaller than 2MB!');
                }
                return isJpgOrPng && isLt2M;
              }}
            >
              <Button icon={<UploadOutlined />} className="mt-3">
                Change Avatar
              </Button>
            </Upload>
          </div>
          <div className="flex-1">
            <h1 className="text-2xl font-bold">{user?.fullName}</h1>
            <p className="text-gray-500">{user?.email}</p>
            <p className="mt-2">
              <span className="bg-blue-100 text-blue-800 px-2 py-1 rounded text-sm">
                {user?.role}
              </span>
            </p>
            <p className="mt-4">
              Account created on {new Date(user?.createdAt || '').toLocaleDateString()}
            </p>
          </div>
        </div>

        <Tabs defaultActiveKey="profile">
          <TabPane tab="Profile Information" key="profile">
            <Form
              form={profileForm}
              layout="vertical"
              onFinish={handleUpdateProfile}
            >
              <Form.Item
                name="fullName"
                label="Full Name"
                rules={[{ required: true, message: 'Please enter your full name' }]}
              >
                <Input prefix={<UserOutlined />} placeholder="Full Name" />
              </Form.Item>

              <Form.Item
                name="email"
                label="Email"
                rules={[
                  { required: true, message: 'Please enter your email' },
                  { type: 'email', message: 'Please enter a valid email' },
                ]}
              >
                <Input prefix={<MailOutlined />} placeholder="Email" />
              </Form.Item>

              <Form.Item
                name="username"
                label="Username"
              >
                <Input prefix={<UserOutlined />} disabled />
              </Form.Item>

              <Form.Item>
                <Button type="primary" htmlType="submit" loading={loading}>
                  Update Profile
                </Button>
              </Form.Item>
            </Form>
          </TabPane>

          <TabPane tab="Change Password" key="password">
            <Form
              form={passwordForm}
              layout="vertical"
              onFinish={handleChangePassword}
            >
              <Form.Item
                name="currentPassword"
                label="Current Password"
                rules={[{ required: true, message: 'Please enter your current password' }]}
              >
                <Input.Password prefix={<LockOutlined />} placeholder="Current Password" />
              </Form.Item>

              <Form.Item
                name="newPassword"
                label="New Password"
                rules={[
                  { required: true, message: 'Please enter your new password' },
                  { min: 6, message: 'Password must be at least 6 characters' },
                ]}
              >
                <Input.Password prefix={<LockOutlined />} placeholder="New Password" />
              </Form.Item>

              <Form.Item
                name="confirmPassword"
                label="Confirm New Password"
                dependencies={['newPassword']}
                rules={[
                  { required: true, message: 'Please confirm your new password' },
                  ({ getFieldValue }) => ({
                    validator(_, value) {
                      if (!value || getFieldValue('newPassword') === value) {
                        return Promise.resolve();
                      }
                      return Promise.reject(new Error('The two passwords do not match!'));
                    },
                  }),
                ]}
              >
                <Input.Password prefix={<LockOutlined />} placeholder="Confirm New Password" />
              </Form.Item>

              <Form.Item>
                <Button type="primary" htmlType="submit" loading={loading}>
                  Change Password
                </Button>
              </Form.Item>
            </Form>
          </TabPane>
        </Tabs>
      </Card>
    </div>
  );
};

export default UserProfile;