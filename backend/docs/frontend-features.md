# Tính năng cần xây dựng trên giao diện (Frontend Features)

## Module quản lý file (File Manager)

Dựa trên thiết kế giao diện trong file UI-mockup.md, dưới đây là danh sách các tính năng cần được xây dựng trên giao diện người dùng cho module quản lý file.

### 1. Quản lý cấu trúc thư mục

#### 1.1. Hiển thị cấu trúc thư mục

- **Component**: `FolderTree`
- **Mô tả**: Hiển thị cấu trúc thư mục dạng cây ở sidebar bên trái
- **API sử dụng**: `GET /api/files/owner/{ownerId}` (với `isFolder=true`)
- **Chức năng**:
  - Hiển thị cây thư mục với các thư mục lồng nhau
  - Mở rộng/thu gọn thư mục con
  - Chọn thư mục để hiển thị nội dung
  - Hiển thị biểu tượng khác nhau cho thư mục đã mở rộng và thu gọn

#### 1.2. Tạo thư mục mới

- **Component**: `CreateFolderModal`
- **Mô tả**: Modal cho phép tạo thư mục mới
- **API sử dụng**: `POST /api/files` (với `isFolder=true`)
- **Chức năng**:
  - Nhập tên thư mục mới
  - Chọn thư mục cha (mặc định là thư mục hiện tại)
  - Tạo thư mục và cập nhật cây thư mục

### 2. Quản lý file

#### 2.1. Hiển thị danh sách file/thư mục

- **Component**: `FileList` và `FileGrid`
- **Mô tả**: Hiển thị danh sách file và thư mục trong thư mục hiện tại
- **API sử dụng**: `GET /api/folders/{folderId}/files`
- **Chức năng**:
  - Chế độ xem lưới (Grid view)
  - Chế độ xem danh sách (List view)
  - Sắp xếp theo tên, ngày sửa đổi, kích thước, v.v.
  - Hiển thị biểu tượng phù hợp với loại file

#### 2.2. Tải lên file

- **Component**: `FileUploader`
- **Mô tả**: Component cho phép tải lên file vào thư mục hiện tại
- **API sử dụng**: `POST /api/files`
- **Chức năng**:
  - Kéo thả file để tải lên
  - Chọn file từ máy tính
  - Hiển thị thanh tiến trình khi tải lên
  - Hỗ trợ tải lên nhiều file cùng lúc
  - Kiểm tra trùng tên file và xử lý

#### 2.3. Tải xuống file

- **Component**: `FileDownloader`
- **Mô tả**: Chức năng tải xuống file từ hệ thống
- **API sử dụng**: `GET /api/files/{fileId}/download`
- **Chức năng**:
  - Tải xuống file đơn lẻ
  - Tải xuống nhiều file dưới dạng ZIP (nếu chọn nhiều)
  - Hiển thị thanh tiến trình khi tải xuống

#### 2.4. Xem trước file

- **Component**: `FilePreview`
- **Mô tả**: Hiển thị xem trước nội dung file
- **API sử dụng**: `GET /api/files/{fileId}/download` (với header phù hợp)
- **Chức năng**:
  - Xem trước hình ảnh
  - Xem trước PDF
  - Xem trước văn bản
  - Xem trước video (nếu hỗ trợ)
  - Hiển thị thông báo "Không thể xem trước" cho các loại file không hỗ trợ

### 3. Chia sẻ và phân quyền

#### 3.1. Chia sẻ file/thư mục

- **Component**: `ShareModal`
- **Mô tả**: Modal cho phép chia sẻ file/thư mục với người dùng khác
- **API sử dụng**: `POST /api/files/{fileId}/permissions`
- **Chức năng**:
  - Tìm kiếm người dùng để chia sẻ
  - Chọn quyền cho người dùng (Xem, Chỉnh sửa, v.v.)
  - Tạo và sao chép liên kết chia sẻ
  - Cài đặt quyền cho liên kết chia sẻ

#### 3.2. Quản lý quyền truy cập

- **Component**: `PermissionManager`
- **Mô tả**: Component quản lý quyền truy cập của file/thư mục
- **API sử dụng**: `GET /api/files/{fileId}/permissions`, `PUT /api/files/{fileId}/permissions/{permissionId}`
- **Chức năng**:
  - Xem danh sách người dùng có quyền truy cập
  - Thay đổi quyền của người dùng
  - Xóa quyền truy cập của người dùng

### 4. Tìm kiếm và lọc

#### 4.1. Tìm kiếm file/thư mục

- **Component**: `SearchBar`
- **Mô tả**: Thanh tìm kiếm file và thư mục
- **API sử dụng**: `GET /api/files/search`
- **Chức năng**:
  - Tìm kiếm theo tên file/thư mục
  - Tìm kiếm trong thư mục hiện tại hoặc toàn bộ
  - Hiển thị kết quả tìm kiếm với highlight từ khóa

#### 4.2. Bộ lọc nâng cao

- **Component**: `AdvancedFilter`
- **Mô tả**: Bộ lọc nâng cao cho tìm kiếm file
- **API sử dụng**: `GET /api/files/search` (với các tham số lọc)
- **Chức năng**:
  - Lọc theo loại file
  - Lọc theo ngày tạo/sửa đổi
  - Lọc theo kích thước
  - Lọc theo người chia sẻ

