<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chi Tiết Phòng ${phong.maPhong} — The Royal Hotel</title>

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
        .detail-layout {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 24px;
        }
        @media (max-width: 900px) {
            .detail-layout { grid-template-columns: 1fr; }
        }
        .info-row {
            display: flex;
            justify-content: space-between;
            padding: 12px 0;
            border-bottom: 1px solid #f0f4f2;
            font-size: 14px;
        }
        .info-label {
            color: #6b7a70;
            font-weight: 500;
        }
        .info-val {
            font-weight: 700;
            color: #222831;
            text-align: right;
        }
        .amenity-chip {
            display: inline-flex;
            align-items: center;
            gap: 8px;
            padding: 8px 14px;
            background: #f8faf9;
            border: 1px solid #e2ebe5;
            border-radius: 8px;
            font-size: 13px;
            color: #2b3a33;
            font-weight: 600;
        }
        .amenity-chip i {
            color: #557c56;
            font-size: 18px;
        }
    </style>
</head>

<body>
    <div class="app-shell">
        <!-- 1. SIDEBAR DÙNG CHUNG -->
        <jsp:include page="/layout/sidebar.jsp">
            <jsp:param name="activeMenu" value="phong" />
        </jsp:include>

        <!-- 2. MAIN AREA -->
        <main class="main-area">
            <!-- HEADER DÙNG CHUNG -->
            <jsp:include page="/layout/header.jsp">
                <jsp:param name="pageTitle" value="Hồ Sơ Chi Tiết Phòng ${phong.maPhong}" />
            </jsp:include>

            <section class="content" style="padding: 28px;">
                <div class="page-heading">
                    <div>
                        <div class="eyebrow" style="font-size: 11px; font-weight: 700; color: #557c56; letter-spacing: 2px;">HỒ SƠ PHÒNG NGHỈ HOÀNG GIA</div>
                        <h1 style="font-size: 24px; font-weight: 800; color: #222831; margin: 4px 0;">Phòng ${phong.maPhong}</h1>
                        <p style="color: #6b7a70; font-size: 13px; margin: 0;">Thông tin chi tiết về không gian, tầm nhìn, phân loại phòng và danh mục tiện nghi trang bị.</p>
                    </div>
                    <div class="heading-actions">
                        <a href="${pageContext.request.contextPath}/quan-ly-phong/view" class="btn btn-ghost">
                            <i class='bx bx-arrow-back'></i> Quay lại
                        </a>
                        <a href="${pageContext.request.contextPath}/quan-ly-phong/view-update?id=${phong.idPhong}" class="btn btn-primary">
                            <i class='bx bx-edit-alt'></i> Sửa thông tin
                        </a>
                    </div>
                </div>

                <div class="detail-layout">
                    <!-- Cột 1: Thông số phòng -->
                    <div class="card">
                        <h3 style="font-size: 16px; font-weight: 700; color: #222831; margin-bottom: 16px; display: flex; align-items: center; gap: 8px;">
                            <i class='bx bx-info-circle' style="color: #557c56;"></i> Thông Số Cơ Bản
                        </h3>

                        <div class="info-row">
                            <span class="info-label">Mã số phòng:</span>
                            <span class="info-val">${phong.maPhong}</span>
                        </div>
                        <div class="info-row">
                            <span class="info-label">Tòa nhà:</span>
                            <span class="info-val">Tòa ${toaNha != null ? toaNha.ten : 'Chưa phân bổ'} (${toaNha != null ? toaNha.maToaNha : ''})</span>
                        </div>
                        <div class="info-row">
                            <span class="info-label">Tầng:</span>
                            <span class="info-val">Tầng ${tang != null ? tang.soTang : 'N/A'}</span>
                        </div>
                        <div class="info-row">
                            <span class="info-label">Loại phòng:</span>
                            <span class="info-val">${loaiPhong != null ? loaiPhong.ten : 'Tiêu chuẩn'}</span>
                        </div>
                        <div class="info-row">
                            <span class="info-label">Giá niêm yết mỗi đêm:</span>
                            <span class="info-val" style="color: #b78103; font-size: 16px;">
                                <fmt:formatNumber value="${loaiPhong != null ? loaiPhong.giaMoiDem : 0}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                            </span>
                        </div>
                        <div class="info-row">
                            <span class="info-label">Tầm nhìn (View):</span>
                            <span class="info-val" style="color: #006064;">${phong.ghiChu}</span>
                        </div>
                        <div class="info-row" style="border-bottom: none;">
                            <span class="info-label">Trạng thái hiện tại:</span>
                            <span class="info-val">
                                <c:choose>
                                    <c:when test="${phong.trangThai == 'SAN_SANG' || phong.trangThai == 'Sẵn sàng'}"><span style="color:#2e7d32;">● Sẵn sàng đón khách</span></c:when>
                                    <c:when test="${phong.trangThai == 'GIU_CHO' || phong.trangThai == 'Giữ chỗ'}"><span style="color:#8e24aa;">● Giữ chỗ (Đã đặt trước)</span></c:when>
                                    <c:when test="${phong.trangThai == 'DANG_O' || phong.trangThai == 'Đang ở'}"><span style="color:#1565c0;">● Khách đang ở</span></c:when>
                                    <c:when test="${phong.trangThai == 'CAN_DON' || phong.trangThai == 'Cần dọn'}"><span style="color:#f57f17;">● Cần dọn phòng</span></c:when>
                                    <c:when test="${phong.trangThai == 'BAO_TRI' || phong.trangThai == 'Bảo trì'}"><span style="color:#c62828;">● Đang bảo trì</span></c:when>
                                    <c:otherwise><span>${phong.trangThai}</span></c:otherwise>
                                </c:choose>
                            </span>
                        </div>
                    </div>

                    <!-- Cột 2: Tiện nghi setup đi kèm -->
                    <div class="card">
                        <h3 style="font-size: 16px; font-weight: 700; color: #222831; margin-bottom: 16px; display: flex; align-items: center; gap: 8px;">
                            <i class='bx bx-spa' style="color: #557c56;"></i> Tiện Nghi Đi Kèm Setup Trong Phòng
                        </h3>
                        <p style="font-size: 13px; color: #6b7a70; margin-bottom: 16px;">
                            Các trang thiết bị và dịch vụ tiện nghi được quản lý thiết lập cho phòng này:
                        </p>

                        <div style="display: flex; flex-wrap: wrap; gap: 10px;">
                            <c:choose>
                                <c:when test="${not empty listTienNghi}">
                                    <c:forEach items="${listTienNghi}" var="tn">
                                        <div class="amenity-chip">
                                            <i class='bx bx-check-shield'></i> ${tn.tenTienNghi} (x${tn.soLuong})
                                        </div>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <div class="amenity-chip"><i class='bx bx-wifi'></i> Wi-Fi 6 tốc độ cao</div>
                                    <div class="amenity-chip"><i class='bx bx-tv'></i> Smart TV 65" 4K OLED</div>
                                    <div class="amenity-chip"><i class='bx bx-wind'></i> Điều hòa 2 chiều Inverter</div>
                                    <div class="amenity-chip"><i class='bx bx-drink'></i> Minibar & Đồ uống miễn phí</div>
                                    <div class="amenity-chip"><i class='bx bx-lock-alt'></i> Két sắt điện tử</div>
                                    <div class="amenity-chip"><i class='bx bx-bath'></i> Bồn tắm nằm cao cấp</div>
                                    <div class="amenity-chip"><i class='bx bx-coffee'></i> Máy pha cà phê Espresso</div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </section>

            <!-- FOOTER DÙNG CHUNG -->
            <jsp:include page="/layout/footer.jsp" />
        </main>
    </div>
</body>
</html>
