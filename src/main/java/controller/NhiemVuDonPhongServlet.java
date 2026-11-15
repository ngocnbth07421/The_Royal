package controller;

import entity.NhiemVuDonPhong;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import repository.NhiemVuDonPhongRepository;

import java.io.IOException;

@WebServlet(name = "controller.NhiemVuDonPhongServlet", value = {
        "/nhiem-vu-don-phong/view",
        "/nhiem-vu-don-phong/detail",
        "/nhiem-vu-don-phong/delete",
        "/nhiem-vu-don-phong/view-update",
        "/nhiem-vu-don-phong/update",
        "/nhiem-vu-don-phong/add"
})
public class NhiemVuDonPhongServlet extends HttpServlet {
    private final NhiemVuDonPhongRepository nhiemVuDonPhongRepository = new NhiemVuDonPhongRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("listNhiemVu", nhiemVuDonPhongRepository.getAll());
        resp.sendRedirect(req.getContextPath() + "/quan-ly-phong/view");
    }
}
