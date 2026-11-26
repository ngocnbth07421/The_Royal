package controller;

import entity.KhachHang;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import repository.KhachHangRepository;
import util.FlashMessage;
import util.RequestUtil;

import java.io.IOException;

@WebServlet(name = "KhachHangServlet", value = {
        "/quan-ly-khach-hang/view",
        "/quan-ly-khach-hang/detail",
        "/quan-ly-khach-hang/delete",
        "/quan-ly-khach-hang/view-update",
        "/quan-ly-khach-hang/update",
        "/quan-ly-khach-hang/add"
})
public class KhachHangServlet extends HttpServlet {

    private final KhachHangRepository khachHangRepository = new KhachHangRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        FlashMessage.nap(req);
        String url = req.getServletPath();

        try {
            switch (url) {
                case "/quan-ly-khach-hang/view-update" -> viewUpdate(req, resp);
                case "/quan-ly-khach-hang/detail" -> detail(req, resp);
                case "/quan-ly-khach-hang/delete" -> delete(req, resp);
                default -> view(req, resp);
            }
        } catch (NghiepVuException e) {
            FlashMessage.loi(req, e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/quan-ly-khach-hang/view");
        }
    }

    private void view(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String tuKhoa = RequestUtil.chuoi(req, "tuKhoa");
        String trangThai = RequestUtil.chuoi(req, "trangThai");
        req.setAttribute("tuKhoa", tuKhoa);
        req.setAttribute("trangThai", trangThai);
        req.setAttribute("listKhachHang", khachHangRepository.search(tuKhoa, trangThai));
        req.getRequestDispatcher("/quan-ly-khach-hang/view.jsp").forward(req, resp);
    }

    private void detail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = RequestUtil.soLong(req, "id");
        if (id == null) throw new NghiepVuException("Thiếu ID khách hàng.");
        KhachHang kh = khachHangRepository.getOne(id);
        if (kh == null) throw new NghiepVuException("Không tìm thấy khách hàng.");
        req.setAttribute("kh", kh);
        req.setAttribute("dsDatPhong", khachHangRepository.layLichSuDatPhong(id));
        req.setAttribute("theThanhVien", khachHangRepository.layTheThanhVien(id));
        req.getRequestDispatcher("/quan-ly-khach-hang/detail.jsp").forward(req, resp);
    }

    private void viewUpdate(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = RequestUtil.soLong(req, "id");
        if (id == null) throw new NghiepVuException("Thiếu ID khách hàng.");
        KhachHang kh = khachHangRepository.getOne(id);
        if (kh == null) throw new NghiepVuException("Không tìm thấy khách hàng.");
        req.setAttribute("kh", kh);
        req.getRequestDispatcher("/quan-ly-khach-hang/view-update.jsp").forward(req, resp);
    }

    private void delete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = RequestUtil.soLong(req, "id");
        if (id == null) throw new NghiepVuException("Thiếu ID khách hàng.");
        khachHangRepository.delete(id);
        FlashMessage.thanhCong(req, "Đã xóa khách hàng.");
        resp.sendRedirect(req.getContextPath() + "/quan-ly-khach-hang/view");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String url = req.getServletPath();

        try {
            if ("/quan-ly-khach-hang/add".equals(url)) {
                KhachHang kh = new KhachHang();
                kh.setHoTen(RequestUtil.chuoi(req, "ho_ten"));
                kh.setSoDienThoai(RequestUtil.chuoi(req, "so_dien_thoai"));
                kh.setEmail(RequestUtil.chuoi(req, "email"));
                khachHangRepository.add(kh);
                FlashMessage.thanhCong(req, "Đã thêm khách hàng mới.");
                resp.sendRedirect(req.getContextPath() + "/quan-ly-khach-hang/view");
            } else if ("/quan-ly-khach-hang/update".equals(url)) {
                Long id = RequestUtil.soLong(req, "id");
                if (id == null) throw new NghiepVuException("Thiếu ID khách hàng.");
                KhachHang kh = khachHangRepository.getOne(id);
                if (kh == null) throw new NghiepVuException("Không tìm thấy khách hàng.");
                kh.setHoTen(RequestUtil.chuoi(req, "ho_ten"));
                kh.setSoDienThoai(RequestUtil.chuoi(req, "so_dien_thoai"));
                kh.setEmail(RequestUtil.chuoi(req, "email"));
                String trangThai = RequestUtil.chuoi(req, "trang_thai");
                if (trangThai != null) kh.setTrangThai(trangThai);
                khachHangRepository.update(kh);
                FlashMessage.thanhCong(req, "Đã cập nhật thông tin khách hàng.");
                resp.sendRedirect(req.getContextPath() + "/quan-ly-khach-hang/view");
            }
        } catch (NghiepVuException e) {
            FlashMessage.loi(req, e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/quan-ly-khach-hang/view");
        }
    }
}
