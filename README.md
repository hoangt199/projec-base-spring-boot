# Microservices Project Base with Spring Boot

Đây là một dự án mẫu cho kiến trúc microservices sử dụng Spring Boot, Spring Cloud và các công nghệ liên quan.

## Kiến trúc

Dự án bao gồm các module sau:

- **common-library**: Thư viện chung chứa các model, DTO, exception và utility được sử dụng bởi các service khác
- **eureka-server**: Service discovery server sử dụng Netflix Eureka
- **config-service**: Centralized configuration server sử dụng Spring Cloud Config
- **gateway-service**: API Gateway sử dụng Spring Cloud Gateway
- **user-service**: Quản lý người dùng, xác thực và phân quyền
- **notification-service**: Quản lý thông báo và gửi email

## Công nghệ sử dụng

- **Spring Boot**: Framework để xây dựng các ứng dụng Java
- **Spring Cloud**: Cung cấp các công cụ để xây dựng hệ thống phân tán
- **Spring Data JPA**: Truy cập dữ liệu với JPA
- **Spring Security**: Bảo mật ứng dụng
- **PostgreSQL**: Cơ sở dữ liệu quan hệ
- **Redis**: Cache và session storage
- **Kafka**: Message broker cho giao tiếp bất đồng bộ
- **RabbitMQ**: Message broker cho giao tiếp bất đồng bộ
- **Flyway**: Database migration
- **Docker**: Containerization
- **Swagger/OpenAPI**: API documentation

## Yêu cầu

- Java 17+
- Maven
- Docker và Docker Compose
- Git

## Cách chạy

### Sử dụng Docker Compose

1. Clone repository:
   ```bash
   git clone https://github.com/yourusername/project-base-spring-boot.git
   cd project-base-spring-boot
   ```

2. Chạy tất cả các service với Docker Compose:
   ```bash
   docker-compose up -d
   ```

3. Kiểm tra trạng thái các service:
   ```bash
   docker-compose ps
   ```

### Truy cập các service

- Eureka Server: http://localhost:8761
- Config Service: http://localhost:8888
- API Gateway: http://localhost:8080
- User Service: http://localhost:8081
- Notification Service: http://localhost:8082
- RabbitMQ Management: http://localhost:15672 (guest/guest)

## API Documentation

Swagger UI được cung cấp cho mỗi service:

- User Service: http://localhost:8081/swagger-ui/index.html
- Notification Service: http://localhost:8082/swagger-ui/index.html

### User Service API

User Service cung cấp các API sau:

- **Authentication API**: Đăng ký, đăng nhập, refresh token, đăng xuất
- **User API**: Quản lý thông tin người dùng
- **Role API**: Quản lý vai trò và phân quyền
- **Permission API**: Quản lý quyền hạn trong hệ thống

### Notification Service API

Notification Service cung cấp các API sau:

- **Notification API**: Tạo, đọc, cập nhật và xóa thông báo
- **Email API**: Gửi email thông báo

## Cấu trúc dự án

```
project-base-spring-boot/
├── common-library/          # Shared library
│   ├── src/main/java
│   │   └── com/example/common
│   │       ├── dto/         # Data Transfer Objects
│   │       ├── exception/   # Custom exceptions
│   │       ├── model/       # Common models
│   │       └── util/        # Utility classes
│   └── pom.xml
├── config-service/          # Configuration server
│   ├── src/main/java
│   │   └── com/example/config
│   ├── src/main/resources
│   │   └── application.yml  # Config server settings
│   └── pom.xml
├── eureka-server/           # Service discovery
│   ├── src/main/java
│   │   └── com/example/eureka
│   ├── src/main/resources
│   │   └── application.yml  # Eureka server settings
│   └── pom.xml
├── gateway-service/         # API Gateway
│   ├── src/main/java
│   │   └── com/example/gateway
│   │       ├── config/      # Gateway configuration
│   │       └── filter/      # Gateway filters
│   ├── src/main/resources
│   │   └── application.yml  # Gateway settings
│   └── pom.xml
├── user-service/            # User management
│   ├── src/main/java
│   │   └── com/example/user
│   │       ├── config/      # Service configuration
│   │       ├── controller/  # REST controllers
│   │       ├── dto/         # Data Transfer Objects
│   │       ├── entity/      # JPA entities
│   │       ├── mapper/      # Object mappers
│   │       ├── repository/  # Data repositories
│   │       ├── security/    # Security configuration
│   │       └── service/     # Business logic
│   ├── src/main/resources
│   │   ├── application.yml  # Service settings
│   │   └── db/migration/    # Flyway migrations
│   └── pom.xml
├── notification-service/    # Notification management
│   ├── src/main/java
│   │   └── com/example/notification
│   │       ├── config/      # Service configuration
│   │       ├── controller/  # REST controllers
│   │       ├── dto/         # Data Transfer Objects
│   │       ├── entity/      # JPA entities
│   │       ├── mapper/      # Object mappers
│   │       ├── repository/  # Data repositories
│   │       └── service/     # Business logic
│   ├── src/main/resources
│   │   ├── application.yml  # Service settings
│   │   └── db/migration/    # Flyway migrations
│   └── pom.xml
├── docker-compose.yml       # Docker Compose configuration
└── pom.xml                  # Parent POM
```

## Phát triển

### Thêm một service mới

1. Tạo một module mới với Spring Initializr
2. Thêm module vào parent pom.xml
3. Thêm các dependency cần thiết
4. Cấu hình bootstrap.yml để kết nối với config-service
5. Thêm service vào docker-compose.yml

### Quản lý Role và Permission

Hệ thống sử dụng cơ chế Role-Based Access Control (RBAC) với các thành phần chính:

- **Role**: Vai trò người dùng (Admin, User, Manager, ...)
- **Permission**: Quyền hạn cụ thể (READ_USER, CREATE_USER, ...)

Mỗi Role có thể được gán nhiều Permission. User Service cung cấp các API để:

- Tạo, cập nhật, xóa và lấy thông tin Role
- Tạo, xóa và lấy thông tin Permission
- Gán Permission cho Role

API được bảo vệ bởi Spring Security với JWT và yêu cầu quyền hạn phù hợp để thực hiện các thao tác quản trị.

### Cấu hình

Cấu hình cho mỗi service được lưu trữ trong config-service. Trong môi trường phát triển, bạn cần tạo một Git repository riêng để lưu trữ các file cấu hình và cập nhật URI trong config-service/application.yml.

## Giấy phép

Dự án này được phân phối dưới giấy phép MIT. Xem file `LICENSE` để biết thêm chi tiết.