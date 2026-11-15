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
        <button class="icon-btn" id="notifBtn" type="button" title="Xem thông báo hệ thống" onclick="toggleNotifDropdown(event)" style="position: relative; width: 36px; height: 36px; border-radius: 8px; border: 1px solid #dce5df; background: #ffffff; cursor: pointer; display: flex; align-items: center; justify-content: center; color: #49584e; font-size: 18px;">
            <i class='bx bx-bell'></i>
            <span id="notifDot" style="position: absolute; top: 7px; right: 7px; width: 8px; height: 8px; background: #e53935; border-radius: 50%; border: 2px solid #ffffff;"></span>
        </button>

        <!-- DROPDOWN DANH SÁCH THÔNG BÁO HỆ THỐNG (CĂN PHẢI CHUẨN XÁC) -->
        <div id="notifDropdown" style="display: none; position: absolute; top: 52px; right: 180px; width: 340px; background: #ffffff; border-radius: 12px; box-shadow: 0 10px 35px rgba(0,0,0,0.18); border: 1px solid #e0e6e2; z-index: 1050; overflow: hidden;">
            <div style="background: #222831; color: #ffffff; padding: 13px 16px; display: flex; justify-content: space-between; align-items: center;">
                <div style="font-weight: 700; font-size: 13px; display: flex; align-items: center; gap: 8px;">
                    <i class='bx bx-bell' style="color: #6d9886; font-size: 16px;"></i>
                    <span>Thông Báo Khách Sạn (04 mới)</span>
                </div>
                <span onclick="closeNotifDropdown()" style="cursor: pointer; font-size: 18px; color: #aeb8b3;">&times;</span>
            </div>

            <div style="max-height: 300px; overflow-y: auto;">
                <div style="padding: 12px 16px; border-bottom: 1px solid #f0f4f1; display: flex; gap: 12px; align-items: flex-start;">
                    <div style="width: 30px; height: 30px; border-radius: 50%; background: #fff8e1; color: #f57f17; display: flex; align-items: center; justify-content: center; flex-shrink: 0; font-size: 15px;">
                        <i class='bx bx-brush'></i>
                    </div>
                    <div>
                        <div style="font-size: 12px; font-weight: 700; color: #222831;">Phòng CL-0302 cần dọn phòng</div>
                        <div style="font-size: 11px; color: #6b7a70; margin-top: 2px;">Khách yêu cầu dọn phòng bổ sung đồ amenities.</div>
                        <small style="color: #9aa7a0; font-size: 10px;">10 phút trước</small>
                    </div>
                </div>

                <div style="padding: 12px 16px; border-bottom: 1px solid #f0f4f1; display: flex; gap: 12px; align-items: flex-start;">
                    <div style="width: 30px; height: 30px; border-radius: 50%; background: #e3f2fd; color: #1565c0; display: flex; align-items: center; justify-content: center; flex-shrink: 0; font-size: 15px;">
                        <i class='bx bx-user-check'></i>
                    </div>
                    <div>
                        <div style="font-size: 12px; font-weight: 700; color: #222831;">Khách check-in phòng WN-0501</div>
                        <div style="font-size: 11px; color: #6b7a70; margin-top: 2px;">Lễ tân đã hoàn tất thủ tục nhận phòng.</div>
                        <small style="color: #9aa7a0; font-size: 10px;">25 phút trước</small>
                    </div>
                </div>

                <div style="padding: 12px 16px; border-bottom: 1px solid #f0f4f1; display: flex; gap: 12px; align-items: flex-start;">
                    <div style="width: 30px; height: 30px; border-radius: 50%; background: #e8f5e9; color: #2e7d32; display: flex; align-items: center; justify-content: center; flex-shrink: 0; font-size: 15px;">
                        <i class='bx bx-check-double'></i>
                    </div>
                    <div>
                        <div style="font-size: 12px; font-weight: 700; color: #222831;">Hoàn tất bảo trì phòng CL-0104</div>
                        <div style="font-size: 11px; color: #6b7a70; margin-top: 2px;">Điều hòa đã sửa xong, phòng sẵn sàng.</div>
                        <small style="color: #9aa7a0; font-size: 10px;">1 giờ trước</small>
                    </div>
                </div>

                <div style="padding: 12px 16px; display: flex; gap: 12px; align-items: flex-start;">
                    <div style="width: 30px; height: 30px; border-radius: 50%; background: #f3e5f5; color: #7b1fa2; display: flex; align-items: center; justify-content: center; flex-shrink: 0; font-size: 15px;">
                        <i class='bx bx-receipt'></i>
                    </div>
                    <div>
                        <div style="font-size: 12px; font-weight: 700; color: #222831;">Thanh toán HD-08 thành công</div>
                        <div style="font-size: 11px; color: #6b7a70; margin-top: 2px;">Đã ghi nhận đặt cọc 30% vào hệ thống.</div>
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
                    <span style="background: #e8eaf6; color: #283593; border: 1px solid #c5cae9; padding: 4px 10px; border-radius: 16px; font-size: 11px; font-weight: 700; display: inline-flex; align-items: center; gap: 4px; white-space: nowrap;">
                        <i class='bx bx-crown'></i> Quản trị viên
                    </span>
                </c:when>
                <c:when test="${sessionScope.currentRole.maVaiTro == 'QUAN_LY'}">
                    <span style="background: #ede7f6; color: #4527a0; border: 1px solid #d1c4e9; padding: 4px 10px; border-radius: 16px; font-size: 11px; font-weight: 700; display: inline-flex; align-items: center; gap: 4px; white-space: nowrap;">
                        <i class='bx bx-shield-quarter'></i> Quản lý
                    </span>
                </c:when>
                <c:when test="${sessionScope.currentRole.maVaiTro == 'LE_TAN'}">
                    <span style="background: #e3f2fd; color: #1565c0; border: 1px solid #bbdefb; padding: 4px 10px; border-radius: 16px; font-size: 11px; font-weight: 700; display: inline-flex; align-items: center; gap: 4px; white-space: nowrap;">
                        <i class='bx bx-bell'></i> Lễ tân
                    </span>
                </c:when>
                <c:when test="${sessionScope.currentRole.maVaiTro == 'BUONG_PHONG'}">
                    <span style="background: #fff8e1; color: #f57f17; border: 1px solid #ffe082; padding: 4px 10px; border-radius: 16px; font-size: 11px; font-weight: 700; display: inline-flex; align-items: center; gap: 4px; white-space: nowrap;">
                        <i class='bx bx-brush'></i> Buồng phòng
                    </span>
                </c:when>
                <c:when test="${sessionScope.currentRole.maVaiTro == 'KE_TOAN'}">
                    <span style="background: #e8f5e9; color: #2e7d32; border: 1px solid #c8e6c9; padding: 4px 10px; border-radius: 16px; font-size: 11px; font-weight: 700; display: inline-flex; align-items: center; gap: 4px; white-space: nowrap;">
                        <i class='bx bx-calculator'></i> Kế toán
                    </span>
                </c:when>
                <c:when test="${sessionScope.currentRole.maVaiTro == 'BAO_TRI'}">
                    <span style="background: #ffebee; color: #c62828; border: 1px solid #ffcdd2; padding: 4px 10px; border-radius: 16px; font-size: 11px; font-weight: 700; display: inline-flex; align-items: center; gap: 4px; white-space: nowrap;">
                        <i class='bx bx-wrench'></i> Bảo trì
                    </span>
                </c:when>
                <c:otherwise>
                    <span style="background: #f0f4f2; color: #3d4a41; border: 1px solid #d4ddd7; padding: 4px 10px; border-radius: 16px; font-size: 11px; font-weight: 700; white-space: nowrap;">
                        ${sessionScope.currentRole.tenVaiTro}
                    </span>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- Khối hồ sơ cá nhân Pill Button: Đồng bộ, căn đều, không bị tràn màn hình -->
        <div class="profile-pill" onclick="toggleProfileDropdown(event)" title="Nhấp để xem và sửa thông tin tài khoản">
            <div class="avatar"><i class='bx bxs-user'></i></div>
            <div class="profile-info">
                <b>
                    ${not empty sessionScope.currentUser.hoTen ? sessionScope.currentUser.hoTen : sessionScope.currentNguoiDung.email}
                </b>
                <small>
                    ${not empty sessionScope.currentUser.maNhanVien ? sessionScope.currentUser.maNhanVien : 'HỆ THỐNG'}
                </small>
            </div>
            <i class='bx bx-chevron-down profile-arrow' id="profileArrow"></i>
        </div>

        <!-- DROPDOWN MENU HỒ SƠ TÀI KHOẢN -->
        <div id="profileDropdown" class="profile-dropdown-menu" style="display: none;">
            <div class="p-dropdown-header">
                <div class="pdh-name">${not empty sessionScope.currentUser.hoTen ? sessionScope.currentUser.hoTen : 'Quản trị viên'}</div>
                <div class="pdh-email">${sessionScope.currentNguoiDung.email}</div>
                <div class="pdh-role">${sessionScope.currentRole.tenVaiTro} · ${not empty sessionScope.currentUser.maNhanVien ? sessionScope.currentUser.maNhanVien : 'ADMIN'}</div>
            </div>
            <div class="p-dropdown-divider"></div>
            <a href="javascript:void(0)" onclick="openEditProfileModal()" class="p-dropdown-item">
                <i class='bx bx-user-circle'></i> Xem &amp; Sửa Thông Tin Cá Nhân
            </a>
            <a href="javascript:void(0)" onclick="openChangePwdModal()" class="p-dropdown-item">
                <i class='bx bx-key'></i> Đổi Mật Khẩu Cá Nhân
            </a>
            <a href="javascript:void(0)" onclick="openChangeEmailModal()" class="p-dropdown-item">
                <i class='bx bx-envelope'></i> Đổi Email Đăng Nhập
            </a>
            <div class="p-dropdown-divider"></div>
            <a href="${pageContext.request.contextPath}/logout" class="p-dropdown-item text-danger">
                <i class='bx bx-log-out'></i> Đăng Xuất Hệ Thống
            </a>
        </div>
    </div>
