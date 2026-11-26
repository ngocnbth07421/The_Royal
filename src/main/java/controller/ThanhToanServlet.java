package controller;

import entity.ThanhToan;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import repository.ThanhToanRepository;
import util.FlashMessage;
import util.RequestUtil;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/thanh-toan")
public class ThanhToanServlet extends HttpServlet {
    private static final String VIEW = "/quan-ly-thanh-toan/";
    private final ThanhToanRepository service = new ThanhToanRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        FlashMessage.nap(req);
        String action = req.getParameter("action") == null ? "danhSach" : req.getParameter("action");
        switch (action) {
            case "detail" -> hienChiTiet(req, resp);
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
        String tuKhoa = RequestUtil.chuoi(req, "tuKhoa");
        String phuongThuc = RequestUtil.chuoi(req, "phuongThucLoc");
        String trangThai = RequestUtil.chuoi(req, "trangThaiLoc");
        req.setAttribute("tuKhoa", tuKhoa);
        req.setAttribute("phuongThucLoc", phuongThuc);
        req.setAttribute("trangThaiLoc", trangThai);
        req.setAttribute("dsThanhToan", service.layDanhSach(tuKhoa, phuongThuc, trangThai));
        req.setAttribute("dsDatPhong", service.layDanhSachDatPhong());
        req.setAttribute("dsTheThanhVien", service.layDanhSachTheThanhVien());
        req.getRequestDispatcher(VIEW + "view.jsp").forward(req, resp);
    }

    private void hienChiTiet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = RequestUtil.soLong(req, "id");
        ThanhToan tt = id == null ? null : service.findById(id);
        if (tt == null) {
            FlashMessage.loi(req, "Không tìm thấy giao dịch thanh toán.");
            resp.sendRedirect(req.getContextPath() + "/thanh-toan");
            return;
        }
        req.setAttribute("tt", tt);
        req.getRequestDispatcher(VIEW + "detail.jsp").forward(req, resp);
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
        service.delete(id);
        FlashMessage.thanhCong(req, "Đã xóa giao dịch thanh toán.");
        resp.sendRedirect(req.getContextPath() + "/thanh-toan");
    }
}
