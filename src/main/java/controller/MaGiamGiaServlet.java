package controller;

import entity.MaGiamGia;
import exception.NghiepVuException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.MaGiamGiaService;
import util.FlashMessage;
import util.RequestUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

/**
 * URL: /ma-giam-gia
 *   GET  ?action=danhSach (mặc định)  -> views/ma-giam-gia/danh-sach.jsp
 *   GET  ?action=form [&id=]          -> views/ma-giam-gia/danh-sach.jsp (form điền sẵn nếu có id)
 *   POST action=luu                   -> thêm/sửa mã giảm giá
 *   POST action=xoa                   -> xóa mã giảm giá
 */
@WebServlet("/ma-giam-gia")
public class MaGiamGiaServlet extends HttpServlet {
    private static final String VIEW = "/WEB-INF/views/ma-giam-gia/";
    private final MaGiamGiaService service = new MaGiamGiaService();

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
            resp.sendRedirect(req.getContextPath() + "/ma-giam-gia");
        }
    }

    private void hienDanhSach(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("dsMaGiamGia", service.layDanhSach());
        req.getRequestDispatcher(VIEW + "danh-sach.jsp").forward(req, resp);
    }

    private void hienForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = RequestUtil.soLong(req, "id");
        if (id != null) {
            req.setAttribute("mggSua", service.timTheoId(id));
        }
        req.setAttribute("dsMaGiamGia", service.layDanhSach());
        req.getRequestDispatcher(VIEW + "danh-sach.jsp").forward(req, resp);
    }

    private void luu(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = RequestUtil.soLong(req, "id");
        String maCode = RequestUtil.chuoi(req, "maGiamGia");
        String loaiGiamGia = RequestUtil.chuoi(req, "loaiGiamGia");
        String giaTriStr = RequestUtil.chuoi(req, "giaTri");
        String batDauStr = RequestUtil.chuoi(req, "batDauLuc");
        String ketThucStr = RequestUtil.chuoi(req, "ketThucLuc");
        String soLuotStr = RequestUtil.chuoi(req, "soLuotDaDung");
        String trangThai = RequestUtil.chuoi(req, "trangThai");

        BigDecimal giaTri;
        try {
            giaTri = giaTriStr != null ? new BigDecimal(giaTriStr) : null;
        } catch (NumberFormatException e) {
            throw new NghiepVuException("Giá trị giảm không hợp lệ.");
        }

        LocalDateTime batDau, ketThuc;
        try {
            batDau = batDauStr != null ? LocalDateTime.parse(batDauStr) : null;
            ketThuc = ketThucStr != null ? LocalDateTime.parse(ketThucStr) : null;
        } catch (DateTimeParseException e) {
            throw new NghiepVuException("Thời gian không hợp lệ.");
        }

        Integer soLuot = 0;
        try {
            if (soLuotStr != null) soLuot = Integer.parseInt(soLuotStr);
        } catch (NumberFormatException e) {
            throw new NghiepVuException("Số lượt đã dùng không hợp lệ.");
        }

        MaGiamGia mgg = new MaGiamGia();
        mgg.setId(id);
        mgg.setMaGiamGia(maCode);
        mgg.setLoaiGiamGia(loaiGiamGia);
        mgg.setGiaTri(giaTri);
        mgg.setBatDauLuc(batDau);
        mgg.setKetThucLuc(ketThuc);
        mgg.setSoLuotDaDung(soLuot);
        mgg.setTrangThai(trangThai != null ? trangThai : "Hoạt động");

        service.luuMaGiamGia(mgg);
        FlashMessage.thanhCong(req, id == null ? "Đã thêm mã giảm giá mới." : "Đã cập nhật mã giảm giá.");
        resp.sendRedirect(req.getContextPath() + "/ma-giam-gia");
    }

    private void xoa(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = RequestUtil.soLong(req, "id");
        service.xoaMaGiamGia(id);
        FlashMessage.thanhCong(req, "Đã xóa mã giảm giá.");
        resp.sendRedirect(req.getContextPath() + "/ma-giam-gia");
    }
}
