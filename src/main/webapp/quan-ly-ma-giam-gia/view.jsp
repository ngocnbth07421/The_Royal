<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Quản lý mã giảm giá - The Royal Hotel</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body class="bg-light">
<div class="container py-4">

    <div class="text-center mb-4">
        <h2 class="text-primary fw-bold"><i class="fa-solid fa-ticket"></i> QUẢN LÝ MÃ GIẢM GIÁ</h2>
        <p class="text-muted">Quản lý voucher, mã khuyến mãi dành cho khách hàng khách sạn</p>
    </div>

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

    <!-- TÌM KIẾM -->
    <form class="card shadow-sm mb-4" method="get" action="${pageContext.request.contextPath}/ma-giam-gia">
        <div class="card-body row g-2 align-items-end">
            <div class="col-md-5">
                <label class="form-label fw-bold">Mã giảm giá</label>
                <input type="text" class="form-control" name="tuKhoa" value="<c:out value='${tuKhoa}'/>" placeholder="Nhập mã code...">
            </div>
            <div class="col-md-4">
                <label class="form-label fw-bold">Trạng thái</label>
                <select class="form-select" name="trangThai">
                    <option value="">Tất cả</option>
                    <option value="Hoạt động" ${trangThai == 'Hoạt động' ? 'selected' : ''}>Hoạt động</option>
                    <option value="Ngưng" ${trangThai == 'Ngưng' ? 'selected' : ''}>Ngưng</option>
                </select>
            </div>
            <div class="col-md-3 text-end">
                <button type="submit" class="btn btn-primary"><i class="fa-solid fa-search"></i> Tìm</button>
                <a href="${pageContext.request.contextPath}/ma-giam-gia" class="btn btn-secondary">Xóa lọc</a>
            </div>
        </div>
    </form>

    <!-- FORM THÊM MỚI -->
    <div class="card shadow-sm mb-4">
        <div class="card-header bg-primary text-white">
            <h5 class="mb-0"><i class="fa-solid fa-plus-circle"></i> Thêm mã giảm giá mới</h5>
        </div>
        <div class="card-body">
            <form action="${pageContext.request.contextPath}/ma-giam-gia" method="POST">
                <input type="hidden" name="action" value="luu">
                <div class="row">
                    <div class="col-md-3 mb-3">
                        <label class="form-label fw-bold">Mã giảm giá <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" name="maGiamGia" placeholder="VD: SUMMER20" required style="text-transform: uppercase;">
                    </div>
                    <div class="col-md-3 mb-3">
                        <label class="form-label fw-bold">Loại giảm giá <span class="text-danger">*</span></label>
                        <select class="form-select" name="loaiGiamGia" required>
                            <option value="Phần trăm">Phần trăm (%)</option>
                            <option value="Số tiền">Số tiền (VNĐ)</option>
                        </select>
                    </div>
                    <div class="col-md-3 mb-3">
                        <label class="form-label fw-bold">Giá trị <span class="text-danger">*</span></label>
                        <input type="number" min="0.01" step="0.01" class="form-control" name="giaTri" required>
                    </div>
                    <div class="col-md-3 mb-3">
                        <label class="form-label fw-bold">Trạng thái</label>
                        <select class="form-select" name="trangThai">
                            <option value="Hoạt động">Hoạt động</option>
                            <option value="Ngưng">Ngưng</option>
                        </select>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-4 mb-3">
                        <label class="form-label fw-bold">Bắt đầu lúc <span class="text-danger">*</span></label>
                        <input type="datetime-local" class="form-control" name="batDauLuc" required>
                    </div>
                    <div class="col-md-4 mb-3">
                        <label class="form-label fw-bold">Kết thúc lúc <span class="text-danger">*</span></label>
                        <input type="datetime-local" class="form-control" name="ketThucLuc" required>
                    </div>
                    <div class="col-md-4 mb-3">
                        <label class="form-label fw-bold">Số lượt còn dùng</label>
                        <input type="number" min="0" class="form-control" name="soLuotDaDung" value="100">
                    </div>
                </div>
                <div class="text-end">
                    <button type="submit" class="btn btn-success"><i class="fa-solid fa-floppy-disk"></i> Thêm mới</button>
                </div>
            </form>
        </div>
    </div>

    <!-- BẢNG DANH SÁCH -->
    <div class="card shadow-sm">
        <div class="card-header bg-secondary text-white">
            <h5 class="mb-0">
                <i class="fa-solid fa-table-list"></i> Danh sách mã giảm giá
                <span class="badge bg-light text-dark ms-2">${dsMaGiamGia.size()} mã</span>
            </h5>
        </div>
        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-bordered table-striped table-hover align-middle text-center">
                    <thead class="table-dark">
                    <tr>
                        <th>ID</th>
                        <th>Mã code</th>
                        <th>Loại</th>
                        <th>Giá trị</th>
                        <th>Bắt đầu</th>
                        <th>Kết thúc</th>
                        <th>Lượt còn</th>
                        <th>Trạng thái</th>
                        <th>Thao tác</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="mgg" items="${dsMaGiamGia}">
                        <tr>
                            <td>${mgg.id}</td>
                            <td class="fw-bold text-primary">${mgg.maGiamGia}</td>
                            <td>${mgg.loaiGiamGia}</td>
                            <td class="fw-bold text-danger">
                                <c:choose>
                                    <c:when test="${mgg.loaiGiamGia == 'Phần trăm'}">${mgg.giaTri}%</c:when>
                                    <c:otherwise><fmt:formatNumber value="${mgg.giaTri}" type="number"/> đ</c:otherwise>
                                </c:choose>
                            </td>
                            <td><small>${mgg.batDauLuc}</small></td>
                            <td><small>${mgg.ketThucLuc}</small></td>
                            <td>${mgg.soLuotDaDung}</td>
                            <td>
                                <span class="badge ${mgg.trangThai == 'Hoạt động' ? 'bg-success' : 'bg-danger'}">${mgg.trangThai}</span>
                            </td>
                            <td>
                                <a href="${pageContext.request.contextPath}/ma-giam-gia?action=form&id=${mgg.id}" class="btn btn-warning btn-sm" title="Sửa">
                                    <i class="fa-solid fa-pen"></i>
                                </a>
                                <form action="${pageContext.request.contextPath}/ma-giam-gia" method="POST" class="d-inline">
                                    <input type="hidden" name="action" value="doiTrangThai">
                                    <input type="hidden" name="id" value="${mgg.id}">
                                    <button type="submit" class="btn btn-secondary btn-sm" title="Đổi trạng thái"><i class="fa-solid fa-rotate"></i></button>
                                </form>
                                <form action="${pageContext.request.contextPath}/ma-giam-gia" method="POST" class="d-inline"
                                      onsubmit="return confirm('Xóa mã giảm giá này?');">
                                    <input type="hidden" name="action" value="xoa">
                                    <input type="hidden" name="id" value="${mgg.id}">
                                    <button type="submit" class="btn btn-danger btn-sm" title="Xóa"><i class="fa-solid fa-trash"></i></button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty dsMaGiamGia}">
                        <tr><td colspan="9" class="text-muted">Chưa có mã giảm giá nào.</td></tr>
                    </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
