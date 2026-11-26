package controller;

import entity.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import repository.DatPhongRepository;
import util.FlashMessage;
import util.RequestUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * URL: /dat-phong
 *   GET  ?action=danhSach (mặc định) [maDatPhong, tuKhoaKhach, trangThai, tuNgay, denNgay]  -> dat-phong/danh-sach.jsp   (TRA CỨU ĐẶT PHÒNG)
 *   GET  ?action=chiTiet&id=                                                                -> dat-phong/chi-tiet.jsp
 *   GET  ?action=timPhong [nhanPhong, traPhong, idHangPhong, idLoaiGiuong, soNguoi]         -> dat-phong/tim-phong.jsp   (TRA CỨU PHÒNG)
 *   GET  ?action=formDat&idPhong=..&idPhong=..&nhanPhong=..&traPhong=..                     -> dat-phong/form-dat.jsp
 *   POST action=dat   (idKhachHang, maGiamGia, nhanPhong, traPhong, idPhong[], soNguoiLon_{id}, soTreEmDuoi6_{id}, soTreEmTu6Tuoi_{id})
 *   POST action=huy   (id, loaiHuy = KHACH_HUY | KHACH_SAN_HUY, ghiChu)
 */
@WebServlet("/dat-phong")
public class DatPhongServlet extends HttpServlet {
    private static final String VIEW = "/quan-ly-dat-phong/";
    private final DatPhongRepository service = new DatPhongRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        FlashMessage.nap(req);
        String action = req.getParameter("action") == null ? "danhSach" : req.getParameter("action");
        switch (action) {
            case "chiTiet" -> hienChiTiet(req, resp);
            case "timPhong" -> hienTimPhong(req, resp);
            case "formDat" -> hienFormDat(req, resp);
            default -> hienDanhSach(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action") == null ? "" : req.getParameter("action");
        switch (action) {
            case "dat" -> dat(req, resp);
            case "huy" -> huy(req, resp);
            default -> resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "action không hợp lệ");
        }
    }

    // ================================================================= TRA CỨU ĐẶT PHÒNG
    private void hienDanhSach(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String ma = RequestUtil.chuoi(req, "maDatPhong");
        String khach = RequestUtil.chuoi(req, "tuKhoaKhach");
        String tt = RequestUtil.chuoi(req, "trangThai");
        try {
            LocalDate tu = RequestUtil.ngay(req, "tuNgay");
            LocalDate den = RequestUtil.ngay(req, "denNgay");
            req.setAttribute("dsDatPhong", service.traCuuDatPhong(ma, khach, tt, tu, den));
        } catch (NghiepVuException e) {
            req.setAttribute("loi", e.getMessage());
            req.setAttribute("dsDatPhong", List.of());
        }
        // trả lại bộ lọc để JSP điền sẵn vào ô tìm kiếm
        for (String k : new String[]{"maDatPhong", "tuKhoaKhach", "trangThai", "tuNgay", "denNgay"})
            req.setAttribute(k, req.getParameter(k));
        req.getRequestDispatcher(VIEW + "view.jsp").forward(req, resp);
    }

