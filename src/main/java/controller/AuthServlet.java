package controller;

import entity.KhachHang;
import entity.NguoiDung;
import entity.NhanVien;
import entity.VaiTro;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import repository.KhachHangRepository;
import repository.NguoiDungRepository;
import repository.NhanVienRepository;
import repository.VaiTroRepository;

import java.io.IOException;

@WebServlet(name = "controller.AuthServlet", value = {
        "/login",
        "/logout",
        "/dang-ky",
        "/doi-mat-khau",
        "/doi-email",
        "/quen-mat-khau",
        "/xac-nhan-ma",
        "/dat-lai-mat-khau"
})
public class AuthServlet extends HttpServlet {
    private final NguoiDungRepository nguoiDungRepository = new NguoiDungRepository();
    private final NhanVienRepository nhanVienRepository = new NhanVienRepository();
    private final VaiTroRepository vaiTroRepository = new VaiTroRepository();
    private final KhachHangRepository khachHangRepository = new KhachHangRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
        if (uri.contains("logout")) {
            HttpSession session = req.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            resp.sendRedirect(req.getContextPath() + "/index.jsp?msg=logged_out");
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/index.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String uri = req.getRequestURI();
        if (uri.contains("login")) {
            xuLyDangNhap(req, resp);
        } else if (uri.contains("dang-ky")) {
            xuLyDangKy(req, resp);
        } else if (uri.contains("doi-mat-khau")) {
            xuLyDoiMatKhau(req, resp);
        } else if (uri.contains("doi-email")) {
            xuLyDoiEmail(req, resp);
        } else if (uri.contains("quen-mat-khau")) {
            xuLyQuenMatKhau(req, resp);
        } else if (uri.contains("xac-nhan-ma")) {
            xuLyXacNhanMa(req, resp);
        } else if (uri.contains("dat-lai-mat-khau")) {
            xuLyDatLaiMatKhau(req, resp);
        }
    }

    /**
     * Đăng nhập hỗ trợ: Email HOẶC Số điện thoại HOẶC Mã nhân viên
     */
    private void xuLyDangNhap(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String taiKhoan = req.getParameter("taiKhoan");
        String password = req.getParameter("password");

        if (taiKhoan == null || taiKhoan.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp?error=empty_fields");
            return;
        }

        taiKhoan = taiKhoan.trim();
        NguoiDung nd = null;
        NhanVien nv = null;

        // 1. Nếu có ký tự @ -> Đăng nhập bằng Email
        if (taiKhoan.contains("@")) {
            nd = nguoiDungRepository.findByEmail(taiKhoan.toLowerCase());
            if (nd != null && nd.getIdNhanVien() != null) {
                nv = nhanVienRepository.getOne(nd.getIdNhanVien());
            }
        } else if (taiKhoan.matches("^[0-9+ ]{9,15}$")) {
            // 2. Nếu là số điện thoại -> Đăng nhập bằng Số điện thoại (SDT)
            String sdtChuan = taiKhoan.replaceAll("[^0-9]", "");
            // Tìm nhân viên theo số điện thoại
            nv = nhanVienRepository.findBySoDienThoai(taiKhoan);
            if (nv == null && !taiKhoan.equals(sdtChuan)) {
                nv = nhanVienRepository.findBySoDienThoai(sdtChuan);
            }

            if (nv != null) {
                nd = nguoiDungRepository.findByNhanVien(nv.getIdNhanVien());
            } else {
                // Thử tìm trong bảng khách hàng theo số điện thoại
                KhachHang kh = khachHangRepository.findBySoDienThoai(taiKhoan);
                if (kh == null && !taiKhoan.equals(sdtChuan)) {
                    kh = khachHangRepository.findBySoDienThoai(sdtChuan);
                }
                if (kh != null && kh.getEmail() != null) {
                    nd = nguoiDungRepository.findByEmail(kh.getEmail());
                }
            }
        } else {
            // 3. Đăng nhập bằng Mã nhân viên (VD: NV001, NV002, ...)
            nv = nhanVienRepository.findByMaNhanVien(taiKhoan.toUpperCase());
            if (nv != null) {
                nd = nguoiDungRepository.findByNhanVien(nv.getIdNhanVien());
            }
        }

        if (nd == null) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp?error=not_found");
            return;
        }

