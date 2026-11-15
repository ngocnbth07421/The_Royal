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
            max-width: 440px;
            padding: 40px 36px;
            box-sizing: border-box;
            position: relative;
            z-index: 10;
            border: 1px solid rgba(226, 210, 186, 0.4);
        }

        .login-brand {
            text-align: center;
            margin-bottom: 28px;
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
        }
        .alert-error { background: #ffebee; color: #c62828; border: 1px solid #ffcdd2; }
        .alert-success { background: #e8f5e9; color: #2e7d32; border: 1px solid #c8e6c9; }

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
            border-radius: 14px;
            width: 90%;
            max-width: 420px;
            box-shadow: 0 10px 40px rgba(0,0,0,0.3);
            box-sizing: border-box;
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

        <!-- Thông báo phản hồi hệ thống -->
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
        <c:if test="${param.msg == 'require_login'}">
            <div class="alert-box alert-error"><i class='bx bx-log-in-circle'></i> Vui lòng đăng nhập để truy cập hệ thống!</div>
        </c:if>
        <c:if test="${param.msg == 'logged_out'}">
            <div class="alert-box alert-success"><i class='bx bx-check-circle'></i> Bạn đã đăng xuất thành công khỏi hệ thống!</div>
        </c:if>
        <c:if test="${param.msg == 'register_success'}">
            <div class="alert-box alert-success"><i class='bx bx-check-circle'></i> Đăng ký thành công! Bạn có thể đăng nhập ngay.</div>
        </c:if>
        <c:if test="${param.msg == 'pwd_reset_success'}">
            <div class="alert-box alert-success"><i class='bx bx-check-circle'></i> Mật khẩu đã được reset về mặc định: <b>123456</b></div>
        </c:if>

        <!-- Form Đăng Nhập Chính Thức -->
        <form action="${pageContext.request.contextPath}/login" method="post" id="loginForm">
            <div class="input-group">
                <label class="form-label" for="taiKhoan">Email hoặc Mã Nhân Viên</label>
                <i class='bx bx-user'></i>
                <input type="text" id="taiKhoan" name="taiKhoan" class="form-input" placeholder="VD: lananh.tran@theroyal.com hoặc NV002" required autofocus>
            </div>

            <div class="input-group">
                <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 6px;">
                    <label class="form-label" for="password" style="margin-bottom: 0;">Mật Khẩu</label>
                    <a href="javascript:void(0)" onclick="openModal('forgotModal')" style="font-size: 12px; color: #557c56; text-decoration: none; font-weight: 600;">Quên mật khẩu?</a>
                </div>
                <i class='bx bx-lock-alt' style="top: 38px;"></i>
                <input type="password" id="password" name="password" class="form-input" placeholder="••••••••" required>
            </div>

            <button type="submit" class="btn-submit">
                <i class='bx bx-log-in'></i> Đăng Nhập Vào Hệ Thống
            </button>

            <div style="text-align: center; margin-top: 18px; font-size: 13px; color: #6b7a70;">
                Chưa có tài khoản? <a href="javascript:void(0)" onclick="openModal('registerModal')" style="color: #557c56; font-weight: 700; text-decoration: none;">Đăng ký ngay</a>
            </div>
        </form>
    </div>

    <!-- MODAL 1: ĐĂNG KÝ TÀI KHOẢN -->
    <div class="modal-wrap" id="registerModal">
        <div class="modal-body-custom">
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px;">
                <h3 style="margin: 0; font-size: 16px; font-weight: 700; color: #222831;"><i class='bx bx-user-plus'></i> Đăng Ký Tài Khoản</h3>
                <span onclick="closeModal('registerModal')" style="cursor: pointer; font-size: 22px; color: #888;">&times;</span>
            </div>
            <form action="${pageContext.request.contextPath}/dang-ky" method="post">
                <div style="margin-bottom: 12px;">
                    <label class="form-label">Email tài khoản <span style="color:red">*</span></label>
                    <input type="email" name="email" class="form-input" style="padding-left: 14px;" placeholder="name@theroyal.com" required>
                </div>
                <div style="margin-bottom: 12px;">
                    <label class="form-label">Mật khẩu khởi tạo <span style="color:red">*</span></label>
                    <input type="password" name="matKhau" class="form-input" style="padding-left: 14px;" placeholder="••••••••" required>
                </div>
                <div style="margin-bottom: 18px;">
                    <label class="form-label">Vai trò mong muốn</label>
                    <select name="idVaiTro" class="form-input" style="padding-left: 14px;">
                        <option value="2">Nhân viên Lễ tân</option>
                        <option value="3">Nhân viên Dọn phòng</option>
                        <option value="4">Nhân viên Kế toán</option>
                        <option value="5">Quản lý</option>
                        <option value="6">Quản trị viên (Admin)</option>
                    </select>
                </div>
                <button type="submit" class="btn-submit"><i class='bx bx-check'></i> Xác Nhận Đăng Ký</button>
            </form>
        </div>
    </div>

    <!-- MODAL 2: QUÊN MẬT KHẨU -->
    <div class="modal-wrap" id="forgotModal">
        <div class="modal-body-custom">
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px;">
                <h3 style="margin: 0; font-size: 16px; font-weight: 700; color: #222831;"><i class='bx bx-help-circle'></i> Quên Mật Khẩu</h3>
                <span onclick="closeModal('forgotModal')" style="cursor: pointer; font-size: 22px; color: #888;">&times;</span>
            </div>
            <p style="font-size: 13px; color: #6b7a70; margin-bottom: 14px; line-height: 1.5;">
                Nhập email tài khoản của bạn để hệ thống tự động reset mật khẩu về mặc định: <b>123456</b>
            </p>
            <form action="${pageContext.request.contextPath}/quen-mat-khau" method="post">
                <div style="margin-bottom: 16px;">
                    <label class="form-label">Email tài khoản</label>
                    <input type="email" name="email" class="form-input" style="padding-left: 14px;" placeholder="name@theroyal.com" required>
                </div>
                <button type="submit" class="btn-submit"><i class='bx bx-refresh'></i> Khôi Phục Mật Khẩu</button>
            </form>
        </div>
    </div>

    <script>
        function openModal(id) {
            document.getElementById(id).style.display = 'flex';
        }

        function closeModal(id) {
            document.getElementById(id).style.display = 'none';
        }

        window.onclick = function(event) {
            if (event.target.classList.contains('modal-wrap')) {
                event.target.style.display = 'none';
            }
        };
    </script>
</body>
</html>