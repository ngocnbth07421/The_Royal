package controller;

import entity.KhachHang;
import entity.DanhGia;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import repository.DanhGiaRepository;

import java.io.IOException;

@WebServlet(name = "danh_giaServlet",value = {
        "/quan-ly-danh-gia/view",
        "/quan-ly-danh-gia/add",
        "/quan-ly-danh-gia/update",
        "/quan-ly-danh-gia/delete",
        "/quan-ly-danh-gia/detail"
})
public class DanhGiaServlet extends HttpServlet {
    DanhGiaRepository DanhGiaRepository = new DanhGiaRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String dg = req.getRequestURI();
        if (dg.contains("view")){
            view(req,resp);
        }else if(dg.contains("add")){
            showAddForm(req,resp);
        }else if(dg.contains("update")){
            viewUpadate(req,resp);
        }else if(dg.contains("delete")){
            delete(req,resp);
        }else if(dg.contains("detail")){
            detail(req,resp);
        }
    }
    private void view(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("listdanh_gia",DanhGiaRepository.getAll());
        req.setAttribute("listdat_phong", DanhGiaRepository.getAllDatPhong());
        req.setAttribute("listkhach_hang", DanhGiaRepository.getAllKhachHang());
        req.getRequestDispatcher("/quan-ly-danh-gia/view.jsp").forward(req,resp);
    }


    private void detail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer id = Integer.parseInt(req.getParameter("id"));
        DanhGia dg = DanhGiaRepository.getOne(id);
        req.setAttribute("dg",dg);
        req.setAttribute("listdat_phong", DanhGiaRepository.getAllDatPhong());
        req.setAttribute("listkhach_hang", DanhGiaRepository.getAllKhachHang());
        req.getRequestDispatcher("/quan-ly-danh-gia/detail.jsp").forward(req,resp);
    }

    private void delete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Integer id = Integer.parseInt(req.getParameter("id"));
        DanhGiaRepository.delete(id);
        resp.sendRedirect(req.getContextPath()+ "/quan-ly-danh-gia/view");
    }
    private void viewUpadate(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer id = Integer.parseInt(req.getParameter("id"));
        DanhGia dg = DanhGiaRepository.getOne(id);
        req.setAttribute("dg",dg);
        req.setAttribute("listdat_phong", DanhGiaRepository.getAllDatPhong());
        req.setAttribute("listkhach_hang", DanhGiaRepository.getAllKhachHang());
        req.getRequestDispatcher("/quan-ly-danh-gia/update.jsp").forward(req,resp);
    }
    private void update(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Integer id = Integer.parseInt(req.getParameter("id"));
        Integer idDatPhong = Integer.parseInt(req.getParameter("idDatPhong"));
        Integer idKhachHang = Integer.parseInt(req.getParameter("idKhachHang"));
        Integer diemDanhGia= Integer.parseInt(req.getParameter("diemDanhGia"));
        String binhLuan= req.getParameter("binhLuan");
        Boolean biAn = req.getParameter("biAn") !=null;
        String lyDoAn = req.getParameter("lyDoAn");

        DanhGia dg = new DanhGia();
        dg.setIdDanhGia(id);
        dg.setIdDatPhong(idDatPhong);
        dg.setIdKhachHang(idKhachHang);
        dg.setDiemDanhGia(diemDanhGia);
        dg.setBinhLuan(binhLuan);
        dg.setBiAn(biAn);
        dg.setLyDoAn(lyDoAn);

        DanhGiaRepository.update(dg);
        resp.sendRedirect(req.getContextPath() + "/quan-ly-danh-gia/view");
    }

    private void add(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Integer idDatPhong = Integer.parseInt(req.getParameter("idDatPhong"));
        Integer idKhachHang = Integer.parseInt(req.getParameter("idKhachHang"));
        Integer diemDanhGia = Integer.parseInt(req.getParameter("diemDanhGia"));
        String binhLuan = req.getParameter("binhLuan");
        Boolean biAn = req.getParameter("biAn") != null;
        String lyDoAn = req.getParameter("lyDoAn");

        DanhGia dg = new DanhGia();
        dg.setIdDatPhong(idDatPhong);
        dg.setIdKhachHang(idKhachHang);
        dg.setDiemDanhGia(diemDanhGia);
        dg.setBinhLuan(binhLuan);
        dg.setBiAn(biAn);
        dg.setLyDoAn(lyDoAn);

        DanhGiaRepository.add(dg);
        resp.sendRedirect(req.getContextPath() + "/quan-ly-danh-gia/view");
    }

    private void showAddForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("listdat_phong", DanhGiaRepository.getAllDatPhong());
        req.setAttribute("listkhach_hang", DanhGiaRepository.getAllKhachHang());
        req.getRequestDispatcher("/quan-ly-danh-gia/add.jsp").forward(req, resp);
    }



    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String dg = req.getRequestURI();
        if (dg.contains("add")) {
            add(req, resp);
        } else if (dg.contains("update")) {
            update(req, resp);
        }
    }
}

