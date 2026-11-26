<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Chi Tiết Khách Hàng - The Royal Hotel</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body class="bg-light">
<div class="container py-4">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2 class="text-primary fw-bold"><i class="fa-solid fa-user"></i> CHI TIẾT KHÁCH HÀNG</h2>
        <a href="${pageContext.request.contextPath}/quan-ly-khach-hang/view" class="btn btn-secondary"><i class="fa-solid fa-arrow-left"></i> Quay lại</a>
    </div>

    <div class="row">
        <div class="col-md-6 mb-4">
            <div class="card shadow-sm h-100">
                <div class="card-header bg-primary text-white"><h5 class="mb-0">Thông tin cá nhân</h5></div>
                <div class="card-body">
                    <table class="table table-borderless">
                        <tr><th>ID:</th><td>#${kh.id_khach_hang}</td></tr>
                        <tr><th>Họ tên:</th><td class="fw-bold">${kh.ho_ten}</td></tr>
                        <tr><th>Số điện thoại:</th><td>${kh.so_dien_thoai}</td></tr>
                        <tr><th>Email:</th><td>${kh.email}</td></tr>
                        <tr><th>Trạng thái:</th><td><span class="badge ${kh.trang_thai == 'HOAT_DONG' ? 'bg-success' : 'bg-danger'}">${kh.trang_thai == 'HOAT_DONG' ? 'Đang hoạt động' : 'Khóa'}</span></td></tr>
                    </table>
                </div>
            </div>
        </div>
        <div class="col-md-6 mb-4">
            <div class="card shadow-sm h-100">
                <div class="card-header bg-warning text-dark"><h5 class="mb-0"><i class="fa-solid fa-id-card"></i> Thẻ thành viên</h5></div>
                <div class="card-body">
                    <c:choose>
                        <c:when test="${not empty theThanhVien}">
                            <table class="table table-borderless">
                                <tr><th>Mã QR:</th><td class="fw-bold">${theThanhVien.maQr}</td></tr>
                                <tr><th>Số dư:</th><td class="text-danger fw-bold"><fmt:formatNumber value="${theThanhVien.soDu}" type="number"/> đ</td></tr>
                                <tr><th>Trạng thái:</th><td>${theThanhVien.trangThai}</td></tr>
                            </table>
                        </c:when>
                        <c:otherwise><p class="text-muted">Khách hàng chưa có thẻ thành viên.</p></c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>

    <div class="card shadow-sm">
        <div class="card-header bg-secondary text-white"><h5 class="mb-0"><i class="fa-solid fa-history"></i> Lịch sử đặt phòng (${dsDatPhong.size()})</h5></div>
        <div class="card-body">
            <table class="table table-bordered table-striped text-center">
                <thead class="table-dark">
                <tr><th>Mã đặt phòng</th><th>Nhận phòng</th><th>Trả phòng</th><th>Tổng tiền</th><th>Trạng thái</th></tr>
                </thead>
                <tbody>
                <c:forEach var="dp" items="${dsDatPhong}">
                    <tr>
                        <td class="fw-bold">${dp.maDatPhong}</td>
                        <td>${dp.thoiGianNhanPhong}</td>
                        <td>${dp.thoiGianTraPhong}</td>
                        <td class="text-danger fw-bold"><fmt:formatNumber value="${dp.tongTien}" type="number"/> đ</td>
                        <td><span class="badge bg-info text-dark">${dp.trangThai}</span></td>
                    </tr>
                </c:forEach>
                <c:if test="${empty dsDatPhong}">
                    <tr><td colspan="5" class="text-muted">Chưa có lịch sử đặt phòng.</td></tr>
                </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>
</body>
</html>
