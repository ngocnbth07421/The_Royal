<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Trang Chủ Quản Trị — The Royal Hotel</title>

    <!-- CSS chính chuẩn mực của The Royal Hotel -->
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
            font-family: 'DM Sans', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
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

        /* Thẻ thống kê KPI Dashboard */
        .stats-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
            gap: 16px;
            margin-bottom: 24px;
        }

        .stat-card {
            background: #fff;
            padding: 18px 20px;
            border-radius: 12px;
            border: 1px solid #e5ebe7;
            box-shadow: 0 2px 8px rgba(0,0,0,0.02);
            display: flex;
            align-items: center;
            gap: 16px;
        }

        .stat-icon {
            width: 48px;
            height: 48px;
            border-radius: 10px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 24px;
            flex-shrink: 0;
        }
        .icon-total { background: #eaf1ed; color: #557c56; }
        .icon-ready { background: #e8f5e9; color: #2e7d32; }
        .icon-occupied { background: #e3f2fd; color: #1565c0; }
        .icon-cleaning { background: #fff8e1; color: #f57f17; }
        .icon-maintain { background: #ffebee; color: #c62828; }

        .stat-val { font-size: 22px; font-weight: 800; color: #222831; margin-bottom: 2px; }
        .stat-name { font-size: 12px; font-weight: 600; color: #6b7a70; }

        /* Bộ lọc & Tìm kiếm */
        .filter-card {
            background: #fff;
            padding: 20px;
            border-radius: 12px;
            border: 1px solid #e5ebe7;
            margin-bottom: 24px;
            box-sizing: border-box;
        }

        .filter-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
            gap: 12px;
            align-items: flex-end;
        }

        .filter-group label {
            display: block;
            font-size: 12px;
            font-weight: 700;
            color: #2b3a33;
            margin-bottom: 6px;
        }

        .form-select, .form-input-sm {
            width: 100%;
            padding: 9px 12px;
            border: 1px solid #d4ddd7;
            border-radius: 8px;
            font-size: 13px;
            color: #222831;
            box-sizing: border-box;
            background: #fafcfb;
            outline: none;
        }

        .form-select:focus, .form-input-sm:focus {
            border-color: #557c56;
            background: #fff;
        }

        /* Chuyển đổi tòa nhà Tabs */
        .building-tabs {
            display: flex;
            gap: 8px;
            background: #eef2ef;
            padding: 4px;
            border-radius: 10px;
            margin-bottom: 16px;
            width: fit-content;
        }

        .b-tab {
            padding: 8px 18px;
            border-radius: 8px;
            font-size: 13px;
            font-weight: 700;
            color: #49584e;
            text-decoration: none;
            transition: all 0.2s ease;
            display: flex;
            align-items: center;
            gap: 6px;
        }

        .b-tab.active {
            background: #557c56;
            color: #ffffff;
            box-shadow: 0 2px 6px rgba(85, 124, 86, 0.3);
        }

        /* Ma trận sơ đồ phòng */
        .matrix-container {
            background: #fff;
            border-radius: 14px;
            border: 1px solid #e5ebe7;
            padding: 24px;
            margin-bottom: 28px;
            box-sizing: border-box;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.02);
        }

        .matrix-legend {
            display: flex;
            gap: 20px;
            margin-bottom: 20px;
            flex-wrap: wrap;
            font-size: 12.5px;
            color: #49584e;
            font-weight: 600;
            padding-bottom: 14px;
            border-bottom: 1px solid #f0f4f2;
        }

        .legend-item { display: flex; align-items: center; gap: 7px; }
        .legend-dot { width: 12px; height: 12px; border-radius: 3px; display: inline-block; flex-shrink: 0; }
        .dot-ready { background: #2e7d32; }
        .dot-occupied { background: #1565c0; }
        .dot-cleaning { background: #f57f17; }
        .dot-maintain { background: #c62828; }

        /* Quick Floor Filter Tabs */
        .floor-tabs-bar {
            display: flex;
            gap: 8px;
            overflow-x: auto;
            padding-bottom: 14px;
            margin-bottom: 18px;
            border-bottom: 1px dashed #e5ebe7;
        }

        .floor-tab-btn {
            padding: 7px 15px;
            border-radius: 8px;
            font-size: 12px;
            font-weight: 700;
            background: #f4f7f5;
            color: #49584e;
            border: 1px solid #dce5df;
            cursor: pointer;
            white-space: nowrap;
            transition: all 0.2s ease;
        }

        .floor-tab-btn:hover {
            background: #eaf1ed;
            color: #2b3a33;
            border-color: #557c56;
        }

        .floor-tab-btn.active {
            background: #557c56;
            color: #ffffff;
            border-color: #557c56;
            box-shadow: 0 2px 6px rgba(85, 124, 86, 0.3);
        }

        .floor-row {
            display: flex;
            align-items: stretch;
            gap: 16px;
            padding: 12px 0;
            border-bottom: 1px solid #f0f4f2;
            transition: all 0.2s ease;
        }

        .floor-row:last-child {
            border-bottom: none;
        }

        .floor-label {
            width: 88px;
            font-size: 12.5px;
            font-weight: 800;
            color: #222831;
            background: #f4f7f5;
            padding: 12px 8px;
            border-radius: 10px;
            text-align: center;
            flex-shrink: 0;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            gap: 4px;
            border: 1px solid #e2ece5;
        }

        .floor-label i {
            font-size: 18px;
            color: #557c56;
        }

        .floor-rooms {
            display: grid;
            grid-template-columns: repeat(5, 1fr);
            gap: 12px;
            flex: 1;
            width: 100%;
            box-sizing: border-box;
        }

        .room-box {
            padding: 12px 14px;
            border-radius: 10px;
            border: 1px solid #dce5df;
            background: #ffffff;
            cursor: pointer;
            transition: all 0.25s cubic-bezier(0.16, 1, 0.3, 1);
            position: relative;
            display: flex;
            flex-direction: column;
            justify-content: space-between;
            min-height: 86px;
            box-sizing: border-box;
        }

        .room-box:hover {
            transform: translateY(-3px);
            box-shadow: 0 6px 18px rgba(0,0,0,0.08);
            border-color: #557c56;
        }

        .room-box.st-ready {
            border-left: 4px solid #2e7d32;
            background: #ffffff;
        }
        .room-box.st-ready:hover {
            background: #f9fdfa;
        }

        .room-box.st-occupied {
            border-left: 4px solid #1565c0;
            background: #f8fbff;
        }
        .room-box.st-occupied:hover {
            background: #f0f6ff;
        }

        .room-box.st-cleaning {
            border-left: 4px solid #f57f17;
            background: #fffdf7;
        }
        .room-box.st-cleaning:hover {
            background: #fff8e8;
        }

        .room-box.st-maintain {
            border-left: 4px solid #c62828;
            background: #fff9f9;
        }
        .room-box.st-maintain:hover {
            background: #fff0f0;
        }

        .r-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 6px;
        }

        .r-code {
            font-size: 14px;
            font-weight: 800;
            color: #222831;
            letter-spacing: 0.5px;
        }

        .r-badge {
            font-size: 10px;
            font-weight: 700;
            padding: 2px 7px;
            border-radius: 10px;
            display: inline-flex;
            align-items: center;
            gap: 3px;
        }
        .badge-ready { background: #e8f5e9; color: #2e7d32; }
        .badge-occupied { background: #e3f2fd; color: #1565c0; }
        .badge-cleaning { background: #fff8e1; color: #f57f17; }
        .badge-maintain { background: #ffebee; color: #c62828; }

        .r-type {
            font-size: 11.5px;
            color: #49584e;
            font-weight: 500;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
            margin-bottom: 6px;
        }

        .r-footer {
            display: flex;
            justify-content: space-between;
            align-items: center;
            font-size: 11px;
            padding-top: 6px;
            border-top: 1px dashed rgba(0,0,0,0.06);
        }

        .r-view {
            font-weight: 600;
            color: #006064;
            display: inline-flex;
            align-items: center;
            gap: 3px;
        }

        .r-price {
            font-weight: 800;
            color: #b78103;
        }

        /* ================================================================= */
        /* RIGHT SIDE PANEL (DRAWER CHI TIẾT PHÒNG BÊN PHẢI MÀN HÌNH)        */
        /* ================================================================= */
        .drawer-backdrop {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0, 0, 0, 0.4);
            z-index: 1040;
            transition: opacity 0.3s ease;
        }

        .drawer-backdrop.open {
            display: block;
        }

        .right-drawer {
            position: fixed;
            top: 0;
            right: 0;
            bottom: 0;
            width: 440px;
            max-width: 90vw;
            height: 100vh;
            background: #ffffff;
            box-shadow: -8px 0 35px rgba(0, 0, 0, 0.18);
            z-index: 1050;
            transform: translateX(100%);
            transition: transform 0.3s cubic-bezier(0.16, 1, 0.3, 1);
            display: flex;
            flex-direction: column;
            box-sizing: border-box;
            overflow: hidden;
        }

        .right-drawer.open {
            transform: translateX(0);
        }

        .drawer-header {
            padding: 20px 24px;
            background: #222831;
            color: #ffffff;
            display: flex;
            justify-content: space-between;
            align-items: center;
            border-bottom: 2px solid #557c56;
        }

        .drawer-title {
            font-size: 16px;
            font-weight: 800;
            letter-spacing: 0.5px;
            margin: 0;
            display: flex;
            align-items: center;
            gap: 8px;
        }

        .drawer-close {
            background: none;
            border: none;
            color: #aeb8b3;
            font-size: 24px;
            cursor: pointer;
            padding: 4px;
            display: flex;
            align-items: center;
            justify-content: center;
            transition: color 0.2s;
        }

        .drawer-close:hover {
            color: #ffffff;
        }

        .drawer-body {
            padding: 24px;
            overflow-y: auto;
            flex: 1;
            display: flex;
            flex-direction: column;
            gap: 18px;
        }

        .drawer-card {
            background: #f8faf9;
            border: 1px solid #e2ebe5;
            border-radius: 10px;
            padding: 16px;
        }

        .drawer-info-row {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 6px 0;
            font-size: 13px;
            border-bottom: 1px solid #edf2ef;
        }

        .drawer-info-row:last-child {
            border-bottom: none;
        }

        .drawer-info-label {
            color: #6b7a70;
            font-weight: 500;
        }

        .drawer-info-val {
            color: #222831;
            font-weight: 700;
        }

        .amenity-pill-list {
            display: flex;
            flex-wrap: wrap;
            gap: 6px;
            margin-top: 8px;
        }

        .amenity-pill-item {
            background: #ffffff;
            border: 1px solid #d4ddd7;
            padding: 4px 10px;
            border-radius: 16px;
            font-size: 11px;
            font-weight: 600;
            color: #3d4a41;
            display: inline-flex;
            align-items: center;
            gap: 4px;
        }

        /* Phân trang chuẩn */
        .pagination-container {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 14px 20px;
            border-top: 1px solid #eef2ef;
            background: #ffffff;
            flex-wrap: wrap;
            gap: 10px;
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
    </style>
</head>

<body>
    <div class="app-shell">
        <!-- 1. THANH MENU BÊN TRÁI CHUẨN GIAO DIỆN MẪU -->
        <jsp:include page="/layout/sidebar.jsp">
            <jsp:param name="activeMenu" value="dashboard" />
        </jsp:include>

        <!-- 2. KHU VỰC CHÍNH (MAIN AREA) -->
        <main class="main-area">
            <!-- THANH HEADER CỐ ĐỊNH -->
            <jsp:include page="/layout/header.jsp">
                <jsp:param name="pageTitle" value="Tổng Quan Dashboard &amp; Sơ Đồ Đặt Phòng" />
            </jsp:include>

            <!-- Nội dung chính Dashboard -->
            <section class="content">
                <div class="page-heading" style="margin-bottom: 22px;">
                    <div>
                        <div class="eyebrow" style="font-size: 11px; font-weight: 700; color: #557c56; letter-spacing: 2px;">THE ROYAL HOTEL · 2 TÒA (CLARENCE &amp; WINDSOR)</div>
                        <h1 style="font-size: 24px; font-weight: 800; color: #222831; margin: 4px 0;">Bảng Điều Khiển &amp; Sơ Đồ Đặt Phòng Trực Tuyến</h1>
                        <p style="color: #6b7a70; font-size: 13px; margin: 0;">Sơ đồ trực quan 300 phòng 2 tòa nhà. Bấm vào bất kỳ phòng nào để xem chi tiết bên phải và thao tác nhanh chóng.</p>
                    </div>
                </div>

                <!-- Thông báo thành công nếu có -->
                <c:if test="${param.success == 'booking_success'}">
                    <div style="background: #e8f5e9; color: #2e7d32; padding: 12px 18px; border-radius: 8px; margin-bottom: 20px; display: flex; align-items: center; gap: 8px; border: 1px solid #c8e6c9;">
                        <i class='bx bx-check-circle' style="font-size: 19px;"></i> Đặt phòng thành công! Trạng thái phòng đã chuyển sang Đang Ở.
                    </div>
                </c:if>
                <c:if test="${param.success == 'status_updated'}">
                    <div style="background: #e8f5e9; color: #2e7d32; padding: 12px 18px; border-radius: 8px; margin-bottom: 20px; display: flex; align-items: center; gap: 8px; border: 1px solid #c8e6c9;">
                        <i class='bx bx-check-circle' style="font-size: 19px;"></i> Cập nhật trạng thái phòng thành công!
                    </div>
                </c:if>

                <!-- 1. DASHBOARD KPI CARDS -->
                <div class="stats-grid">
                    <div class="stat-card">
                        <div class="stat-icon icon-total"><i class='bx bx-buildings'></i></div>
                        <div>
                            <div class="stat-val">${totalRooms}</div>
                            <div class="stat-name">Tổng Số Phòng (2 Tòa)</div>
                        </div>
                    </div>

                    <div class="stat-card">
                        <div class="stat-icon icon-ready"><i class='bx bx-check-circle'></i></div>
                        <div>
                            <div class="stat-val" style="color: #2e7d32;">${countReady}</div>
                            <div class="stat-name">Phòng Sẵn Sàng (Trống)</div>
                        </div>
                    </div>

                    <div class="stat-card">
                        <div class="stat-icon icon-occupied"><i class='bx bx-user-check'></i></div>
                        <div>
                            <div class="stat-val" style="color: #1565c0;">${countOccupied}</div>
                            <div class="stat-name">Khách Đang Lưu Trú</div>
                        </div>
                    </div>

                    <div class="stat-card">
                        <div class="stat-icon icon-cleaning"><i class='bx bx-brush'></i></div>
                        <div>
                            <div class="stat-val" style="color: #f57f17;">${countCleaning}</div>
                            <div class="stat-name">Phòng Chờ Dọn Dẹp</div>
                        </div>
                    </div>

                    <div class="stat-card">
                        <div class="stat-icon icon-maintain"><i class='bx bx-wrench'></i></div>
                        <div>
                            <div class="stat-val" style="color: #c62828;">${countMaintain}</div>
                            <div class="stat-name">Phòng Đang Bảo Trì</div>
                        </div>
                    </div>
                </div>

                <!-- 2. BỘ LỌC, TÌM KIẾM & CHECK TRẠNG THÁI THEO THỜI GIAN -->
                <div class="filter-card">
                    <div style="margin-bottom: 14px; font-size: 15px; font-weight: 700; color: #222831; display: flex; align-items: center; gap: 8px;">
                        <i class='bx bx-filter-alt' style="color: #557c56;"></i> Bộ Lọc &amp; Tra Cứu Trạng Thái Phòng Theo Thời Gian
                    </div>

                    <form action="${pageContext.request.contextPath}/trang-chu" method="get" id="filterForm">
                        <input type="hidden" name="building" value="${selectedBuildingCode}">

                        <div class="filter-grid">
                            <div class="filter-group">
                                <label><i class='bx bx-calendar-event'></i> Ngày nhận (Check-in)</label>
                                <input type="date" name="checkInDate" class="form-input-sm" value="${checkInDate}">
                            </div>

                            <div class="filter-group">
                                <label><i class='bx bx-calendar-check'></i> Ngày trả (Check-out)</label>
                                <input type="date" name="checkOutDate" class="form-input-sm" value="${checkOutDate}">
                            </div>

                            <div class="filter-group">
                                <label>Tầng</label>
                                <select name="tang" class="form-select">
                                    <option value="ALL">Tất cả tầng (1 - 30)</option>
                                    <c:forEach begin="1" end="30" var="tg">
                                        <option value="${tg}" ${filterTang == tg ? 'selected' : ''}>Tầng ${tg}</option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="filter-group">
                                <label>Hạng &amp; Loại phòng</label>
                                <select name="loaiPhong" class="form-select">
                                    <option value="ALL">Tất cả loại phòng</option>
                                    <c:forEach items="${listLoaiPhong}" var="lp">
                                        <option value="${lp.idLoaiPhong}" ${filterLoaiPhong == lp.idLoaiPhong ? 'selected' : ''}>${lp.ten}</option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="filter-group">
                                <label>Tầm nhìn (View)</label>
                                <select name="view" class="form-select">
                                    <option value="ALL">Tất cả View</option>
                                    <option value="Ocean" ${filterView == 'Ocean' ? 'selected' : ''}>🌊 Ocean View</option>
                                    <option value="City" ${filterView == 'City' ? 'selected' : ''}>🏙️ City View</option>
                                    <option value="Mountain" ${filterView == 'Mountain' ? 'selected' : ''}>⛰️ Mountain View</option>
                                </select>
                            </div>

                            <div class="filter-group">
                                <label>Trạng thái</label>
                                <select name="trangThai" class="form-select">
                                    <option value="ALL">Tất cả trạng thái</option>
                                    <option value="SAN_SANG" ${filterTrangThai == 'SAN_SANG' ? 'selected' : ''}>Sẵn sàng (Trống)</option>
                                    <option value="DANG_O" ${filterTrangThai == 'DANG_O' ? 'selected' : ''}>Đang ở</option>
                                    <option value="CAN_DON" ${filterTrangThai == 'CAN_DON' ? 'selected' : ''}>Cần dọn</option>
                                    <option value="BAO_TRI" ${filterTrangThai == 'BAO_TRI' ? 'selected' : ''}>Bảo trì</option>
                                </select>
                            </div>

                            <div class="filter-group">
                                <label>Tìm mã phòng</label>
                                <input type="text" name="search" class="form-input-sm" placeholder="VD: CL-0101..." value="${searchKeyword}">
                            </div>

                            <div class="filter-group" style="display: flex; gap: 8px;">
                                <button type="submit" class="btn btn-primary" style="flex: 1; padding: 9px 12px; font-size: 13px;">
                                    <i class='bx bx-search'></i> Lọc &amp; Check
                                </button>
                                <a href="${pageContext.request.contextPath}/trang-chu?building=${selectedBuildingCode}" class="btn btn-ghost" style="padding: 9px 12px; font-size: 13px;" title="Xóa bộ lọc về mặc định">
                                    <i class='bx bx-reset'></i>
                                </a>
                            </div>
                        </div>
                    </form>
                </div>

                <!-- 3. SƠ ĐỒ KHU VỰC PHÒNG THEO TÒA NHÀ & TẦNG -->
                <div class="matrix-container" id="so-do-phong">
                    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; flex-wrap: wrap; gap: 12px;">
                        <div>
                            <h2 style="font-size: 18px; font-weight: 800; color: #222831; margin: 0 0 4px 0;">
                                <i class='bx bx-map-pin' style="color: #557c56;"></i> Sơ Đồ Khu Vực Tòa Nhà &amp; Trạng Thái Phòng
                            </h2>
                            <small style="color: #6b7a70;">Bấm vào bất kỳ phòng nào để hiển thị toàn bộ chi tiết và thao tác ở thanh bên phải</small>
                        </div>

                        <!-- Hai nút chuyển tòa nhà hoạt động trơn tru -->
                        <div class="building-tabs" style="margin-bottom: 0;">
                            <a href="${pageContext.request.contextPath}/trang-chu?building=CL#so-do-phong" class="b-tab ${selectedBuildingCode == 'CL' ? 'active' : ''}">
                                <i class='bx bx-buildings'></i> Tòa Clarence (30 Tầng)
                            </a>
                            <a href="${pageContext.request.contextPath}/trang-chu?building=WN#so-do-phong" class="b-tab ${selectedBuildingCode == 'WN' ? 'active' : ''}">
                                <i class='bx bx-buildings'></i> Tòa Windsor (30 Tầng)
                            </a>
                        </div>
                    </div>

                    <div class="matrix-legend">
                        <div class="legend-item"><span class="legend-dot dot-ready"></span> Sẵn sàng (Trống - Bấm để đặt)</div>
                        <div class="legend-item"><span class="legend-dot dot-occupied"></span> Khách đang lưu trú</div>
                        <div class="legend-item"><span class="legend-dot dot-cleaning"></span> Chờ dọn phòng</div>
                        <div class="legend-item"><span class="legend-dot dot-maintain"></span> Đang bảo trì kỹ thuật</div>
                    </div>

                    <!-- THANH CHỌN TẦNG NHANH: Xem rõ ràng, không bị kéo dọc quá dài -->
                    <div class="floor-tabs-bar">
                        <button type="button" class="floor-tab-btn active" onclick="filterFloorTab('ALL', this)">
                            <i class='bx bx-grid-alt'></i> Tất cả các tầng
                        </button>
                        <button type="button" class="floor-tab-btn" onclick="filterFloorTab('1-5', this)">Tầng 1 - 5</button>
                        <button type="button" class="floor-tab-btn" onclick="filterFloorTab('6-10', this)">Tầng 6 - 10</button>
                        <button type="button" class="floor-tab-btn" onclick="filterFloorTab('11-15', this)">Tầng 11 - 15</button>
                        <button type="button" class="floor-tab-btn" onclick="filterFloorTab('16-20', this)">Tầng 16 - 20</button>
                        <button type="button" class="floor-tab-btn" onclick="filterFloorTab('21-25', this)">Tầng 21 - 25</button>
                        <button type="button" class="floor-tab-btn" onclick="filterFloorTab('26-30', this)">Tầng 26 - 30</button>
                    </div>

                    <!-- KHU VỰC CÁC TẦNG & PHÒNG (CHIỀU CAO TỰ NHIÊN, KHÔNG CÓ CUỘN DỌC CỤC BỘ) -->
                    <div class="matrix-floors-wrapper" id="matrixFloorsList">
                        <c:forEach items="${matrixFloors}" var="entry">
                            <div class="floor-row" data-floor="${entry.key}">
                                <div class="floor-label">
                                    <i class='bx bx-layer'></i>
                                    <span>Tầng ${entry.key < 10 ? '0' : ''}${entry.key}</span>
                                </div>
                                <div class="floor-rooms">
                                    <c:forEach items="${entry.value}" var="room">
                                        <c:set var="lpInfo" value="${mapLoaiPhong[room.idLoaiPhong]}" />
                                        <c:set var="stClass" value="st-ready" />
                                        <c:set var="stBadge" value="badge-ready" />
                                        <c:set var="stIcon" value="bx-check-circle" />
                                        <c:set var="stText" value="Trống" />
                                        <c:choose>
                                            <c:when test="${room.trangThai == 'DANG_O'}">
                                                <c:set var="stClass" value="st-occupied" />
                                                <c:set var="stBadge" value="badge-occupied" />
                                                <c:set var="stIcon" value="bx-user" />
                                                <c:set var="stText" value="Đang ở" />
                                            </c:when>
                                            <c:when test="${room.trangThai == 'CAN_DON'}">
                                                <c:set var="stClass" value="st-cleaning" />
                                                <c:set var="stBadge" value="badge-cleaning" />
                                                <c:set var="stIcon" value="bx-brush" />
                                                <c:set var="stText" value="Cần dọn" />
                                            </c:when>
                                            <c:when test="${room.trangThai == 'BAO_TRI'}">
                                                <c:set var="stClass" value="st-maintain" />
                                                <c:set var="stBadge" value="badge-maintain" />
                                                <c:set var="stIcon" value="bx-wrench" />
                                                <c:set var="stText" value="Bảo trì" />
                                            </c:when>
                                        </c:choose>

                                        <!-- Thẻ phòng sang trọng, thông thoáng -->
                                        <div class="room-box ${stClass}" 
                                             onclick="openRoomDrawer('${room.idPhong}', '${room.maPhong}', '${lpInfo != null ? lpInfo.ten : 'Phòng Tiêu Chuẩn'}', '${room.ghiChu}', '${lpInfo != null ? lpInfo.giaMoiDem : 0}', '${room.trangThai}', '${entry.key}', '${selectedBuildingCode == 'CL' ? 'Clarence' : 'Windsor'}')"
                                             title="Nhấp để xem chi tiết phòng ${room.maPhong}">
                                            <div class="r-header">
                                                <span class="r-code">${room.maPhong}</span>
                                                <span class="r-badge ${stBadge}">
                                                    <i class='bx ${stIcon}'></i> ${stText}
                                                </span>
                                            </div>
                                            <div class="r-type" title="${lpInfo != null ? lpInfo.ten : 'Phòng Tiêu Chuẩn'}">
                                                ${lpInfo != null ? lpInfo.ten : 'Tiêu Chuẩn'}
                                            </div>
                                            <div class="r-footer">
                                                <span class="r-view" title="${room.ghiChu}">
                                                    ${room.ghiChu}
                                                </span>
                                                <span class="r-price">
                                                    <fmt:formatNumber value="${lpInfo != null ? lpInfo.giaMoiDem : 0}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                                                </span>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>
                            </div>
                        </c:forEach>
                        <c:if test="${empty matrixFloors}">
                            <div style="padding: 40px; text-align: center; color: #8b9990;">
                                <i class='bx bx-search' style="font-size: 36px; display: block; margin-bottom: 8px;"></i>
                                Không tìm thấy phòng nào phù hợp với bộ lọc hiện tại.
                            </div>
                        </c:if>
                    </div>
                </div>

            </section>

            <!-- THANH FOOTER CỐ ĐỊNH -->
            <jsp:include page="/layout/footer.jsp" />
        </main>
    </div>

    <!-- ========================================================================= -->
    <!-- 5. RIGHT SIDE PANEL: DRAWER CHI TIẾT PHÒNG BÊN PHẢI (THEO YÊU CẦU CỦA USER) -->
    <!-- ========================================================================= -->
    <div class="drawer-backdrop" id="drawerBackdrop" onclick="closeRoomDrawer()"></div>

    <aside class="right-drawer" id="roomDrawer">
        <!-- Header Drawer -->
        <div class="drawer-header">
            <div>
                <div style="font-size: 11px; color: #aeb8b3; text-transform: uppercase; letter-spacing: 1px;">THÔNG TIN CHI TIẾT PHÒNG</div>
                <h3 class="drawer-title" id="drawerRoomCode">
                    <i class='bx bx-hotel' style="color: #6d9886;"></i> Phòng ...
                </h3>
            </div>
            <button type="button" class="drawer-close" onclick="closeRoomDrawer()" title="Đóng panel">&times;</button>
        </div>

        <!-- Body Drawer -->
        <div class="drawer-body">
            <!-- Thông số cơ bản của phòng -->
            <div class="drawer-card">
                <div class="drawer-info-row">
                    <span class="drawer-info-label">Vị trí:</span>
                    <span class="drawer-info-val" id="drawerLocation">Tòa Clarence — Tầng 01</span>
                </div>
                <div class="drawer-info-row">
                    <span class="drawer-info-label">Hạng &amp; Loại phòng:</span>
                    <span class="drawer-info-val" id="drawerType" style="color: #557c56;">Deluxe Room</span>
                </div>
                <div class="drawer-info-row">
                    <span class="drawer-info-label">Tầm nhìn (View):</span>
                    <span class="drawer-info-val" id="drawerView" style="color: #006064;">City View</span>
                </div>
                <div class="drawer-info-row">
                    <span class="drawer-info-label">Đơn giá niêm yết:</span>
                    <span class="drawer-info-val" id="drawerPrice" style="color: #b78103; font-size: 15px;">0 ₫ / đêm</span>
                </div>
                <div class="drawer-info-row">
                    <span class="drawer-info-label">Trạng thái hiện tại:</span>
                    <span id="drawerStatusBadge"></span>
                </div>
            </div>

            <!-- Chính sách phụ thu trẻ em The Royal Hotel -->
            <div style="background: #fdfbf7; border: 1px solid #f4e8d3; border-radius: 10px; padding: 14px; font-size: 12px; color: #5a4b32; line-height: 1.5;">
                <b style="display: flex; align-items: center; gap: 6px; color: #875a13; margin-bottom: 4px;">
                    <i class='bx bx-child'></i> Chính Sách Phụ Thu Trẻ Em Đi Kèm:
                </b>
                Tối đa 2 trẻ em &lt; 6 tuổi / phòng. Bé thứ 1: Miễn phí (0 ₫), bé thứ 2: phụ thu 5% giá phòng/đêm. Trẻ từ 6 tuổi trở lên tính như 1 người lớn.
            </div>

            <!-- Tiện nghi phòng trang bị -->
            <div class="drawer-card">
                <b style="font-size: 12px; color: #222831; display: flex; align-items: center; gap: 6px;">
                    <i class='bx bx-spa' style="color: #557c56;"></i> Tiện Nghi &amp; Dịch Vụ Đi Kèm:
                </b>
                <div class="amenity-pill-list">
                    <span class="amenity-pill-item"><i class='bx bx-wifi'></i> Wifi 5G tốc độ cao</span>
                    <span class="amenity-pill-item"><i class='bx bx-wind'></i> Điều hòa 2 chiều</span>
                    <span class="amenity-pill-item"><i class='bx bx-tv'></i> Smart TV 55" 4K</span>
                    <span class="amenity-pill-item"><i class='bx bx-drink'></i> Minibar &amp; Đồ uống</span>
                    <span class="amenity-pill-item"><i class='bx bx-lock-alt'></i> Két an toàn điện tử</span>
                    <span class="amenity-pill-item"><i class='bx bx-bath'></i> Bồn tắm nằm cao cấp</span>
                </div>
            </div>

            <!-- KHỐI 1: FORM ĐẶT PHÒNG NHANH (NẾU PHÒNG LÀ SẴN SÀNG) -->
            <div id="bookingFormSection" style="display: none; background: #ffffff; border: 1px solid #c8e6c9; border-radius: 10px; padding: 18px;">
                <h4 style="margin: 0 0 12px 0; font-size: 14px; font-weight: 800; color: #2e7d32; display: flex; align-items: center; gap: 6px;">
                    <i class='bx bx-calendar-plus'></i> Đặt Phòng Nhanh Trực Tuyến
                </h4>

                <form action="${pageContext.request.contextPath}/trang-chu/dat-phong" method="post">
                    <input type="hidden" name="idPhong" id="drawerFormIdPhong">
                    <input type="hidden" name="building" value="${selectedBuildingCode}">

                    <div style="margin-bottom: 10px;">
                        <label style="display: block; font-size: 12px; font-weight: 700; color: #2b3a33; margin-bottom: 4px;">Họ tên khách hàng <span style="color:red">*</span></label>
                        <input type="text" name="tenKhach" class="form-input-sm" placeholder="Nguyễn Văn A" required>
                    </div>

                    <div style="margin-bottom: 10px;">
                        <label style="display: block; font-size: 12px; font-weight: 700; color: #2b3a33; margin-bottom: 4px;">Số điện thoại <span style="color:red">*</span></label>
                        <input type="text" name="sdtKhach" class="form-input-sm" placeholder="090xxxxxxx" required>
                    </div>

                    <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 10px; margin-bottom: 12px;">
                        <div>
                            <label style="display: block; font-size: 12px; font-weight: 700; color: #2b3a33; margin-bottom: 4px;">Người lớn</label>
                            <select name="nguoiLon" class="form-select">
                                <option value="1">1 người</option>
                                <option value="2" selected>2 người</option>
                                <option value="3">3 người</option>
                                <option value="4">4 người</option>
                            </select>
                        </div>
                        <div>
                            <label style="display: block; font-size: 12px; font-weight: 700; color: #2b3a33; margin-bottom: 4px;">Trẻ em (&lt;6t)</label>
                            <select name="treEm" class="form-select">
                                <option value="0">0 trẻ</option>
                                <option value="1">1 trẻ (0đ)</option>
                                <option value="2">2 trẻ (+5%)</option>
                            </select>
                        </div>
                    </div>

                    <div style="font-size: 11px; color: #6b7a70; background: #fafcfb; padding: 8px 10px; border-radius: 6px; margin-bottom: 14px; border: 1px dashed #d4ddd7;">
                        💰 Đặt cọc 30% giá phòng khi xác nhận. Phần còn lại thanh toán khi trả phòng.
                    </div>

                    <button type="submit" class="btn btn-primary" style="width: 100%; padding: 11px; font-size: 13px; font-weight: 700; display: flex; align-items: center; justify-content: center; gap: 6px;">
                        <i class='bx bx-check'></i> Xác Nhận Đặt Phòng Này
                    </button>
                </form>
            </div>

            <!-- KHỐI 2: HÀNH ĐỘNG KHI PHÒNG KHÔNG Ở TRẠNG THÁI SẴN SÀNG -->
            <div id="otherStatusSection" style="display: none; background: #ffffff; border: 1px solid #e0e6e2; border-radius: 10px; padding: 16px;">
                <div id="otherStatusMsg" style="font-size: 13px; color: #49584e; margin-bottom: 14px; line-height: 1.5;"></div>

                <form action="${pageContext.request.contextPath}/trang-chu/doi-trang-thai" method="post" id="statusChangeForm">
                    <input type="hidden" name="idPhong" id="drawerStatusIdPhong">
                    <input type="hidden" name="trangThaiMoi" id="drawerStatusNewVal">
                    <input type="hidden" name="building" value="${selectedBuildingCode}">

                    <div id="statusActionButtons" style="display: flex; flex-direction: column; gap: 8px;"></div>
                </form>
            </div>

            <!-- Nút điều hướng sang trang chi tiết phòng đầy đủ -->
            <div style="margin-top: auto; padding-top: 14px;">
                <a id="drawerFullDetailLink" href="#" class="btn btn-ghost" style="width: 100%; padding: 10px; font-size: 13px; text-align: center; display: block; box-sizing: border-box; text-decoration: none;">
                    <i class='bx bx-link-external'></i> Mở Trang Quản Lý &amp; Cấu Hình Phòng
                </a>
            </div>
        </div>
    </aside>

    <!-- Script điều khiển Right Drawer & Tương tác -->
    <script>
        function openRoomDrawer(id, code, type, view, price, status, floor, building) {
            document.getElementById('drawerFormIdPhong').value = id;
            document.getElementById('drawerStatusIdPhong').value = id;
            document.getElementById('drawerRoomCode').innerHTML = "<i class='bx bx-hotel' style='color:#6d9886;'></i> Phòng " + code;
            document.getElementById('drawerLocation').innerText = "Tòa " + building + " — Tầng " + floor;
            document.getElementById('drawerType').innerText = type;
            document.getElementById('drawerView').innerText = view || "City View";
            document.getElementById('drawerPrice').innerText = Number(price).toLocaleString('vi-VN') + " ₫ / đêm";
            document.getElementById('drawerFullDetailLink').href = "${pageContext.request.contextPath}/quan-ly-phong/detail?id=" + id;

            // Render status badge
            const badgeEl = document.getElementById('drawerStatusBadge');
            const bookingSection = document.getElementById('bookingFormSection');
            const otherSection = document.getElementById('otherStatusSection');
            const otherMsg = document.getElementById('otherStatusMsg');
            const actionBtns = document.getElementById('statusActionButtons');

            if (status === 'SAN_SANG') {
                badgeEl.innerHTML = '<span class="badge" style="background:#e8f5e9; color:#2e7d32; padding:4px 10px; border-radius:12px; font-weight:700;">Sẵn sàng (Trống)</span>';
                bookingSection.style.display = 'block';
                otherSection.style.display = 'none';
            } else if (status === 'DANG_O') {
                badgeEl.innerHTML = '<span class="badge" style="background:#e3f2fd; color:#1565c0; padding:4px 10px; border-radius:12px; font-weight:700;">Khách đang ở</span>';
                bookingSection.style.display = 'none';
                otherSection.style.display = 'block';
                otherMsg.innerHTML = "<b>Phòng đang có khách lưu trú.</b> Bạn có thể thực hiện trả phòng (Check-out) để chuyển trạng thái sang Cần dọn phòng:";
                actionBtns.innerHTML = `
                    <button type="button" class="btn btn-warning" style="padding:10px; font-size:13px; font-weight:700;" onclick="submitStatusChange('CAN_DON')">
                        <i class='bx bx-log-out-circle'></i> Khách Trả Phòng (Check-out) -> Cần Dọn
                    </button>
                `;
            } else if (status === 'CAN_DON') {
                badgeEl.innerHTML = '<span class="badge" style="background:#fff8e1; color:#f57f17; padding:4px 10px; border-radius:12px; font-weight:700;">Chờ dọn dẹp</span>';
                bookingSection.style.display = 'none';
                otherSection.style.display = 'block';
                otherMsg.innerHTML = "<b>Phòng vừa trả và đang chờ dọn vệ sinh.</b> Sau khi nhân viên buồng phòng dọn xong, bấm nút dưới để chuyển sang Sẵn sàng:";
                actionBtns.innerHTML = `
                    <button type="button" class="btn btn-success" style="padding:10px; font-size:13px; font-weight:700;" onclick="submitStatusChange('SAN_SANG')">
                        <i class='bx bx-check-double'></i> Đã Dọn Phòng Xong -> Sẵn Sàng Đón Khách
                    </button>
                `;
            } else if (status === 'BAO_TRI') {
                badgeEl.innerHTML = '<span class="badge" style="background:#ffebee; color:#c62828; padding:4px 10px; border-radius:12px; font-weight:700;">Đang bảo trì</span>';
                bookingSection.style.display = 'none';
                otherSection.style.display = 'block';
                otherMsg.innerHTML = "<b>Phòng đang trong quy trình bảo trì / sửa chữa thiết bị.</b> Khi hoàn tất kỹ thuật, bấm nút dưới để đưa vào hoạt động:";
                actionBtns.innerHTML = `
                    <button type="button" class="btn btn-success" style="padding:10px; font-size:13px; font-weight:700;" onclick="submitStatusChange('SAN_SANG')">
                        <i class='bx bx-wrench'></i> Hoàn Tất Sửa Chữa -> Sẵn Sàng Đón Khách
                    </button>
                `;
            }

            // Mở Drawer và Backdrop
            document.getElementById('roomDrawer').classList.add('open');
            document.getElementById('drawerBackdrop').classList.add('open');
        }

        function closeRoomDrawer() {
            document.getElementById('roomDrawer').classList.remove('open');
            document.getElementById('drawerBackdrop').classList.remove('open');
        }

        function submitStatusChange(newStatus) {
            document.getElementById('drawerStatusNewVal').value = newStatus;
            document.getElementById('statusChangeForm').submit();
        }

        // Lọc tầng hiển thị nhanh ở sơ đồ (Không làm tải lại trang)
        function filterFloorTab(range, btn) {
            document.querySelectorAll('.floor-tab-btn').forEach(b => b.classList.remove('active'));
            if (btn) btn.classList.add('active');

            const rows = document.querySelectorAll('.floor-row');
            if (range === 'ALL') {
                rows.forEach(r => r.style.display = 'flex');
                return;
            }

            const parts = range.split('-');
            const minF = parseInt(parts[0]);
            const maxF = parseInt(parts[1]);

            rows.forEach(r => {
                const f = parseInt(r.getAttribute('data-floor'));
                if (f >= minF && f <= maxF) {
                    r.style.display = 'flex';
                } else {
                    r.style.display = 'none';
                }
            });
        }

        // Bấm phím Escape đóng drawer
        document.addEventListener('keydown', function(e) {
            if (e.key === 'Escape') {
                closeRoomDrawer();
            }
        });
    </script>
</body>
</html>