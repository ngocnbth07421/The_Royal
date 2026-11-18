package controller;

import dto.PhongYeuCau;
import dto.YeuCauDatPhong;
import entity.DatPhong;
import entity.DatPhongChiTiet;
import entity.Phong;
import exception.NghiepVuException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.DatPhongService;
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
 *   GET  ?action=lich                                                                       -> dat-phong/lich.jsp         (LỊCH kiểu Google Calendar)
 *   GET  ?action=lichDuLieu&start=&end=[&maPhong=&trangThai=&goiHuy=1]                      -> JSON các lượt ở cho lịch
 *   GET  ?action=chiTiet&id=                                                                -> dat-phong/chi-tiet.jsp
 *   GET  ?action=timPhong [nhanPhong, traPhong, idHangPhong, idLoaiGiuong, soNguoi]         -> dat-phong/tim-phong.jsp   (TRA CỨU PHÒNG)
 *   GET  ?action=formDat&idPhong=..&idPhong=..&nhanPhong=..&traPhong=..                     -> dat-phong/form-dat.jsp
 *   POST action=dat   (idKhachHang, maGiamGia, nhanPhong, traPhong, idPhong[], soNguoiLon_{id}, soTreEmDuoi6_{id}, soTreEmTu6Tuoi_{id})
 *   POST action=huy   (id, loaiHuy = KHACH_HUY | KHACH_SAN_HUY, ghiChu)
 */
@WebServlet("/dat-phong")
public class DatPhongServlet extends HttpServlet {
    private static final String VIEW = "/WEB-INF/views/dat-phong/";
    private final DatPhongService service = new DatPhongService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action") == null ? "danhSach" : req.getParameter("action");
        if ("lichDuLieu".equals(action)) { traLichJson(req, resp); return; }   // JSON: không được "ăn" thông báo flash
        FlashMessage.nap(req);
        switch (action) {
            case "lich" -> hienLich(req, resp);
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


    // ================================================================= LỊCH
    private void hienLich(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(VIEW + "lich.jsp").forward(req, resp);
    }

    /** Trả JSON theo định dạng sự kiện của FullCalendar: mỗi lượt ở = 1 thanh kéo từ ngày nhận đến ngày trả. */
    private void traLichJson(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        try {
            LocalDate tu = layNgayDauChuoi(req.getParameter("start"));
            LocalDate den = layNgayDauChuoi(req.getParameter("end"));
            List<DatPhongChiTiet> ds = service.layLich(tu, den, req.getParameter("maPhong"),
                    req.getParameter("trangThai"), "1".equals(req.getParameter("goiHuy")));

            StringBuilder sb = new StringBuilder("[");
            boolean dau = true;
            for (DatPhongChiTiet c : ds) {
                DatPhong d = c.getDatPhong();
                String[] mau = mauTheoTrangThai(d.getTrangThai());   // {nền, viền, chữ}
                if (!dau) sb.append(',');
                dau = false;
                sb.append("{\"id\":\"").append(c.getId()).append("\"")
                  .append(",\"title\":\"").append(json(c.getPhong().getMaPhong() + " · " + d.getKhachHang().getHoTen())).append("\"")
                  .append(",\"start\":\"").append(c.getNgayNhan()).append("\"")
                  .append(",\"end\":\"").append(c.getNgayTra()).append("\"")      // FullCalendar: end là ngày loại trừ = ngày trả phòng
                  .append(",\"allDay\":true")
                  .append(",\"backgroundColor\":\"").append(mau[0]).append("\"")
                  .append(",\"borderColor\":\"").append(mau[1]).append("\"")
                  .append(",\"textColor\":\"").append(mau[2]).append("\"")
                  .append(",\"extendedProps\":{")
                  .append("\"idDatPhong\":").append(d.getId())
                  .append(",\"maDatPhong\":\"").append(json(d.getMaDatPhong())).append("\"")
                  .append(",\"phong\":\"").append(json(c.getPhong().getMaPhong())).append("\"")
                  .append(",\"khach\":\"").append(json(d.getKhachHang().getHoTen())).append("\"")
                  .append(",\"sdt\":\"").append(json(d.getKhachHang().getSoDienThoai() == null ? "" : d.getKhachHang().getSoDienThoai())).append("\"")
                  .append(",\"trangThai\":\"").append(json(nhanTrangThai(d.getTrangThai()))).append("\"")
                  .append(",\"nhan\":\"").append(d.getThoiGianNhanPhong().toLocalDate()).append(" ").append(d.getThoiGianNhanPhong().toLocalTime()).append("\"")
                  .append(",\"tra\":\"").append(d.getThoiGianTraPhong().toLocalDate()).append(" ").append(d.getThoiGianTraPhong().toLocalTime()).append("\"")
                  .append(",\"nguoiLon\":").append(c.getSoNguoiLon())
                  .append(",\"treEm\":").append(c.getSoTreEmDuoi6() + c.getSoTreEmTu6Tuoi())
                  .append("}}");
            }
            resp.getWriter().write(sb.append(']').toString());
        } catch (NghiepVuException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"loi\":\"" + json(e.getMessage()) + "\"}");
        }
    }

