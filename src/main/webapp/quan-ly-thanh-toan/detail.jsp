<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Chi tiết thanh toán - The Royal Hotel</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body class="bg-light">
<div class="container py-4">
    <div class="card shadow-sm mx-auto" style="max-width: 700px;">
        <div class="card-header bg-info text-white d-flex justify-content-between align-items-center">
            <h5 class="mb-0"><i class="fa-solid fa-receipt"></i> Chi tiết giao dịch #${tt.id}</h5>
            <a href="${pageContext.request.contextPath}/thanh-toan" class="btn btn-light btn-sm"><i class="fa-solid fa-arrow-left"></i> Quay lại</a>
        </div>
        <div class="card-body">
            <table class="table table-bordered">
                <tr><th>Mã giao dịch</th><td>#${tt.id}</td></tr>
                <tr><th>Mã đặt phòng</th><td class="fw-bold">${tt.datPhong.maDatPhong}</td></tr>
                <tr><th>Phương thức</th><td>${tt.phuongThucThanhToan}</td></tr>
                <tr><th>Số tiền</th><td class="text-danger fw-bold"><fmt:formatNumber value="${tt.tienThanhToan}" type="number"/> đ</td></tr>
                <tr><th>Trạng thái</th><td><span class="badge bg-primary">${tt.trangThai}</span></td></tr>
                <c:if test="${not empty tt.theThanhVien}">
                    <tr><th>Thẻ thành viên</th><td>${tt.theThanhVien.maQr}</td></tr>
                </c:if>
            </table>
        </div>
    </div>
</div>
</body>
</html>
