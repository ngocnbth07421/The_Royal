package controller;

import entity.VaiTro;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import repository.VaiTroRepository;

import java.io.IOException;

@WebServlet(name = "controller.VaiTroServlet", value = {
        "/vai-tro/view",
        "/vai-tro/detail",
        "/vai-tro/delete",
        "/vai-tro/view-update",
        "/vai-tro/update",
        "/vai-tro/add"
})
public class VaiTroServlet extends HttpServlet {
    private final VaiTroRepository vaiTroRepository = new VaiTroRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("listVaiTro", vaiTroRepository.getAll());
        resp.sendRedirect(req.getContextPath() + "/quan-ly-phong/view");
    }
}