        // Kiểm tra mật khẩu
        if (!password.trim().equals(nd.getMatKhau())) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp?error=wrong_password");
            return;
        }

        // Kiểm tra trạng thái tài khoản (chấp nhận cả 'Hoạt động' và 'HOAT_DONG')
        String tt = nd.getTrangThai();
        if (tt != null && (tt.equalsIgnoreCase("BI_KHOA") || tt.equalsIgnoreCase("Bị khóa") || tt.contains("KHOA"))) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp?error=account_locked");
            return;
        }

        if (nv == null && nd.getIdNhanVien() != null) {
            nv = nhanVienRepository.getOne(nd.getIdNhanVien());
        }

        VaiTro vaiTro = vaiTroRepository.getOne(nd.getIdVaiTro());

        HttpSession session = req.getSession(true);
        session.setAttribute("currentNguoiDung", nd);
        session.setAttribute("currentUser", nv);
        session.setAttribute("currentRole", vaiTro);

        resp.sendRedirect(req.getContextPath() + "/trang-chu");
    }

    /**
     * Đăng ký tài khoản: Chỉ dành cho Khách hàng (có số điện thoại, làm sau)
     */
    private void xuLyDangKy(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String hoTen = req.getParameter("hoTen");
        String soDienThoai = req.getParameter("soDienThoai");
        String email = req.getParameter("email");
        String matKhau = req.getParameter("matKhau");

        if (email == null || email.trim().isEmpty() || matKhau == null || matKhau.trim().isEmpty()
                || soDienThoai == null || soDienThoai.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp?error=empty_fields&modal=register");
            return;
        }

        email = email.trim().toLowerCase();
        soDienThoai = soDienThoai.trim();

        // Kiểm tra email trùng
        if (nguoiDungRepository.findByEmail(email) != null) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp?error=email_exists&modal=register");
            return;
        }

        // Kiểm tra số điện thoại trùng trong khách hàng
        if (khachHangRepository.findBySoDienThoai(soDienThoai) != null) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp?error=phone_exists&modal=register");
            return;
        }

        // 1. Tạo bản ghi Khách hàng
        KhachHang kh = new KhachHang();
        kh.setHoTen(hoTen != null && !hoTen.trim().isEmpty() ? hoTen.trim() : "Khách Hàng Mới");
        kh.setSoDienThoai(soDienThoai);
        kh.setEmail(email);
        kh.setTrangThai("Hoạt động");
        khachHangRepository.add(kh);

        // 2. Tạo tài khoản người dùng tương ứng (Vai trò = 1: KHACH_HANG)
        NguoiDung nd = new NguoiDung();
        nd.setEmail(email);
        nd.setMatKhau(matKhau.trim());
        nd.setIdVaiTro(1L); // 1 = KHACH_HANG
        nd.setTrangThai("Hoạt động");
        nguoiDungRepository.add(nd);

        resp.sendRedirect(req.getContextPath() + "/index.jsp?msg=register_customer_success");
    }

    /**
     * Đổi mật khẩu cá nhân khi đã đăng nhập
     */
    private void xuLyDoiMatKhau(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("currentNguoiDung") == null) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }

        NguoiDung nd = (NguoiDung) session.getAttribute("currentNguoiDung");
        String matKhauCu = req.getParameter("matKhauCu");
        String matKhauMoi = req.getParameter("matKhauMoi");

        if (matKhauCu == null || !matKhauCu.equals(nd.getMatKhau())) {
            resp.sendRedirect(req.getContextPath() + "/trang-chu?error=wrong_old_pwd");
            return;
        }

        nd.setMatKhau(matKhauMoi.trim());
        nguoiDungRepository.update(nd);
        session.setAttribute("currentNguoiDung", nd);
        resp.sendRedirect(req.getContextPath() + "/trang-chu?success=pwd_changed");
    }

    /**
     * Thay đổi Email mới cho tài khoản đang đăng nhập
     */
    private void xuLyDoiEmail(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("currentNguoiDung") == null) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }

        NguoiDung nd = (NguoiDung) session.getAttribute("currentNguoiDung");
        String emailMoi = req.getParameter("emailMoi");
        String matKhauXacNhan = req.getParameter("matKhauXacNhan");

        if (emailMoi == null || emailMoi.trim().isEmpty() || !emailMoi.contains("@")) {
            resp.sendRedirect(req.getContextPath() + "/trang-chu?error=invalid_email");
            return;
        }

        if (matKhauXacNhan == null || !matKhauXacNhan.equals(nd.getMatKhau())) {
            resp.sendRedirect(req.getContextPath() + "/trang-chu?error=wrong_pwd_for_email");
            return;
        }

        emailMoi = emailMoi.trim().toLowerCase();

        // Kiểm tra xem email mới đã được dùng bởi tài khoản khác chưa
        NguoiDung existing = nguoiDungRepository.findByEmail(emailMoi);
        if (existing != null && !existing.getIdNguoiDung().equals(nd.getIdNguoiDung())) {
            resp.sendRedirect(req.getContextPath() + "/trang-chu?error=email_already_used");
            return;
        }

        // Cập nhật email mới
        nd.setEmail(emailMoi);
        nguoiDungRepository.update(nd);
        session.setAttribute("currentNguoiDung", nd);

        resp.sendRedirect(req.getContextPath() + "/trang-chu?success=email_changed");
    }

    /**
     * QUÊN MẬT KHẨU - BƯỚC 1: Nhập email, kiểm tra tồn tại và gửi mã xác nhận qua email (Sơ đồ Hình 3)
     */
    private void xuLyQuenMatKhau(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String email = req.getParameter("email");
        if (email == null || email.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp?error=empty_email&forgotStep=1");
            return;
        }

        email = email.trim().toLowerCase();
        NguoiDung nd = nguoiDungRepository.findByEmail(email);
        if (nd == null) {
            // Email không tồn tại trong hệ thống (nhánh No trong sơ đồ)
            resp.sendRedirect(req.getContextPath() + "/index.jsp?error=email_not_found&forgotStep=1&inputEmail=" + email);
            return;
        }

        // Email tồn tại (nhánh Yes trong sơ đồ) -> Sinh mã xác nhận OTP 6 số
        String otp = String.format("%06d", (int) (Math.random() * 1000000));

        HttpSession session = req.getSession(true);
        session.setAttribute("forgotEmail", email);
        session.setAttribute("forgotOtp", otp);
        session.setAttribute("forgotOtpTime", System.currentTimeMillis());

        // Chuyển sang Bước 2: Nhập mã xác nhận (kèm mã demo để kiểm thử ngay)
        resp.sendRedirect(req.getContextPath() + "/index.jsp?forgotStep=2&email=" + email + "&otpDemo=" + otp);
    }

    /**
     * QUÊN MẬT KHẨU - BƯỚC 2: Kiểm tra mã xác nhận (Sơ đồ Hình 3)
     */
    private void xuLyXacNhanMa(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        String maXacNhan = req.getParameter("maXacNhan");

        if (session == null || session.getAttribute("forgotOtp") == null) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp?error=session_expired&forgotStep=1");
            return;
        }

        String savedOtp = (String) session.getAttribute("forgotOtp");
        String email = (String) session.getAttribute("forgotEmail");
        Long otpTime = (Long) session.getAttribute("forgotOtpTime");

        // Kiểm tra thời hạn 10 phút
        if (otpTime == null || (System.currentTimeMillis() - otpTime) > 10 * 60 * 1000) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp?error=otp_expired&forgotStep=2&email=" + email);
            return;
        }

        // Kiểm tra mã xác nhận (nhánh No/Yes trong sơ đồ)
        if (maXacNhan == null || !maXacNhan.trim().equals(savedOtp)) {
            // Sai mã xác nhận -> quay lại giao diện nhập mã xác nhận
            resp.sendRedirect(req.getContextPath() + "/index.jsp?error=wrong_otp&forgotStep=2&email=" + email + "&otpDemo=" + savedOtp);
            return;
        }

        // Mã xác nhận chính xác -> Cho phép hiển thị giao diện nhập mật khẩu mới (Bước 3)
        session.setAttribute("otpVerified", Boolean.TRUE);
        resp.sendRedirect(req.getContextPath() + "/index.jsp?forgotStep=3&email=" + email);
    }

    /**
     * QUÊN MẬT KHẨU - BƯỚC 3: Nhập và cập nhật mật khẩu mới trong Database (Sơ đồ Hình 3)
     */
    private void xuLyDatLaiMatKhau(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session == null || !Boolean.TRUE.equals(session.getAttribute("otpVerified"))) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp?error=unauthorized_reset&forgotStep=1");
            return;
        }

        String email = (String) session.getAttribute("forgotEmail");
        String matKhauMoi = req.getParameter("matKhauMoi");
        String xacNhanMatKhau = req.getParameter("xacNhanMatKhau");

        if (matKhauMoi == null || matKhauMoi.trim().isEmpty() || xacNhanMatKhau == null) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp?error=empty_pwd&forgotStep=3&email=" + email);
            return;
        }

        if (!matKhauMoi.trim().equals(xacNhanMatKhau.trim())) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp?error=pwd_not_match&forgotStep=3&email=" + email);
            return;
        }

        NguoiDung nd = nguoiDungRepository.findByEmail(email);
        if (nd == null) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp?error=not_found&forgotStep=1");
            return;
        }

        // Cập nhật mật khẩu mới vào CSDL
        nd.setMatKhau(matKhauMoi.trim());
        nguoiDungRepository.update(nd);

        // Xóa thông tin tạm trong session
        session.removeAttribute("forgotEmail");
        session.removeAttribute("forgotOtp");
        session.removeAttribute("forgotOtpTime");
        session.removeAttribute("otpVerified");

        // Thông báo đổi mật khẩu thành công và kết thúc luồng
        resp.sendRedirect(req.getContextPath() + "/index.jsp?msg=reset_success");
    }
}
