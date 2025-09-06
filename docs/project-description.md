# Mô tả dự án

## Tổng quan

Dự án này là một hệ thống microservices được xây dựng bằng Spring Boot, cung cấp các chức năng quản lý người dùng, phân quyền, quản lý file và thông báo. Hệ thống được thiết kế theo kiến trúc microservices, với các service riêng biệt giao tiếp với nhau thông qua API Gateway.

## Kiến trúc hệ thống

Hệ thống bao gồm các service chính sau:

1. **User Service**: Quản lý người dùng, vai trò, quyền hạn, module, menu và router
2. **File Service**: Quản lý tải lên và tải xuống file
3. **Notification Service**: Quản lý thông báo cho người dùng
4. **Gateway Service**: API Gateway, điểm vào duy nhất cho client
5. **Config Service**: Quản lý cấu hình tập trung
6. **Eureka Server**: Service discovery

## Công nghệ sử dụng

- **Spring Boot**: Framework chính để xây dựng các microservices
- **Spring Security**: Xác thực và phân quyền
- **JWT (JSON Web Token)**: Xác thực người dùng
- **Spring Cloud**: Cung cấp các công cụ để xây dựng hệ thống microservices
- **Spring Data JPA**: Tương tác với cơ sở dữ liệu
- **PostgreSQL**: Cơ sở dữ liệu quan hệ
- **Docker**: Containerization
- **Maven**: Quản lý dependency và build

## Chức năng chính

### User Service

1. **Quản lý người dùng**:
   - Đăng ký, đăng nhập, đăng xuất
   - Quản lý thông tin cá nhân
   - Quản lý vai trò và quyền hạn

2. **Quản lý module**:
   - Tạo, cập nhật, xóa module
   - Gán module cho người dùng

3. **Quản lý menu**:
   - Tạo, cập nhật, xóa menu
   - Cấu trúc menu dạng cây
   - Liên kết menu với router

4. **Quản lý router**:
   - Tạo, cập nhật, xóa router
   - Cấu trúc router dạng cây

5. **Phân quyền**:
   - Phân quyền người dùng theo module
   - Phân quyền người dùng theo menu
   - Kiểm tra quyền truy cập

### File Service

1. **Quản lý file**:
   - Tải lên file
   - Tải xuống file
   - Xóa file

### Notification Service

1. **Quản lý thông báo**:
   - Gửi thông báo
   - Đánh dấu đã đọc
   - Xóa thông báo

## Luồng xử lý chính

1. **Đăng ký người dùng**:
   - Client gửi request đăng ký qua API Gateway
   - User Service xử lý đăng ký, tạo người dùng mới
   - Gán module và vai trò mặc định cho người dùng

2. **Đăng nhập**:
   - Client gửi request đăng nhập qua API Gateway
   - User Service xác thực người dùng
   - Trả về JWT token

3. **Truy cập menu và router**:
   - Client gửi request lấy menu và router qua API Gateway với JWT token
   - API Gateway xác thực token
   - User Service kiểm tra quyền truy cập
   - Trả về danh sách menu và router theo quyền của người dùng

4. **Phân quyền người dùng**:
   - Admin gán module và vai trò cho người dùng
   - Admin phân quyền menu cho người dùng

## Bảo mật

1. **Xác thực**:
   - Sử dụng JWT token
   - Refresh token

2. **Phân quyền**:
   - Phân quyền theo vai trò (RBAC)
   - Phân quyền theo module
   - Phân quyền theo menu

3. **Bảo mật API**:
   - CORS
   - CSRF protection
   - Rate limiting

## Triển khai

Hệ thống được triển khai bằng Docker và Docker Compose, cho phép dễ dàng triển khai và mở rộng.