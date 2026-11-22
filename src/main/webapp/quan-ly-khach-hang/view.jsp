
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Quản Lý Khách Hàng - The Royal Hotel</title>

    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>

<body class="bg-light">

<div class="container py-4">

    <h2 class="mb-4 text-center text-primary fw-bold">
        <i class="fa-solid fa-users"></i> QUẢN LÝ KHÁCH HÀNG
    </h2>

    <!-- FORM THÊM / SỬA KHÁCH HÀNG -->
    <div class="card shadow-sm mb-4">
        <div class="card-header bg-primary text-white">
            <h5 class="mb-0">
                <i class="fa-solid fa-user-pen"></i>
                ${kh != null ? "Cập nhật thông tin khách hàng" : "Thêm khách hàng mới"}
            </h5>
        </div>

        <div class="card-body">

            <c:choose>
            <c:when test="${kh != null}">
            <form action="${pageContext.request.contextPath}/quan-ly-khach-hang/update?id=${kh.id_khach_hang}" method="POST">
                </c:when>
                <c:otherwise>
                <form action="${pageContext.request.contextPath}/quan-ly-khach-hang/add" method="POST">
                    </c:otherwise>
                    </c:choose>

                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label class="form-label fw-bold">Họ Tên:</label>
                            <input type="text" class="form-control"
                                   name="ho_ten"
                                   value="${kh.ho_ten}"
                                   required
                                   placeholder="Nhập họ tên khách hàng...">
                        </div>

                        <div class="col-md-6 mb-3">
                            <label class="form-label fw-bold">Số Điện thoại:</label>
                            <input type="text" class="form-control"
                                   name="so_dien_thoai"
                                   value="${kh.so_dien_thoai}"
                                   required
                                   placeholder="Nhập số điện thoại...">
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label class="form-label fw-bold">Email:</label>
                            <input type="email" class="form-control"
                                   name="email"
                                   value="${kh.email}"
                                   required
                                   placeholder="Nhập địa chỉ email...">
                        </div>

                        <div class="col-md-6 mb-3">
                            <label class="form-label fw-bold">Trạng Thái:</label>
                            <select class="form-select" name="trang_thai">
                                <option value="HOAT_DONG"
                                ${kh.trang_thai == 'HOAT_DONG' ? 'selected' : ''}>
                                    Đang hoạt động
                                </option>

                                <option value="BI_KHOA"
                                ${kh.trang_thai == 'BI_KHOA' ? 'selected' : ''}>
                                    Khóa
                                </option>
                            </select>

                        </div>
                    </div>

                    <div class="text-end">
                        <c:if test="${kh != null}">
                            <a href="${pageContext.request.contextPath}/quan-ly-khach-hang/view"
                               class="btn btn-secondary me-2">
                                Hủy
                            </a>
                        </c:if>

                        <button type="submit" class="btn btn-success">
                            <i class="fa-solid fa-floppy-disk"></i>
                            ${kh != null ? "Cập Nhật" : "Thêm Mới"}
                        </button>
                    </div>

                </form>

        </div>
    </div>

    <!-- BẢNG DANH SÁCH KHÁCH HÀNG -->
    <div class="card shadow-sm">
        <div class="card-header bg-secondary text-white">
            <h5 class="mb-0">
                <i class="fa-solid fa-table-list"></i> Danh Sách Khách Hàng
            </h5>
        </div>

        <div class="card-body">
            <div class="table-responsive">

                <table class="table table-bordered table-striped table-hover align-middle text-center">

                    <thead class="table-dark">
                    <tr>
                        <th>ID</th>
                        <th>Họ Tên</th>
                        <th>Số Điện Thoại</th>
                        <th>Email</th>
                        <th>Trạng Thái</th>
                        <th>Thao Tác</th>
                    </tr>
                    </thead>

                    <tbody>

                    <c:forEach var="item" items="${listKhachHang}">
                        <tr>
                            <td>${item.id_khach_hang}</td>

                            <td class="fw-bold text-start ps-3">
                                    ${item.ho_ten}
                            </td>

                            <td>${item.so_dien_thoai}</td>

                            <td>${item.email}</td>

                            <td>
                                <span class="badge ${item.trang_thai == 'HOAT_DONG' ? 'bg-success' : 'bg-danger'}">
                                        ${item.trang_thai == 'HOAT_DONG' ? 'Đang hoạt động' : 'Khóa'}
                                </span>
                            </td>

                            <td>
                                <a href="${pageContext.request.contextPath}/quan-ly-khach-hang/view-update?id=${item.id_khach_hang}"
                                   class="btn btn-warning btn-sm"
                                   title="Sửa">
                                    <i class="fa-solid fa-pen"></i>
                                </a>

                                <a href="${pageContext.request.contextPath}/quan-ly-khach-hang/detail?id=${item.id_khach_hang}"
                                   class="btn btn-info btn-sm text-white"
                                   title="Chi tiết">
                                    <i class="fa-solid fa-eye"></i>
                                </a>

                                <a href="${pageContext.request.contextPath}/quan-ly-khach-hang/delete?id=${item.id_khach_hang}"
                                   class="btn btn-danger btn-sm"
                                   onclick="return confirm('Bạn có chắc chắn muốn xóa khách hàng này?');"
                                   title="Xóa">
                                    <i class="fa-solid fa-trash"></i>
                                </a>
                            </td>
                        </tr>
                    </c:forEach>

                    <c:if test="${empty listKhachHang}">
                        <tr>
                            <td colspan="6" class="text-muted">
                                Không có dữ liệu khách hàng nào.
                            </td>
                        </tr>
                    </c:if>

                    </tbody>
                </table>

            </div>
        </div>
    </div>

</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>
