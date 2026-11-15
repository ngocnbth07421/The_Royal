<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Hồ Sơ Nhân Viên ${nhanVien.hoTen} — The Royal Hotel</title>

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
        .profile-card-top {
            display: flex;
            align-items: center;
            gap: 16px;
            margin-bottom: 20px;
            padding-bottom: 16px;
            border-bottom: 1px solid #eef2ef;
        }
        .profile-big-avatar {
            width: 64px;
            height: 64px;
            border-radius: 50%;
            background: #eaf1ed;
            color: #557c56;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 32px;
            border: 2px solid #557c56;
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
                <jsp:param name="pageTitle" value="Hồ Sơ Nhân Viên ${nhanVien.hoTen}" />
            </jsp:include>

            <section class="content" style="padding: 28px;">
                <div class="page-heading">
                    <div>
                        <div class="eyebrow" style="font-size: 11px; font-weight: 700; color: #557c56; letter-spacing: 2px;">HỒ SƠ CÔNG CHỨC NỘI BỘ</div>
                        <h1 style="font-size: 24px; font-weight: 800; color: #222831; margin: 4px 0;">Nhân Viên: ${nhanVien.hoTen}</h1>
                        <p style="color: #6b7a70; font-size: 13px; margin: 0;">Hồ sơ nhân sự chi tiết, vị trí phân quyền và cơ chế tiền lương vận hành tại khách sạn.</p>
                    </div>
                    <div class="heading-actions">
                        <a href="${pageContext.request.contextPath}/quan-ly-nhan-vien/view" class="btn btn-ghost">
                            <i class='bx bx-arrow-back'></i> Quay lại
                        </a>
                        <a href="${pageContext.request.contextPath}/quan-ly-nhan-vien/view-update?id=${nhanVien.idNhanVien}" class="btn btn-primary">
                            <i class='bx bx-edit-alt'></i> Sửa hồ sơ
                        </a>
                    </div>
                </div>

                <div class="detail-layout">
                    <!-- Cột 1: Thông tin nhân sự -->
                    <div class="card">
                        <div class="profile-card-top">
                            <div class="profile-big-avatar">
                                <i class='bx bxs-user'></i>
                            </div>
                            <div>
                                <h2 style="font-size: 18px; font-weight: 700; color: #222831; margin-bottom: 4px;">${nhanVien.hoTen}</h2>
                                <span style="font-size: 13px; color: #557c56; font-weight: 600;">
                                    ${vaiTro != null ? vaiTro.tenVaiTro : 'Chưa gán vai trò'} (${nhanVien.maNhanVien})
                                </span>
                            </div>
                        </div>

                        <div class="info-row">
                            <span class="info-label">Mã số nhân viên:</span>
                            <span class="info-val">${nhanVien.maNhanVien}</span>
                        </div>
                        <div class="info-row">
                            <span class="info-label">Số điện thoại liên hệ:</span>
                            <span class="info-val">${nhanVien.soDienThoai}</span>
                        </div>
                        <div class="info-row">
                            <span class="info-label">Vai trò hệ thống:</span>
                            <span class="info-val" style="color: #4527a0;">${vaiTro != null ? vaiTro.tenVaiTro : 'N/A'}</span>
                        </div>
                        <div class="info-row">
                            <span class="info-label">Ngày vào làm chính thức:</span>
                            <span class="info-val">
                                <fmt:formatDate value="${nhanVien.ngayVaoLam}" pattern="dd/MM/yyyy"/>
                            </span>
                        </div>
                        <div class="info-row" style="border-bottom: none;">
                            <span class="info-label">Trạng thái công tác:</span>
                            <span class="info-val">
                                <c:choose>
                                    <c:when test="${nhanVien.trangThai == 'HOAT_DONG'}"><span style="color:#2e7d32;">● Đang hoạt động</span></c:when>
                                    <c:otherwise><span style="color:#c62828;">● Tạm khóa</span></c:otherwise>
                                </c:choose>
                            </span>
                        </div>
                    </div>

                    <!-- Cột 2: Đãi ngộ và Bảng lương -->
                    <div class="card">
                        <h3 style="font-size: 16px; font-weight: 700; color: #222831; margin-bottom: 16px; display: flex; align-items: center; gap: 8px;">
                            <i class='bx bx-wallet-alt' style="color: #557c56;"></i> Cơ Cấu Lương &amp; Đãi Ngộ Hàng Tháng
                        </h3>

                        <div class="info-row">
                            <span class="info-label">Lương cơ bản thỏa thuận:</span>
                            <span class="info-val">
                                <fmt:formatNumber value="${nhanVien.luongCoBan}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                            </span>
                        </div>
                        <div class="info-row">
                            <span class="info-label">Phụ cấp trách nhiệm / ca:</span>
                            <span class="info-val" style="color: #2e7d32;">
                                + <fmt:formatNumber value="${nhanVien.phuCap}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                            </span>
                        </div>
                        <div class="info-row">
                            <span class="info-label">Khoản khấu trừ (Bảo hiểm / Vi phạm):</span>
                            <span class="info-val" style="color: #c62828;">
                                - <fmt:formatNumber value="${nhanVien.khauTru}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                            </span>
                        </div>
                        <div class="info-row" style="border-bottom: none; padding-top: 18px; margin-top: 8px; border-top: 2px dashed #e2ebe5;">
                            <span class="info-label" style="font-weight: 700; color: #222831; font-size: 15px;">TỔNG THỰC LĨNH / THÁNG:</span>
                            <span class="info-val" style="color: #b78103; font-size: 18px; font-weight: 800;">
                                <fmt:formatNumber value="${nhanVien.luongCoBan + nhanVien.phuCap - nhanVien.khauTru}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                            </span>
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
