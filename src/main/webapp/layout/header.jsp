<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- ===================================================================== -->
<!-- 2. THANH HEADER TRÊN CÙNG CHUẨN GIAO DIỆN MẪU (TOPBAR HEADER)         -->
<!-- ===================================================================== -->
<header class="topbar">
    <!-- Breadcrumb điều hướng vị trí trang -->
    <div class="breadcrumb">
        <span>THE ROYAL HOTEL</span>
        <i>/</i>
        <b>${not empty param.pageTitle ? param.pageTitle : 'Tổng quan Dashboard'}</b>
    </div>

    <!-- Khu vực bên phải: Chuông thông báo, Vai trò & Tài khoản người dùng -->
    <div class="top-actions" style="position: relative;">
        <!-- Nút chuông thông báo hệ thống -->
        <button class="icon-btn" id="notifBtn" type="button" title="Xem thông báo hệ thống" onclick="toggleNotifDropdown(event)" style="position: relative;">
            <i class='bx bx-bell'></i>
            <span id="notifDot" style="position: absolute; top: 6px; right: 6px; width: 9px; height: 9px; background: #e53935; border-radius: 50%; border: 2px solid #ffffff;"></span>
        </button>

        <!-- DROPDOWN DANH SÁCH THÔNG BÁO HỆ THỐNG -->
        <div id="notifDropdown" style="display: none; position: absolute; top: 52px; right: 200px; width: 360px; background: #ffffff; border-radius: 12px; box-shadow: 0 10px 35px rgba(0,0,0,0.18); border: 1px solid #e0e6e2; z-index: 1050; overflow: hidden;">
            <div style="background: #222831; color: #ffffff; padding: 14px 18px; display: flex; justify-content: space-between; align-items: center;">
                <div style="font-weight: 700; font-size: 13px; display: flex; align-items: center; gap: 8px;">
                    <i class='bx bx-bell' style="color: #6d9886; font-size: 16px;"></i>
                    <span>Thông Báo Khách Sạn (04 mới)</span>
                </div>
                <span onclick="closeNotifDropdown()" style="cursor: pointer; font-size: 18px; color: #aeb8b3;">&times;</span>
            </div>

            <div style="max-height: 320px; overflow-y: auto;">
                <div style="padding: 12px 16px; border-bottom: 1px solid #f0f4f1; display: flex; gap: 12px; align-items: flex-start;">
                    <div style="width: 32px; height: 32px; border-radius: 50%; background: #fff8e1; color: #f57f17; display: flex; align-items: center; justify-content: center; flex-shrink: 0;">
                        <i class='bx bx-brush'></i>
                    </div>
                    <div>
                        <div style="font-size: 12px; font-weight: 700; color: #222831;">Phòng CL-0302 cần dọn phòng</div>
                        <div style="font-size: 11px; color: #6b7a70; margin-top: 2px;">Khách vừa yêu cầu dọn phòng bổ sung đồ amenities.</div>
                        <small style="color: #9aa7a0; font-size: 10px;">10 phút trước</small>
                    </div>
                </div>

                <div style="padding: 12px 16px; border-bottom: 1px solid #f0f4f1; display: flex; gap: 12px; align-items: flex-start;">
                    <div style="width: 32px; height: 32px; border-radius: 50%; background: #e3f2fd; color: #1565c0; display: flex; align-items: center; justify-content: center; flex-shrink: 0;">
                        <i class='bx bx-user-check'></i>
                    </div>
                    <div>
                        <div style="font-size: 12px; font-weight: 700; color: #222831;">Khách check-in phòng WN-0501</div>
                        <div style="font-size: 11px; color: #6b7a70; margin-top: 2px;">Lễ tân đã hoàn tất thủ tục nhận phòng cho khách Nguyễn Văn An.</div>
                        <small style="color: #9aa7a0; font-size: 10px;">25 phút trước</small>
                    </div>
                </div>

                <div style="padding: 12px 16px; border-bottom: 1px solid #f0f4f1; display: flex; gap: 12px; align-items: flex-start;">
                    <div style="width: 32px; height: 32px; border-radius: 50%; background: #e8f5e9; color: #2e7d32; display: flex; align-items: center; justify-content: center; flex-shrink: 0;">
                        <i class='bx bx-check-double'></i>
                    </div>
                    <div>
                        <div style="font-size: 12px; font-weight: 700; color: #222831;">Hoàn tất bảo trì phòng CL-0104</div>
                        <div style="font-size: 11px; color: #6b7a70; margin-top: 2px;">Sự cố điều hòa nhiệt độ đã khắc phục xong, phòng sẵn sàng đón khách.</div>
                        <small style="color: #9aa7a0; font-size: 10px;">1 giờ trước</small>
                    </div>
                </div>

                <div style="padding: 12px 16px; display: flex; gap: 12px; align-items: flex-start;">
                    <div style="width: 32px; height: 32px; border-radius: 50%; background: #f3e5f5; color: #7b1fa2; display: flex; align-items: center; justify-content: center; flex-shrink: 0;">
                        <i class='bx bx-receipt'></i>
                    </div>
                    <div>
                        <div style="font-size: 12px; font-weight: 700; color: #222831;">Thanh toán hóa đơn HD-08 thành công</div>
                        <div style="font-size: 11px; color: #6b7a70; margin-top: 2px;">Đã ghi nhận đặt cọc 30% (1.500.000 ₫) vào hệ thống thu chi.</div>
                        <small style="color: #9aa7a0; font-size: 10px;">2 giờ trước</small>
                    </div>
                </div>
            </div>

            <div style="background: #fafcfb; padding: 10px 16px; border-top: 1px solid #eef2ef; text-align: center;">
                <button type="button" onclick="readAllNotifs()" style="background: none; border: none; color: #557c56; font-size: 11px; font-weight: 700; cursor: pointer;">
                    <i class='bx bx-check-circle'></i> Đánh dấu tất cả là đã đọc
                </button>
            </div>
        </div>

        <div class="top-divider"></div>

        <!-- Khối hiển thị vai trò chức vụ -->
        <div>
            <c:choose>
                <c:when test="${sessionScope.currentRole.maVaiTro == 'ADMIN'}">
                    <span style="background: #e8eaf6; color: #283593; border: 1px solid #c5cae9; padding: 4px 12px; border-radius: 20px; font-size: 11px; font-weight: 700; display: inline-flex; align-items: center; gap: 5px;">
                        <i class='bx bx-crown'></i> Quản trị viên
                    </span>
                </c:when>
                <c:when test="${sessionScope.currentRole.maVaiTro == 'QUAN_LY'}">
                    <span style="background: #ede7f6; color: #4527a0; border: 1px solid #d1c4e9; padding: 4px 12px; border-radius: 20px; font-size: 11px; font-weight: 700; display: inline-flex; align-items: center; gap: 5px;">
                        <i class='bx bx-shield-quarter'></i> Quản lý
                    </span>
                </c:when>
                <c:when test="${sessionScope.currentRole.maVaiTro == 'LE_TAN'}">
                    <span style="background: #e3f2fd; color: #1565c0; border: 1px solid #bbdefb; padding: 4px 12px; border-radius: 20px; font-size: 11px; font-weight: 700; display: inline-flex; align-items: center; gap: 5px;">
                        <i class='bx bx-bell'></i> Lễ tân
                    </span>
                </c:when>
                <c:when test="${sessionScope.currentRole.maVaiTro == 'BUONG_PHONG'}">
                    <span style="background: #fff8e1; color: #f57f17; border: 1px solid #ffe082; padding: 4px 12px; border-radius: 20px; font-size: 11px; font-weight: 700; display: inline-flex; align-items: center; gap: 5px;">
                        <i class='bx bx-brush'></i> Buồng phòng
                    </span>
                </c:when>
                <c:when test="${sessionScope.currentRole.maVaiTro == 'KE_TOAN'}">
                    <span style="background: #e8f5e9; color: #2e7d32; border: 1px solid #c8e6c9; padding: 4px 12px; border-radius: 20px; font-size: 11px; font-weight: 700; display: inline-flex; align-items: center; gap: 5px;">
                        <i class='bx bx-calculator'></i> Kế toán
                    </span>
                </c:when>
                <c:when test="${sessionScope.currentRole.maVaiTro == 'BAO_TRI'}">
                    <span style="background: #ffebee; color: #c62828; border: 1px solid #ffcdd2; padding: 4px 12px; border-radius: 20px; font-size: 11px; font-weight: 700; display: inline-flex; align-items: center; gap: 5px;">
                        <i class='bx bx-wrench'></i> Kỹ thuật / Bảo trì
                    </span>
                </c:when>
                <c:otherwise>
                    <span style="background: #f0f4f2; color: #3d4a41; border: 1px solid #d4ddd7; padding: 4px 12px; border-radius: 20px; font-size: 11px; font-weight: 700;">
                        ${sessionScope.currentRole.tenVaiTro}
                    </span>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- Khối hồ sơ cá nhân hiển thị ĐÚNG Họ tên và Mã NV của tài khoản đăng nhập -->
        <div class="profile" title="Tài khoản cá nhân" style="display: flex; align-items: center; gap: 10px;">
            <div class="avatar"><i class='bx bxs-user'></i></div>
            <div class="profile-info">
                <b style="font-size: 12px; color: #222831; display: block; line-height: 1.2;">
                    ${not empty sessionScope.currentUser.hoTen ? sessionScope.currentUser.hoTen : sessionScope.currentNguoiDung.email}
                </b>
                <small style="font-size: 10.5px; color: #6b7a70;">
                    ${not empty sessionScope.currentUser.maNhanVien ? sessionScope.currentUser.maNhanVien : 'HỆ THỐNG'}
                </small>
            </div>
            
            <!-- Nút đổi mật khẩu -->
            <button type="button" onclick="openChangePwdModal()" title="Đổi mật khẩu tài khoản" class="icon-btn" style="width: 32px; height: 32px; border: none; background: transparent; cursor: pointer; color: #6b7a70; font-size: 18px; margin-left: 2px;">
                <i class='bx bx-key'></i>
            </button>

            <!-- Nút đăng xuất -->
            <a href="${pageContext.request.contextPath}/logout" title="Đăng xuất khỏi hệ thống" class="icon-btn" style="width: 32px; height: 32px; border: none; background: transparent; cursor: pointer; color: #c62828; font-size: 18px; text-decoration: none; display: flex; align-items: center; justify-content: center;">
                <i class='bx bx-power-off'></i>
            </a>
        </div>
    </div>
