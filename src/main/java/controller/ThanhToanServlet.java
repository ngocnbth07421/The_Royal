package controller;

import entity.ThanhToan;
import entity.DatPhong;
import entity.TheThanhVien;
import exception.NghiepVuException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.ThanhToanService;
import util.FlashMessage;
import util.RequestUtil;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * URL: /thanh-toan
 *   GET  ?action=danhSach (mặc định)  -> views/thanh-toan/danh-sach.jsp
 *   GET  ?action=form [&id=]          -> views/thanh-toan/danh-sach.jsp (form sẽ điền sẵn nếu có id)
 *   POST action=luu                   -> thêm mới giao dịch thanh toán
 *   POST action=doiTrangThai          -> đổi trạng thái giao dịch
 *   POST action=xoa                   -> xóa giao dịch
 */
@WebServlet("/thanh-toan")
public class ThanhToanServlet extends HttpServlet {
    private static final String VIEW = "/WEB-INF/views/thanh-toan/";
    private final ThanhToanService service = new ThanhToanService();

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
                case "doiTrangThai" -> doiTrangThai(req, resp);
                case "xoa" -> xoa(req, resp);
                default -> resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "action không hợp lệ");
            }
        } catch (NghiepVuException e) {
            FlashMessage.loi(req, e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/thanh-toan");
        }
    }

    private void hienDanhSach(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("dsThanhToan", service.layDanhSach());
        req.setAttribute("dsDatPhong", service.layDanhSachDatPhong());
        req.setAttribute("dsTheThanhVien", service.layDanhSachTheThanhVien());
        req.getRequestDispatcher(VIEW + "danh-sach.jsp").forward(req, resp);
    }

    private void hienForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = RequestUtil.soLong(req, "id");
        if (id != null) {
            // Chế độ sửa trạng thái - truyền thông tin thanh toán
            req.setAttribute("thanhToanSua", findById(id));
        }
        req.setAttribute("dsDatPhong", service.layDanhSachDatPhong());
        req.setAttribute("dsTheThanhVien", service.layDanhSachTheThanhVien());
        req.setAttribute("dsThanhToan", service.layDanhSach());
        req.getRequestDispatcher(VIEW + "danh-sach.jsp").forward(req, resp);
    }

    private ThanhToan findById(Long id) {
        for (ThanhToan tt : service.layDanhSach()) {
            if (tt.getId().equals(id)) return tt;
        }
        return null;
    }

    private void luu(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long idDatPhong = RequestUtil.soLong(req, "idDatPhong");
        Long idTheThanhVien = RequestUtil.soLong(req, "idTheThanhVien");
        String phuongThuc = RequestUtil.chuoi(req, "phuongThuc");
        String soTienStr = RequestUtil.chuoi(req, "soTien");
        String trangThai = RequestUtil.chuoi(req, "trangThai");

        BigDecimal soTien;
        try {
            soTien = soTienStr != null ? new BigDecimal(soTienStr) : null;
        } catch (NumberFormatException e) {
            throw new NghiepVuException("Số tiền thanh toán không hợp lệ.");
        }

        service.taoGiaoDichThanhToan(idDatPhong, idTheThanhVien, phuongThuc, soTien, trangThai);
        FlashMessage.thanhCong(req, "Đã tạo giao dịch thanh toán thành công.");
        resp.sendRedirect(req.getContextPath() + "/thanh-toan");
    }

    private void doiTrangThai(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = RequestUtil.soLong(req, "id");
        String trangThaiMoi = RequestUtil.chuoi(req, "trangThaiMoi");
        service.doiTrangThai(id, trangThaiMoi);
        FlashMessage.thanhCong(req, "Đã cập nhật trạng thái thanh toán.");
        resp.sendRedirect(req.getContextPath() + "/thanh-toan");
    }

    private void xoa(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = RequestUtil.soLong(req, "id");
        new repository.ThanhToanRepository().delete(id);
        FlashMessage.thanhCong(req, "Đã xóa giao dịch thanh toán.");
        resp.sendRedirect(req.getContextPath() + "/thanh-toan");
    }
}
