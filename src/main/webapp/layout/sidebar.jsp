<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- ===================================================================== -->
<!-- 1. THANH ĐIỀU HƯỚNG BÊN TRÁI CHUẨN GIAO DIỆN MẪU (SIDEBAR NAVIGATION) -->
<!-- ===================================================================== -->
<aside class="sidebar" id="sidebar">
    <!-- Khu vực Logo khách sạn (Nền tròn #EEEEEE theo yêu cầu nhận diện hoàng gia) -->
    <div class="brand">
        <div class="brand-mark">
            <img src="${pageContext.request.contextPath}/assets/logo-royal.png" alt="The Royal Hotel Logo">
        </div>
        <div class="brand-info" style="white-space: nowrap; overflow: hidden;">
            <div class="brand-name">THE ROYAL</div>
            <div class="brand-sub">HOTEL MANAGEMENT</div>
        </div>
    </div>

    <!-- Phân mục điều hướng chính -->
    <div class="side-label" style="white-space: nowrap;">MENU CHÍNH</div>
    <nav id="navigation">
        <a href="${pageContext.request.contextPath}/trang-chu" class="nav-item ${param.activeMenu == 'dashboard' ? 'active' : ''}">
            <i class='bx bx-grid-alt'></i> <span>Tổng quan Dashboard</span>
        </a>

        <div class="nav-label" style="white-space: nowrap;">VẬN HÀNH KHÁCH SẠN</div>
        <a href="${pageContext.request.contextPath}/quan-ly-phong/view" class="nav-item ${param.activeMenu == 'phong' ? 'active' : ''}">
            <i class='bx bx-hotel'></i> <span>Quản lý sơ đồ phòng</span>
        </a>
        <a href="${pageContext.request.contextPath}/trang-chu#so-do-phong" class="nav-item ${param.activeMenu == 'dat-phong' ? 'active' : ''}">
            <i class='bx bx-calendar-check'></i> <span>Đặt phòng &amp; Sơ đồ</span>
        </a>
        <a href="${pageContext.request.contextPath}/quan-ly-nhan-vien/view" class="nav-item ${param.activeMenu == 'nhan-vien' ? 'active' : ''}">
            <i class='bx bx-id-card'></i> <span>Quản lý nhân viên</span>
        </a>
        <a href="${pageContext.request.contextPath}/quan-ly-khach-hang/view" class="nav-item ${param.activeMenu == 'quan-ly-khach-hang' ? 'active' : ''}">
            <i class='bx bx-user-pin'></i> <span>Quản lý khách hàng</span>
        </a>

        <div class="nav-label" style="white-space: nowrap;">DỊCH VỤ &amp; TÀI CHÍNH</div>
        <a href="${pageContext.request.contextPath}/quan-ly-dich-vu/view" class="nav-item ${param.activeMenu == 'quan-ly-dich-vu' ? 'active' : ''}">
            <i class='bx bx-dish'></i> <span>Quản lý dịch vụ</span>
        </a>
        <a href="${pageContext.request.contextPath}/quan-ly-danh-gia/view" class="nav-item ${param.activeMenu == 'quan-ly-danh-gia' ? 'active' : ''}">
            <i class='bx bx-star'></i> <span>Đánh giá khách hàng</span>
        </a>
        <a href="${pageContext.request.contextPath}/quan-ly-phong/view" class="nav-item">
            <i class='bx bx-spa'></i> <span>Tiện nghi phòng</span>
        </a>
        <a href="${pageContext.request.contextPath}/trang-chu" class="nav-item">
            <i class='bx bx-receipt'></i> <span>Hóa đơn &amp; Thu chi</span>
        </a>

        <!-- Phân quyền nhanh theo vai trò -->
        <c:if test="${sessionScope.currentRole.maVaiTro == 'BUONG_PHONG'}">
            <div class="nav-label" style="color: #ffe082; white-space: nowrap;">BUỒNG PHÒNG</div>
            <a href="${pageContext.request.contextPath}/trang-chu?trangThai=CAN_DON" class="nav-item" style="color: #ffd54f;">
                <i class='bx bx-brush'></i> <span>Nhiệm vụ dọn phòng</span>
            </a>
        </c:if>

        <c:if test="${sessionScope.currentRole.maVaiTro == 'BAO_TRI'}">
            <div class="nav-label" style="color: #ff8a80; white-space: nowrap;">KỸ THUẬT &amp; BẢO TRÌ</div>
            <a href="${pageContext.request.contextPath}/trang-chu?trangThai=BAO_TRI" class="nav-item" style="color: #ff8a80;">
                <i class='bx bx-wrench'></i> <span>Yêu cầu sửa chữa</span>
            </a>
        </c:if>
    </nav>

    <!-- Hộp hỗ trợ kỹ thuật và thông tin nhân viên trực ở đáy sidebar -->
    <div class="sidebar-bottom" style="flex-direction: column; align-items: stretch; gap: 8px; margin-top: auto; padding-top: 14px;">
        <div style="display: flex; gap: 10px; align-items: center;">
            <div class="support-icon"><i class='bx bx-support'></i></div>
            <div style="white-space: nowrap; overflow: hidden;">
                <b style="font-size: 12px; color: #eee; display: block;">Trung tâm hỗ trợ IT</b>
                <small style="color: #9eafa7; font-size: 11px;">Hotline: 1900-ROYAL (24/7)</small>
            </div>
        </div>

        <div style="padding-top: 8px; border-top: 1px solid rgba(255,255,255,0.08); display: flex; align-items: center; justify-content: space-between;">
            <div style="white-space: nowrap; overflow: hidden; max-width: 170px;">
                <small style="color: #87938e; font-size: 10px; display: block;">${not empty sessionScope.currentUser ? 'Nhân viên trực ca:' : 'Đang đăng nhập:'}</small>
                <b style="font-size: 12px; color: #eeeeee; text-overflow: ellipsis; overflow: hidden; display: block;">${not empty sessionScope.currentUser.hoTen ? sessionScope.currentUser.hoTen : (not empty sessionScope.currentKhachHang.hoTen ? sessionScope.currentKhachHang.hoTen : 'Quản trị viên')}</b>
            </div>
            <a href="${pageContext.request.contextPath}/logout" class="icon-btn" style="width: 30px; height: 30px; border-radius: 6px; border: 1px solid rgba(255,255,255,0.15); color: #ff8a80; background: rgba(255,255,255,0.05); display: flex; align-items: center; justify-content: center; text-decoration: none;" title="Đăng xuất khỏi hệ thống">
                <i class='bx bx-log-out' style="font-size: 16px;"></i>
            </a>
        </div>
    </div>
</aside>
