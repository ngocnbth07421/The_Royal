package controller;

import entity.NguoiDung;
import entity.NhanVien;
import entity.VaiTro;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import repository.NguoiDungRepository;
import repository.NhanVienRepository;
import repository.VaiTroRepository;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@WebServlet(name = "controller.NhanVienServlet", value = {
        "/quan-ly-nhan-vien/view",
        "/quan-ly-nhan-vien/detail",
        "/quan-ly-nhan-vien/delete",
        "/quan-ly-nhan-vien/view-update",
        "/quan-ly-nhan-vien/update",
        "/quan-ly-nhan-vien/add"
})
public class NhanVienServlet extends HttpServlet {
    private final NhanVienRepository nhanVienRepository = new NhanVienRepository();
    private final VaiTroRepository vaiTroRepository = new VaiTroRepository();
    private final NguoiDungRepository nguoiDungRepository = new NguoiDungRepository();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

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
        List<VaiTro> listVaiTro = vaiTroRepository.getAll();
        Map<Long, VaiTro> mapVaiTro = new HashMap<>();
        for (VaiTro vt : listVaiTro) {
            mapVaiTro.put(vt.getIdVaiTro(), vt);
        }
        req.setAttribute("listVaiTro", listVaiTro);
        req.setAttribute("mapVaiTro", mapVaiTro);
    }

    private void view(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        prepareMasterData(req);

        List<NhanVien> allStaff = nhanVienRepository.getAll();
        long countHoatDong = 0;
        long countBiKhoa = 0;
        for (NhanVien nv : allStaff) {
            String st = nv.getTrangThai();
            if (st != null && (st.equalsIgnoreCase("Hoạt động") || st.equalsIgnoreCase("HOAT_DONG"))) {
                countHoatDong++;
            } else {
                countBiKhoa++;
            }
        }

        // Phân trang chuẩn 10 dòng / trang
        int page = 1;
        int pageSize = 10;
        String pageParam = req.getParameter("page");
        if (pageParam != null && !pageParam.isEmpty()) {
            try {
                page = Math.max(1, Integer.parseInt(pageParam));
            } catch (NumberFormatException ignored) {}
        }

        int totalStaff = allStaff.size();
        int totalPages = (int) Math.ceil((double) totalStaff / pageSize);
        if (totalPages == 0) totalPages = 1;
        if (page > totalPages) page = totalPages;

        int fromIndex = (page - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, totalStaff);
        List<NhanVien> pagedStaff = (fromIndex < totalStaff) ? allStaff.subList(fromIndex, toIndex) : Collections.emptyList();

        req.setAttribute("listNhanVien", pagedStaff);
        req.setAttribute("totalStaff", totalStaff);
        req.setAttribute("countHoatDong", countHoatDong);
        req.setAttribute("countBiKhoa", countBiKhoa);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("pageSize", pageSize);

        req.getRequestDispatcher("/quan-ly-nhan-vien/view.jsp").forward(req, resp);
    }

    private void detail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/quan-ly-nhan-vien/view");
            return;
        }

        Long id = Long.valueOf(idParam);
        NhanVien nhanVien = nhanVienRepository.getOne(id);
        if (nhanVien == null) {
            resp.sendRedirect(req.getContextPath() + "/quan-ly-nhan-vien/view");
            return;
        }

        VaiTro vaiTro = vaiTroRepository.getOne(nhanVien.getIdVaiTro());
        req.setAttribute("nhanVien", nhanVien);
        req.setAttribute("vaiTro", vaiTro);

        req.getRequestDispatcher("/quan-ly-nhan-vien/detail.jsp").forward(req, resp);
    }

    private void viewUpdate(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/quan-ly-nhan-vien/view");
            return;
        }

        Long id = Long.valueOf(idParam);
        NhanVien nhanVien = nhanVienRepository.getOne(id);
        if (nhanVien == null) {
            resp.sendRedirect(req.getContextPath() + "/quan-ly-nhan-vien/view");
            return;
        }

        prepareMasterData(req);
        req.setAttribute("nhanVien", nhanVien);
        try {
            NguoiDung nd = nguoiDungRepository.findByNhanVien(nhanVien.getIdNhanVien());
            if (nd != null) {
                req.setAttribute("accountEmail", nd.getEmail());
            }
        } catch (Exception ignored) {}

        req.getRequestDispatcher("/quan-ly-nhan-vien/view-update.jsp").forward(req, resp);
    }

    private void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        if (idParam != null && !idParam.isEmpty()) {
            Long id = Long.valueOf(idParam);
            // Xóa người dùng tương ứng trước nếu có
            NguoiDung nd = nguoiDungRepository.findByNhanVien(id);
            if (nd != null) {
                nguoiDungRepository.delete(nd.getIdNguoiDung());
            }
            nhanVienRepository.delete(id);
        }
        resp.sendRedirect(req.getContextPath() + "/quan-ly-nhan-vien/view");
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
        String maNhanVien = req.getParameter("maNhanVien");
        String hoTen = req.getParameter("hoTen");
        String soDienThoai = req.getParameter("soDienThoai");
        String idVaiTroStr = req.getParameter("idVaiTro");
        String ngayVaoLamStr = req.getParameter("ngayVaoLam");
        String luongCoBanStr = req.getParameter("luongCoBan");
        String phuCapStr = req.getParameter("phuCap");
        String khauTruStr = req.getParameter("khauTru");
        String trangThai = req.getParameter("trangThai");
        String email = req.getParameter("email");
        String matKhau = req.getParameter("matKhau");

        if (maNhanVien == null || maNhanVien.trim().isEmpty() || hoTen == null || hoTen.trim().isEmpty() || idVaiTroStr == null) {
            resp.sendRedirect(req.getContextPath() + "/quan-ly-nhan-vien/view?error=invalid_input");
            return;
        }

        maNhanVien = maNhanVien.trim().toUpperCase();
        if (nhanVienRepository.findByMaNhanVien(maNhanVien) != null) {
            resp.sendRedirect(req.getContextPath() + "/quan-ly-nhan-vien/view?error=duplicate_manv");
            return;
        }

        // Chuẩn hóa trạng thái tiếng Việt
        if (trangThai == null || trangThai.trim().isEmpty() || "HOAT_DONG".equalsIgnoreCase(trangThai)) {
            trangThai = "Hoạt động";
        } else if ("BI_KHOA".equalsIgnoreCase(trangThai)) {
            trangThai = "Bị khóa";
        }

        NhanVien nv = new NhanVien();
        nv.setMaNhanVien(maNhanVien);
        nv.setHoTen(hoTen.trim());
        nv.setSoDienThoai(soDienThoai != null ? soDienThoai.trim() : "");
        nv.setIdVaiTro(Long.valueOf(idVaiTroStr));

        if (ngayVaoLamStr != null && !ngayVaoLamStr.trim().isEmpty()) {
            try {
                nv.setNgayVaoLam(dateFormat.parse(ngayVaoLamStr.trim()));
            } catch (ParseException e) {
                nv.setNgayVaoLam(new Date());
            }
        } else {
            nv.setNgayVaoLam(new Date());
        }

        nv.setLuongCoBan(parseBigDecimal(luongCoBanStr, BigDecimal.ZERO));
        nv.setPhuCap(parseBigDecimal(phuCapStr, BigDecimal.ZERO));
        nv.setKhauTru(parseBigDecimal(khauTruStr, BigDecimal.ZERO));
        nv.setTrangThai(trangThai);

        nhanVienRepository.add(nv);

        // Tạo tài khoản người dùng tương ứng cho nhân viên
        // Admin/Quản lý cấp email & mật khẩu khởi tạo (mặc định 123456 nếu để trống)
        try {
            if (email == null || email.trim().isEmpty()) {
                email = maNhanVien.toLowerCase() + "@theroyal.com";
            } else {
                email = email.trim().toLowerCase();
            }

            if (matKhau == null || matKhau.trim().isEmpty()) {
                matKhau = "123456";
            } else {
                matKhau = matKhau.trim();
            }

            // Kiểm tra tránh trùng email người dùng
            if (nguoiDungRepository.findByEmail(email) == null) {
                NguoiDung nd = new NguoiDung();
                nd.setIdNhanVien(nv.getIdNhanVien());
                nd.setEmail(email);
                nd.setMatKhau(matKhau);
                nd.setIdVaiTro(nv.getIdVaiTro());
                nd.setTrangThai(trangThai);
                nguoiDungRepository.add(nd);
            }
        } catch (Exception ignored) {}

        resp.sendRedirect(req.getContextPath() + "/quan-ly-nhan-vien/view?success=add");
    }

    private void update(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        String maNhanVien = req.getParameter("maNhanVien");
        String hoTen = req.getParameter("hoTen");
        String soDienThoai = req.getParameter("soDienThoai");
        String idVaiTroStr = req.getParameter("idVaiTro");
        String ngayVaoLamStr = req.getParameter("ngayVaoLam");
        String luongCoBanStr = req.getParameter("luongCoBan");
        String phuCapStr = req.getParameter("phuCap");
        String khauTruStr = req.getParameter("khauTru");
        String trangThai = req.getParameter("trangThai");
        String email = req.getParameter("email");

        if (idParam == null || maNhanVien == null || maNhanVien.trim().isEmpty() || hoTen == null || hoTen.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/quan-ly-nhan-vien/view?error=invalid_input");
            return;
        }

        Long id = Long.valueOf(idParam);
        NhanVien nv = nhanVienRepository.getOne(id);
        if (nv == null) {
            resp.sendRedirect(req.getContextPath() + "/quan-ly-nhan-vien/view");
            return;
        }

        // Chuẩn hóa trạng thái tiếng Việt
        if (trangThai == null || trangThai.trim().isEmpty() || "HOAT_DONG".equalsIgnoreCase(trangThai)) {
            trangThai = "Hoạt động";
        } else if ("BI_KHOA".equalsIgnoreCase(trangThai)) {
            trangThai = "Bị khóa";
        }

        nv.setMaNhanVien(maNhanVien.trim().toUpperCase());
        nv.setHoTen(hoTen.trim());
        nv.setSoDienThoai(soDienThoai != null ? soDienThoai.trim() : "");
        nv.setIdVaiTro(Long.valueOf(idVaiTroStr));

        if (ngayVaoLamStr != null && !ngayVaoLamStr.trim().isEmpty()) {
            try {
                nv.setNgayVaoLam(dateFormat.parse(ngayVaoLamStr.trim()));
            } catch (ParseException e) {
                // Giữ nguyên ngày cũ nếu parse lỗi
            }
        }

        nv.setLuongCoBan(parseBigDecimal(luongCoBanStr, nv.getLuongCoBan()));
        nv.setPhuCap(parseBigDecimal(phuCapStr, nv.getPhuCap()));
        nv.setKhauTru(parseBigDecimal(khauTruStr, nv.getKhauTru()));
        nv.setTrangThai(trangThai);

        nhanVienRepository.update(nv);

        // Cập nhật vai trò, trạng thái và email tài khoản tương ứng
        try {
            NguoiDung nd = nguoiDungRepository.findByNhanVien(nv.getIdNhanVien());
            if (nd != null) {
                nd.setIdVaiTro(nv.getIdVaiTro());
                nd.setTrangThai(trangThai);
                if (email != null && !email.trim().isEmpty() && email.contains("@")) {
                    nd.setEmail(email.trim().toLowerCase());
                }
                nguoiDungRepository.update(nd);
            }
        } catch (Exception ignored) {}

        resp.sendRedirect(req.getContextPath() + "/quan-ly-nhan-vien/view?success=update");
    }

    private BigDecimal parseBigDecimal(String val, BigDecimal defaultVal) {
        if (val == null || val.trim().isEmpty()) {
            return defaultVal;
        }
        try {
            return new BigDecimal(val.trim());
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }
}
