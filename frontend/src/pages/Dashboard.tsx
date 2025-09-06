import React, { useState, useEffect } from 'react';
import { Row, Col, Card, Statistic, Table, Progress, List, Avatar } from 'antd';
import {
  FileOutlined,
  TeamOutlined,
  FolderOutlined,
  CloudUploadOutlined,
  UserOutlined,
} from '@ant-design/icons';
import { useAppSelector } from '../store';
import axiosInstance from '../services/api/axios';
import { API_ENDPOINTS } from '../config/api.config';
import { FileItem, User } from '../types';
import { formatFileSize, formatDate, getInitials } from '../utils';

interface DashboardStats {
  totalFiles: number;
  totalFolders: number;
  totalUsers: number;
  storageUsed: number;
  storageLimit: number;
}

const Dashboard: React.FC = () => {
  const { user } = useAppSelector((state) => state.auth);
  const [stats, setStats] = useState<DashboardStats>({
    totalFiles: 0,
    totalFolders: 0,
    totalUsers: 0,
    storageUsed: 0,
    storageLimit: 1024 * 1024 * 1024, // 1GB default
  });
  const [recentFiles, setRecentFiles] = useState<FileItem[]>([]);
  const [recentUsers, setRecentUsers] = useState<User[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchDashboardData = async () => {
      try {
        setLoading(true);
        // In a real application, these would be actual API calls
        // For now, we'll simulate with mock data
        
        // Mock stats data
        setStats({
          totalFiles: 128,
          totalFolders: 24,
          totalUsers: 15,
          storageUsed: 536870912, // 512MB
          storageLimit: 1073741824, // 1GB
        });

        // Mock recent files
        setRecentFiles([
          {
            id: '1',
            name: 'Project Proposal.docx',
            type: 'file',
            mimeType: 'application/vnd.openxmlformats-officedocument.wordprocessingml.document',
            size: 2500000,
            createdAt: new Date().toISOString(),
            updatedAt: new Date().toISOString(),
            createdBy: 'user1',
            parentId: 'root',
          },
          {
            id: '2',
            name: 'Financial Report.xlsx',
            type: 'file',
            mimeType: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
            size: 1800000,
            createdAt: new Date(Date.now() - 86400000).toISOString(), // 1 day ago
            updatedAt: new Date(Date.now() - 86400000).toISOString(),
            createdBy: 'user2',
            parentId: 'root',
          },
          {
            id: '3',
            name: 'Team Photo.jpg',
            type: 'file',
            mimeType: 'image/jpeg',
            size: 5200000,
            createdAt: new Date(Date.now() - 172800000).toISOString(), // 2 days ago
            updatedAt: new Date(Date.now() - 172800000).toISOString(),
            createdBy: 'user1',
            parentId: 'root',
          },
          {
            id: '4',
            name: 'Product Presentation.pptx',
            type: 'file',
            mimeType: 'application/vnd.openxmlformats-officedocument.presentationml.presentation',
            size: 7800000,
            createdAt: new Date(Date.now() - 259200000).toISOString(), // 3 days ago
            updatedAt: new Date(Date.now() - 259200000).toISOString(),
            createdBy: 'user3',
            parentId: 'root',
          },
        ]);

        // Mock recent users
        setRecentUsers([
          {
            id: '1',
            username: 'johndoe',
            email: 'john.doe@example.com',
            fullName: 'John Doe',
            role: 'ADMIN',
            createdAt: new Date(Date.now() - 604800000).toISOString(), // 7 days ago
            updatedAt: new Date(Date.now() - 172800000).toISOString(), // 2 days ago
          },
          {
            id: '2',
            username: 'janedoe',
            email: 'jane.doe@example.com',
            fullName: 'Jane Doe',
            role: 'USER',
            createdAt: new Date(Date.now() - 518400000).toISOString(), // 6 days ago
            updatedAt: new Date(Date.now() - 86400000).toISOString(), // 1 day ago
          },
          {
            id: '3',
            username: 'bobsmith',
            email: 'bob.smith@example.com',
            fullName: 'Bob Smith',
            role: 'USER',
            createdAt: new Date(Date.now() - 432000000).toISOString(), // 5 days ago
            updatedAt: new Date(Date.now() - 259200000).toISOString(), // 3 days ago
          },
        ]);

        setLoading(false);
      } catch (error) {
        console.error('Error fetching dashboard data:', error);
        setLoading(false);
      }
    };

    fetchDashboardData();
  }, []);

  const storagePercentage = Math.round((stats.storageUsed / stats.storageLimit) * 100);

  const recentFilesColumns = [
    {
      title: 'Name',
      dataIndex: 'name',
      key: 'name',
      render: (text: string, record: FileItem) => (
        <span>
          {record.type === 'folder' ? <FolderOutlined /> : <FileOutlined />} {text}
        </span>
      ),
    },
    {
      title: 'Size',
      dataIndex: 'size',
      key: 'size',
      render: (size: number) => formatFileSize(size),
    },
    {
      title: 'Last Modified',
      dataIndex: 'updatedAt',
      key: 'updatedAt',
      render: (date: string) => formatDate(date),
    },
  ];

  return (
    <div>
      <h1 className="text-2xl font-bold mb-6">Dashboard</h1>
      
      <Row gutter={[16, 16]}>
        <Col xs={24} sm={12} md={6}>
          <Card>
            <Statistic
              title="Total Files"
              value={stats.totalFiles}
              prefix={<FileOutlined />}
              loading={loading}
            />
          </Card>
        </Col>
        <Col xs={24} sm={12} md={6}>
          <Card>
            <Statistic
              title="Total Folders"
              value={stats.totalFolders}
              prefix={<FolderOutlined />}
              loading={loading}
            />
          </Card>
        </Col>
        <Col xs={24} sm={12} md={6}>
          <Card>
            <Statistic
              title="Total Users"
              value={stats.totalUsers}
              prefix={<TeamOutlined />}
              loading={loading}
            />
          </Card>
        </Col>
        <Col xs={24} sm={12} md={6}>
          <Card>
            <Statistic
              title="Storage Used"
              value={formatFileSize(stats.storageUsed)}
              prefix={<CloudUploadOutlined />}
              loading={loading}
            />
            <Progress percent={storagePercentage} status={storagePercentage > 90 ? 'exception' : 'normal'} />
          </Card>
        </Col>
      </Row>

      <Row gutter={[16, 16]} className="mt-6">
        <Col xs={24} lg={16}>
          <Card title="Recent Files" loading={loading}>
            <Table
              dataSource={recentFiles}
              columns={recentFilesColumns}
              rowKey="id"
              pagination={false}
              size="small"
            />
          </Card>
        </Col>
        <Col xs={24} lg={8}>
          <Card title="Recent Users" loading={loading}>
            <List
              dataSource={recentUsers}
              renderItem={(item) => (
                <List.Item>
                  <List.Item.Meta
                    avatar={
                      <Avatar style={{ backgroundColor: '#1890ff' }}>
                        {getInitials(item.fullName)}
                      </Avatar>
                    }
                    title={item.fullName}
                    description={`${item.role} • ${item.email}`}
                  />
                </List.Item>
              )}
            />
          </Card>
        </Col>
      </Row>
    </div>
  );
};

export default Dashboard;