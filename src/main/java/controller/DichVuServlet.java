
package controller;

import entity.DichVu;
import entity.SuDungDichVu;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import repository.DichVuRepository;
import repository.SuDungDichVuRepository;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@WebServlet(name = "DichVuServlet", value = {
        "/quan-ly-dich-vu/view",
        "/quan-ly-dich-vu/detail",
        "/quan-ly-dich-vu/delete",
        "/quan-ly-dich-vu/view-update",
        "/quan-ly-dich-vu/update",
        "/quan-ly-dich-vu/add",

        "/quan-ly-dich-vu/sddv-detail",
        "/quan-ly-dich-vu/sddv-delete",
        "/quan-ly-dich-vu/sddv-view-update",
        "/quan-ly-dich-vu/sddv-update",
        "/quan-ly-dich-vu/sddv-add"
})
public class DichVuServlet extends HttpServlet {

    private final DichVuRepository dichVuRepository =
            new DichVuRepository();

    private final SuDungDichVuRepository suDungDichVuRepository =
            new SuDungDichVuRepository();

    // =========================
    // LOAD DU LIEU CUA CA 2 BANG
    // =========================

    private void loadData(HttpServletRequest req) {
        String tuKhoa = util.RequestUtil.chuoi(req, "tuKhoa");
        String trangThai = util.RequestUtil.chuoi(req, "trangThai");
        req.setAttribute("tuKhoa", tuKhoa);
        req.setAttribute("trangThai", trangThai);
        req.setAttribute("listDichVu", dichVuRepository.search(tuKhoa, trangThai));
        req.setAttribute("listSuDungDichVu", suDungDichVuRepository.getAll());
    }

    private void showPage(HttpServletRequest req,
                          HttpServletResponse resp)
            throws ServletException, IOException {

        loadData(req);

        req.getRequestDispatcher("/quan-ly-dich-vu/view.jsp")
                .forward(req, resp);
    }

    private void redirectView(HttpServletRequest req,
                              HttpServletResponse resp)
            throws IOException {

        resp.sendRedirect(req.getContextPath() + "/quan-ly-dich-vu/view");
    }

    private void showError(HttpServletRequest req,
                           HttpServletResponse resp,
                           String message)
            throws ServletException, IOException {

        req.setAttribute("errorMessage", message);
        showPage(req, resp);
    }

    // =========================
    // DO GET
    // =========================

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        util.FlashMessage.nap(req);
        String url = req.getServletPath();

