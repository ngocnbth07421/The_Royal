package controller;

import dto.TienNghiThongKe;
import entity.TienNghi;
import exception.NghiepVuException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.TienNghiService;
import util.FlashMessage;
import util.RequestUtil;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * URL: /tien-nghi
 *   GET  ?action=danhSach (mặc định) [&tuKhoa=]     -> views/tien-nghi/danh-sach.jsp   (kèm số liệu kho)
 *   GET  ?action=chiTiet&id= [&q=mã phòng]            -> views/tien-nghi/chi-tiet.jsp    (phòng ĐÃ có / CHƯA có)
 *   GET  ?action=form [&id=]                          -> views/tien-nghi/form.jsp        (không id = thêm mới)
 *   GET  ?action=phong&idPhong=                       -> views/tien-nghi/gan-phong.jsp   (1 phòng -> các tiện nghi)
 *   POST action=luu   (id, tenTienNghi, soLuong)
 *   POST action=xoa   (id)
 *   POST action=ganPhong (idPhong, idTienNghi[])
 *   POST action=ganNhieuPhong (idTienNghi, idPhong[], mode = gan | go)
 */
@WebServlet("/tien-nghi")
public class TienNghiServlet extends HttpServlet {
    private static final String VIEW = "/WEB-INF/views/tien-nghi/";
    private final TienNghiService service = new TienNghiService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        FlashMessage.nap(req);
        String action = req.getParameter("action") == null ? "danhSach" : req.getParameter("action");
        try {
            switch (action) {
                case "form"    -> hienForm(req, resp);
                case "phong"   -> hienGanPhong(req, resp);
                case "chiTiet" -> hienChiTiet(req, resp);
                default        -> hienDanhSach(req, resp);
            }
        } catch (NghiepVuException e) {
            FlashMessage.loi(req, e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/tien-nghi");
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
                case "ganPhong" -> ganPhong(req, resp);
                case "ganNhieuPhong" -> ganNhieuPhong(req, resp);
                default -> resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "action không hợp lệ");
            }
        } catch (NghiepVuException e) {
            if ("luu".equals(action)) {
                // Lỗi nhập liệu: quay lại đúng form, giữ nguyên giá trị người dùng vừa nhập
                TienNghi t = new TienNghi(req.getParameter("tenTienNghi"), null);
                t.setId(RequestUtil.soLong(req, "id"));
                req.setAttribute("tienNghi", t);
                req.setAttribute("soLuongNhap", req.getParameter("soLuong"));
                req.setAttribute("loi", e.getMessage());
                req.getRequestDispatcher(VIEW + "form.jsp").forward(req, resp);
                return;
            }
            FlashMessage.loi(req, e.getMessage());
            String idTn = req.getParameter("idTienNghi");
            String idPhong = req.getParameter("idPhong");
            String quayLai = "/tien-nghi";
            if ("ganNhieuPhong".equals(action) && idTn != null) quayLai += "?action=chiTiet&id=" + idTn;
            else if ("ganPhong".equals(action) && idPhong != null) quayLai += "?action=phong&idPhong=" + idPhong;
            resp.sendRedirect(req.getContextPath() + quayLai);
        }
    }

    // ----------------------------------------------------------------- GET
    private void hienDanhSach(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String tuKhoa = RequestUtil.chuoi(req, "tuKhoa");
        req.setAttribute("dsTienNghi", service.danhSachThongKe(tuKhoa));   // List<TienNghiThongKe>
        req.setAttribute("tuKhoa", tuKhoa);
        req.getRequestDispatcher(VIEW + "danh-sach.jsp").forward(req, resp);
    }

    private void hienChiTiet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = RequestUtil.soLong(req, "id");
        if (id == null) throw new NghiepVuException("Thiếu mã tiện nghi.");
        req.setAttribute("chiTiet", service.layChiTiet(id, RequestUtil.chuoi(req, "q")));   // TienNghiChiTiet
        req.getRequestDispatcher(VIEW + "chi-tiet.jsp").forward(req, resp);
    }

    private void hienForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = RequestUtil.soLong(req, "id");
        req.setAttribute("tienNghi", id == null ? null : service.layTheoId(id));
        req.getRequestDispatcher(VIEW + "form.jsp").forward(req, resp);
    }

    private void hienGanPhong(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long idPhong = RequestUtil.soLong(req, "idPhong");
        if (idPhong == null) throw new NghiepVuException("Thiếu mã phòng.");
        List<TienNghi> daGan = service.tienNghiCuaPhong(idPhong);
        Set<Long> idDaGan = daGan.stream().map(TienNghi::getId).collect(Collectors.toSet());
        List<TienNghiThongKe> tatCa = service.danhSachThongKe(null);
        req.setAttribute("phong", service.layPhong(idPhong));
        req.setAttribute("dsThongKe", tatCa);   // mỗi phần tử có getConLai() để biết còn hàng trong kho không
        req.setAttribute("idDaGan", idDaGan);   // JSP: ${idDaGan.contains(...)} để tick sẵn checkbox
        req.getRequestDispatcher(VIEW + "gan-phong.jsp").forward(req, resp);
    }

    // ----------------------------------------------------------------- POST
    private void luu(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = RequestUtil.soLong(req, "id");
        String ten = req.getParameter("tenTienNghi");
        String sl = RequestUtil.chuoi(req, "soLuong");
        Integer soLuong;
        try { soLuong = sl == null ? null : Integer.valueOf(sl); }
        catch (NumberFormatException e) { throw new NghiepVuException("Số lượng phải là số nguyên >= 0."); }

        if (id == null) {
            service.them(ten, soLuong);
            FlashMessage.thanhCong(req, "Đã thêm tiện nghi mới.");
        } else {
            service.capNhat(id, ten, soLuong);
            FlashMessage.thanhCong(req, "Đã cập nhật tiện nghi.");
        }
        resp.sendRedirect(req.getContextPath() + "/tien-nghi");
    }

    private void xoa(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        service.xoa(RequestUtil.soLong(req, "id"));
        FlashMessage.thanhCong(req, "Đã xóa tiện nghi.");
        resp.sendRedirect(req.getContextPath() + "/tien-nghi");
    }

    private void ganPhong(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long idPhong = RequestUtil.soLong(req, "idPhong");
        service.ganChoPhong(idPhong, RequestUtil.danhSachLong(req, "idTienNghi"));
        FlashMessage.thanhCong(req, "Đã cập nhật tiện nghi cho phòng.");
        resp.sendRedirect(req.getContextPath() + "/tien-nghi?action=phong&idPhong=" + idPhong);
    }

    private void ganNhieuPhong(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long idTienNghi = RequestUtil.soLong(req, "idTienNghi");
        boolean gan = !"go".equals(req.getParameter("mode"));
        int soPhong = service.capNhatPhongChoTienNghi(idTienNghi, RequestUtil.danhSachLong(req, "idPhong"), gan);
        FlashMessage.thanhCong(req, (gan ? "Đã gán tiện nghi cho " : "Đã gỡ tiện nghi khỏi ") + soPhong + " phòng.");
        resp.sendRedirect(req.getContextPath() + "/tien-nghi?action=chiTiet&id=" + idTienNghi);
    }
}
