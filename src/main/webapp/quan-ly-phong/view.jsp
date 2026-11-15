<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản Lý Sơ Đồ Phòng — The Royal Hotel</title>

    <!-- Tập tin CSS chính của The Royal Hotel -->
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
            grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
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
        .stat-icon.ready { background: #e8f5e9; color: #2e7d32; }
        .stat-icon.occupied { background: #e3f2fd; color: #1565c0; }
        .stat-icon.cleaning { background: #fff8e1; color: #f57f17; }
        .stat-icon.maintain { background: #ffebee; color: #c62828; }
        .stat-info b { font-size: 20px; color: #222831; display: block; font-weight: 700; }
        .stat-info span { font-size: 12px; color: #6b7a70; font-weight: 500; }
        
        .badge-ready { background: #e8f5e9; color: #2e7d32; border: 1px solid #c8e6c9; }
        .badge-occupied { background: #e3f2fd; color: #1565c0; border: 1px solid #bbdefb; }
        .badge-cleaning { background: #fff8e1; color: #f57f17; border: 1px solid #ffe082; }
        .badge-maintain { background: #ffebee; color: #c62828; border: 1px solid #ffcdd2; }
        
        .view-pill {
            display: inline-flex;
            align-items: center;
            gap: 5px;
            padding: 3px 8px;
            border-radius: 6px;
            font-size: 11px;
            font-weight: 600;
        }
        .view-ocean { background: #e0f7fa; color: #006064; }
        .view-city { background: #ede7f6; color: #4527a0; }
        .view-mountain { background: #f1f8e9; color: #33691e; }
        
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
        
        .pagination-container {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 16px 20px;
            border-top: 1px solid #eef2ef;
        }
        .pagination-links {
            display: flex;
            gap: 6px;
        }
        .page-link {
            padding: 6px 12px;
            border-radius: 6px;
            border: 1px solid #d4ddd7;
            background: #fff;
            color: #2b3a33;
            text-decoration: none;
            font-size: 13px;
            font-weight: 600;
            transition: all 0.2s;
        }
        .page-link.active {
            background: #557c56;
            color: #fff;
            border-color: #557c56;
        }
        .page-link:hover:not(.active) {
            background: #f0f4f2;
        }
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
            <jsp:param name="activeMenu" value="phong" />
        </jsp:include>

        <!-- 2. KHU VỰC CHÍNH -->
        <main class="main-area">
            <!-- THANH HEADER CỐ ĐỊNH (TÁCH RIÊNG DÙNG CHUNG) -->
            <jsp:include page="/layout/header.jsp">
                <jsp:param name="pageTitle" value="Quản Lý Sơ Đồ Phòng &amp; Tiện Nghi" />
            </jsp:include>

            <!-- Nội dung chính -->
            <section class="content" style="padding: 28px;">
                <div class="page-heading">
                    <div>
                        <div class="eyebrow" style="font-size: 11px; font-weight: 700; color: #557c56; letter-spacing: 2px;">THE ROYAL HOTEL · 2 TÒA (CLARENCE & WINDSOR) · 300 PHÒNG</div>
                        <h1 style="font-size: 24px; font-weight: 800; color: #222831; margin: 4px 0;">Quản lý sơ đồ phòng &amp; Tiện nghi</h1>
                        <p style="color: #6b7a70; font-size: 13px; margin: 0;">Theo dõi tình trạng phòng thực tế, thiết lập phân loại view và thực hiện CRUD phòng nhanh chóng.</p>
                    </div>
                </div>

                <!-- Thông báo -->
                <c:if test="${param.success == 'add'}">
                    <div class="alert-msg alert-success"><i class='bx bx-check-circle'></i> Thêm phòng mới thành công!</div>
                </c:if>
                <c:if test="${param.success == 'update'}">
                    <div class="alert-msg alert-success"><i class='bx bx-check-circle'></i> Cập nhật thông tin phòng thành công!</div>
                </c:if>
                <c:if test="${param.error == 'duplicate_maphong'}">
                    <div class="alert-msg alert-danger"><i class='bx bx-error-circle'></i> Mã phòng đã tồn tại trên hệ thống. Vui lòng chọn mã khác!</div>
                </c:if>

                <!-- 4 Thẻ thống kê nhanh -->
                <div class="stats-banner">
                    <div class="stat-card">
                        <div class="stat-icon total"><i class='bx bx-hotel'></i></div>
                        <div class="stat-info">
                            <b>${totalCount}</b>
                            <span>Tổng số phòng</span>
                        </div>
                    </div>
                    <div class="stat-card">
                        <div class="stat-icon ready"><i class='bx bx-check'></i></div>
                        <div class="stat-info">
                            <b>Sẵn sàng</b>
                            <span>Phòng trống đón khách</span>
                        </div>
                    </div>
                    <div class="stat-card">
                        <div class="stat-icon occupied"><i class='bx bx-user-check'></i></div>
                        <div class="stat-info">
                            <b>Đang ở</b>
                            <span>Khách đang lưu trú</span>
                        </div>
                    </div>
                    <div class="stat-card">
                        <div class="stat-icon cleaning"><i class='bx bx-brush'></i></div>
                        <div class="stat-info">
                            <b>Cần dọn</b>
                            <span>Chờ buồng phòng xử lý</span>
                        </div>
                    </div>
                </div>

                <!-- CARD 1: FORM THÊM PHÒNG MỚI -->
                <div class="card" style="margin-bottom: 24px;">
                    <div class="card-header" style="border-bottom: 1px solid #eef2ef; padding-bottom: 14px; margin-bottom: 18px;">
                        <h3 style="font-size: 16px; font-weight: 700; color: #222831; display: flex; align-items: center; gap: 8px;">
                            <i class='bx bx-plus-circle' style="color: #557c56; font-size: 20px;"></i> Thêm Phòng Mới Vào Hệ Thống
                        </h3>
                    </div>

                    <form action="${pageContext.request.contextPath}/quan-ly-phong/add" method="post">
                        <div class="form-grid">
                            <div class="form-group">
                                <label>Mã phòng <span style="color:red">*</span></label>
                                <input type="text" name="maPhong" class="form-control" placeholder="VD: CL-0101, WN-0205..." required>
                            </div>

                            <div class="form-group">
                                <label>Tòa nhà & Tầng <span style="color:red">*</span></label>
                                <select name="idTang" class="form-control" required>
                                    <c:forEach items="${listTang}" var="tang">
                                        <c:set var="tNha" value="${mapToaNha[tang.idToaNha]}" />
                                        <option value="${tang.idTang}">
                                            Tòa ${tNha != null ? tNha.ten : 'Chung'} — Tầng ${tang.soTang}
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="form-group">
                                <label>Hạng & Loại phòng <span style="color:red">*</span></label>
                                <select name="idLoaiPhong" class="form-control" required>
                                    <c:forEach items="${listLoaiPhong}" var="lp">
                                        <option value="${lp.idLoaiPhong}">
                                            ${lp.ten} (<fmt:formatNumber value="${lp.giaMoiDem}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>)
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="form-group">
                                <label>Tầm nhìn (View) <span style="color:red">*</span></label>
                                <select name="ghiChu" class="form-control" required>
                                    <option value="Ocean View">🌊 Ocean View (Hướng biển)</option>
                                    <option value="City View">🏙️ City View (Hướng thành phố)</option>
                                    <option value="Mountain View">⛰️ Mountain View (Hướng núi)</option>
                                </select>
                            </div>

                            <div class="form-group">
                                <label>Trạng thái ban đầu</label>
                                <select name="trangThai" class="form-control">
                                    <option value="SAN_SANG">Sẵn sàng đón khách</option>
                                    <option value="DANG_O">Đang ở</option>
                                    <option value="CAN_DON">Cần dọn</option>
                                    <option value="BAO_TRI">Bảo trì</option>
                                </select>
                            </div>

                            <div class="form-group">
                                <label>Đường dẫn ảnh phòng</label>
                                <input type="text" name="hinhAnhPhong" class="form-control" value="assets/rooms/default.jpg" placeholder="assets/rooms/...">
                            </div>
                        </div>

                        <div style="display: flex; justify-content: flex-end; gap: 10px; margin-top: 10px;">
                            <button type="reset" class="btn btn-ghost"><i class='bx bx-reset'></i> Nhập lại</button>
                            <button type="submit" class="btn btn-primary"><i class='bx bx-check'></i> Lưu Phòng Mới</button>
                        </div>
                    </form>
                </div>

                <!-- CARD 2: BẢNG DANH SÁCH PHÒNG -->
                <div class="card" style="padding: 0; overflow: hidden;">
                    <div style="padding: 18px 20px; display: flex; justify-content: space-between; align-items: center; border-bottom: 1px solid #eef2ef;">
                        <div>
                            <h3 style="font-size: 16px; font-weight: 700; color: #222831; margin-bottom: 4px;">Danh Sách Phòng</h3>
                            <small style="color: #6b7a70;">Hiển thị trang ${currentPage} / ${totalPages} (Tổng cộng ${totalCount} phòng)</small>
                        </div>
                    </div>

                    <div style="overflow-x: auto;">
                        <table class="table" style="width: 100%; border-collapse: collapse;">
                            <thead>
                            <tr style="background: #f8faf9; border-bottom: 1px solid #e5ebe7; text-align: left;">
                                <th style="padding: 12px 16px; font-size: 12px; font-weight: 700; color: #49584e;">STT</th>
                                <th style="padding: 12px 16px; font-size: 12px; font-weight: 700; color: #49584e;">MÃ PHÒNG</th>
                                <th style="padding: 12px 16px; font-size: 12px; font-weight: 700; color: #49584e;">VỊ TRÍ (TÒA - TẦNG)</th>
                                <th style="padding: 12px 16px; font-size: 12px; font-weight: 700; color: #49584e;">LOẠI PHÒNG</th>
                                <th style="padding: 12px 16px; font-size: 12px; font-weight: 700; color: #49584e;">GIÁ / ĐÊM</th>
                                <th style="padding: 12px 16px; font-size: 12px; font-weight: 700; color: #49584e;">TẦM NHÌN (VIEW)</th>
                                <th style="padding: 12px 16px; font-size: 12px; font-weight: 700; color: #49584e;">TRẠNG THÁI</th>
                                <th style="padding: 12px 16px; font-size: 12px; font-weight: 700; color: #49584e; text-align: center;">THAO TÁC</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${listPhong}" var="p" varStatus="loop">
                                <c:set var="t" value="${mapTang[p.idTang]}" />
                                <c:set var="tn" value="${t != null ? mapToaNha[t.idToaNha] : null}" />
                                <c:set var="lp" value="${mapLoaiPhong[p.idLoaiPhong]}" />
                                <tr style="border-bottom: 1px solid #f0f4f2; transition: background 0.15s;">
                                    <td style="padding: 12px 16px; font-size: 13px; color: #6b7a70;">
                                        ${(currentPage - 1) * 10 + loop.index + 1}
                                    </td>
                                    <td style="padding: 12px 16px; font-size: 13px; font-weight: 700; color: #222831;">
                                        ${p.maPhong}
                                    </td>
                                    <td style="padding: 12px 16px; font-size: 13px; color: #3d4a41;">
                                        <i class='bx bx-buildings' style="color: #557c56;"></i>
                                        Tòa ${tn != null ? tn.ten : 'Chưa gán'} — Tầng ${t != null ? t.soTang : 'N/A'}
                                    </td>
                                    <td style="padding: 12px 16px; font-size: 13px; font-weight: 600; color: #2b3a33;">
                                        ${lp != null ? lp.ten : 'Loại phòng ' += p.idLoaiPhong}
                                    </td>
                                    <td style="padding: 12px 16px; font-size: 13px; font-weight: 700; color: #b78103;">
                                        <fmt:formatNumber value="${lp != null ? lp.giaMoiDem : 0}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                                    </td>
                                    <td style="padding: 12px 16px; font-size: 13px;">
                                        <c:choose>
                                            <c:when test="${p.ghiChu == 'Ocean View'}">
                                                <span class="view-pill view-ocean"><i class='bx bx-water'></i> Ocean View</span>
                                            </c:when>
                                            <c:when test="${p.ghiChu == 'City View'}">
                                                <span class="view-pill view-city"><i class='bx bx-buildings'></i> City View</span>
                                            </c:when>
                                            <c:when test="${p.ghiChu == 'Mountain View'}">
                                                <span class="view-pill view-mountain"><i class='bx bx-landscape'></i> Mountain View</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="view-pill" style="background:#eee; color:#555;">${p.ghiChu}</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td style="padding: 12px 16px; font-size: 12px;">
                                        <c:choose>
                                            <c:when test="${p.trangThai == 'SAN_SANG'}">
                                                <span class="badge badge-ready">Sẵn sàng</span>
                                            </c:when>
                                            <c:when test="${p.trangThai == 'DANG_O'}">
                                                <span class="badge badge-occupied">Đang ở</span>
                                            </c:when>
                                            <c:when test="${p.trangThai == 'CAN_DON'}">
                                                <span class="badge badge-cleaning">Cần dọn</span>
                                            </c:when>
                                            <c:when test="${p.trangThai == 'BAO_TRI'}">
                                                <span class="badge badge-maintain">Bảo trì</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge">${p.trangThai}</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td style="padding: 12px 16px; text-align: center;">
                                        <div class="action-btns" style="justify-content: center;">
                                            <a href="${pageContext.request.contextPath}/quan-ly-phong/detail?id=${p.idPhong}" class="action-btn btn-view-detail" title="Xem chi tiết & tiện nghi">
                                                <i class='bx bx-show'></i>
                                            </a>
                                            <a href="${pageContext.request.contextPath}/quan-ly-phong/view-update?id=${p.idPhong}" class="action-btn btn-view-edit" title="Cập nhật phòng">
                                                <i class='bx bx-edit-alt'></i>
                                            </a>
                                            <a href="${pageContext.request.contextPath}/quan-ly-phong/delete?id=${p.idPhong}" class="action-btn btn-view-del" title="Xóa phòng" onclick="return confirm('Bạn có chắc chắn muốn xóa phòng [${p.maPhong}] không?')">
                                                <i class='bx bx-trash'></i>
                                            </a>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>

                    <!-- PHÂN TRANG -->
                    <div class="pagination-container">
                        <span style="font-size: 13px; color: #6b7a70;">
                            Đang xem 10 dòng trên trang ${currentPage}
                        </span>
                        <div class="pagination-links">
                            <c:if test="${currentPage > 1}">
                                <a href="${pageContext.request.contextPath}/quan-ly-phong/view?page=${currentPage - 1}" class="page-link">
                                    <i class='bx bx-chevron-left'></i> Trước
                                </a>
                            </c:if>

                            <c:set var="startPage" value="${currentPage - 2 > 1 ? currentPage - 2 : 1}" />
                            <c:set var="endPage" value="${startPage + 4 < totalPages ? startPage + 4 : totalPages}" />

                            <c:forEach begin="${startPage}" end="${endPage}" var="i">
                                <a href="${pageContext.request.contextPath}/quan-ly-phong/view?page=${i}" class="page-link ${i == currentPage ? 'active' : ''}">
                                    ${i}
                                </a>
                            </c:forEach>

                            <c:if test="${currentPage < totalPages}">
                                <a href="${pageContext.request.contextPath}/quan-ly-phong/view?page=${currentPage + 1}" class="page-link">
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
