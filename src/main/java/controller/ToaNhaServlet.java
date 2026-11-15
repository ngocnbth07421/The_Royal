package controller;

import entity.ToaNha;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import repository.ToaNhaRepository;

import java.io.IOException;

@WebServlet(name = "controller.ToaNhaServlet", value = {
        "/toa-nha/view",
        "/toa-nha/detail",
        "/toa-nha/delete",
        "/toa-nha/view-update",
        "/toa-nha/update",
        "/toa-nha/add"
})
public class ToaNhaServlet extends HttpServlet {
    private final ToaNhaRepository toaNhaRepository = new ToaNhaRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("listToaNha", toaNhaRepository.getAll());
        resp.sendRedirect(req.getContextPath() + "/quan-ly-phong/view");
    }
}
