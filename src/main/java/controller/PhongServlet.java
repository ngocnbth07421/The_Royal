package controller;

import entity.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import repository.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "controller.PhongServlet", value = {
        "/quan-ly-phong/view",
        "/quan-ly-phong/detail",
        "/quan-ly-phong/delete",
        "/quan-ly-phong/view-update",
        "/quan-ly-phong/update",
        "/quan-ly-phong/add"
})
public class PhongServlet extends HttpServlet {
    private final PhongRepository phongRepository = new PhongRepository();
    private final TangRepository tangRepository = new TangRepository();
    private final ToaNhaRepository toaNhaRepository = new ToaNhaRepository();
    private final LoaiPhongRepository loaiPhongRepository = new LoaiPhongRepository();
    private final TienNghiPhongRepository tienNghiPhongRepository = new TienNghiPhongRepository();
    private final TienNghiRepository tienNghiRepository = new TienNghiRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String uri = req.getRequestURI();
        if (uri.contains("view-update")) {
            viewUpdate(req, resp);
        } else if (uri.contains("detail")) {
            detail(req, resp);
        } else if (uri.contains("delete")) {
            delete(req, resp);
        } else {
            view(req, resp);
        }
    }

    private void prepareMasterData(HttpServletRequest req) {
        List<ToaNha> listToaNha = toaNhaRepository.getAll();
        List<Tang> listTang = tangRepository.getAll();
        List<LoaiPhong> listLoaiPhong = loaiPhongRepository.getAll();

        Map<Long, ToaNha> mapToaNha = new HashMap<>();
        for (ToaNha tn : listToaNha) {
            mapToaNha.put(tn.getIdToaNha(), tn);
        }

        Map<Long, Tang> mapTang = new HashMap<>();
        for (Tang t : listTang) {
            mapTang.put(t.getIdTang(), t);
        }

        Map<Long, LoaiPhong> mapLoaiPhong = new HashMap<>();
        for (LoaiPhong lp : listLoaiPhong) {
            mapLoaiPhong.put(lp.getIdLoaiPhong(), lp);
        }

        req.setAttribute("listToaNha", listToaNha);
        req.setAttribute("listTang", listTang);
        req.setAttribute("listLoaiPhong", listLoaiPhong);
        req.setAttribute("mapToaNha", mapToaNha);
        req.setAttribute("mapTang", mapTang);
        req.setAttribute("mapLoaiPhong", mapLoaiPhong);
    }

    private void view(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        prepareMasterData(req);

        // Phân trang: ĐÚNG 10 DÒNG / TRANG THEO YÊU CẦU
        int page = 1;
        int pageSize = 10;
        String pageParam = req.getParameter("page");
        if (pageParam != null && !pageParam.isEmpty()) {
            try {
                page = Math.max(1, Integer.parseInt(pageParam));
            } catch (NumberFormatException ignored) {}
        }

        long totalCount = phongRepository.count();
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);
        if (totalPages == 0) totalPages = 1;
        if (page > totalPages) page = totalPages;

        List<Phong> listPhong = phongRepository.getPage(page, pageSize);

        req.setAttribute("listPhong", listPhong);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("totalCount", totalCount);

        req.getRequestDispatcher("/quan-ly-phong/view.jsp").forward(req, resp);
    }

    private void detail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/quan-ly-phong/view");
            return;
        }

        Long id = Long.valueOf(idParam);
        Phong phong = phongRepository.getOne(id);
        if (phong == null) {
            resp.sendRedirect(req.getContextPath() + "/quan-ly-phong/view");
            return;
        }

        Tang tang = tangRepository.getOne(phong.getIdTang());
        ToaNha toaNha = (tang != null) ? toaNhaRepository.getOne(tang.getIdToaNha()) : null;
        LoaiPhong loaiPhong = loaiPhongRepository.getOne(phong.getIdLoaiPhong());
        List<TienNghi> listTienNghi = tienNghiPhongRepository.getTienNghiListByPhong(id);

        req.setAttribute("phong", phong);
        req.setAttribute("tang", tang);
        req.setAttribute("toaNha", toaNha);
        req.setAttribute("loaiPhong", loaiPhong);
        req.setAttribute("listTienNghi", listTienNghi);

        req.getRequestDispatcher("/quan-ly-phong/detail.jsp").forward(req, resp);
    }

    private void viewUpdate(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/quan-ly-phong/view");
            return;
        }

        Long id = Long.valueOf(idParam);
        Phong phong = phongRepository.getOne(id);
        if (phong == null) {
            resp.sendRedirect(req.getContextPath() + "/quan-ly-phong/view");
            return;
        }

        prepareMasterData(req);
        req.setAttribute("phong", phong);

        req.getRequestDispatcher("/quan-ly-phong/view-update.jsp").forward(req, resp);
    }

    private void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        if (idParam != null && !idParam.isEmpty()) {
            Long id = Long.valueOf(idParam);
            tienNghiPhongRepository.deleteByPhong(id);
            phongRepository.delete(id);
        }
        resp.sendRedirect(req.getContextPath() + "/quan-ly-phong/view");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String uri = req.getRequestURI();
        if (uri.contains("add")) {
            add(req, resp);
        } else if (uri.contains("update")) {
            update(req, resp);
        }
    }

    private void add(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String maPhong = req.getParameter("maPhong");
        String idTangStr = req.getParameter("idTang");
        String idLoaiPhongStr = req.getParameter("idLoaiPhong");
        String hinhAnhPhong = req.getParameter("hinhAnhPhong");
        String trangThai = req.getParameter("trangThai");
        String ghiChu = req.getParameter("ghiChu");

        if (maPhong == null || maPhong.trim().isEmpty() || idTangStr == null || idLoaiPhongStr == null) {
            resp.sendRedirect(req.getContextPath() + "/quan-ly-phong/view?error=invalid_input");
            return;
        }

        maPhong = maPhong.trim().toUpperCase();
        if (phongRepository.findByMaPhong(maPhong) != null) {
            resp.sendRedirect(req.getContextPath() + "/quan-ly-phong/view?error=duplicate_maphong");
            return;
        }

        Phong phong = new Phong();
        phong.setMaPhong(maPhong);
        phong.setIdTang(Long.valueOf(idTangStr));
        phong.setIdLoaiPhong(Long.valueOf(idLoaiPhongStr));
        phong.setHinhAnhPhong(hinhAnhPhong != null && !hinhAnhPhong.trim().isEmpty() ? hinhAnhPhong.trim() : "assets/rooms/default.jpg");
        phong.setTrangThai(trangThai != null && !trangThai.isEmpty() ? trangThai : "SAN_SANG");
        phong.setGhiChu(ghiChu != null ? ghiChu.trim() : "");

        phongRepository.add(phong);
        resp.sendRedirect(req.getContextPath() + "/quan-ly-phong/view?success=add");
    }

    private void update(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        String maPhong = req.getParameter("maPhong");
        String idTangStr = req.getParameter("idTang");
        String idLoaiPhongStr = req.getParameter("idLoaiPhong");
        String hinhAnhPhong = req.getParameter("hinhAnhPhong");
        String trangThai = req.getParameter("trangThai");
        String ghiChu = req.getParameter("ghiChu");

        if (idParam == null || maPhong == null || maPhong.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/quan-ly-phong/view?error=invalid_input");
            return;
        }

        Long id = Long.valueOf(idParam);
        Phong phong = phongRepository.getOne(id);
        if (phong == null) {
            resp.sendRedirect(req.getContextPath() + "/quan-ly-phong/view");
            return;
        }

        phong.setMaPhong(maPhong.trim().toUpperCase());
        phong.setIdTang(Long.valueOf(idTangStr));
        phong.setIdLoaiPhong(Long.valueOf(idLoaiPhongStr));
        if (hinhAnhPhong != null && !hinhAnhPhong.trim().isEmpty()) {
            phong.setHinhAnhPhong(hinhAnhPhong.trim());
        }
        phong.setTrangThai(trangThai);
        phong.setGhiChu(ghiChu != null ? ghiChu.trim() : "");

        phongRepository.update(phong);
        resp.sendRedirect(req.getContextPath() + "/quan-ly-phong/view?success=update");
    }
}
