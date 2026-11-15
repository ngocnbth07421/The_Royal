package controller;

import entity.Tang;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import repository.TangRepository;

import java.io.IOException;

@WebServlet(name = "controller.TangServlet", value = {
        "/tang/view",
        "/tang/detail",
        "/tang/delete",
        "/tang/view-update",
        "/tang/update",
        "/tang/add"
})
public class TangServlet extends HttpServlet {
    private final TangRepository tangRepository = new TangRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("listTang", tangRepository.getAll());
        resp.sendRedirect(req.getContextPath() + "/quan-ly-phong/view");
    }
}
