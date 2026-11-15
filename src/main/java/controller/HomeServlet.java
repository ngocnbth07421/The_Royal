package controller;

import entity.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import repository.*;

import java.io.IOException;
import java.util.*;

@WebServlet(name = "controller.HomeServlet", value = {
        "/trang-chu",
        "/trang-chu/dat-phong",
        "/trang-chu/doi-trang-thai"
})
public class HomeServlet extends HttpServlet {
    private final PhongRepository phongRepository = new PhongRepository();
    private final TangRepository tangRepository = new TangRepository();
    private final ToaNhaRepository toaNhaRepository = new ToaNhaRepository();
    private final LoaiPhongRepository loaiPhongRepository = new LoaiPhongRepository();
    private final TienNghiPhongRepository tienNghiPhongRepository = new TienNghiPhongRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("currentUser") == null) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp?msg=require_login");
            return;
        }

        // Lấy master data
        List<ToaNha> listToaNha = toaNhaRepository.getAll();
        List<Tang> listTang = tangRepository.getAll();
        List<LoaiPhong> listLoaiPhong = loaiPhongRepository.getAll();
        List<Phong> allRooms = phongRepository.getAll();
        List<TienNghiPhong> allTienNghi = tienNghiPhongRepository.getAll();

        Map<Long, ToaNha> mapToaNha = new HashMap<>();
        for (ToaNha tn : listToaNha) mapToaNha.put(tn.getIdToaNha(), tn);

        Map<Long, Tang> mapTang = new HashMap<>();
        for (Tang t : listTang) mapTang.put(t.getIdTang(), t);

        Map<Long, LoaiPhong> mapLoaiPhong = new HashMap<>();
        for (LoaiPhong lp : listLoaiPhong) mapLoaiPhong.put(lp.getIdLoaiPhong(), lp);

        // Đếm trạng thái phòng
        long countReady = 0;
        long countOccupied = 0;
        long countCleaning = 0;
        long countMaintain = 0;

        for (Phong p : allRooms) {
            if ("SAN_SANG".equalsIgnoreCase(p.getTrangThai())) countReady++;
            else if ("DANG_O".equalsIgnoreCase(p.getTrangThai())) countOccupied++;
            else if ("CAN_DON".equalsIgnoreCase(p.getTrangThai())) countCleaning++;
            else if ("BAO_TRI".equalsIgnoreCase(p.getTrangThai())) countMaintain++;
        }

        // Đọc các tham số bộ lọc và tìm kiếm
        String selectedBuildingCode = req.getParameter("building");
        if (selectedBuildingCode == null || selectedBuildingCode.isEmpty()) {
            selectedBuildingCode = "CL"; // Mặc định Clarence
        }

        String filterTangStr = req.getParameter("tang");
        String filterLoaiPhongStr = req.getParameter("loaiPhong");
        String filterTrangThai = req.getParameter("trangThai");
        String filterView = req.getParameter("view");
        String searchKeyword = req.getParameter("search");
        String checkInDate = req.getParameter("checkInDate");
        String checkOutDate = req.getParameter("checkOutDate");

        // Tìm id tòa nhà đang chọn
        Long selectedBuildingId = null;
        for (ToaNha tn : listToaNha) {
            if (tn.getMaToaNha().equalsIgnoreCase(selectedBuildingCode)) {
                selectedBuildingId = tn.getIdToaNha();
                break;
            }
        }
        if (selectedBuildingId == null && !listToaNha.isEmpty()) {
            selectedBuildingId = listToaNha.get(0).getIdToaNha();
        }

        // Lọc danh sách phòng theo tòa nhà và các tiêu chí
        List<Phong> filteredRooms = new ArrayList<>();
        for (Phong p : allRooms) {
            Tang t = mapTang.get(p.getIdTang());
            if (t == null) continue;

            // Lọc tòa nhà
            if (selectedBuildingId != null && !selectedBuildingId.equals(t.getIdToaNha())) {
                continue;
            }

            // Lọc tầng
            if (filterTangStr != null && !filterTangStr.isEmpty() && !filterTangStr.equals("ALL")) {
                try {
                    int ft = Integer.parseInt(filterTangStr);
                    if (t.getSoTang() != ft) continue;
                } catch (NumberFormatException ignored) {}
            }

            // Lọc loại phòng
            if (filterLoaiPhongStr != null && !filterLoaiPhongStr.isEmpty() && !filterLoaiPhongStr.equals("ALL")) {
                try {
                    Long flp = Long.valueOf(filterLoaiPhongStr);
                    if (!flp.equals(p.getIdLoaiPhong())) continue;
                } catch (NumberFormatException ignored) {}
            }

            // Lọc trạng thái
            if (filterTrangThai != null && !filterTrangThai.isEmpty() && !filterTrangThai.equals("ALL")) {
                if (!filterTrangThai.equalsIgnoreCase(p.getTrangThai())) continue;
            }

            // Lọc view
            if (filterView != null && !filterView.isEmpty() && !filterView.equals("ALL")) {
                if (p.getGhiChu() == null || !p.getGhiChu().toLowerCase().contains(filterView.toLowerCase())) {
                    continue;
                }
            }

            // Tìm kiếm mã phòng
            if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
                if (!p.getMaPhong().toLowerCase().contains(searchKeyword.trim().toLowerCase())) {
                    continue;
                }
            }

            filteredRooms.add(p);
        }

        // Phân trang danh sách phòng: ĐÚNG 10 DÒNG DỮ LIỆU / TRANG THEO YÊU CẦU
        int page = 1;
        int pageSize = 10;
        String pageStr = req.getParameter("page");
        if (pageStr != null && !pageStr.isEmpty()) {
            try {
                page = Math.max(1, Integer.parseInt(pageStr));
            } catch (NumberFormatException ignored) {}
        }

        int totalFiltered = filteredRooms.size();
        int totalPages = (int) Math.ceil((double) totalFiltered / pageSize);
        if (totalPages == 0) totalPages = 1;
        if (page > totalPages) page = totalPages;

        int fromIndex = (page - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, totalFiltered);
        List<Phong> pagedRooms = (fromIndex < totalFiltered) ? filteredRooms.subList(fromIndex, toIndex) : Collections.emptyList();

        // Chuẩn bị ma trận sơ đồ phòng theo Tầng (từ danh sách phòng đã lọc hoặc toàn bộ tòa nhà)
        Map<Integer, List<Phong>> matrixFloors = new TreeMap<>(Collections.reverseOrder());
        for (Phong p : filteredRooms) {
            Tang t = mapTang.get(p.getIdTang());
            if (t != null) {
                matrixFloors.computeIfAbsent(t.getSoTang(), k -> new ArrayList<>()).add(p);
            }
        }
        for (List<Phong> roomList : matrixFloors.values()) {
            roomList.sort(Comparator.comparing(Phong::getMaPhong));
        }

        // Đẩy dữ liệu ra view
        req.setAttribute("listToaNha", listToaNha);
        req.setAttribute("listTang", listTang);
        req.setAttribute("listLoaiPhong", listLoaiPhong);
        req.setAttribute("listTienNghi", allTienNghi);
        req.setAttribute("mapToaNha", mapToaNha);
        req.setAttribute("mapTang", mapTang);
        req.setAttribute("mapLoaiPhong", mapLoaiPhong);

        req.setAttribute("totalRooms", allRooms.size());
        req.setAttribute("countReady", countReady);
        req.setAttribute("countOccupied", countOccupied);
        req.setAttribute("countCleaning", countCleaning);
        req.setAttribute("countMaintain", countMaintain);

        req.setAttribute("selectedBuildingCode", selectedBuildingCode);
        req.setAttribute("selectedBuildingId", selectedBuildingId);
        req.setAttribute("filterTang", filterTangStr);
        req.setAttribute("filterLoaiPhong", filterLoaiPhongStr);
        req.setAttribute("filterTrangThai", filterTrangThai);
        req.setAttribute("filterView", filterView);
        req.setAttribute("searchKeyword", searchKeyword);
        req.setAttribute("checkInDate", checkInDate);
        req.setAttribute("checkOutDate", checkOutDate);

        req.setAttribute("pagedRooms", pagedRooms);
        req.setAttribute("matrixFloors", matrixFloors);
        req.setAttribute("currentPage", page);
        req.setAttribute("pageSize", pageSize);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("totalFiltered", totalFiltered);

        req.getRequestDispatcher("/trang-chu/home.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String uri = req.getRequestURI();
        if (uri.contains("dat-phong")) {
            datPhongNhanh(req, resp);
        } else if (uri.contains("doi-trang-thai")) {
            doiTrangThaiPhong(req, resp);
        }
    }

    private void datPhongNhanh(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String idPhongStr = req.getParameter("idPhong");
        String building = req.getParameter("building");

        if (idPhongStr != null && !idPhongStr.isEmpty()) {
            Long idPhong = Long.valueOf(idPhongStr);
            Phong phong = phongRepository.getOne(idPhong);
            if (phong != null) {
                phong.setTrangThai("DANG_O");
                phongRepository.update(phong);
            }
        }

        String redirectUrl = req.getContextPath() + "/trang-chu?success=booking_success";
        if (building != null && !building.isEmpty()) {
            redirectUrl += "&building=" + building;
        }
        resp.sendRedirect(redirectUrl);
    }

    private void doiTrangThaiPhong(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String idPhongStr = req.getParameter("idPhong");
        String trangThaiMoi = req.getParameter("trangThaiMoi");
        String building = req.getParameter("building");

        if (idPhongStr != null && !idPhongStr.isEmpty() && trangThaiMoi != null) {
            Long idPhong = Long.valueOf(idPhongStr);
            Phong phong = phongRepository.getOne(idPhong);
            if (phong != null) {
                phong.setTrangThai(trangThaiMoi.trim());
                phongRepository.update(phong);
            }
        }

        String redirectUrl = req.getContextPath() + "/trang-chu?success=status_updated";
        if (building != null && !building.isEmpty()) {
            redirectUrl += "&building=" + building;
        }
        resp.sendRedirect(redirectUrl);
    }
}
