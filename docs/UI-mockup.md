# Mô tả giao diện người dùng (UI Mockup)

## Module quản lý file (File Manager)

### Tổng quan giao diện

Giao diện quản lý file được thiết kế tương tự Google Drive, với bố cục chính gồm:

1. **Sidebar bên trái**: Hiển thị cấu trúc thư mục dạng cây và các liên kết nhanh
2. **Khu vực chính**: Hiển thị nội dung của thư mục hiện tại dưới dạng lưới hoặc danh sách
3. **Sidebar bên phải**: Hiển thị thông tin chi tiết của file/thư mục được chọn
4. **Thanh công cụ trên cùng**: Chứa các nút chức năng và thanh tìm kiếm

### Chi tiết các thành phần

#### 1. Sidebar bên trái

```
+------------------------+
| + Tạo mới              |
+------------------------+
| ● Tất cả file          |
| ● File của tôi         |
| ● Được chia sẻ với tôi |
| ● Gần đây              |
| ● Đã đánh dấu sao      |
| ● Thùng rác            |
+------------------------+
| Thư mục                |
| ├── Thư mục con 1      |
| ├── Thư mục con 2      |
| │   └── Thư mục con 2.1|
| └── Thư mục con 3      |
+------------------------+
| Dung lượng: 2.5GB/15GB |
+------------------------+
```

#### 2. Khu vực chính

**Chế độ xem lưới (Grid view)**:

```
+-------+  +-------+  +-------+  +-------+
| [📁]  |  | [📁]  |  | [📄]  |  | [📷]  |
| Thư   |  | Tài   |  | Báo   |  | Ảnh   |
| mục A |  | liệu  |  | cáo   |  | logo  |
+-------+  +-------+  +-------+  +-------+

+-------+  +-------+  +-------+  +-------+
| [📊]  |  | [📝]  |  | [📄]  |  | [📽️]  |
| Biểu  |  | Ghi   |  | Hợp   |  | Video |
| đồ    |  | chú   |  | đồng  |  | demo  |
+-------+  +-------+  +-------+  +-------+
```

**Chế độ xem danh sách (List view)**:

```
+--------+--------------+----------+--------+----------+
| [Icon] | Tên          | Chủ sở   | Kích   | Ngày     |
|        |              | hữu      | thước  | sửa đổi  |
+--------+--------------+----------+--------+----------+
| [📁]   | Thư mục A    | Tôi      | -      | 12/05/23 |
| [📁]   | Tài liệu     | Tôi      | -      | 10/05/23 |
| [📄]   | Báo cáo.docx | Tôi      | 2.5MB  | 08/05/23 |
| [📷]   | Ảnh logo.png | Nguyễn A | 1.2MB  | 05/05/23 |
| [📊]   | Biểu đồ.xlsx | Tôi      | 3.7MB  | 01/05/23 |
| [📝]   | Ghi chú.txt  | Tôi      | 15KB   | 28/04/23 |
| [📄]   | Hợp đồng.pdf | Trần B   | 5.1MB  | 25/04/23 |
| [📽️]   | Video demo   | Tôi      | 25.4MB | 20/04/23 |
+--------+--------------+----------+--------+----------+
```

#### 3. Sidebar bên phải (Khi chọn file)

```
+------------------------+
| [Icon file lớn]        |
| Tên file.xlsx          |
+------------------------+
| Loại: Microsoft Excel  |
| Kích thước: 3.7MB      |
| Tạo: 01/05/2023        |
| Sửa đổi: 01/05/2023    |
+------------------------+
| Chủ sở hữu: Tôi        |
+------------------------+
| Chia sẻ với:           |
| - Nguyễn A (Chỉnh sửa) |
| - Trần B (Xem)         |
| + Thêm người           |
+------------------------+
| [Nút Tải xuống]        |
| [Nút Xem]              |
| [Nút Chia sẻ]          |
+------------------------+
```

#### 4. Thanh công cụ trên cùng

```
+---------------------------------------------------------------+
| [Logo] File Manager | [Thanh tìm kiếm                      🔍] |
+---------------------------------------------------------------+
| [Tạo mới ▼] [Tải lên] [Chia sẻ] [Xóa] | [Lưới] [Danh sách]    |
+---------------------------------------------------------------+
```

### Các dialog và modal

#### Dialog tạo mới

```
+-------------------+
| Tạo mới           |
+-------------------+
| ● Thư mục         |
| ● Tài liệu Google |
| ● Bảng tính Google|
| ● Tải lên file    |
+-------------------+
```

#### Modal tải lên file

