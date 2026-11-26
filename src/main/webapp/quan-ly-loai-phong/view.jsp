<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Quản lý loại phòng - The Royal Hotel</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body class="bg-light">
<div class="container py-4">

    <div class="text-center mb-4">
        <h2 class="text-primary fw-bold">
            <i class="fa-solid fa-bed"></i> QUẢN LÝ LOẠI PHÒNG
        </h2>
        <p class="text-muted">Quản lý danh mục loại phòng khách sạn theo hạng phòng và loại giường</p>
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
    <form class="card shadow-sm mb-4" method="get" action="${pageContext.request.contextPath}/loai-phong">
        <div class="card-body row g-2 align-items-end">
            <div class="col-md-8">
                <label class="form-label fw-bold">Tìm kiếm loại phòng</label>
                <input type="text" class="form-control" name="tuKhoa" value="<c:out value='${tuKhoa}'/>" placeholder="Nhập tên loại phòng hoặc hạng phòng...">
            </div>
            <div class="col-md-4 text-end">
                <button type="submit" class="btn btn-primary"><i class="fa-solid fa-search"></i> Tìm kiếm</button>
                <a href="${pageContext.request.contextPath}/loai-phong" class="btn btn-secondary">Xóa lọc</a>
            </div>
        </div>
    </form>

    <!-- FORM THÊM MỚI -->
    <div class="card shadow-sm mb-4">
        <div class="card-header bg-primary text-white">
            <h5 class="mb-0"><i class="fa-solid fa-plus-circle"></i> Thêm loại phòng mới</h5>
        </div>
        <div class="card-body">
            <form action="${pageContext.request.contextPath}/loai-phong" method="POST">
                <input type="hidden" name="action" value="luu">
                <div class="row">
                    <div class="col-md-4 mb-3">
                        <label class="form-label fw-bold">Tên loại phòng <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" name="ten" placeholder="VD: Phòng Deluxe - Giường Đôi" required>
                    </div>
                    <div class="col-md-4 mb-3">
                        <label class="form-label fw-bold">Hạng phòng <span class="text-danger">*</span></label>
                        <select class="form-select" name="idHangPhong" required>
                            <option value="">-- Chọn hạng --</option>
                            <c:forEach var="hp" items="${dsHangPhong}">
                                <option value="${hp.id}">${hp.moTa} (${hp.maHangPhong})</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-md-4 mb-3">
                        <label class="form-label fw-bold">Loại giường <span class="text-danger">*</span></label>
                        <select class="form-select" name="idLoaiGiuong" required>
                            <option value="">-- Chọn loại giường --</option>
                            <c:forEach var="lg" items="${dsLoaiGiuong}">
                                <option value="${lg.id}">${lg.ten} (sức chứa: ${lg.sucChua})</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-4 mb-3">
                        <label class="form-label fw-bold">Giá mỗi đêm (VNĐ) <span class="text-danger">*</span></label>
                        <input type="number" min="1" step="1000" class="form-control" name="giaMoiDem" required>
                    </div>
                    <div class="col-md-8 mb-3">
                        <label class="form-label fw-bold">Mô tả</label>
                        <input type="text" class="form-control" name="moTa" placeholder="Mô tả ngắn (không bắt buộc)">
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
                <i class="fa-solid fa-table-list"></i> Danh sách loại phòng
                <span class="badge bg-light text-dark ms-2">${dsLoaiPhong.size()} loại</span>
            </h5>
        </div>
        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-bordered table-striped table-hover align-middle text-center">
                    <thead class="table-dark">
                    <tr>
                        <th>ID</th>
                        <th>Tên loại phòng</th>
                        <th>Hạng phòng</th>
                        <th>Loại giường</th>
                        <th>Sức chứa</th>
                        <th>Giá/đêm</th>
                        <th>Mô tả</th>
                        <th>Thao tác</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="lp" items="${dsLoaiPhong}">
                        <tr>
                            <td>${lp.id}</td>
                            <td class="fw-bold text-start ps-3">${lp.ten}</td>
                            <td><span class="badge bg-info text-white">${lp.hangPhong.maHangPhong}</span></td>
                            <td>${lp.loaiGiuong.ten}</td>
                            <td>${lp.loaiGiuong.sucChua}</td>
                            <td class="text-danger fw-bold"><fmt:formatNumber value="${lp.giaMoiDem}" type="number"/> đ</td>
                            <td class="text-start">${lp.moTa}</td>
                            <td>
                                <a href="${pageContext.request.contextPath}/loai-phong?action=form&id=${lp.id}" class="btn btn-warning btn-sm" title="Sửa">
                                    <i class="fa-solid fa-pen"></i>
                                </a>
                                <form action="${pageContext.request.contextPath}/loai-phong" method="POST" class="d-inline"
                                      onsubmit="return confirm('Xóa loại phòng này? Hành động không thể hoàn tác!');">
                                    <input type="hidden" name="action" value="xoa">
                                    <input type="hidden" name="id" value="${lp.id}">
                                    <button type="submit" class="btn btn-danger btn-sm" title="Xóa">
                                        <i class="fa-solid fa-trash"></i>
                                    </button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty dsLoaiPhong}">
                        <tr><td colspan="8" class="text-muted">Chưa có loại phòng nào.</td></tr>
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