### 5. Thao tác với file/thư mục

#### 5.1. Menu ngữ cảnh

- **Component**: `ContextMenu`
- **Mô tả**: Menu hiển thị khi click chuột phải vào file/thư mục
- **Chức năng**:
  - Mở file/thư mục
  - Tải xuống
  - Chia sẻ
  - Đổi tên
  - Di chuyển
  - Xóa
  - Xem chi tiết

#### 5.2. Đổi tên file/thư mục

- **Component**: `RenameModal`
- **Mô tả**: Modal cho phép đổi tên file/thư mục
- **API sử dụng**: `PUT /api/files/{fileId}`
- **Chức năng**:
  - Nhập tên mới cho file/thư mục
  - Kiểm tra trùng tên
  - Cập nhật tên và refresh danh sách

#### 5.3. Di chuyển file/thư mục

- **Component**: `MoveModal`
- **Mô tả**: Modal cho phép di chuyển file/thư mục đến thư mục khác
- **API sử dụng**: `PUT /api/files/{fileId}` (cập nhật parentFolderId)
- **Chức năng**:
  - Hiển thị cây thư mục để chọn thư mục đích
  - Di chuyển file/thư mục và refresh danh sách

#### 5.4. Xóa file/thư mục

- **Component**: `DeleteConfirmModal`
- **Mô tả**: Modal xác nhận xóa file/thư mục
- **API sử dụng**: `DELETE /api/files/{fileId}`
- **Chức năng**:
  - Xác nhận xóa
  - Xóa file/thư mục và refresh danh sách

### 6. Thông tin chi tiết

#### 6.1. Hiển thị thông tin chi tiết

- **Component**: `FileDetails`
- **Mô tả**: Hiển thị thông tin chi tiết của file/thư mục ở sidebar bên phải
- **API sử dụng**: `GET /api/files/{fileId}`
- **Chức năng**:
  - Hiển thị tên, loại, kích thước, ngày tạo, ngày sửa đổi
  - Hiển thị chủ sở hữu và người được chia sẻ
  - Hiển thị các nút tác vụ nhanh (Tải xuống, Chia sẻ, v.v.)

### 7. Thông báo

#### 7.1. Thông báo file được chia sẻ

- **Component**: `NotificationCenter`
- **Mô tả**: Hiển thị thông báo khi có file mới được chia sẻ
- **API sử dụng**: `GET /api/notifications/user/{userId}/unread`
- **Chức năng**:
  - Hiển thị số lượng thông báo chưa đọc
  - Hiển thị danh sách thông báo
  - Đánh dấu thông báo đã đọc

### 8. Kéo thả (Drag & Drop)

#### 8.1. Kéo thả file để tải lên

- **Component**: `DragDropUploader`
- **Mô tả**: Cho phép kéo thả file từ máy tính vào ứng dụng để tải lên
- **API sử dụng**: `POST /api/files`
- **Chức năng**:
  - Hiển thị vùng kéo thả
  - Xử lý sự kiện kéo thả
  - Tải lên file được kéo thả

#### 8.2. Kéo thả để di chuyển

- **Component**: `DragDropMover`
- **Mô tả**: Cho phép kéo thả file/thư mục giữa các thư mục để di chuyển
- **API sử dụng**: `PUT /api/files/{fileId}` (cập nhật parentFolderId)
- **Chức năng**:
  - Xử lý sự kiện kéo thả
  - Hiển thị hiệu ứng khi kéo thả
  - Di chuyển file/thư mục khi thả

## Công nghệ đề xuất

### Frontend Framework
- **React**: Xây dựng giao diện người dùng
- **TypeScript**: Đảm bảo type-safe cho code

### UI Components
- **Ant Design**: Thư viện UI components
- **TailwindCSS**: Styling

### State Management
- **Redux Toolkit**: Quản lý state toàn cục
- **React Query**: Quản lý state server và caching

### File Upload/Download
- **Axios**: HTTP client
- **react-dropzone**: Xử lý kéo thả file

### Drag & Drop
- **react-dnd**: Xử lý kéo thả

### File Preview
- **react-pdf**: Xem trước PDF
- **react-image**: Xem trước hình ảnh
- **react-player**: Xem trước video/audio

## Kế hoạch triển khai

1. **Thiết lập dự án**:
   - Cài đặt React + TypeScript
   - Cài đặt và cấu hình các thư viện cần thiết
   - Thiết lập cấu trúc thư mục dự án

2. **Xây dựng components cơ bản**:
   - Layout chung
   - Sidebar trái (cây thư mục)
   - Khu vực chính (danh sách file)
   - Sidebar phải (thông tin chi tiết)

3. **Triển khai các tính năng cơ bản**:
   - Hiển thị cấu trúc thư mục
   - Hiển thị danh sách file/thư mục
   - Tạo thư mục mới
   - Tải lên file

4. **Triển khai các tính năng nâng cao**:
   - Chia sẻ file/thư mục
   - Quản lý quyền truy cập
   - Tìm kiếm và lọc
   - Kéo thả

5. **Tối ưu hóa và kiểm thử**:
   - Tối ưu hiệu suất
   - Kiểm thử các tính năng
   - Sửa lỗi

6. **Triển khai và tích hợp**:
   - Tích hợp với backend
   - Triển khai lên môi trường staging
   - Kiểm thử tích hợp