    /** FullCalendar gửi dạng 2026-11-01T00:00:00+07:00 -> chỉ lấy 10 ký tự ngày đầu. */
    private LocalDate layNgayDauChuoi(String v) {
        if (v == null || v.length() < 10) throw new NghiepVuException("Thiếu khoảng ngày của lịch.");
        try { return LocalDate.parse(v.substring(0, 10)); }
        catch (java.time.format.DateTimeParseException e) { throw new NghiepVuException("Ngày của lịch không hợp lệ."); }
    }

    private static String[] mauTheoTrangThai(String tt) {
        return switch (tt) {
            case DatPhong.CHO_COC       -> new String[]{"#fff8e1", "#b78103", "#7a5600"};
            case DatPhong.DA_XAC_NHAN   -> new String[]{"#e8f5e9", "#2e7d32", "#1b5e20"};
            case DatPhong.DANG_LUU_TRU  -> new String[]{"#e3f2fd", "#1565c0", "#0d47a1"};
            case DatPhong.DA_TRA_PHONG  -> new String[]{"#eceff1", "#546e7a", "#37474f"};
            default                     -> new String[]{"#ffebee", "#c62828", "#8e0000"};   // các loại hủy
        };
    }

    private static String nhanTrangThai(String tt) {
        return switch (tt) {
            case DatPhong.CHO_COC       -> "Chờ cọc";
            case DatPhong.DA_XAC_NHAN   -> "Đã xác nhận";
            case DatPhong.DANG_LUU_TRU  -> "Đang lưu trú";
            case DatPhong.DA_TRA_PHONG  -> "Đã trả phòng";
            case DatPhong.KHACH_HUY     -> "Khách hủy";
            case DatPhong.KHACH_SAN_HUY -> "Khách sạn hủy";
            case DatPhong.TU_HUY        -> "Tự hủy";
            default -> tt;
        };
    }

    /** Escape chuỗi để nhúng vào JSON. */
    private static String json(String v) {
        if (v == null) return "";
        StringBuilder sb = new StringBuilder();
        for (char ch : v.toCharArray()) {
            switch (ch) {
                case '"'  -> sb.append("\\\"");
                case '\\' -> sb.append("\\\\");
                case '\n' -> sb.append("\\n");
                case '\r' -> sb.append("\\r");
                case '\t' -> sb.append("\\t");
                case '<'  -> sb.append("\\u003c");
                default   -> { if (ch < 0x20) sb.append(String.format("\\u%04x", (int) ch)); else sb.append(ch); }
            }
        }
        return sb.toString();
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
        req.getRequestDispatcher(VIEW + "danh-sach.jsp").forward(req, resp);
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
            req.getRequestDispatcher(VIEW + "chi-tiet.jsp").forward(req, resp);
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
            req.getRequestDispatcher(VIEW + "form-dat.jsp").forward(req, resp);
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