</header>

<!-- ========================================================================= -->
<!-- MODAL 1: XEM VÀ SỬA THÔNG TIN TÀI KHOẢN CÁ NHÂN (YÊU CẦU CỦA USER)        -->
<!-- ========================================================================= -->
<div id="editProfileModal" class="modal-wrap" style="display: none; position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.5); z-index: 2000; align-items: center; justify-content: center;">
    <div style="background: #ffffff; width: 90%; max-width: 500px; padding: 26px; border-radius: 14px; box-shadow: 0 10px 40px rgba(0,0,0,0.25); box-sizing: border-box;">
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; border-bottom: 1px solid #edf2ef; padding-bottom: 12px;">
            <h3 style="margin: 0; font-size: 16px; font-weight: 700; color: #222831; display: flex; align-items: center; gap: 8px;">
                <i class='bx bx-user-pin' style="color: #557c56; font-size: 22px;"></i> Xem &amp; Sửa Thông Tin Tài Khoản
            </h3>
            <span onclick="closeEditProfileModal()" style="cursor: pointer; font-size: 22px; color: #888;">&times;</span>
        </div>

        <form action="${pageContext.request.contextPath}/sua-thong-tin" method="post">
            <!-- Thông tin định danh chỉ xem -->
            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 10px; margin-bottom: 16px; background: #f8faf9; padding: 12px 14px; border-radius: 10px; border: 1px solid #e2ebe5;">
                <div>
                    <label style="font-size: 11px; color: #6b7a70; font-weight: 600; display: block; margin-bottom: 2px;">MÃ TÀI KHOẢN</label>
                    <b style="font-size: 13px; color: #222831;">${not empty sessionScope.currentUser.maNhanVien ? sessionScope.currentUser.maNhanVien : 'ADMIN'}</b>
                </div>
                <div>
                    <label style="font-size: 11px; color: #6b7a70; font-weight: 600; display: block; margin-bottom: 2px;">VAI TRÒ / CHỨC VỤ</label>
                    <span style="color: #2e7d32; font-weight: 700; font-size: 12px;">
                        ${sessionScope.currentRole.tenVaiTro}
                    </span>
                </div>
                <div>
                    <label style="font-size: 11px; color: #6b7a70; font-weight: 600; display: block; margin-bottom: 2px;">NGÀY VÀO LÀM</label>
                    <span style="font-size: 12px; color: #222831;">
                        ${not empty sessionScope.currentUser.ngayVaoLam ? sessionScope.currentUser.ngayVaoLam : 'Chưa cập nhật'}
                    </span>
                </div>
                <div>
                    <label style="font-size: 11px; color: #6b7a70; font-weight: 600; display: block; margin-bottom: 2px;">TRẠNG THÁI</label>
                    <span style="color: #2e7d32; font-size: 12px; font-weight: 700;">● Hoạt động</span>
                </div>
            </div>

            <!-- Các trường có thể chỉnh sửa -->
            <div style="margin-bottom: 12px;">
                <label style="display: block; font-size: 12px; font-weight: 700; color: #2b3a33; margin-bottom: 4px;">
                    Họ và tên <span style="color: red;">*</span>
                </label>
                <input type="text" name="hoTen" style="width: 100%; padding: 10px 12px; border: 1px solid #d4ddd7; border-radius: 8px; font-size: 13px; box-sizing: border-box;" 
                       value="${sessionScope.currentUser.hoTen}" required>
            </div>

            <div style="margin-bottom: 12px;">
                <label style="display: block; font-size: 12px; font-weight: 700; color: #2b3a33; margin-bottom: 4px;">
                    Số điện thoại liên hệ <span style="color: red;">*</span>
                </label>
                <input type="tel" name="soDienThoai" style="width: 100%; padding: 10px 12px; border: 1px solid #d4ddd7; border-radius: 8px; font-size: 13px; box-sizing: border-box;" 
                       value="${sessionScope.currentUser.soDienThoai}" required>
            </div>

            <div style="margin-bottom: 18px;">
                <label style="display: block; font-size: 12px; font-weight: 700; color: #2b3a33; margin-bottom: 4px;">
                    Email tài khoản đăng nhập <span style="color: red;">*</span>
                </label>
                <input type="email" name="email" style="width: 100%; padding: 10px 12px; border: 1px solid #d4ddd7; border-radius: 8px; font-size: 13px; box-sizing: border-box;" 
                       value="${sessionScope.currentNguoiDung.email}" required>
            </div>

            <div style="display: flex; justify-content: flex-end; gap: 10px; border-top: 1px solid #edf2ef; padding-top: 14px;">
                <button type="button" class="btn btn-ghost" onclick="closeEditProfileModal()">Hủy</button>
                <button type="submit" class="btn btn-primary" style="padding: 10px 18px; font-size: 13px; font-weight: 700;">
                    <i class='bx bx-save'></i> Lưu Thông Tin
                </button>
            </div>
        </form>
    </div>
