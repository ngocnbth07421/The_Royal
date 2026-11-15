package controller;

import entity.TienNghiPhong;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import repository.TienNghiPhongRepository;

import java.io.IOException;

@WebServlet(name = "controller.TienNghiPhongServlet", value = {
        "/tien-nghi-phong/view",
        "/tien-nghi-phong/detail",
        "/tien-nghi-phong/delete",
        "/tien-nghi-phong/view-update",
        "/tien-nghi-phong/update",
        "/tien-nghi-phong/add"
})
public class TienNghiPhongServlet extends HttpServlet {
    private final TienNghiPhongRepository tienNghiPhongRepository = new TienNghiPhongRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("listTienNghiPhong", tienNghiPhongRepository.getAll());
        resp.sendRedirect(req.getContextPath() + "/quan-ly-phong/view");
    }
}
