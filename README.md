# The_Royal
> Link draw.io: https://app.diagrams.net/#G1KdQ7YlyVGIFZSQktqhMxKOMm-s7f-Yqj#%7B%22pageId%22%3A%22ogO7tvfcbi842MFMhqg6%22%7D  
> Link project color: https://coolors.co/eeeeee-222831-6d9886-f2e7d5  
> Link logo: https://res.cloudinary.com/ho5im5em/image/upload/v1794413797/Logo_ky8f5y.png
<<<<<<< HEAD
> Sử dụng JDK 17
=======

## API admin: tiện nghi và đặt phòng

Phần này dùng Java Servlet, Hibernate và SQL Server theo schema trong `database/the_royal_sqlserver.sql`. Các API trả JSON; chưa bao gồm trang JSP/CSS để nhóm có thể làm giao diện riêng.

### Đăng nhập và quyền truy cập

Mọi đường dẫn `/admin/*` đều qua `AdminAuthorizationFilter`. Sau khi đăng nhập, phần xác thực của dự án cần lưu hai thuộc tính vào `HttpSession`:

- `idNhanVien`: khóa `nhan_vien.id_nhan_vien` của nhân viên đang đăng nhập.
- `maVaiTro`: mã vai trò `ADMIN` hoặc `QUAN_LY`.

Repo hiện chưa có chức năng đăng nhập, nên các API sẽ trả `401` cho đến khi module đăng nhập đặt các thuộc tính này vào session. Không gửi các giá trị này từ form người dùng.

### Tiện nghi

- `GET /admin/api/tien-nghi`: danh sách tiện nghi, số lượng và số phòng đang được gán.
- `GET /admin/api/tien-nghi/phong/{roomId}`: tiện nghi của một phòng.
- `POST /admin/api/tien-nghi`, `application/x-www-form-urlencoded`:
  - `action=create`, `name`, `quantity`
  - `action=update`, `id`, `name`, `quantity`
  - `action=delete`, `id`
  - `action=assign`, `roomId`, `amenityId`
  - `action=unassign`, `roomId`, `amenityId`

Không xóa tiện nghi khi tiện nghi đó còn được gán vào phòng; cần gỡ các liên kết trước.

### Đặt phòng

- `GET /admin/api/dat-phong?keyword=...&status=...`: tìm theo mã đặt phòng, tên hoặc số điện thoại khách; tối đa 200 kết quả mới nhất.
- `GET /admin/api/dat-phong/{id}`: thông tin đặt phòng và các phòng trong đơn.
- `GET /admin/api/dat-phong/phong-trong?ngayNhan=2026-12-01&ngayTra=2026-12-03&soNguoiLon=2&soTreEmDuoi6=0&soTreEmTu6=0`: phòng còn trống trong khoảng ngày.
- `POST /admin/api/dat-phong`, `application/x-www-form-urlencoded`: `customerId`, `roomId`, `ngayNhan`, `ngayTra`, `soNguoiLon`, `soTreEmDuoi6`, `soTreEmTu6`.
- `POST /admin/api/dat-phong/{id}/huy`: `soTienHoan` và `lyDo`.

Ngày được tính theo khoảng `[ngày nhận, ngày trả)`, phù hợp trigger chống trùng trong SQL. Đơn mới ở trạng thái `CHO_COC`; giá phòng được tính theo `loai_phong.gia_moi_dem × số đêm`, chưa áp dụng mã giảm giá, phí dịch vụ hoặc VAT. Hủy chỉ áp dụng cho đơn `CHO_COC` hoặc `DA_XAC_NHAN`, cập nhật chi tiết thành `DA_HUY` và ghi `lich_su_dat_phong`. Số tiền hoàn được lưu vào lịch sử; nếu hoàn toàn bộ tiền đã thanh toán thành công thì các khoản thanh toán chuyển sang `DA_HOAN`. Việc chuyển tiền thực tế không nằm trong API này.
>>>>>>> cf280652620e7ac6f1130e4f8eb04f232362527d
