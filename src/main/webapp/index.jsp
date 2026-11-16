<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng Nhập Hệ Thống — The Royal Hotel</title>

    <!-- CSS phân tầng của The Royal Hotel -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
    <!-- Boxicons -->
    <link href="https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/boxicons@2.1.4/css/boxicons.min.css" rel="stylesheet">

    <style>
        body {
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            background: linear-gradient(135deg, #1b2420 0%, #222831 50%, #1a221e 100%);
            font-family: 'DM Sans', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
            margin: 0;
            padding: 20px;
            box-sizing: border-box;
            position: relative;
            overflow-x: hidden;
        }

        body::before {
            content: '';
            position: absolute;
            top: 0; left: 0; right: 0; bottom: 0;
            background: radial-gradient(circle at 80% 20%, rgba(205, 164, 94, 0.15) 0%, transparent 50%),
                        radial-gradient(circle at 20% 80%, rgba(85, 124, 86, 0.2) 0%, transparent 60%);
            pointer-events: none;
        }

        .login-card {
            background: #ffffff;
            border-radius: 20px;
            box-shadow: 0 20px 60px rgba(0, 0, 0, 0.35);
            width: 100%;
            max-width: 450px;
            padding: 40px 36px;
            box-sizing: border-box;
            position: relative;
            z-index: 10;
            border: 1px solid rgba(226, 210, 186, 0.4);
        }

        .login-brand {
            text-align: center;
            margin-bottom: 24px;
        }

        .login-logo {
            width: 72px;
            height: 72px;
            border-radius: 50%;
            background: #EEEEEE;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            margin-bottom: 12px;
            border: 2px solid #557c56;
            box-shadow: 0 6px 16px rgba(85, 124, 86, 0.2);
        }

        .login-logo img {
            width: 46px;
            height: 46px;
            object-fit: contain;
        }

        .hotel-name {
            font-family: 'Cinzel', 'Playfair Display', Georgia, serif;
            font-size: 24px;
            font-weight: 800;
            color: #222831;
            letter-spacing: 2px;
            margin: 0;
        }

        .hotel-sub {
            font-size: 11px;
            font-weight: 700;
            color: #557c56;
            letter-spacing: 3px;
            margin-top: 4px;
            text-transform: uppercase;
        }

        .form-label {
            display: block;
            font-size: 13px;
            font-weight: 700;
            color: #2b3a33;
            margin-bottom: 6px;
        }

        .input-group {
            position: relative;
            margin-bottom: 18px;
        }

        .input-group i {
            position: absolute;
            left: 14px;
            top: 38px;
            font-size: 18px;
            color: #8b9990;
        }

        .form-input {
            width: 100%;
            padding: 12px 14px 12px 42px;
            border: 1px solid #d4ddd7;
            border-radius: 10px;
            font-size: 14px;
            color: #222831;
            box-sizing: border-box;
            outline: none;
            transition: all 0.25s ease;
            background: #fafcfb;
        }

        .form-input:focus {
            border-color: #557c56;
            background: #fff;
            box-shadow: 0 0 0 4px rgba(85, 124, 86, 0.15);
        }

        .btn-submit {
            width: 100%;
            padding: 13px;
            background: #557c56;
            color: #ffffff;
            border: none;
            border-radius: 10px;
            font-size: 14px;
            font-weight: 700;
            cursor: pointer;
            transition: all 0.25s ease;
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 8px;
            box-shadow: 0 4px 14px rgba(85, 124, 86, 0.3);
            margin-top: 6px;
        }

        .btn-submit:hover {
            background: #426343;
            transform: translateY(-2px);
            box-shadow: 0 6px 18px rgba(85, 124, 86, 0.4);
        }

        .alert-box {
            padding: 11px 14px;
            border-radius: 8px;
            font-size: 13px;
            margin-bottom: 18px;
            display: flex;
            align-items: center;
            gap: 8px;
            line-height: 1.4;
        }
        .alert-error { background: #ffebee; color: #c62828; border: 1px solid #ffcdd2; }
        .alert-success { background: #e8f5e9; color: #2e7d32; border: 1px solid #c8e6c9; }
        .alert-info { background: #e3f2fd; color: #1565c0; border: 1px solid #bbdefb; }

        /* Modal popup */
        .modal-wrap {
            display: none;
            position: fixed;
            top: 0; left: 0; width: 100%; height: 100%;
            background: rgba(0,0,0,0.6);
            z-index: 1000;
            align-items: center;
            justify-content: center;
        }
        .modal-body-custom {
            background: #fff;
            padding: 28px;
            border-radius: 16px;
            width: 90%;
            max-width: 440px;
            box-shadow: 0 10px 40px rgba(0,0,0,0.3);
            box-sizing: border-box;
            position: relative;
        }

    </style>
</head>

<body>
    <div class="login-card">
        <div class="login-brand">
            <div class="login-logo">
                <img src="${pageContext.request.contextPath}/assets/logo-royal.png" alt="The Royal Hotel">
            </div>
            <h1 class="hotel-name">THE ROYAL</h1>
            <div class="hotel-sub">HOTEL MANAGEMENT SYSTEM</div>
        </div>

        <!-- THÔNG BÁO HỆ THỐNG -->
        <c:if test="${param.error == 'not_found'}">
            <div class="alert-box alert-error"><i class='bx bx-error-circle'></i> Tài khoản không tồn tại trên hệ thống!</div>
        </c:if>
        <c:if test="${param.error == 'wrong_password'}">
            <div class="alert-box alert-error"><i class='bx bx-error'></i> Mật khẩu không chính xác!</div>
        </c:if>
        <c:if test="${param.error == 'account_locked'}">
            <div class="alert-box alert-error"><i class='bx bx-lock-alt'></i> Tài khoản này đang bị tạm khóa!</div>
        </c:if>
        <c:if test="${param.error == 'empty_fields'}">
            <div class="alert-box alert-error"><i class='bx bx-error'></i> Vui lòng điền đầy đủ tài khoản và mật khẩu!</div>
        </c:if>
        <c:if test="${param.error == 'email_exists'}">
            <div class="alert-box alert-error"><i class='bx bx-error'></i> Email này đã được đăng ký trước đó!</div>
        </c:if>
        <c:if test="${param.error == 'phone_exists'}">
            <div class="alert-box alert-error"><i class='bx bx-error'></i> Số điện thoại này đã có tài khoản khách hàng!</div>
        </c:if>
        <c:if test="${param.msg == 'require_login'}">
            <div class="alert-box alert-error"><i class='bx bx-log-in-circle'></i> Vui lòng đăng nhập để truy cập hệ thống!</div>
        </c:if>
        <c:if test="${param.msg == 'logged_out'}">
            <div class="alert-box alert-success"><i class='bx bx-check-circle'></i> Bạn đã đăng xuất thành công khỏi hệ thống!</div>
        </c:if>
        <c:if test="${param.msg == 'reset_success'}">
            <div class="alert-box alert-success"><i class='bx bx-check-circle'></i> <b>Đổi mật khẩu thành công!</b> Bạn có thể đăng nhập bằng mật khẩu mới.</div>
        </c:if>
        <c:if test="${param.msg == 'register_customer_success'}">
            <div class="alert-box alert-success"><i class='bx bx-check-circle'></i> Đăng ký tài khoản khách hàng thành công! Bạn có thể đăng nhập ngay.</div>
        </c:if>

        <!-- FORM ĐĂNG NHẬP CHÍNH THỨC: HỖ TRỢ EMAIL HOẶC SỐ ĐIỆN THOẠI HOẶC MÃ NV -->
        <form action="${pageContext.request.contextPath}/login" method="post" id="loginForm">
            <div class="input-group">
                <label class="form-label" for="taiKhoan">Email hoặc Số điện thoại</label>
                <i class='bx bx-user'></i>
                <input type="text" id="taiKhoan" name="taiKhoan" class="form-input"
                       placeholder="Email, Số điện thoại hoặc Mã NV" required autofocus>
            </div>

            <div class="input-group">
                <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 6px;">
                    <label class="form-label" for="password" style="margin-bottom: 0;">Mật Khẩu</label>
                    <a href="javascript:void(0)" onclick="openModal('forgotStep1Modal')" style="font-size: 12px; color: #557c56; text-decoration: none; font-weight: 600;">Quên mật khẩu?</a>
                </div>
                <i class='bx bx-lock-alt' style="top: 38px;"></i>
                <input type="password" id="password" name="password" class="form-input" placeholder="••••••••" required>
            </div>

            <button type="submit" class="btn-submit">
                <i class='bx bx-log-in'></i> Đăng Nhập Vào Hệ Thống
            </button>

            <!-- ĐĂNG KÝ TÀI KHOẢN CHỈ CHO KHÁCH HÀNG (TẠM ĐÓNG VÌ CHƯA CÓ LUỒNG KHÁCH HÀNG) -->
            <%--
            <div style="text-align: center; margin-top: 18px; font-size: 13px; color: #6b7a70;">
                Chưa có tài khoản khách hàng?
                <a href="javascript:void(0)" onclick="openModal('registerCustomerModal')" style="color: #557c56; font-weight: 700; text-decoration: none;">Đăng ký ngay</a>
            </div>
            --%>
        </form>

    </div>

    <!-- ===================================================================== -->
    <!-- MODAL ĐĂNG KÝ TÀI KHOẢN: CHỈ DÀNH CHO KHÁCH HÀNG (LÀM SAU)             -->
    <!-- (Tài khoản nhân viên do Quản lý và Admin thêm trong hệ thống)          -->
    <!-- ===================================================================== -->
    <%--
    <div class="modal-wrap" id="registerCustomerModal">
        <div class="modal-body-custom">
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 14px;">
                <h3 style="margin: 0; font-size: 16px; font-weight: 700; color: #222831;">
                    <i class='bx bx-user-plus' style="color: #557c56;"></i> Đăng Ký Tài Khoản Khách Hàng
                </h3>
                <span onclick="closeModal('registerCustomerModal')" style="cursor: pointer; font-size: 22px; color: #888;">&times;</span>
            </div>

            <form action="${pageContext.request.contextPath}/dang-ky" method="post">
                <div style="margin-bottom: 12px;">
                    <label class="form-label">Họ và tên khách hàng <span style="color:red">*</span></label>
                    <input type="text" name="hoTen" class="form-input" style="padding-left: 14px;" placeholder="VD: Nguyễn Văn An" required>
                </div>
                <div style="margin-bottom: 12px;">
                    <label class="form-label">Số điện thoại liên hệ <span style="color:red">*</span></label>
                    <input type="tel" name="soDienThoai" class="form-input" style="padding-left: 14px;" placeholder="VD: 0912001001" required>
                </div>
                <div style="margin-bottom: 12px;">
                    <label class="form-label">Email tài khoản <span style="color:red">*</span></label>
                    <input type="email" name="email" class="form-input" style="padding-left: 14px;" placeholder="an.nguyen@gmail.com" required>
                </div>
                <div style="margin-bottom: 18px;">
                    <label class="form-label">Mật khẩu khởi tạo <span style="color:red">*</span></label>
                    <input type="password" name="matKhau" class="form-input" style="padding-left: 14px;" placeholder="••••••••" required>
                </div>
                <button type="submit" class="btn-submit"><i class='bx bx-check'></i> Xác Nhận Đăng Ký Khách Hàng</button>
            </form>
        </div>
    </div>
    --%>

    <!-- ===================================================================== -->
    <!-- QUY TRÌNH QUÊN MẬT KHẨU THEO CHUẨN SƠ ĐỒ HOẠT ĐỘNG (HÌNH 3)           -->
    <!-- BƯỚC 1: NHẬP EMAIL & KIỂM TRA TỒN TẠI                                -->
    <!-- ===================================================================== -->
    <div class="modal-wrap" id="forgotStep1Modal">
        <div class="modal-body-custom">
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px;">
                <h3 style="margin: 0; font-size: 16px; font-weight: 700; color: #222831;">
                    <i class='bx bx-mail-send' style="color: #557c56;"></i> Quên Mật Khẩu (Bước 1/3)
                </h3>
                <span onclick="closeModal('forgotStep1Modal')" style="cursor: pointer; font-size: 22px; color: #888;">&times;</span>
            </div>

            <p style="font-size: 13px; color: #6b7a70; margin-bottom: 14px; line-height: 1.5;">
                Vui lòng nhập địa chỉ Email đăng ký tài khoản. Hệ thống sẽ kiểm tra và gửi mã xác nhận OTP 6 số đến hòm thư của bạn.
            </p>

            <c:if test="${param.error == 'email_not_found'}">
                <div class="alert-box alert-error" style="margin-bottom: 14px;">
                    <i class='bx bx-error-circle'></i> Email [<b>${param.inputEmail}</b>] không tồn tại trên hệ thống! Vui lòng thử lại.
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/quen-mat-khau" method="post">
                <div style="margin-bottom: 18px;">
                    <label class="form-label">Email tài khoản</label>
                    <input type="email" name="email" class="form-input" style="padding-left: 14px;"
                           placeholder="name@theroyal.com hoặc user@gmail.com"
                           value="${param.inputEmail != null ? param.inputEmail : ''}" required>
                </div>
                <button type="submit" class="btn-submit">
                    <i class='bx bx-send'></i> Gửi Mã Xác Nhận Qua Email
                </button>
            </form>
        </div>
    </div>

    <!-- ===================================================================== -->
    <!-- BƯỚC 2: NHẬP MÃ XÁC NHẬN GỬI VỀ EMAIL                                 -->
    <!-- ===================================================================== -->
    <div class="modal-wrap" id="forgotStep2Modal">
        <div class="modal-body-custom">
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 14px;">
                <h3 style="margin: 0; font-size: 16px; font-weight: 700; color: #222831;">
                    <i class='bx bx-check-shield' style="color: #557c56;"></i> Nhập Mã Xác Nhận (Bước 2/3)
                </h3>
                <span onclick="closeModal('forgotStep2Modal')" style="cursor: pointer; font-size: 22px; color: #888;">&times;</span>
            </div>

            <p style="font-size: 13px; color: #6b7a70; margin-bottom: 12px; line-height: 1.5;">
                Hệ thống đã gửi mã xác nhận 6 số đến email: <br>
                <b style="color: #222831;">${param.email}</b>
            </p>

            <!-- Thông báo khi gửi email thật qua Gmail thành công -->
            <c:if test="${param.sentRealEmail == 'true'}">
                <div class="alert-box alert-success" style="margin-bottom: 14px;">
                    <i class='bx bx-check-circle'></i> Mã xác thực đã được gửi đến hộp thư Gmail của bạn. Vui lòng kiểm tra Hộp thư đến (hoặc thư mục Spam / Rác)!
                </div>
            </c:if>

            <!-- Hộp mã xác nhận OTP (hiển thị khi chưa cấu hình mật khẩu ứng dụng Gmail) -->
            <c:if test="${not empty param.otpDemo}">
                <div class="alert-box alert-info" style="margin-bottom: 14px; display: block; text-align: center;">
                    <div style="font-size: 11px; text-transform: uppercase; letter-spacing: 1px; margin-bottom: 4px;">
                        <i class='bx bx-envelope'></i> Mã xác nhận (Chế độ mô phỏng khi chưa nhập App Password):
                    </div>
                    <div style="font-size: 22px; font-weight: 800; letter-spacing: 6px; color: #0d47a1;">${param.otpDemo}</div>
                    <div style="font-size: 10.5px; color: #546e7a; margin-top: 4px;">(Mã có hiệu lực trong 10 phút)</div>
                </div>
            </c:if>

            <c:if test="${param.error == 'wrong_otp'}">
                <div class="alert-box alert-error" style="margin-bottom: 14px;">
                    <i class='bx bx-error-circle'></i> Mã xác nhận không chính xác! Vui lòng nhập lại.
                </div>
            </c:if>
            <c:if test="${param.error == 'otp_expired'}">
                <div class="alert-box alert-error" style="margin-bottom: 14px;">
                    <i class='bx bx-time-five'></i> Mã xác nhận đã hết hiệu lực! Vui lòng yêu cầu mã mới.
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/xac-nhan-ma" method="post">
                <div style="margin-bottom: 18px;">
                    <label class="form-label">Nhập mã xác nhận (OTP 6 số)</label>
                    <input type="text" name="maXacNhan" class="form-input"
                           style="padding-left: 14px; text-align: center; font-size: 18px; letter-spacing: 4px; font-weight: 700;"
                           placeholder="••••••" maxlength="6" required autofocus>
                </div>
                <button type="submit" class="btn-submit">
                    <i class='bx bx-check-circle'></i> Xác Nhận Mã
                </button>
            </form>

            <div style="display: flex; justify-content: space-between; align-items: center; margin-top: 14px; font-size: 12px;">
                <a href="${pageContext.request.contextPath}/index.jsp?forgotStep=1&inputEmail=${param.email}" style="color: #6b7a70; text-decoration: none;">
                    <i class='bx bx-left-arrow-alt'></i> Đổi email khác
                </a>
                <form action="${pageContext.request.contextPath}/quen-mat-khau" method="post" style="margin: 0;">
                    <input type="hidden" name="email" value="${param.email}">
                    <button type="submit" style="background: none; border: none; color: #557c56; font-weight: 700; cursor: pointer; padding: 0;">
                        <i class='bx bx-refresh'></i> Gửi lại mã
                    </button>
                </form>
            </div>
        </div>
    </div>

    <!-- ===================================================================== -->
    <!-- BƯỚC 3: ĐẶT MẬT KHẨU MỚI & CẬP NHẬT DATABASE                          -->
    <!-- ===================================================================== -->
    <div class="modal-wrap" id="forgotStep3Modal">
        <div class="modal-body-custom">
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 14px;">
                <h3 style="margin: 0; font-size: 16px; font-weight: 700; color: #222831;">
                    <i class='bx bx-lock-open-alt' style="color: #557c56;"></i> Đặt Mật Khẩu Mới (Bước 3/3)
                </h3>
                <span onclick="closeModal('forgotStep3Modal')" style="cursor: pointer; font-size: 22px; color: #888;">&times;</span>
            </div>

            <p style="font-size: 13px; color: #6b7a70; margin-bottom: 14px; line-height: 1.5;">
                Mã xác nhận hợp lệ. Vui lòng thiết lập mật khẩu mới cho tài khoản: <br>
                <b style="color: #222831;">${param.email}</b>
            </p>

            <c:if test="${param.error == 'pwd_not_match'}">
                <div class="alert-box alert-error" style="margin-bottom: 14px;">
                    <i class='bx bx-error-circle'></i> Mật khẩu xác nhận không khớp!
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/dat-lai-mat-khau" method="post">
                <div style="margin-bottom: 12px;">
                    <label class="form-label">Mật khẩu mới</label>
                    <input type="password" name="matKhauMoi" class="form-input" style="padding-left: 14px;" placeholder="••••••••" required autofocus>
                </div>
                <div style="margin-bottom: 18px;">
                    <label class="form-label">Xác nhận mật khẩu mới</label>
                    <input type="password" name="xacNhanMatKhau" class="form-input" style="padding-left: 14px;" placeholder="••••••••" required>
                </div>
                <button type="submit" class="btn-submit">
                    <i class='bx bx-save'></i> Cập Nhật Mật Khẩu Mới
                </button>
            </form>
        </div>
    </div>

    <script>
        function openModal(id) {
            const m = document.getElementById(id);
            if (m) m.style.display = 'flex';
        }

        function closeModal(id) {
            const m = document.getElementById(id);
            if (m) m.style.display = 'none';
        }

        window.onclick = function(event) {
            if (event.target.classList.contains('modal-wrap')) {
                event.target.style.display = 'none';
            }
        };

        // Tự động mở đúng modal khi có tham số bước Quên Mật Khẩu từ URL
        window.onload = function() {
            const urlParams = new URLSearchParams(window.location.search);
            const forgotStep = urlParams.get('forgotStep');
            const modal = urlParams.get('modal');

            if (forgotStep === '1') {
                openModal('forgotStep1Modal');
            } else if (forgotStep === '2') {
                openModal('forgotStep2Modal');
            } else if (forgotStep === '3') {
                openModal('forgotStep3Modal');
            } else if (modal === 'register') {
                openModal('registerCustomerModal');
            }
        };
    </script>
</body>
</html>
