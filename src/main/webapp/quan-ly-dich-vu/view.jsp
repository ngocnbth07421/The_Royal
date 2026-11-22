
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>Quản lý dịch vụ - The Royal Hotel</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
          rel="stylesheet">

    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>

<body class="bg-light">

<div class="container py-4">

    <!-- TIEU DE -->
    <div class="text-center mb-4">

        <h2 class="text-primary fw-bold">
            <i class="fa-solid fa-bell-concierge"></i>
            QUẢN LÝ DỊCH VỤ
        </h2>

        <p class="text-muted">
            Quản lý danh mục dịch vụ và lịch sử sử dụng dịch vụ khách sạn
        </p>

    </div>

    <!-- THONG BAO LOI -->
    <c:if test="${not empty errorMessage}">

        <div class="alert alert-danger alert-dismissible fade show">

            <i class="fa-solid fa-circle-exclamation"></i>
                ${errorMessage}

            <button type="button"
                    class="btn-close"
                    data-bs-dismiss="alert">
            </button>

        </div>

    </c:if>


    <!-- ================================================== -->
    <!-- PHAN 1: QUAN LY DICH VU -->
    <!-- ================================================== -->

    <div class="card shadow-sm mb-4">

        <div class="card-header bg-primary text-white">

            <h5 class="mb-0">
                <i class="fa-solid fa-concierge-bell"></i>
                1. QUẢN LÝ DỊCH VỤ
            </h5>

        </div>

        <div class="card-body">

            <c:set var="serviceAction"
                   value="${pageContext.request.contextPath}/dich-vu/add"/>

            <c:if test="${not empty dv}">
                <c:set var="serviceAction"
                       value="${pageContext.request.contextPath}/dich-vu/update"/>
            </c:if>

            <!-- FORM DICH VU -->

            <form action="${serviceAction}" method="POST">

                <c:if test="${not empty dv}">
                    <input type="hidden"
                           name="id"
                           value="${dv.id_dich_vu}">
                </c:if>

                <h6 class="fw-bold text-primary mb-3">

                    <i class="fa-solid fa-pen-to-square"></i>

                    ${not empty dv ? "Cập nhật dịch vụ" : "Thêm dịch vụ mới"}

                </h6>

                <div class="row">

                    <div class="col-md-4 mb-3">

                        <label class="form-label fw-bold">
                            Mã dịch vụ
                        </label>

                        <input type="text"
                               class="form-control"
                               name="ma_dich_vu"
                               value="${dv.ma_dich_vu}"
                               placeholder="VD: DV001"
                               required>

                    </div>

                    <div class="col-md-4 mb-3">

                        <label class="form-label fw-bold">
                            Tên dịch vụ
                        </label>

                        <input type="text"
                               class="form-control"
                               name="ten_dich_vu"
                               value="${dv.ten_dich_vu}"
                               placeholder="VD: Giặt ủi"
                               required>

                    </div>

                    <div class="col-md-4 mb-3">

                        <label class="form-label fw-bold">
                            Danh mục
                        </label>

                        <input type="text"
                               class="form-control"
                               name="danh_muc"
                               value="${dv.danh_muc}"
                               placeholder="VD: Ăn uống">

                    </div>

                </div>

                <div class="row">

                    <div class="col-md-4 mb-3">

                        <label class="form-label fw-bold">
                            Đơn vị tính
                        </label>

                        <input type="text"
                               class="form-control"
                               name="don_vi_tinh"
                               value="${dv.don_vi_tinh}"
                               placeholder="VD: Lần, kg, suất"
                               required>

                    </div>

                    <div class="col-md-4 mb-3">

                        <label class="form-label fw-bold">
                            Đơn giá (VNĐ)
                        </label>

                        <input type="number"
                               min="0"
                               step="1"
                               class="form-control"
                               name="don_gia"
                               value="${dv.don_gia}"
                               required>

                    </div>

                    <div class="col-md-4 mb-3">

                        <label class="form-label fw-bold">
                            Trạng thái
                        </label>

                        <select class="form-select"
                                name="trang_thai"
                                required>

                            <option value="HOAT_DONG"
                            ${dv.trang_thai == 'HOAT_DONG' ? 'selected' : ''}>
                                Hoạt động
                            </option>

                            <option value="NGUNG"
                            ${dv.trang_thai == 'NGUNG' ? 'selected' : ''}>
                                Ngừng hoạt động
                            </option>

                        </select>

                    </div>

                </div>

                <div class="text-end">

                    <c:if test="${not empty dv}">

                        <a href="${pageContext.request.contextPath}/dich-vu/view"
                           class="btn btn-secondary me-2">

                            <i class="fa-solid fa-xmark"></i>
                            Hủy sửa

                        </a>

                    </c:if>

                    <button type="submit" class="btn btn-success">

                        <i class="fa-solid fa-floppy-disk"></i>

                        ${not empty dv ? "Cập nhật dịch vụ" : "Thêm dịch vụ"}

                    </button>

                </div>

            </form>

            <hr class="my-4">

            <!-- BANG DICH VU -->

            <div class="d-flex justify-content-between align-items-center mb-3">

                <h6 class="fw-bold mb-0">

                    <i class="fa-solid fa-list text-primary"></i>
                    Danh sách dịch vụ

                </h6>

                <span class="badge bg-primary">
                    ${listDichVu.size()} dịch vụ
                </span>

            </div>

            <div class="table-responsive">

                <table class="table table-bordered table-striped table-hover align-middle text-center">

                    <thead class="table-dark">

                    <tr>
                        <th>ID</th>
                        <th>Mã DV</th>
                        <th>Tên dịch vụ</th>
                        <th>Danh mục</th>
                        <th>Đơn vị</th>
                        <th>Đơn giá</th>
                        <th>Trạng thái</th>
                        <th>Thao tác</th>
                    </tr>

                    </thead>

                    <tbody>

                    <c:forEach var="item" items="${listDichVu}">

                        <tr>

                            <td>${item.id_dich_vu}</td>

                            <td class="fw-bold">
                                    ${item.ma_dich_vu}
                            </td>

                            <td>
                                    ${item.ten_dich_vu}
                            </td>

                            <td>
                                    ${item.danh_muc}
                            </td>

                            <td>
                                    ${item.don_vi_tinh}
                            </td>

                            <td class="text-danger fw-bold">

                                <fmt:formatNumber
                                        value="${item.don_gia}"
                                        type="number"/> đ

                            </td>

                            <td>

                                <c:choose>

                                    <c:when test="${item.trang_thai == 'HOAT_DONG'}">

                                        <span class="badge bg-success">
                                            Hoạt động
                                        </span>

                                    </c:when>

                                    <c:otherwise>

                                        <span class="badge bg-danger">
                                            Ngừng hoạt động
                                        </span>

                                    </c:otherwise>

                                </c:choose>

                            </td>

                            <td>

                                <a href="${pageContext.request.contextPath}/dich-vu/view-update?id=${item.id_dich_vu}"
                                   class="btn btn-warning btn-sm"
                                   title="Sửa">

                                    <i class="fa-solid fa-pen"></i>

                                </a>

                                <a href="${pageContext.request.contextPath}/dich-vu/detail?id=${item.id_dich_vu}"
                                   class="btn btn-info btn-sm text-white"
                                   title="Chi tiết">

                                    <i class="fa-solid fa-eye"></i>

                                </a>

                                <a href="${pageContext.request.contextPath}/dich-vu/delete?id=${item.id_dich_vu}"
                                   class="btn btn-danger btn-sm"
                                   onclick="return confirm('Bạn có chắc muốn xóa dịch vụ này?')"
                                   title="Xóa">

                                    <i class="fa-solid fa-trash"></i>

                                </a>

                            </td>

                        </tr>

                    </c:forEach>

                    <c:if test="${empty listDichVu}">

                        <tr>
                            <td colspan="8" class="text-muted">
                                Chưa có dữ liệu dịch vụ.
                            </td>
                        </tr>

                    </c:if>

                    </tbody>

                </table>

            </div>

        </div>

    </div>


    <!-- ================================================== -->
    <!-- PHAN 2: SU DUNG DICH VU -->
    <!-- ================================================== -->

    <div class="card shadow-sm mb-4">

        <div class="card-header bg-success text-white">

            <h5 class="mb-0">

                <i class="fa-solid fa-clipboard-list"></i>
                2. QUẢN LÝ SỬ DỤNG DỊCH VỤ

            </h5>

        </div>

        <div class="card-body">

            <c:set var="usageAction"
                   value="${pageContext.request.contextPath}/dich-vu/sddv-add"/>

            <c:if test="${not empty sddv}">
                <c:set var="usageAction"
                       value="${pageContext.request.contextPath}/dich-vu/sddv-update"/>
            </c:if>

            <!-- FORM SU DUNG DICH VU -->

            <form action="${usageAction}" method="POST">

                <c:if test="${not empty sddv}">

                    <input type="hidden"
                           name="id"
                           value="${sddv.id_su_dung_dich_vu}">

                </c:if>

                <h6 class="fw-bold text-success mb-3">

                    <i class="fa-solid fa-pen-to-square"></i>

                    ${not empty sddv ? "Cập nhật sử dụng dịch vụ" : "Thêm lượt sử dụng dịch vụ"}

                </h6>

                <div class="row">

                    <div class="col-md-4 mb-3">

                        <label class="form-label fw-bold">
                            ID đặt phòng
                        </label>

                        <input type="number"
                               min="1"
                               class="form-control"
                               name="id_dat_phong"
                               value="${sddv.id_dat_phong}"
                               placeholder="Nhập ID đặt phòng"
                               required>

                    </div>

                    <div class="col-md-4 mb-3">

                        <label class="form-label fw-bold">
                            Chọn dịch vụ
                        </label>

                        <select class="form-select"
                                name="id_dich_vu"
                                required>

                            <option value="">
                                -- Chọn dịch vụ --
                            </option>

                            <c:forEach var="dvItem" items="${listDichVu}">

                                <option value="${dvItem.id_dich_vu}"
                                    ${sddv.id_dich_vu == dvItem.id_dich_vu ? 'selected' : ''}>

                                        ${dvItem.ma_dich_vu}
                                    -
                                        ${dvItem.ten_dich_vu}

                                </option>

                            </c:forEach>

                        </select>

                    </div>

                    <div class="col-md-4 mb-3">

                        <label class="form-label fw-bold">
                            Số lượng
                        </label>

                        <input type="number"
                               min="0.01"
                               step="0.01"
                               class="form-control"
                               name="so_luong"
                               value="${sddv.so_luong}"
                               required>

                    </div>

                </div>

                <div class="row">

                    <div class="col-md-4 mb-3">

                        <label class="form-label fw-bold">
                            Thành tiền (VNĐ)
                        </label>

                        <input type="number"
                               min="0"
                               step="1"
                               class="form-control"
                               name="thanh_tien"
                               value="${sddv.thanh_tien}"
                               required>

                    </div>

                    <div class="col-md-4 mb-3">

                        <label class="form-label fw-bold">
                            Tiền giảm giá (VNĐ)
                        </label>

                        <input type="number"
                               min="0"
                               step="1"
                               class="form-control"
                               name="tien_giam_gia"
                               value="${not empty sddv ? sddv.tien_giam_gia : 0}"
                               required>

                    </div>

                    <div class="col-md-4 mb-3">

                        <label class="form-label fw-bold">
                            Thời gian sử dụng
                        </label>

                        <input type="datetime-local"
                               class="form-control"
                               name="thoi_gian_su_dung"
                               value="${sddv.thoi_gian_su_dung}">

                    </div>

                </div>

                <div class="text-end">

                    <c:if test="${not empty sddv}">

                        <a href="${pageContext.request.contextPath}/dich-vu/view"
                           class="btn btn-secondary me-2">

                            <i class="fa-solid fa-xmark"></i>
                            Hủy sửa

                        </a>

                    </c:if>

                    <button type="submit" class="btn btn-success">

                        <i class="fa-solid fa-floppy-disk"></i>

                        ${not empty sddv ? "Cập nhật sử dụng dịch vụ" : "Thêm sử dụng dịch vụ"}

                    </button>

                </div>

            </form>

            <hr class="my-4">

            <!-- BANG SU DUNG DICH VU -->

            <div class="d-flex justify-content-between align-items-center mb-3">

                <h6 class="fw-bold mb-0">

                    <i class="fa-solid fa-table-list text-success"></i>
                    Danh sách sử dụng dịch vụ

                </h6>

                <span class="badge bg-success">
                    ${listSuDungDichVu.size()} lượt sử dụng
                </span>

            </div>

            <div class="table-responsive">

                <table class="table table-bordered table-striped table-hover align-middle text-center">

                    <thead class="table-dark">

                    <tr>
                        <th>ID</th>
                        <th>ID đặt phòng</th>
                        <th>Mã dịch vụ</th>
                        <th>Tên dịch vụ</th>
                        <th>Số lượng</th>
                        <th>Thành tiền</th>
                        <th>Giảm giá</th>
                        <th>Thời gian sử dụng</th>
                        <th>Thao tác</th>
                    </tr>

                    </thead>

                    <tbody>

                    <c:forEach var="item" items="${listSuDungDichVu}">

                        <tr>

                            <td>
                                    ${item.id_su_dung_dich_vu}
                            </td>

                            <td>
                                    ${item.id_dat_phong}
                            </td>

                            <!-- NOI ID DICH VU VOI BANG DICH VU -->

                            <td>

                                <c:forEach var="dvItem" items="${listDichVu}">

                                    <c:if test="${dvItem.id_dich_vu == item.id_dich_vu}">

                                        ${dvItem.ma_dich_vu}

                                    </c:if>

                                </c:forEach>

                            </td>

                            <td class="fw-bold">

                                <c:forEach var="dvItem" items="${listDichVu}">

                                    <c:if test="${dvItem.id_dich_vu == item.id_dich_vu}">

                                        ${dvItem.ten_dich_vu}

                                    </c:if>

                                </c:forEach>

                            </td>

                            <td>
                                    ${item.so_luong}
                            </td>

                            <td class="text-danger fw-bold">

                                <fmt:formatNumber
                                        value="${item.thanh_tien}"
                                        type="number"/> đ

                            </td>

                            <td>

                                <fmt:formatNumber
                                        value="${item.tien_giam_gia}"
                                        type="number"/> đ

                            </td>

                            <td>
                                    ${item.thoi_gian_su_dung}
                            </td>

                            <td>

                                <a href="${pageContext.request.contextPath}/dich-vu/sddv-view-update?id=${item.id_su_dung_dich_vu}"
                                   class="btn btn-warning btn-sm"
                                   title="Sửa">

                                    <i class="fa-solid fa-pen"></i>

                                </a>

                                <a href="${pageContext.request.contextPath}/dich-vu/sddv-detail?id=${item.id_su_dung_dich_vu}"
                                   class="btn btn-info btn-sm text-white"
                                   title="Chi tiết">

                                    <i class="fa-solid fa-eye"></i>

                                </a>

                                <a href="${pageContext.request.contextPath}/dich-vu/sddv-delete?id=${item.id_su_dung_dich_vu}"
                                   class="btn btn-danger btn-sm"
                                   onclick="return confirm('Bạn có chắc muốn xóa lượt sử dụng này?')"
                                   title="Xóa">

                                    <i class="fa-solid fa-trash"></i>

                                </a>

                            </td>

                        </tr>

                    </c:forEach>

                    <c:if test="${empty listSuDungDichVu}">

                        <tr>

                            <td colspan="9" class="text-muted">

                                Chưa có dữ liệu sử dụng dịch vụ.

                            </td>

                        </tr>

                    </c:if>

                    </tbody>

                </table>

            </div>

        </div>

    </div>

</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js">
</script>

</body>
</html>