</div>

<!-- ========================================================================= -->
<!-- MODAL 2: ĐỔI MẬT KHẨU CÁ NHÂN                                              -->
<!-- ========================================================================= -->
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

<!-- ========================================================================= -->
<!-- MODAL 3: THAY ĐỔI EMAIL TÀI KHOẢN                                          -->
<!-- ========================================================================= -->
<div id="changeEmailModal" style="display: none; position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.5); z-index: 2000; align-items: center; justify-content: center;">
    <div style="background: #ffffff; width: 90%; max-width: 420px; padding: 26px; border-radius: 14px; box-shadow: 0 10px 40px rgba(0,0,0,0.25); box-sizing: border-box;">
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px;">
            <h3 style="margin: 0; font-size: 16px; font-weight: 700; color: #222831; display: flex; align-items: center; gap: 8px;">
                <i class='bx bx-envelope' style="color: #557c56;"></i> Thay Đổi Email Tài Khoản
            </h3>
            <span onclick="closeChangeEmailModal()" style="cursor: pointer; font-size: 22px; color: #888;">&times;</span>
        </div>
        <form action="${pageContext.request.contextPath}/doi-email" method="post">
            <div style="margin-bottom: 12px;">
                <label style="display: block; font-size: 12px; font-weight: 700; color: #2b3a33; margin-bottom: 5px;">Email hiện tại</label>
                <input type="text" readonly style="width: 100%; padding: 10px 12px; border: 1px solid #e0e6e2; background: #f7faf8; border-radius: 8px; font-size: 13px; color: #6b7a70; box-sizing: border-box;" value="${sessionScope.currentNguoiDung.email}">
            </div>
            <div style="margin-bottom: 14px;">
                <label style="display: block; font-size: 12px; font-weight: 700; color: #2b3a33; margin-bottom: 5px;">Email mới <span style="color: red;">*</span></label>
                <input type="email" name="emailMoi" style="width: 100%; padding: 10px 12px; border: 1px solid #d4ddd7; border-radius: 8px; font-size: 13px; box-sizing: border-box;" placeholder="VD: email.moi@theroyal.com" required>
            </div>
            <div style="margin-bottom: 18px;">
                <label style="display: block; font-size: 12px; font-weight: 700; color: #2b3a33; margin-bottom: 5px;">Xác nhận mật khẩu hiện tại <span style="color: red;">*</span></label>
                <input type="password" name="matKhauXacNhan" style="width: 100%; padding: 10px 12px; border: 1px solid #d4ddd7; border-radius: 8px; font-size: 13px; box-sizing: border-box;" placeholder="••••••••" required>
            </div>
            <div style="display: flex; justify-content: flex-end; gap: 10px;">
                <button type="button" class="btn btn-ghost" onclick="closeChangeEmailModal()">Hủy</button>
                <button type="submit" class="btn btn-primary"><i class='bx bx-check'></i> Cập Nhật Email</button>
            </div>
        </form>
    </div>
