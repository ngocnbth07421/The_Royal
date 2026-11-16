package controller;

import entity.khach_hang;
import entity.dat_phong;
import entity.danh_gia;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import repository.danh_giaRepository;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "danh_giaServlet",value = {
        "/danh-gia/view",
        "/danh-gia/add",
        "/danh-gia/update",
        "/danh-gia/delete",
        "/danh-gia/detail"
})
public class danh_giaServlet extends HttpServlet {
    danh_giaRepository danh_giaRepository = new danh_giaRepository();

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
        req.setAttribute("listdanh_gia",danh_giaRepository.getAll());
        req.setAttribute("listdat_phong", danh_giaRepository.getAllDatPhong());
        req.setAttribute("listkhach_hang", danh_giaRepository.getAllKhachHang());
        req.getRequestDispatcher("/danh-gia/view.jsp").forward(req,resp);
    }


    private void detail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer id = Integer.parseInt(req.getParameter("id"));
        danh_gia dg = danh_giaRepository.getOne(id);
        req.setAttribute("dg",dg);
        req.setAttribute("listdat_phong", danh_giaRepository.getAllDatPhong());
        req.setAttribute("listkhach_hang", danh_giaRepository.getAllKhachHang());
        req.getRequestDispatcher("/danh-gia/detail.jsp").forward(req,resp);
    }

    private void delete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Integer id = Integer.parseInt(req.getParameter("id"));
        danh_giaRepository.delete(id);
        resp.sendRedirect(req.getContextPath()+ "/danh-gia/view");
    }
    private void viewUpadate(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer id = Integer.parseInt(req.getParameter("id"));
        danh_gia dg = danh_giaRepository.getOne(id);
        req.setAttribute("dg",dg);
        req.setAttribute("listdat_phong", danh_giaRepository.getAllDatPhong());
        req.setAttribute("listkhach_hang", danh_giaRepository.getAllKhachHang());
        req.getRequestDispatcher("/danh-gia/update.jsp").forward(req,resp);
    }
    private void update(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Integer id = Integer.parseInt(req.getParameter("id"));
        Integer idDatPhong = Integer.parseInt(req.getParameter("idDatPhong"));
        Integer idKhachHang = Integer.parseInt(req.getParameter("idKhachHang"));
        Integer diemDanhGia= Integer.parseInt(req.getParameter("diemDanhGia"));
        String binhLuan= req.getParameter("binhLuan");
        Boolean biAn = req.getParameter("biAn") !=null;
        String lyDoAn = req.getParameter("lyDoAn");

        danh_gia dg = new danh_gia();
        dg.setIdDanhGia(id);
        dg.setIdDatPhong(idDatPhong);
        dg.setIdKhachHang(idKhachHang);
        dg.setDiemDanhGia(diemDanhGia);
        dg.setBinhLuan(binhLuan);
        dg.setBiAn(biAn);
        dg.setLyDoAn(lyDoAn);

        danh_giaRepository.update(dg);
        resp.sendRedirect(req.getContextPath() + "/danh-gia/view");
    }

    private void add(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Integer idDatPhong = Integer.parseInt(req.getParameter("idDatPhong"));
        Integer idKhachHang = Integer.parseInt(req.getParameter("idKhachHang"));
        Integer diemDanhGia = Integer.parseInt(req.getParameter("diemDanhGia"));
        String binhLuan = req.getParameter("binhLuan");
        Boolean biAn = req.getParameter("biAn") != null;
        String lyDoAn = req.getParameter("lyDoAn");

        danh_gia dg = new danh_gia();
        dg.setIdDatPhong(idDatPhong);
        dg.setIdKhachHang(idKhachHang);
        dg.setDiemDanhGia(diemDanhGia);
        dg.setBinhLuan(binhLuan);
        dg.setBiAn(biAn);
        dg.setLyDoAn(lyDoAn);

        danh_giaRepository.add(dg);
        resp.sendRedirect(req.getContextPath() + "/danh-gia/view");
    }

    private void showAddForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("listdat_phong", danh_giaRepository.getAllDatPhong());
        req.setAttribute("listkhach_hang", danh_giaRepository.getAllKhachHang());
        req.getRequestDispatcher("/danh-gia/add.jsp").forward(req, resp);
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
