package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import repository.KhachHangRepository; // Đảm bảo tên package repository của em khớp nhé
import entity.KhachHang;

import java.io.IOException;

@WebServlet(name = "KhachHangServlet", value = {
        "/khach-hang/view",
        "/khach-hang/detail",
        "/khach-hang/delete",
        "/khach-hang/view-update",
        "/khach-hang/update",
        "/khach-hang/add"
})
public class KhachHangServlet extends HttpServlet {

    KhachHangRepository khachHangRepository = new KhachHangRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getRequestURI();

        if (url.contains("view-update")) {
            viewUpdate(req, resp);
        }
        else if (url.contains("detail")) {
            detail(req, resp);
        }
        else if (url.contains("delete")) {
            delete(req, resp);
        }
        else if (url.contains("view")) {
            view(req, resp);
        }
    }

    // Hiển thị thông tin khách hàng lên form sửa
    private void viewUpdate(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Vì id_khach_hang trong SQL là bigint nên dùng Long.valueOf()
        Long id = Long.valueOf(req.getParameter("id"));

        KhachHang kh = khachHangRepository.getOne(id);

        req.setAttribute("kh", kh);

        req.getRequestDispatcher("/khach-hang/view.jsp")
                .forward(req, resp);
    }

    // Xóa khách hàng
    private void delete(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        Long id = Long.valueOf(req.getParameter("id"));

        khachHangRepository.delete(id);

        // Chú ý sửa lại đường dẫn redirect đúng với URL view của khách hàng
        resp.sendRedirect(req.getContextPath() + "/khach-hang/view");
    }

    // Xem chi tiết khách hàng
    private void detail(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Long id = Long.valueOf(req.getParameter("id"));

        KhachHang kh = khachHangRepository.getOne(id);

        req.setAttribute("kh", kh);

        req.getRequestDispatcher("/khach-hang/view.jsp")
                .forward(req, resp);
    }

    // Hiển thị danh sách khách hàng
    private void view(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("listKhachHang", khachHangRepository.getAll());
        req.getRequestDispatcher("/khach-hang/view.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String url = req.getRequestURI();

        if (url.contains("add")) {

            String hoTen = req.getParameter("ho_ten");
            String soDienThoai = req.getParameter("so_dien_thoai");
            String email = req.getParameter("email");
            String trangThai = req.getParameter("trang_thai");

            KhachHang kh = new KhachHang();
            kh.setHo_ten(hoTen);
            kh.setSo_dien_thoai(soDienThoai);
            kh.setEmail(email);
            kh.setTrang_thai(trangThai);

            khachHangRepository.add(kh);

            resp.sendRedirect(req.getContextPath() + "/khach-hang/view");

        }else if (url.contains("update")) {

            String idParam = req.getParameter("id");

            if (idParam == null || idParam.trim().isEmpty()) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu ID khách hàng");
                return;
            }

            Long id = Long.valueOf(idParam);

            KhachHang kh = khachHangRepository.getOne(id);

            if (kh == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy khách hàng");
                return;
            }

            String hoTen = req.getParameter("ho_ten");
            String soDienThoai = req.getParameter("so_dien_thoai");
            String email = req.getParameter("email");
            String trangThai = req.getParameter("trang_thai");

            kh.setHo_ten(hoTen);
            kh.setSo_dien_thoai(soDienThoai);
            kh.setEmail(email);
            kh.setTrang_thai(trangThai);

            khachHangRepository.update(kh);

            resp.sendRedirect(req.getContextPath() + "/khach-hang/view");
        }

    }
}
