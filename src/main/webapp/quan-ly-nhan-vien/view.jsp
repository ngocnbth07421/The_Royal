<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản Lý Nhân Sự &amp; Nhân Viên — The Royal Hotel</title>

    <!-- CSS chính phân tầng của The Royal Hotel -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
    <!-- Boxicons -->
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
        .page-link-btn {
            padding: 6px 12px;
            border: 1px solid #d4ddd7;
            border-radius: 6px;
            text-decoration: none;
            color: #333333;
            font-size: 12px;
            font-weight: 600;
            background: #ffffff;
            transition: all 0.2s;
        }
        .page-link-btn:hover {
            border-color: #557c56;
            color: #557c56;
        }
        .page-link-btn.active {
            background: #557c56;
            border-color: #557c56;
            color: #ffffff;
        }
        .form-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
            gap: 16px;
            margin-bottom: 16px;
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
            padding: 9px 14px;
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
        .stats-banner {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 16px;
            margin-bottom: 24px;
        }
        .stat-card {
            background: #fff;
            padding: 16px 20px;
            border-radius: 12px;
            border: 1px solid #e5ebe7;
            box-shadow: 0 2px 6px rgba(0,0,0,0.02);
            display: flex;
            align-items: center;
            gap: 14px;
        }
        .stat-icon {
            width: 44px;
            height: 44px;
            border-radius: 10px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 22px;
        }
        .stat-icon.total { background: #eaf1ed; color: #557c56; }
        .stat-icon.active { background: #e8f5e9; color: #2e7d32; }
        .stat-icon.locked { background: #ffebee; color: #c62828; }
        .stat-info b { font-size: 20px; color: #222831; display: block; font-weight: 700; }
        .stat-info span { font-size: 12px; color: #6b7a70; font-weight: 500; }
        
        .badge-active { background: #e8f5e9; color: #2e7d32; border: 1px solid #c8e6c9; }
        .badge-locked { background: #ffebee; color: #c62828; border: 1px solid #ffcdd2; }
        
        .role-pill {
            display: inline-flex;
            align-items: center;
            gap: 5px;
            padding: 4px 10px;
            border-radius: 6px;
            font-size: 12px;
            font-weight: 600;
            background: #f0f4f2;
            color: #2b3a33;
        }
        
        .action-btns {
            display: flex;
            gap: 6px;
            align-items: center;
        }
        .action-btn {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            width: 32px;
            height: 32px;
            border-radius: 6px;
            font-size: 15px;
            text-decoration: none;
            transition: all 0.2s;
        }
        .btn-view-detail { background: #f0f4f2; color: #557c56; }
        .btn-view-detail:hover { background: #557c56; color: #fff; }
        .btn-view-edit { background: #fff8e1; color: #f57f17; }
        .btn-view-edit:hover { background: #f57f17; color: #fff; }
        .btn-view-del { background: #ffebee; color: #c62828; }
        .btn-view-del:hover { background: #c62828; color: #fff; }
        
        .alert-msg {
            padding: 12px 18px;
            border-radius: 8px;
            margin-bottom: 16px;
            font-size: 13px;
            display: flex;
            align-items: center;
            gap: 10px;
        }
        .alert-success { background: #e8f5e9; color: #2e7d32; border: 1px solid #c8e6c9; }
        .alert-danger { background: #ffebee; color: #c62828; border: 1px solid #ffcdd2; }
    </style>
</head>

<body>
    <div class="app-shell">
        <!-- 1. THANH MENU BÊN TRÁI CỐ ĐỊNH (TÁCH RIÊNG DÙNG CHUNG) -->
        <jsp:include page="/layout/sidebar.jsp">
            <jsp:param name="activeMenu" value="nhan-vien" />
        </jsp:include>

        <!-- 2. KHU VỰC CHÍNH -->
        <main class="main-area">
            <!-- THANH HEADER CỐ ĐỊNH (TÁCH RIÊNG DÙNG CHUNG) -->
            <jsp:include page="/layout/header.jsp">
                <jsp:param name="pageTitle" value="Quản Lý Nhân Sự &amp; Nhân Viên" />
            </jsp:include>

            <!-- Nội dung trang -->
            <section class="content" style="padding: 28px;">
                <div class="page-heading">
                    <div>
                        <div class="eyebrow" style="font-size: 11px; font-weight: 700; color: #557c56; letter-spacing: 2px;">HỆ THỐNG QUẢN TRỊ NHÂN LỰC KHÁCH SẠN HOÀNG GIA</div>
                        <h1 style="font-size: 24px; font-weight: 800; color: #222831; margin: 4px 0;">Quản Lý Nhân Viên &amp; Chức Vụ</h1>
                        <p style="color: #6b7a70; font-size: 13px; margin: 0;">Theo dõi hồ sơ nhân sự, phân quyền vai trò (Lễ tân, Buồng phòng, Kế toán, Quản lý, Bảo trì) và chế độ đãi ngộ lương thưởng.</p>
                    </div>
                </div>

                <!-- Thông báo -->
                <c:if test="${param.success == 'add'}">
                    <div class="alert-msg alert-success"><i class='bx bx-check-circle'></i> Thêm nhân viên mới thành công!</div>
                </c:if>
                <c:if test="${param.success == 'update'}">
                    <div class="alert-msg alert-success"><i class='bx bx-check-circle'></i> Cập nhật hồ sơ nhân viên thành công!</div>
                </c:if>
                <c:if test="${param.error == 'duplicate_manv'}">
                    <div class="alert-msg alert-danger"><i class='bx bx-error-circle'></i> Mã nhân viên đã tồn tại trên hệ thống. Vui lòng nhập mã khác!</div>
                </c:if>

                <!-- 3 Thẻ thống kê -->
                <div class="stats-banner">
                    <div class="stat-card">
                        <div class="stat-icon total"><i class='bx bx-user-pin'></i></div>
                        <div class="stat-info">
                            <b>${totalStaff}</b>
                            <span>Tổng số nhân sự</span>
                        </div>
                    </div>
                    <div class="stat-card">
                        <div class="stat-icon active"><i class='bx bx-user-check'></i></div>
                        <div class="stat-info">
                            <b>${countHoatDong}</b>
                            <span>Đang hoạt động</span>
                        </div>
                    </div>
                    <div class="stat-card">
                        <div class="stat-icon locked"><i class='bx bx-user-x'></i></div>
                        <div class="stat-info">
                            <b>${countBiKhoa}</b>
                            <span>Tạm khóa / Tạm dừng</span>
                        </div>
                    </div>
                </div>

                <!-- CARD 1: FORM THÊM NHÂN VIÊN MỚI -->
                <div class="card" style="margin-bottom: 24px;">
                    <div class="card-header" style="border-bottom: 1px solid #eef2ef; padding-bottom: 14px; margin-bottom: 18px;">
                        <h3 style="font-size: 16px; font-weight: 700; color: #222831; display: flex; align-items: center; gap: 8px;">
                            <i class='bx bx-user-plus' style="color: #557c56; font-size: 20px;"></i> Thêm Mới Hồ Sơ Nhân Viên
                        </h3>
                    </div>

                    <form action="${pageContext.request.contextPath}/quan-ly-nhan-vien/add" method="post">
                        <div class="form-grid">
                            <div class="form-group">
                                <label>Mã nhân viên <span style="color:red">*</span></label>
                                <input type="text" name="maNhanVien" class="form-control" placeholder="VD: NV011, NV012..." required>
                            </div>

                            <div class="form-group">
                                <label>Họ và tên <span style="color:red">*</span></label>
                                <input type="text" name="hoTen" class="form-control" placeholder="Họ và tên nhân viên" required>
                            </div>

                            <div class="form-group">
                                <label>Số điện thoại</label>
                                <input type="text" name="soDienThoai" class="form-control" placeholder="090xxxxxxx">
                            </div>

                            <div class="form-group">
                                <label>Vai trò / Chức danh <span style="color:red">*</span></label>
                                <select name="idVaiTro" class="form-control" required>
                                    <c:forEach items="${listVaiTro}" var="vt">
                                        <option value="${vt.idVaiTro}">
                                            ${vt.tenVaiTro} (${vt.maVaiTro})
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="form-group">
                                <label>Ngày vào làm</label>
                                <input type="date" name="ngayVaoLam" class="form-control">
                            </div>

                            <div class="form-group">
                                <label>Lương cơ bản (VNĐ) <span style="color:red">*</span></label>
                                <input type="number" name="luongCoBan" class="form-control" placeholder="12000000" min="0" step="100000" required>
                            </div>

                            <div class="form-group">
                                <label>Phụ cấp (VNĐ)</label>
                                <input type="number" name="phuCap" class="form-control" placeholder="1500000" min="0" step="50000" value="0">
                            </div>

                            <div class="form-group">
                                <label>Khấu trừ (VNĐ)</label>
                                <input type="number" name="khauTru" class="form-control" placeholder="200000" min="0" step="50000" value="0">
                            </div>

                            <div class="form-group">
                                <label>Trạng thái</label>
                                <select name="trangThai" class="form-control">
                                    <option value="Hoạt động">Đang hoạt động</option>
                                    <option value="Bị khóa">Tạm khóa</option>
                                </select>
                            </div>

                            <div class="form-group">
                                <label>Email tài khoản đăng nhập</label>
                                <input type="email" name="email" class="form-control" placeholder="Tự sinh nếu để trống (vd: nv011@theroyal.com)">
                            </div>

                            <div class="form-group">
                                <label>Mật khẩu khởi tạo</label>
                                <input type="password" name="matKhau" class="form-control" placeholder="Mặc định: 123456" value="123456">
                            </div>
                        </div>

                        <div style="display: flex; justify-content: flex-end; gap: 10px; margin-top: 10px;">
                            <button type="reset" class="btn btn-ghost"><i class='bx bx-reset'></i> Nhập lại</button>
                            <button type="submit" class="btn btn-primary"><i class='bx bx-check'></i> Lưu Hồ Sơ Nhân Viên</button>
                        </div>
                    </form>
                </div>

                <!-- CARD 2: BẢNG DANH SÁCH NHÂN VIÊN -->
                <div class="card" style="padding: 0; overflow: hidden;">
                    <div style="padding: 18px 20px; display: flex; justify-content: space-between; align-items: center; border-bottom: 1px solid #eef2ef;">
                        <div>
                            <h3 style="font-size: 16px; font-weight: 700; color: #222831; margin-bottom: 4px;">Danh Sách Nhân Sự</h3>
                            <small style="color: #6b7a70;">Tổng cộng ${totalStaff} nhân viên</small>
                        </div>
                    </div>

                    <div style="overflow-x: auto;">
                        <table class="table" style="width: 100%; border-collapse: collapse;">
                            <thead>
                            <tr style="background: #f8faf9; border-bottom: 1px solid #e5ebe7; text-align: left;">
                                <th style="padding: 12px 16px; font-size: 12px; font-weight: 700; color: #49584e;">STT</th>
                                <th style="padding: 12px 16px; font-size: 12px; font-weight: 700; color: #49584e;">MÃ NV</th>
                                <th style="padding: 12px 16px; font-size: 12px; font-weight: 700; color: #49584e;">HỌ VÀ TÊN</th>
                                <th style="padding: 12px 16px; font-size: 12px; font-weight: 700; color: #49584e;">ĐIỆN THOẠI</th>
                                <th style="padding: 12px 16px; font-size: 12px; font-weight: 700; color: #49584e;">VAI TRÒ / CHỨC DANH</th>
                                <th style="padding: 12px 16px; font-size: 12px; font-weight: 700; color: #49584e;">NGÀY VÀO LÀM</th>
                                <th style="padding: 12px 16px; font-size: 12px; font-weight: 700; color: #49584e;">LƯƠNG CƠ BẢN</th>
                                <th style="padding: 12px 16px; font-size: 12px; font-weight: 700; color: #49584e;">THỰC NHẬN</th>
                                <th style="padding: 12px 16px; font-size: 12px; font-weight: 700; color: #49584e;">TRẠNG THÁI</th>
                                <th style="padding: 12px 16px; font-size: 12px; font-weight: 700; color: #49584e; text-align: center;">THAO TÁC</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${listNhanVien}" var="nv" varStatus="loop">
                                <c:set var="vt" value="${mapVaiTro[nv.idVaiTro]}" />
                                <c:set var="thucNhan" value="${nv.luongCoBan + nv.phuCap - nv.khauTru}" />
                                <tr style="border-bottom: 1px solid #f0f4f2; transition: background 0.15s;">
                                    <td style="padding: 12px 16px; font-size: 13px; color: #6b7a70;">
                                        ${(currentPage - 1) * pageSize + loop.index + 1}
                                    </td>
                                    <td style="padding: 12px 16px; font-size: 13px; font-weight: 700; color: #222831;">
                                        ${nv.maNhanVien}
                                    </td>
                                    <td style="padding: 12px 16px; font-size: 13px; font-weight: 600; color: #2b3a33;">
                                        <div style="display: flex; align-items: center; gap: 8px;">
                                            <i class='bx bx-user-circle' style="color: #557c56; font-size: 18px;"></i>
                                            ${nv.hoTen}
                                        </div>
                                    </td>
                                    <td style="padding: 12px 16px; font-size: 13px; color: #555f58;">
                                        ${nv.soDienThoai}
                                    </td>
                                    <td style="padding: 12px 16px; font-size: 13px;">
                                        <span class="role-pill">
                                            <i class='bx bx-badge-check' style="color: #557c56;"></i>
                                            ${vt != null ? vt.tenVaiTro : 'Chưa phân quyền'}
                                        </span>
                                    </td>
                                    <td style="padding: 12px 16px; font-size: 13px; color: #6b7a70;">
                                        <fmt:formatDate value="${nv.ngayVaoLam}" pattern="dd/MM/yyyy"/>
                                    </td>
                                    <td style="padding: 12px 16px; font-size: 13px; color: #3d4a41;">
                                        <fmt:formatNumber value="${nv.luongCoBan}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                                    </td>
                                    <td style="padding: 12px 16px; font-size: 13px; font-weight: 700; color: #b78103;">
                                        <fmt:formatNumber value="${thucNhan}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                                    </td>
                                    <td style="padding: 12px 16px; font-size: 12px;">
                                        <c:choose>
                                            <c:when test="${nv.trangThai == 'HOAT_DONG'}">
                                                <span class="badge badge-active">Hoạt động</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge badge-locked">Bị khóa</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td style="padding: 12px 16px; text-align: center;">
                                        <div class="action-btns" style="justify-content: center;">
                                            <a href="${pageContext.request.contextPath}/quan-ly-nhan-vien/detail?id=${nv.idNhanVien}" class="action-btn btn-view-detail" title="Xem hồ sơ chi tiết">
                                                <i class='bx bx-show'></i>
                                            </a>
                                            <a href="${pageContext.request.contextPath}/quan-ly-nhan-vien/view-update?id=${nv.idNhanVien}" class="action-btn btn-view-edit" title="Cập nhật hồ sơ">
                                                <i class='bx bx-edit-alt'></i>
                                            </a>
                                            <a href="${pageContext.request.contextPath}/quan-ly-nhan-vien/delete?id=${nv.idNhanVien}" class="action-btn btn-view-del" title="Xóa nhân viên" onclick="return confirm('Bạn có chắc chắn muốn xóa nhân viên [${nv.hoTen}] không?')">
                                                <i class='bx bx-trash'></i>
                                            </a>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>

                    <!-- PHÂN TRANG CHUẨN 10 DÒNG / TRANG -->
                    <div style="display: flex; justify-content: space-between; align-items: center; padding: 14px 20px; border-top: 1px solid #eef2ef; background: #ffffff; flex-wrap: wrap; gap: 10px;">
                        <span style="font-size: 12px; color: #6b7a70;">
                            Đang xem trang <b>${currentPage}</b> / <b>${totalPages}</b> (10 nhân viên/trang)
                        </span>
                        <div style="display: flex; gap: 6px;">
                            <c:if test="${currentPage > 1}">
                                <a href="${pageContext.request.contextPath}/quan-ly-nhan-vien/view?page=${currentPage - 1}" class="page-link-btn">
                                    <i class='bx bx-chevron-left'></i> Trước
                                </a>
                            </c:if>

                            <c:forEach begin="1" end="${totalPages}" var="i">
                                <a href="${pageContext.request.contextPath}/quan-ly-nhan-vien/view?page=${i}" class="page-link-btn ${i == currentPage ? 'active' : ''}">
                                    ${i}
                                </a>
                            </c:forEach>

                            <c:if test="${currentPage < totalPages}">
                                <a href="${pageContext.request.contextPath}/quan-ly-nhan-vien/view?page=${currentPage + 1}" class="page-link-btn">
                                    Sau <i class='bx bx-chevron-right'></i>
                                </a>
                            </c:if>
                        </div>
                    </div>
                </div>
            </section>

            <!-- THANH FOOTER CỐ ĐỊNH (TÁCH RIÊNG DÙNG CHUNG) -->
            <jsp:include page="/layout/footer.jsp" />
        </main>
    </div>
</body>
</html>
