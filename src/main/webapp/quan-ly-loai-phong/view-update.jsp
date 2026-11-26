<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Cập nhật loại phòng - The Royal Hotel</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body class="bg-light">
<div class="container py-4">
    <div class="card shadow-sm mx-auto" style="max-width: 800px;">
        <div class="card-header bg-warning text-dark">
            <h5 class="mb-0"><i class="fa-solid fa-pen-to-square"></i> Cập nhật loại phòng #${loaiPhongSua.id}</h5>
        </div>
        <div class="card-body">
            <form action="${pageContext.request.contextPath}/loai-phong" method="POST">
                <input type="hidden" name="action" value="luu">
                <input type="hidden" name="id" value="${loaiPhongSua.id}">
                <div class="mb-3">
                    <label class="form-label fw-bold">Tên loại phòng <span class="text-danger">*</span></label>
                    <input type="text" class="form-control" name="ten" value="<c:out value='${loaiPhongSua.ten}'/>" required>
                </div>
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label class="form-label fw-bold">Hạng phòng <span class="text-danger">*</span></label>
                        <select class="form-select" name="idHangPhong" required>
                            <c:forEach var="hp" items="${dsHangPhong}">
                                <option value="${hp.id}" ${loaiPhongSua.hangPhong.id == hp.id ? 'selected' : ''}>${hp.moTa} (${hp.maHangPhong})</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label class="form-label fw-bold">Loại giường <span class="text-danger">*</span></label>
                        <select class="form-select" name="idLoaiGiuong" required>
                            <c:forEach var="lg" items="${dsLoaiGiuong}">
                                <option value="${lg.id}" ${loaiPhongSua.loaiGiuong.id == lg.id ? 'selected' : ''}>${lg.ten} (sức chứa: ${lg.sucChua})</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div class="mb-3">
                    <label class="form-label fw-bold">Giá mỗi đêm (VNĐ) <span class="text-danger">*</span></label>
                    <input type="number" min="1" step="1000" class="form-control" name="giaMoiDem" value="${loaiPhongSua.giaMoiDem}" required>
                </div>
                <div class="mb-3">
                    <label class="form-label fw-bold">Mô tả</label>
                    <textarea class="form-control" name="moTa" rows="3"><c:out value='${loaiPhongSua.moTa}'/></textarea>
                </div>
                <div class="text-end">
                    <a href="${pageContext.request.contextPath}/loai-phong" class="btn btn-secondary me-2">Hủy</a>
                    <button type="submit" class="btn btn-success"><i class="fa-solid fa-floppy-disk"></i> Lưu thay đổi</button>
                </div>
            </form>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