    private void hienChiTiet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Long id = RequestUtil.soLong(req, "id");
            if (id == null) throw new NghiepVuException("Thiếu mã đơn đặt phòng.");
            DatPhong dp = service.layChiTiet(id);
            req.setAttribute("datPhong", dp);
            req.setAttribute("lichSuHuy", service.layLichSuHuy(id));   // null nếu chưa hủy
            // nút "Hủy" chỉ hiện khi đơn còn hủy được
            req.setAttribute("coTheHuy", DatPhong.CHO_COC.equals(dp.getTrangThai())
                    || DatPhong.DA_XAC_NHAN.equals(dp.getTrangThai()));
            req.getRequestDispatcher(VIEW + "detail.jsp").forward(req, resp);
        } catch (NghiepVuException e) {
            FlashMessage.loi(req, e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/dat-phong");
        }
    }

    // ================================================================= TRA CỨU PHÒNG
    private void hienTimPhong(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("dsHangPhong", service.layHangPhong());
        req.setAttribute("dsLoaiGiuong", service.layLoaiGiuong());
        for (String k : new String[]{"nhanPhong", "traPhong", "idHangPhong", "idLoaiGiuong", "soNguoi"})
            req.setAttribute(k, req.getParameter(k));

        // Chưa bấm "Tìm" (chưa có ngày) -> chỉ hiện form
        if (RequestUtil.chuoi(req, "nhanPhong") != null || RequestUtil.chuoi(req, "traPhong") != null) {
            try {
                req.setAttribute("dsPhongTrong", service.traCuuPhongTrong(
                        RequestUtil.ngay(req, "nhanPhong"), RequestUtil.ngay(req, "traPhong"),
                        RequestUtil.soLong(req, "idHangPhong"), RequestUtil.soLong(req, "idLoaiGiuong"),
                        RequestUtil.soInt(req, "soNguoi", 1)));
                req.setAttribute("daTim", true);
            } catch (NghiepVuException e) {
                req.setAttribute("loi", e.getMessage());
            }
        }
        req.getRequestDispatcher(VIEW + "tim-phong.jsp").forward(req, resp);
    }

    // ================================================================= ĐẶT PHÒNG
    private void hienFormDat(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Long> idPhong = RequestUtil.danhSachLong(req, "idPhong");
            LocalDate nhan = RequestUtil.ngay(req, "nhanPhong");
            LocalDate tra = RequestUtil.ngay(req, "traPhong");
            if (idPhong.isEmpty() || nhan == null || tra == null || !tra.isAfter(nhan))
                throw new NghiepVuException("Vui lòng chọn phòng và khoảng ngày hợp lệ trước khi đặt.");

            List<Phong> dsPhong = service.layPhongDeDat(idPhong);
            long soDem = ChronoUnit.DAYS.between(nhan, tra);
            BigDecimal tamTinh = BigDecimal.ZERO;
            for (Phong p : dsPhong) tamTinh = tamTinh.add(p.getLoaiPhong().getGiaMoiDem());
            tamTinh = tamTinh.multiply(BigDecimal.valueOf(soDem));

            req.setAttribute("dsPhongChon", dsPhong);
            req.setAttribute("soDem", soDem);
            req.setAttribute("tienPhongTamTinh", tamTinh);   // chưa trừ giảm giá, chưa VAT - chỉ để tham khảo
            req.setAttribute("dsKhachHang", service.timKhachHang(RequestUtil.chuoi(req, "tuKhoaKhach")));
            req.setAttribute("nhanPhong", nhan.toString());
            req.setAttribute("traPhong", tra.toString());
            req.getRequestDispatcher(VIEW + "add.jsp").forward(req, resp);
        } catch (NghiepVuException e) {
            FlashMessage.loi(req, e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/dat-phong?action=timPhong");
        }
    }

    private void dat(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<PhongYeuCau> phongs = new ArrayList<>();
            for (Long id : RequestUtil.danhSachLong(req, "idPhong")) {
                phongs.add(new PhongYeuCau(id,
                        RequestUtil.soInt(req, "soNguoiLon_" + id, 1),
                        RequestUtil.soInt(req, "soTreEmDuoi6_" + id, 0),
                        RequestUtil.soInt(req, "soTreEmTu6Tuoi_" + id, 0)));
            }
            Object nv = req.getSession().getAttribute("idNhanVien");   // do module Đăng nhập gán; không có = khách tự đặt
            Long idNguoiTao = (nv instanceof Long l) ? l : null;

            DatPhong dp = service.datPhong(new YeuCauDatPhong(
                    RequestUtil.soLong(req, "idKhachHang"), idNguoiTao, RequestUtil.chuoi(req, "maGiamGia"),
                    RequestUtil.ngay(req, "nhanPhong"), RequestUtil.ngay(req, "traPhong"), phongs));

            FlashMessage.thanhCong(req, "Đặt phòng thành công. Mã đơn: " + dp.getMaDatPhong());
            resp.sendRedirect(req.getContextPath() + "/dat-phong?action=chiTiet&id=" + dp.getId());
        } catch (NghiepVuException e) {
            req.setAttribute("loi", e.getMessage());
            hienFormDat(req, resp);   // dựng lại form, các tham số người dùng nhập vẫn còn trong request
        }
    }

    // ================================================================= HỦY PHÒNG
    private void huy(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = null;
        try {
            id = RequestUtil.soLong(req, "id");
            Object nv = req.getSession().getAttribute("idNhanVien");
            Long idNguoiHuy = (nv instanceof Long l) ? l : null;
            String loaiHuy = RequestUtil.chuoi(req, "loaiHuy") == null ? DatPhong.KHACH_HUY : req.getParameter("loaiHuy");
            var ls = service.huyDatPhong(id, loaiHuy, idNguoiHuy, RequestUtil.chuoi(req, "ghiChu"));
            FlashMessage.thanhCong(req, "Đã hủy đơn đặt phòng. Số tiền hoàn: " + ls.getSoTienHoan().toPlainString() + " VNĐ.");
        } catch (NghiepVuException e) {
            FlashMessage.loi(req, e.getMessage());
        }
        resp.sendRedirect(req.getContextPath() + (id == null
                ? "/dat-phong" : "/dat-phong?action=chiTiet&id=" + id));
    }
}