</div>

<script>
    function toggleNotifDropdown(e) {
        e.stopPropagation();
        closeProfileDropdown();
        const drop = document.getElementById('notifDropdown');
        drop.style.display = (drop.style.display === 'block') ? 'none' : 'block';
    }

    function closeNotifDropdown() {
        const drop = document.getElementById('notifDropdown');
        if (drop) drop.style.display = 'none';
    }

    function readAllNotifs() {
        const dot = document.getElementById('notifDot');
        if (dot) dot.style.display = 'none';
        closeNotifDropdown();
    }

    function toggleProfileDropdown(e) {
        e.stopPropagation();
        closeNotifDropdown();
        const drop = document.getElementById('profileDropdown');
        const arrow = document.getElementById('profileArrow');
        if (drop.style.display === 'block') {
            drop.style.display = 'none';
            if (arrow) arrow.style.transform = 'rotate(0deg)';
        } else {
            drop.style.display = 'block';
            if (arrow) arrow.style.transform = 'rotate(180deg)';
        }
    }

    function closeProfileDropdown() {
        const drop = document.getElementById('profileDropdown');
        const arrow = document.getElementById('profileArrow');
        if (drop) drop.style.display = 'none';
        if (arrow) arrow.style.transform = 'rotate(0deg)';
    }

    function openEditProfileModal() {
        closeProfileDropdown();
        document.getElementById('editProfileModal').style.display = 'flex';
    }

    function closeEditProfileModal() {
        document.getElementById('editProfileModal').style.display = 'none';
    }

    function openChangePwdModal() {
        closeProfileDropdown();
        document.getElementById('changePwdModal').style.display = 'flex';
    }

    function closeChangePwdModal() {
        document.getElementById('changePwdModal').style.display = 'none';
    }

    function openChangeEmailModal() {
        closeProfileDropdown();
        document.getElementById('changeEmailModal').style.display = 'flex';
    }

    function closeChangeEmailModal() {
        document.getElementById('changeEmailModal').style.display = 'none';
    }

    document.addEventListener('click', function(e) {
        const notifDrop = document.getElementById('notifDropdown');
        const notifBtn = document.getElementById('notifBtn');
        if (notifDrop && notifDrop.style.display === 'block' && !notifDrop.contains(e.target) && !notifBtn.contains(e.target)) {
            notifDrop.style.display = 'none';
        }

        const profDrop = document.getElementById('profileDropdown');
        const profBtn = document.querySelector('.profile-pill');
        if (profDrop && profDrop.style.display === 'block' && !profDrop.contains(e.target) && !profBtn.contains(e.target)) {
            closeProfileDropdown();
        }
    });
</script>