        switch (url) {

            case "/quan-ly-dich-vu/view":
                showPage(req, resp);
                break;

            case "/quan-ly-dich-vu/view-update":
                Long idEdit = Long.valueOf(req.getParameter("id"));
                DichVu dvEdit = dichVuRepository.getOne(idEdit);
                if (dvEdit == null) {
                    showError(req, resp, "Không tìm thấy dịch vụ!");
                    return;
                }
                req.setAttribute("dv", dvEdit);
                req.getRequestDispatcher("/quan-ly-dich-vu/view-update.jsp").forward(req, resp);
                break;

            case "/quan-ly-dich-vu/detail":

                Long idDichVu = Long.valueOf(req.getParameter("id"));

                DichVu dv = dichVuRepository.getOne(idDichVu);

                if (dv == null) {
                    showError(req, resp, "Không tìm thấy dịch vụ!");
                    return;
                }

                req.setAttribute("dv", dv);
                showPage(req, resp);
                break;

            // XOA DICH VU
            case "/quan-ly-dich-vu/delete":

                try {
                    Long id = Long.valueOf(req.getParameter("id"));

                    dichVuRepository.delete(id);

                    redirectView(req, resp);

                } catch (Exception e) {
                    e.printStackTrace();

                    showError(req, resp,
                            "Không thể xóa dịch vụ đang được sử dụng!");
                }
                break;

            // SUA SU DUNG DICH VU
            case "/quan-ly-dich-vu/sddv-view-update":
            case "/quan-ly-dich-vu/sddv-detail":

                Long idSuDung = Long.valueOf(req.getParameter("id"));

                SuDungDichVu sddv =
                        suDungDichVuRepository.getOne(idSuDung);

                if (sddv == null) {
                    showError(req, resp,
                            "Không tìm thấy thông tin sử dụng dịch vụ!");
                    return;
                }

                req.setAttribute("sddv", sddv);
                showPage(req, resp);
                break;

            // XOA SU DUNG DICH VU
            case "/quan-ly-dich-vu/sddv-delete":

                try {
                    Long id = Long.valueOf(req.getParameter("id"));

                    suDungDichVuRepository.delete(id);

                    redirectView(req, resp);

                } catch (Exception e) {
                    e.printStackTrace();

                    showError(req, resp,
                            "Không thể xóa thông tin sử dụng dịch vụ!");
                }
                break;

            default:
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
        }
    }

    // =========================
    // DO POST
    // =========================

    @Override
    protected void doPost(HttpServletRequest req,
                          HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String url = req.getServletPath();

        try {

            switch (url) {

                // =====================
                // THEM DICH VU
                // =====================

                case "/quan-ly-dich-vu/add":

                    DichVu dv = new DichVu();

                    dv.setMa_dich_vu(
                            req.getParameter("ma_dich_vu"));

                    dv.setTen_dich_vu(
                            req.getParameter("ten_dich_vu"));

                    dv.setDanh_muc(
                            req.getParameter("danh_muc"));

                    dv.setDon_vi_tinh(
                            req.getParameter("don_vi_tinh"));

                    dv.setDon_gia(
                            new BigDecimal(req.getParameter("don_gia")));

                    dv.setTrang_thai(
                            req.getParameter("trang_thai"));

                    dichVuRepository.add(dv);

                    redirectView(req, resp);
                    break;

                // =====================
                // CAP NHAT DICH VU
                // =====================

                case "/quan-ly-dich-vu/update":

                    Long id = Long.valueOf(
                            req.getParameter("id"));

                    DichVu dvUpdate = new DichVu();

                    dvUpdate.setId_dich_vu(id);

                    dvUpdate.setMa_dich_vu(
                            req.getParameter("ma_dich_vu"));

                    dvUpdate.setTen_dich_vu(
                            req.getParameter("ten_dich_vu"));

                    dvUpdate.setDanh_muc(
                            req.getParameter("danh_muc"));

                    dvUpdate.setDon_vi_tinh(
                            req.getParameter("don_vi_tinh"));

                    dvUpdate.setDon_gia(
                            new BigDecimal(req.getParameter("don_gia")));

                    dvUpdate.setTrang_thai(
                            req.getParameter("trang_thai"));

                    dichVuRepository.update(dvUpdate);

                    redirectView(req, resp);
                    break;

                // =====================
                // THEM SU DUNG DICH VU
                // =====================

                case "/quan-ly-dich-vu/sddv-add":

                    SuDungDichVu sddv = new SuDungDichVu();

                    sddv.setId_dat_phong(
                            Long.valueOf(req.getParameter("id_dat_phong")));

                    sddv.setId_dich_vu(
                            Long.valueOf(req.getParameter("id_dich_vu")));

                    sddv.setSo_luong(
                            new BigDecimal(req.getParameter("so_luong")));

                    sddv.setThanh_tien(
                            new BigDecimal(req.getParameter("thanh_tien")));

                    sddv.setTien_giam_gia(
                            new BigDecimal(req.getParameter("tien_giam_gia")));

                    String thoiGianThem =
                            req.getParameter("thoi_gian_su_dung");

                    if (thoiGianThem == null || thoiGianThem.isEmpty()) {
                        sddv.setThoi_gian_su_dung(LocalDateTime.now());
                    } else {
                        sddv.setThoi_gian_su_dung(
                                LocalDateTime.parse(thoiGianThem));
                    }

                    suDungDichVuRepository.add(sddv);

                    redirectView(req, resp);
                    break;

                // =====================
                // CAP NHAT SU DUNG DICH VU
                // =====================

                case "/quan-ly-dich-vu/sddv-update":

                    Long idSddv = Long.valueOf(
                            req.getParameter("id"));

                    SuDungDichVu sddvUpdate = new SuDungDichVu();

                    sddvUpdate.setId_su_dung_dich_vu(idSddv);

                    sddvUpdate.setId_dat_phong(
                            Long.valueOf(req.getParameter("id_dat_phong")));

                    sddvUpdate.setId_dich_vu(
                            Long.valueOf(req.getParameter("id_dich_vu")));

                    sddvUpdate.setSo_luong(
                            new BigDecimal(req.getParameter("so_luong")));

                    sddvUpdate.setThanh_tien(
                            new BigDecimal(req.getParameter("thanh_tien")));

                    sddvUpdate.setTien_giam_gia(
                            new BigDecimal(req.getParameter("tien_giam_gia")));

                    String thoiGianSua =
                            req.getParameter("thoi_gian_su_dung");

                    if (thoiGianSua == null || thoiGianSua.isEmpty()) {
                        SuDungDichVu old =
                                suDungDichVuRepository.getOne(idSddv);

                        sddvUpdate.setThoi_gian_su_dung(
                                old != null
                                        ? old.getThoi_gian_su_dung()
                                        : LocalDateTime.now());
                    } else {
                        sddvUpdate.setThoi_gian_su_dung(
                                LocalDateTime.parse(thoiGianSua));
                    }

                    suDungDichVuRepository.update(sddvUpdate);

                    redirectView(req, resp);
                    break;

                default:
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                    break;
            }

        } catch (Exception e) {

            e.printStackTrace();

            showError(req, resp,
                    "Thao tác thất bại! Kiểm tra dữ liệu nhập và khóa ngoại trong database.");
        }
    }
}
