<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cập Nhật Hồ Sơ Nhân Viên — The Royal Hotel</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
    <link href="https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/boxicons@2.1.4/css/boxicons.min.css" rel="stylesheet">

    <style>
        html, body {
            margin: 0;
            padding: 0;
            width: 100%;
            overflow-x: hidden;
            background: #f7f9f8;
        }
        .app-shell {
            display: flex;
            width: 100%;
            max-width: 100%;
            min-height: 100vh;
            overflow-x: hidden;
            background: #f7f9f8;
            box-sizing: border-box;
        }
        .main-area {
            margin-left: 260px;
            min-height: 100vh;
            flex: 1;
            width: calc(100% - 260px);
            max-width: calc(100% - 260px);
            display: flex;
            flex-direction: column;
            overflow-x: hidden;
            box-sizing: border-box;
            background: #f7f9f8;
        }
        .content {
            padding: 24px 32px 48px;
            flex: 1;
            max-width: 100%;
            box-sizing: border-box;
            overflow-x: hidden;
        }
        .form-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
            gap: 20px;
            margin-bottom: 24px;
        }
        .form-group label {
            display: block;
            font-size: 13px;
            font-weight: 600;
            color: #2b3a33;
            margin-bottom: 6px;
        }
        .form-control {
            width: 100%;
            padding: 10px 14px;
            border: 1px solid #d4ddd7;
            border-radius: 8px;
            font-size: 13px;
            background: #fff;
            color: #222831;
            outline: none;
            transition: all 0.2s ease;
        }
        .form-control:focus {
            border-color: #557c56;
            box-shadow: 0 0 0 3px rgba(85, 124, 86, 0.15);
        }
    </style>
</head>

<body>
    <div class="app-shell">
        <!-- 1. SIDEBAR DÙNG CHUNG -->
        <jsp:include page="/layout/sidebar.jsp">
            <jsp:param name="activeMenu" value="nhan-vien" />
        </jsp:include>

        <!-- 2. MAIN AREA -->
        <main class="main-area">
            <!-- HEADER DÙNG CHUNG -->
            <jsp:include page="/layout/header.jsp">
                <jsp:param name="pageTitle" value="Cập Nhật Nhân Viên ${nhanVien.maNhanVien}" />
            </jsp:include>

            <section class="content" style="padding: 28px;">
                <div class="page-heading">
                    <div>
                        <div class="eyebrow" style="font-size: 11px; font-weight: 700; color: #557c56; letter-spacing: 2px;">CHỈNH SỬA HỒ SƠ NHÂN SỰ</div>
                        <h1 style="font-size: 24px; font-weight: 800; color: #222831; margin: 4px 0;">Cập Nhật Nhân Viên: ${nhanVien.hoTen}</h1>
                        <p style="color: #6b7a70; font-size: 13px; margin: 0;">Cập nhật số điện thoại, điều chỉnh vai trò công tác, lương cơ bản, phụ cấp hoặc trạng thái tài khoản.</p>
                    </div>
                    <div class="heading-actions">
                        <a href="${pageContext.request.contextPath}/quan-ly-nhan-vien/view" class="btn btn-ghost">
                            <i class='bx bx-arrow-back'></i> Quay lại danh sách
                        </a>
                    </div>
                </div>

                <div class="card">
                    <form action="${pageContext.request.contextPath}/quan-ly-nhan-vien/update" method="post">
                        <input type="hidden" name="id" value="${nhanVien.idNhanVien}">

                        <div class="form-grid">
                            <div class="form-group">
                                <label>Mã nhân viên <span style="color:red">*</span></label>
                                <input type="text" name="maNhanVien" class="form-control" value="${nhanVien.maNhanVien}" required>
                            </div>

                            <div class="form-group">
                                <label>Họ và tên <span style="color:red">*</span></label>
                                <input type="text" name="hoTen" class="form-control" value="${nhanVien.hoTen}" required>
                            </div>

                            <div class="form-group">
                                <label>Số điện thoại</label>
                                <input type="text" name="soDienThoai" class="form-control" value="${nhanVien.soDienThoai}">
                            </div>

                            <div class="form-group">
                                <label>Vai trò / Chức danh <span style="color:red">*</span></label>
                                <select name="idVaiTro" class="form-control" required>
                                    <c:forEach items="${listVaiTro}" var="vt">
                                        <option value="${vt.idVaiTro}" ${vt.idVaiTro == nhanVien.idVaiTro ? 'selected' : ''}>
                                            ${vt.tenVaiTro} (${vt.maVaiTro})
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="form-group">
                                <label>Ngày vào làm</label>
                                <fmt:formatDate value="${nhanVien.ngayVaoLam}" pattern="yyyy-MM-dd" var="formattedDate" />
                                <input type="date" name="ngayVaoLam" class="form-control" value="${formattedDate}">
                            </div>

                            <div class="form-group">
                                <label>Lương cơ bản (VNĐ) <span style="color:red">*</span></label>
                                <input type="number" name="luongCoBan" class="form-control" value="${nhanVien.luongCoBan}" min="0" step="100000" required>
                            </div>

                            <div class="form-group">
                                <label>Phụ cấp (VNĐ)</label>
                                <input type="number" name="phuCap" class="form-control" value="${nhanVien.phuCap}" min="0" step="50000">
                            </div>

                            <div class="form-group">
                                <label>Khấu trừ (VNĐ)</label>
                                <input type="number" name="khauTru" class="form-control" value="${nhanVien.khauTru}" min="0" step="50000">
                            </div>

                            <div class="form-group">
                                <label>Trạng thái</label>
                                <select name="trangThai" class="form-control">
                                    <option value="HOAT_DONG" ${nhanVien.trangThai == 'HOAT_DONG' ? 'selected' : ''}>Đang hoạt động</option>
                                    <option value="BI_KHOA" ${nhanVien.trangThai == 'BI_KHOA' ? 'selected' : ''}>Tạm khóa</option>
                                </select>
                            </div>
                        </div>

                        <div style="display: flex; justify-content: flex-end; gap: 12px; border-top: 1px solid #eef2ef; padding-top: 18px;">
                            <a href="${pageContext.request.contextPath}/quan-ly-nhan-vien/view" class="btn btn-ghost">Hủy bỏ</a>
                            <button type="submit" class="btn btn-primary"><i class='bx bx-save'></i> Lưu Cập Nhật</button>
                        </div>
                    </form>
                </div>
            </section>

            <!-- FOOTER DÙNG CHUNG -->
            <jsp:include page="/layout/footer.jsp" />
        </main>
    </div>
</body>
</html>
