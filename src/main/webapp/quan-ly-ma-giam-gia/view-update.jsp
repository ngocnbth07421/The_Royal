<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Cập nhật mã giảm giá - The Royal Hotel</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body class="bg-light">
<div class="container py-4">
    <div class="card shadow-sm mx-auto" style="max-width: 800px;">
        <div class="card-header bg-warning text-dark">
            <h5 class="mb-0"><i class="fa-solid fa-pen-to-square"></i> ${not empty mggSua ? 'Cập nhật mã giảm giá' : 'Thêm mã giảm giá'}</h5>
        </div>
        <div class="card-body">
            <form action="${pageContext.request.contextPath}/ma-giam-gia" method="POST">
                <input type="hidden" name="action" value="luu">
                <c:if test="${not empty mggSua}"><input type="hidden" name="id" value="${mggSua.id}"></c:if>
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label class="form-label fw-bold">Mã giảm giá <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" name="maGiamGia" value="<c:out value='${mggSua.maGiamGia}'/>" required style="text-transform: uppercase;">
                    </div>
                    <div class="col-md-6 mb-3">
                        <label class="form-label fw-bold">Loại giảm giá <span class="text-danger">*</span></label>
                        <select class="form-select" name="loaiGiamGia" required>
                            <option value="Phần trăm" ${mggSua.loaiGiamGia == 'Phần trăm' ? 'selected' : ''}>Phần trăm (%)</option>
                            <option value="Số tiền" ${mggSua.loaiGiamGia == 'Số tiền' ? 'selected' : ''}>Số tiền (VNĐ)</option>
                        </select>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label class="form-label fw-bold">Giá trị <span class="text-danger">*</span></label>
                        <input type="number" min="0.01" step="0.01" class="form-control" name="giaTri" value="${mggSua.giaTri}" required>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label class="form-label fw-bold">Trạng thái</label>
                        <select class="form-select" name="trangThai">
                            <option value="Hoạt động" ${mggSua.trangThai == 'Hoạt động' ? 'selected' : ''}>Hoạt động</option>
                            <option value="Ngưng" ${mggSua.trangThai == 'Ngưng' ? 'selected' : ''}>Ngưng</option>
                        </select>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-4 mb-3">
                        <label class="form-label fw-bold">Bắt đầu lúc <span class="text-danger">*</span></label>
                        <input type="datetime-local" class="form-control" name="batDauLuc" value="${mggSua.batDauLuc}" required>
                    </div>
                    <div class="col-md-4 mb-3">
                        <label class="form-label fw-bold">Kết thúc lúc <span class="text-danger">*</span></label>
                        <input type="datetime-local" class="form-control" name="ketThucLuc" value="${mggSua.ketThucLuc}" required>
                    </div>
                    <div class="col-md-4 mb-3">
                        <label class="form-label fw-bold">Số lượt còn dùng</label>
                        <input type="number" min="0" class="form-control" name="soLuotDaDung" value="${not empty mggSua ? mggSua.soLuotDaDung : 0}">
                    </div>
                </div>
                <div class="text-end">
                    <a href="${pageContext.request.contextPath}/ma-giam-gia" class="btn btn-secondary me-2">Hủy</a>
                    <button type="submit" class="btn btn-success"><i class="fa-solid fa-floppy-disk"></i> Lưu thay đổi</button>
                </div>
            </form>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