</header>

<!-- MODAL ĐỔI MẬT KHẨU -->
<div id="changePwdModal" style="display: none; position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.5); z-index: 2000; align-items: center; justify-content: center;">
    <div style="background: #ffffff; width: 90%; max-width: 400px; padding: 26px; border-radius: 14px; box-shadow: 0 10px 40px rgba(0,0,0,0.25); box-sizing: border-box;">
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px;">
            <h3 style="margin: 0; font-size: 16px; font-weight: 700; color: #222831; display: flex; align-items: center; gap: 8px;">
                <i class='bx bx-lock-alt' style="color: #557c56;"></i> Đổi Mật Khẩu Cá Nhân
            </h3>
            <span onclick="closeChangePwdModal()" style="cursor: pointer; font-size: 22px; color: #888;">&times;</span>
        </div>
        <form action="${pageContext.request.contextPath}/doi-mat-khau" method="post">
            <div style="margin-bottom: 14px;">
                <label style="display: block; font-size: 12px; font-weight: 700; color: #2b3a33; margin-bottom: 5px;">Mật khẩu hiện tại</label>
                <input type="password" name="matKhauCu" style="width: 100%; padding: 10px 12px; border: 1px solid #d4ddd7; border-radius: 8px; font-size: 13px; box-sizing: border-box;" placeholder="••••••••" required>
            </div>
            <div style="margin-bottom: 18px;">
                <label style="display: block; font-size: 12px; font-weight: 700; color: #2b3a33; margin-bottom: 5px;">Mật khẩu mới</label>
                <input type="password" name="matKhauMoi" style="width: 100%; padding: 10px 12px; border: 1px solid #d4ddd7; border-radius: 8px; font-size: 13px; box-sizing: border-box;" placeholder="••••••••" required>
            </div>
            <div style="display: flex; justify-content: flex-end; gap: 10px;">
                <button type="button" class="btn btn-ghost" onclick="closeChangePwdModal()">Hủy</button>
                <button type="submit" class="btn btn-primary"><i class='bx bx-check'></i> Cập Nhật</button>
            </div>
        </form>
    </div>
</div>

<script>
    function toggleNotifDropdown(e) {
        e.stopPropagation();
        const drop = document.getElementById('notifDropdown');
        if (drop.style.display === 'block') {
            drop.style.display = 'none';
        } else {
            drop.style.display = 'block';
        }
    }

    function closeNotifDropdown() {
        document.getElementById('notifDropdown').style.display = 'none';
    }

    function readAllNotifs() {
        const dot = document.getElementById('notifDot');
        if (dot) dot.style.display = 'none';
        closeNotifDropdown();
    }

    function openChangePwdModal() {
        document.getElementById('changePwdModal').style.display = 'flex';
    }

    function closeChangePwdModal() {
        document.getElementById('changePwdModal').style.display = 'none';
    }

    document.addEventListener('click', function(e) {
        const drop = document.getElementById('notifDropdown');
        const btn = document.getElementById('notifBtn');
        if (drop && drop.style.display === 'block' && !drop.contains(e.target) && !btn.contains(e.target)) {
            drop.style.display = 'none';
        }
    });
</script>
