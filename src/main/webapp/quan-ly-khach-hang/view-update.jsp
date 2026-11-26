<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Cập Nhật Khách Hàng - The Royal Hotel</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body class="bg-light">
<div class="container py-4">
    <div class="card shadow-sm mx-auto" style="max-width: 750px;">
        <div class="card-header bg-warning text-dark">
            <h5 class="mb-0"><i class="fa-solid fa-user-pen"></i> Cập nhật thông tin khách hàng #${kh.id_khach_hang}</h5>
        </div>
        <div class="card-body">
            <form action="${pageContext.request.contextPath}/quan-ly-khach-hang/update?id=${kh.id_khach_hang}" method="POST">
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label class="form-label fw-bold">Họ Tên:</label>
                        <input type="text" class="form-control" name="ho_ten" value="<c:out value='${kh.ho_ten}'/>" required>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label class="form-label fw-bold">Số Điện thoại:</label>
                        <input type="text" class="form-control" name="so_dien_thoai" value="<c:out value='${kh.so_dien_thoai}'/>" required>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label class="form-label fw-bold">Email:</label>
                        <input type="email" class="form-control" name="email" value="<c:out value='${kh.email}'/>" required>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label class="form-label fw-bold">Trạng Thái:</label>
                        <select class="form-select" name="trang_thai">
                            <option value="HOAT_DONG" ${kh.trang_thai == 'HOAT_DONG' ? 'selected' : ''}>Đang hoạt động</option>
                            <option value="BI_KHOA" ${kh.trang_thai == 'BI_KHOA' ? 'selected' : ''}>Khóa</option>
                        </select>
                    </div>
                </div>
                <div class="text-end">
                    <a href="${pageContext.request.contextPath}/quan-ly-khach-hang/view" class="btn btn-secondary me-2">Hủy</a>
                    <button type="submit" class="btn btn-success"><i class="fa-solid fa-floppy-disk"></i> Lưu Cập Nhật</button>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>
