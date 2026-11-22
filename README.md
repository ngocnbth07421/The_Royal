# The_Royal
> Link draw.io: https://app.diagrams.net/#G1KdQ7YlyVGIFZSQktqhMxKOMm-s7f-Yqj#%7B%22pageId%22%3A%22ogO7tvfcbi842MFMhqg6%22%7D  
> Link project color: https://coolors.co/eeeeee-222831-6d9886-f2e7d5  
> Link logo: https://res.cloudinary.com/ho5im5em/image/upload/v1794413797/Logo_ky8f5y.png

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

## Ba module quan tri bo sung: Loai phong, Ma giam gia, Thanh toan

Ba module duoi day duoc bo sung truc tiep vao project theo stack hien tai: Java 17, Jakarta Servlet 6 (Tomcat 10.1), Hibernate 5.6 Jakarta, SQL Server va JSP/JSTL. Cac trang dung chung `layout/header.jspf`, `layout/footer.jspf`, `main.css` va `royal-jsp.css`, nen giu cung sidebar, mau sac, font va bo cuc The Royal.

### 1. Quan ly loai phong — `/loai-phong`
- Danh sach: tim theo ten loai phong/ma hang phong; hien thi hang phong, loai giuong, gia moi dem va so phong dang su dung.
- Them/sua: chon `hang_phong` va `loai_giuong`, nhap ten, gia va mo ta.
- Rang buoc: ten bat buoc, toi da 150 ky tu; gia moi dem phai la so nguyen VNĐ lon hon 0; mo ta toi da 4000 ky tu; moi cap `(id_hang_phong,id_loai_giuong)` chi duoc co mot loai phong.
- Xoa: khong cho xoa neu bang `phong` van con phong tham chieu loai phong do.

### 2. Quan ly ma giam gia — `/ma-giam-gia`
- Danh sach: ma, loai giam, gia tri, khoang hieu luc, luot da dung va trang thai.
- Them/sua: `PHAN_TRAM` hoac `SO_TIEN`; trang thai `HOAT_DONG`/`NGUNG`.
- Rang buoc: ma 3-30 ky tu (A-Z, 0-9, `_`, `-`) va khong trung; gia tri > 0; neu la phan tram thi khong duoc vuot 100; thoi diem ket thuc phai sau bat dau.
- Xoa: khong cho xoa neu da co don `dat_phong` tham chieu; trong truong hop do chuyen trang thai sang `NGUNG`.
- Luu y: module quan tri nay chua tu dong ap ma vao don dat phong; phan tinh giam gia luc tao dat phong can duoc tich hop tiep trong `DatPhongService` va cap nhat `so_luot_da_dung` trong cung transaction.

### 3. Quan ly thanh toan — `/thanh-toan`
- Danh sach: loc theo ma dat phong/ten khach va trang thai; hien thi phuong thuc, so tien va trang thai giao dich.
- Tao giao dich: chon don dat phong, phuong thuc `TIEN_MAT`/`VNPAY`/`THE`, so tien; `THE` bat buoc co ID the thanh vien dang hoat dong. Giao dich moi o `CHO_XU_LY`.
- Xac nhan: `CHO_XU_LY -> THANH_CONG` se cong tien vao `dat_phong.tien_thanh_toan`; khong cho tong tien da thu vuot `dat_phong.tong_tien`.
- That bai: `CHO_XU_LY -> THAT_BAI`, khong thay doi so tien da thu.
- Hoan tien: `THANH_CONG -> DA_HOAN` tru so tien giao dich khoi `dat_phong.tien_thanh_toan`. Day la ghi nhan hoan tien noi bo, khong thuc hien chuyen tien qua ngan hang.
- So tien thanh toan phai la so nguyen VNĐ lon hon 0 va khong vuot so tien con phai tra tai thoi diem tao giao dich.

### Cau truc code
- `controller/LoaiPhongServlet.java`, `MaGiamGiaServlet.java`, `ThanhToanServlet.java`: tiep nhan GET/POST, doc tham so, chuyen trang va nap du lieu cho JSP.
- `service/QuanLyDanhMucService.java`: chua validate nghiep vu va thao tac SQL. Cac thao tac ghi dung `TransactionHelper` de commit/rollback theo tung nghiep vu.
- `WEB-INF/views/{loai-phong,ma-giam-gia,thanh-toan}/`: danh sach va form JSP.
- `WEB-INF/views/layout/header.jspf`: cac muc sidebar da duoc tro den URL chay that.
- `css/royal-jsp.css`: bo sung style form va badge trang thai, su dung cac bien mau The Royal da co.

Cac module su dung truc tiep cac bang co san trong `the_royal_sqlserver.sql`; khong can tao them bang hoac sua cau truc database. Can dam bao database `THE_ROYAL` da duoc tao va ket noi trong `HibernateConfig` dung thong tin may cua ban.
