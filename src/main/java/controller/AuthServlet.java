package controller;

import entity.NguoiDung;
import entity.NhanVien;
import entity.VaiTro;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import repository.NguoiDungRepository;
import repository.NhanVienRepository;
import repository.VaiTroRepository;

import java.io.IOException;

@WebServlet(name = "controller.AuthServlet", value = {
        "/login",
        "/logout",
        "/dang-ky",
        "/doi-mat-khau",
        "/quen-mat-khau"
})
public class AuthServlet extends HttpServlet {
    private final NguoiDungRepository nguoiDungRepository = new NguoiDungRepository();
    private final NhanVienRepository nhanVienRepository = new NhanVienRepository();
    private final VaiTroRepository vaiTroRepository = new VaiTroRepository();

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
        } else if (uri.contains("quen-mat-khau")) {
            xuLyQuenMatKhau(req, resp);
        }
    }

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

        // 1. Thử tìm theo Email trong bảng nguoi_dung
        if (taiKhoan.contains("@")) {
            nd = nguoiDungRepository.findByEmail(taiKhoan.toLowerCase());
            if (nd != null && nd.getIdNhanVien() != null) {
                nv = nhanVienRepository.getOne(nd.getIdNhanVien());
            }
        } else {
            // 2. Thử tìm theo Mã nhân viên trong bảng nhan_vien
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

        // Kiểm tra trạng thái tài khoản
        if (!"HOAT_DONG".equalsIgnoreCase(nd.getTrangThai())) {
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

    private void xuLyDangKy(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String email = req.getParameter("email");
        String matKhau = req.getParameter("matKhau");
        String idVaiTroStr = req.getParameter("idVaiTro");

        if (email == null || email.trim().isEmpty() || matKhau == null || matKhau.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp?error=empty_fields");
            return;
        }

        if (nguoiDungRepository.findByEmail(email.trim().toLowerCase()) != null) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp?error=email_exists");
            return;
        }

        NguoiDung nd = new NguoiDung();
        nd.setEmail(email.trim().toLowerCase());
        nd.setMatKhau(matKhau.trim());
        nd.setIdVaiTro(idVaiTroStr != null && !idVaiTroStr.isEmpty() ? Long.valueOf(idVaiTroStr) : 2L);
        nd.setTrangThai("HOAT_DONG");

        nguoiDungRepository.add(nd);
        resp.sendRedirect(req.getContextPath() + "/index.jsp?msg=register_success");
    }

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

    private void xuLyQuenMatKhau(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String email = req.getParameter("email");
        if (email != null && !email.trim().isEmpty()) {
            NguoiDung nd = nguoiDungRepository.findByEmail(email.trim().toLowerCase());
            if (nd != null) {
                nd.setMatKhau("123456");
                nguoiDungRepository.update(nd);
                resp.sendRedirect(req.getContextPath() + "/index.jsp?msg=pwd_reset_success");
                return;
            }
        }
        resp.sendRedirect(req.getContextPath() + "/index.jsp?error=email_not_found");
    }
}
