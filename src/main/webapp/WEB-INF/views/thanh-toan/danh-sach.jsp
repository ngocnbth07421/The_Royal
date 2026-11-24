<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Quản lý thanh toán - The Royal Hotel</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body class="bg-light">
<div class="container py-4">

    <div class="text-center mb-4">
        <h2 class="text-primary fw-bold">
            <i class="fa-solid fa-credit-card"></i> QUẢN LÝ THANH TOÁN
        </h2>
        <p class="text-muted">Quản lý giao dịch thanh toán đơn đặt phòng khách sạn</p>
    </div>

    <!-- THÔNG BÁO -->
    <c:if test="${not empty thongBao}">
        <div class="alert alert-success alert-dismissible fade show">
            <i class="fa-solid fa-check-circle"></i> ${thongBao}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>
    <c:if test="${not empty loi}">
        <div class="alert alert-danger alert-dismissible fade show">
            <i class="fa-solid fa-circle-exclamation"></i> ${loi}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <!-- FORM THÊM THANH TOÁN -->
    <div class="card shadow-sm mb-4">
        <div class="card-header bg-primary text-white">
            <h5 class="mb-0">
                <i class="fa-solid fa-plus-circle"></i> Tạo giao dịch thanh toán mới
            </h5>
        </div>
        <div class="card-body">
            <form action="${pageContext.request.contextPath}/thanh-toan" method="POST">
                <input type="hidden" name="action" value="luu">

                <div class="row">
                    <div class="col-md-4 mb-3">
                        <label class="form-label fw-bold">Đơn đặt phòng <span class="text-danger">*</span></label>
                        <select class="form-select" name="idDatPhong" required>
                            <option value="">-- Chọn đơn đặt phòng --</option>
                            <c:forEach var="dp" items="${dsDatPhong}">
                                <option value="${dp.id}">${dp.maDatPhong} - ${dp.trangThai}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="col-md-4 mb-3">
                        <label class="form-label fw-bold">Phương thức <span class="text-danger">*</span></label>
                        <select class="form-select" name="phuongThuc" id="phuongThuc" required onchange="toggleTheThanhVien()">
                            <option value="Tiền mặt">Tiền mặt</option>
                            <option value="VNPay">VNPay</option>
                            <option value="Thẻ">Thẻ thành viên</option>
                        </select>
                    </div>

                    <div class="col-md-4 mb-3">
                        <label class="form-label fw-bold">Số tiền (VNĐ) <span class="text-danger">*</span></label>
                        <input type="number" min="1" step="1" class="form-control" name="soTien" placeholder="Nhập số tiền" required>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-4 mb-3" id="theThanhVienDiv" style="display:none;">
                        <label class="form-label fw-bold">Thẻ thành viên</label>
                        <select class="form-select" name="idTheThanhVien">
                            <option value="">-- Chọn thẻ --</option>
                            <c:forEach var="ttv" items="${dsTheThanhVien}">
                                <option value="${ttv.id}">QR: ${ttv.maQr} - Số dư: <fmt:formatNumber value="${ttv.soDu}" type="number"/> đ</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="col-md-4 mb-3">
                        <label class="form-label fw-bold">Trạng thái</label>
                        <select class="form-select" name="trangThai">
                            <option value="Thành công">Thành công</option>
                            <option value="Chờ xử lý">Chờ xử lý</option>
                            <option value="Thất bại">Thất bại</option>
                        </select>
                    </div>
                </div>

                <div class="text-end">
                    <button type="submit" class="btn btn-success">
                        <i class="fa-solid fa-floppy-disk"></i> Tạo thanh toán
                    </button>
                </div>
            </form>
        </div>
    </div>

    <!-- BẢNG DANH SÁCH THANH TOÁN -->
    <div class="card shadow-sm">
        <div class="card-header bg-secondary text-white">
            <h5 class="mb-0">
                <i class="fa-solid fa-table-list"></i> Danh sách giao dịch thanh toán
                <span class="badge bg-light text-dark ms-2">${dsThanhToan.size()} giao dịch</span>
            </h5>
        </div>
        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-bordered table-striped table-hover align-middle text-center">
                    <thead class="table-dark">
                    <tr>
                        <th>ID</th>
                        <th>Mã đặt phòng</th>
                        <th>Phương thức</th>
                        <th>Số tiền</th>
                        <th>Trạng thái</th>
                        <th>Thao tác</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="tt" items="${dsThanhToan}">
                        <tr>
                            <td>${tt.id}</td>
                            <td class="fw-bold">${tt.datPhong.maDatPhong}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${tt.phuongThucThanhToan == 'Tiền mặt'}">
                                        <span class="badge bg-success"><i class="fa-solid fa-money-bill"></i> Tiền mặt</span>
                                    </c:when>
                                    <c:when test="${tt.phuongThucThanhToan == 'VNPay'}">
                                        <span class="badge bg-info text-white"><i class="fa-solid fa-qrcode"></i> VNPay</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-warning text-dark"><i class="fa-solid fa-credit-card"></i> Thẻ TV</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-danger fw-bold">
                                <fmt:formatNumber value="${tt.tienThanhToan}" type="number"/> đ
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${tt.trangThai == 'Thành công'}">
                                        <span class="badge bg-success">Thành công</span>
                                    </c:when>
                                    <c:when test="${tt.trangThai == 'Chờ xử lý'}">
                                        <span class="badge bg-warning text-dark">Chờ xử lý</span>
                                    </c:when>
                                    <c:when test="${tt.trangThai == 'Đã hoàn'}">
                                        <span class="badge bg-secondary">Đã hoàn</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-danger">Thất bại</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <!-- Đổi trạng thái -->
                                <form action="${pageContext.request.contextPath}/thanh-toan" method="POST" class="d-inline">
                                    <input type="hidden" name="action" value="doiTrangThai">
                                    <input type="hidden" name="id" value="${tt.id}">
                                    <select name="trangThaiMoi" class="form-select form-select-sm d-inline-block" style="width:auto;" onchange="this.form.submit()">
                                        <option value="">Đổi...</option>
                                        <option value="Thành công">Thành công</option>
                                        <option value="Chờ xử lý">Chờ xử lý</option>
                                        <option value="Thất bại">Thất bại</option>
                                        <option value="Đã hoàn">Đã hoàn</option>
                                    </select>
                                </form>

                                <!-- Xóa -->
                                <form action="${pageContext.request.contextPath}/thanh-toan" method="POST" class="d-inline"
                                      onsubmit="return confirm('Bạn có chắc chắn muốn xóa giao dịch này?');">
                                    <input type="hidden" name="action" value="xoa">
                                    <input type="hidden" name="id" value="${tt.id}">
                                    <button type="submit" class="btn btn-danger btn-sm" title="Xóa">
                                        <i class="fa-solid fa-trash"></i>
                                    </button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty dsThanhToan}">
                        <tr><td colspan="6" class="text-muted">Chưa có giao dịch thanh toán nào.</td></tr>
                    </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    function toggleTheThanhVien() {
        var pt = document.getElementById('phuongThuc').value;
        document.getElementById('theThanhVienDiv').style.display = (pt === 'Thẻ') ? 'block' : 'none';
    }
</script>
</body>
</html>
