package com.boutique.servlet.admin;

import com.boutique.dao.PartenaireDAO;
import com.boutique.modele.Partenaire;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/admin/partenaires")
public class AdminPartenaireServlet extends HttpServlet {

    private final PartenaireDAO partenaireDAO = new PartenaireDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            if ("supprimer".equals(action)) {
                partenaireDAO.supprimer(Integer.parseInt(req.getParameter("id")));
                resp.sendRedirect(req.getContextPath() + "/admin/partenaires");
                return;
            }
            req.setAttribute("partenaires", partenaireDAO.listerTous());
            req.getRequestDispatcher("/WEB-INF/views/admin/partenaires.jsp").forward(req, resp);
        } catch (SQLException | NumberFormatException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Partenaire p = new Partenaire();
        p.setNom(req.getParameter("nom"));
        p.setDescription(req.getParameter("description"));
        p.setLogoUrl(req.getParameter("logoUrl"));
        p.setSiteWeb(req.getParameter("siteWeb"));
        try {
            partenaireDAO.creer(p);
            resp.sendRedirect(req.getContextPath() + "/admin/partenaires");
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
