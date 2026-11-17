package controller;

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
 *   GET  ?action=danhSach (mặc định) [&tuKhoa=]  -> views/tien-nghi/danh-sach.jsp
 *   GET  ?action=form [&id=]                      -> views/tien-nghi/form.jsp        (không id = thêm mới)
 *   GET  ?action=phong&idPhong=                   -> views/tien-nghi/gan-phong.jsp
 *   POST action=luu   (id, tenTienNghi, soLuong)  -> thêm/sửa rồi redirect về danh sách
 *   POST action=xoa   (id)
 *   POST action=ganPhong (idPhong, idTienNghi[])
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
                case "form"  -> hienForm(req, resp);
                case "phong" -> hienGanPhong(req, resp);
                default      -> hienDanhSach(req, resp);
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
                default -> resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "action không hợp lệ");
            }
        } catch (NghiepVuException e) {
            // Lỗi nhập liệu: quay lại đúng form, giữ nguyên giá trị người dùng vừa nhập
            if ("luu".equals(action)) {
                TienNghi t = new TienNghi(req.getParameter("tenTienNghi"), null);
                t.setId(RequestUtil.soLong(req, "id"));
                req.setAttribute("tienNghi", t);
                req.setAttribute("soLuongNhap", req.getParameter("soLuong"));
                req.setAttribute("loi", e.getMessage());
                req.getRequestDispatcher(VIEW + "form.jsp").forward(req, resp);
            } else {
                FlashMessage.loi(req, e.getMessage());
                resp.sendRedirect(req.getContextPath() + "/tien-nghi");
            }
        }
    }

    // ----------------------------------------------------------------- GET
    private void hienDanhSach(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String tuKhoa = RequestUtil.chuoi(req, "tuKhoa");
        req.setAttribute("dsTienNghi", service.danhSach(tuKhoa));
        req.setAttribute("tuKhoa", tuKhoa);
        req.getRequestDispatcher(VIEW + "danh-sach.jsp").forward(req, resp);
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
        req.setAttribute("phong", service.layPhong(idPhong));
        req.setAttribute("dsTatCaTienNghi", service.danhSach(null));
        req.setAttribute("idDaGan", idDaGan);   // JSP: ${idDaGan.contains(t.id)} để tick sẵn checkbox
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
}