```
+------------------------------------------+
| Tải lên file                      [X]    |
+------------------------------------------+
|                                          |
|     Kéo thả file vào đây                |
|                hoặc                      |
|        [Chọn file từ máy tính]          |
|                                          |
+------------------------------------------+
| Đang tải lên: file1.docx (45%)          |
| [===========------] 2.5MB/5.5MB         |
+------------------------------------------+
|                [Hủy] [Tải lên]          |
+------------------------------------------+
```

#### Modal chia sẻ file

```
+------------------------------------------+
| Chia sẻ "Báo cáo.docx"            [X]   |
+------------------------------------------+
| Những người có quyền truy cập:           |
| [Tôi (Chủ sở hữu)                   ▼]   |
| [Nguyễn A (Chỉnh sửa)               ▼] X |
| [Trần B (Xem)                       ▼] X |
+------------------------------------------+
| Thêm người:                              |
| [Nhập email hoặc tên              🔍]    |
+------------------------------------------+
| Liên kết chia sẻ:                        |
| [https://file-app.com/share/abc123   📋] |
| [Bất kỳ ai có liên kết (Xem)         ▼]  |
+------------------------------------------+
|                [Hủy] [Lưu]               |
+------------------------------------------+
```

#### Menu ngữ cảnh (Click chuột phải)

```
+------------------+
| Mở               |
| Tải xuống        |
| Chia sẻ          |
+------------------+
| Đổi tên          |
| Di chuyển đến    |
| Tạo bản sao      |
+------------------+
| Đánh dấu sao     |
| Xóa              |
+------------------+
| Xem chi tiết     |
+------------------+
```

### Trang xem trước file

```
+---------------------------------------------------------------+
| [Logo] File Manager | [Thanh tìm kiếm                      🔍] |
+---------------------------------------------------------------+
| [< Quay lại] | Báo cáo.docx                                    |
+---------------------------------------------------------------+
|                                                                 |
|                                                                 |
|                                                                 |
|                  [Nội dung xem trước của file]                  |
|                                                                 |
|                                                                 |
|                                                                 |
+---------------------------------------------------------------+
| [Tải xuống] [Chia sẻ] [In] [Mở bằng ứng dụng khác]             |
+---------------------------------------------------------------+
```

## Các tính năng tương tác

1. **Kéo thả (Drag & Drop)**:
   - Kéo file từ máy tính vào khu vực chính để tải lên
   - Kéo file/thư mục vào thư mục khác để di chuyển

2. **Menu ngữ cảnh (Context Menu)**:
   - Click chuột phải vào file/thư mục để hiển thị menu ngữ cảnh
   - Các tùy chọn: Mở, Tải xuống, Chia sẻ, Đổi tên, Di chuyển, Xóa, v.v.

3. **Chọn nhiều file**:
   - Giữ Ctrl/Cmd và click để chọn nhiều file
   - Giữ Shift và click để chọn một dải file
   - Kéo chuột để chọn nhiều file cùng lúc

4. **Tìm kiếm nâng cao**:
   - Tìm theo tên, loại file, người chia sẻ
   - Bộ lọc theo ngày tạo, kích thước, v.v.

5. **Thông báo**:
   - Hiển thị thông báo khi có file mới được chia sẻ
   - Hiển thị thông báo khi tải lên/tải xuống hoàn tất

## Responsive Design

- **Màn hình lớn (Desktop)**: Hiển thị đầy đủ 3 cột (sidebar trái, khu vực chính, sidebar phải)
- **Màn hình trung bình (Tablet)**: Ẩn sidebar phải, hiển thị khi cần
- **Màn hình nhỏ (Mobile)**: Ẩn cả 2 sidebar, hiển thị menu hamburger để truy cập sidebar trái

## Màu sắc và Theme

- **Theme sáng (mặc định)**:
  - Nền: Trắng (#FFFFFF)
  - Sidebar: Xám nhạt (#F5F5F5)
  - Text: Đen (#333333)
  - Accent: Xanh (#4285F4)

- **Theme tối**:
  - Nền: Đen (#121212)
  - Sidebar: Xám đậm (#1E1E1E)
  - Text: Trắng (#FFFFFF)
  - Accent: Xanh (#4285F4)

## Biểu tượng file

- 📁 Thư mục
- 📄 Tài liệu (DOC, DOCX, TXT)
- 📊 Bảng tính (XLS, XLSX, CSV)
- 📑 PDF
- 📷 Hình ảnh (JPG, PNG, GIF)
- 📽️ Video (MP4, AVI, MOV)
- 🎵 Âm thanh (MP3, WAV)
- 📦 Nén (ZIP, RAR)
- 💻 Mã nguồn (JS, HTML, CSS, JAVA)
- ❓ Không xác định