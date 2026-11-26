package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import repository.LoaiPhongRepository;
import util.FlashMessage;
import util.RequestUtil;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/loai-phong")
public class LoaiPhongServlet extends HttpServlet {
    private static final String VIEW = "/quan-ly-loai-phong/";
    private final LoaiPhongRepository service = new LoaiPhongRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        FlashMessage.nap(req);
        String action = req.getParameter("action") == null ? "danhSach" : req.getParameter("action");
        switch (action) {
            case "form" -> hienForm(req, resp);
            default -> hienDanhSach(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action") == null ? "" : req.getParameter("action");
        try {
            switch (action) {
                case "luu" -> luu(req, resp);
                case "xoa" -> xoa(req, resp);
                default -> resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "action không hợp lệ");
            }
        } catch (NghiepVuException e) {
            FlashMessage.loi(req, e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/loai-phong");
        }
    }

    private void hienDanhSach(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String tuKhoa = RequestUtil.chuoi(req, "tuKhoa");
        req.setAttribute("tuKhoa", tuKhoa);
        req.setAttribute("dsLoaiPhong", service.layDanhSach(tuKhoa));
        req.setAttribute("dsHangPhong", service.layDanhSachHangPhong());
        req.setAttribute("dsLoaiGiuong", service.layDanhSachLoaiGiuong());
        req.getRequestDispatcher(VIEW + "view.jsp").forward(req, resp);
    }

    private void hienForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = RequestUtil.soLong(req, "id");
        if (id != null) {
            req.setAttribute("loaiPhongSua", service.timTheoId(id));
        }
        req.setAttribute("dsHangPhong", service.layDanhSachHangPhong());
        req.setAttribute("dsLoaiGiuong", service.layDanhSachLoaiGiuong());
        req.getRequestDispatcher(VIEW + "view-update.jsp").forward(req, resp);
    }

    private void luu(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = RequestUtil.soLong(req, "id");
        String ten = RequestUtil.chuoi(req, "ten");
        String giaMoiDemStr = RequestUtil.chuoi(req, "giaMoiDem");
        String moTa = RequestUtil.chuoi(req, "moTa");
        Long idHangPhong = RequestUtil.soLong(req, "idHangPhong");
        Long idLoaiGiuong = RequestUtil.soLong(req, "idLoaiGiuong");

        BigDecimal giaMoiDem;
        try {
            giaMoiDem = giaMoiDemStr != null ? new BigDecimal(giaMoiDemStr) : null;
        } catch (NumberFormatException e) {
            throw new NghiepVuException("Giá mỗi đêm không hợp lệ.");
        }

        service.luuLoaiPhong(id, ten, giaMoiDem, moTa, idHangPhong, idLoaiGiuong);
        FlashMessage.thanhCong(req, id == null ? "Đã thêm loại phòng mới." : "Đã cập nhật loại phòng.");
        resp.sendRedirect(req.getContextPath() + "/loai-phong");
    }

    private void xoa(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = RequestUtil.soLong(req, "id");
        service.xoaLoaiPhong(id);
        FlashMessage.thanhCong(req, "Đã xóa loại phòng.");
        resp.sendRedirect(req.getContextPath() + "/loai-phong");
    }
}
