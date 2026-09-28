package com.boutique.servlet.admin;

import com.boutique.dao.TailleDAO;
import com.boutique.modele.Taille;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Gestion des tailles (US/FR) et du stock pour chaque modele de chaussure.
 * action=ajouter | modifier-stock | supprimer
 */
@WebServlet("/admin/tailles")
public class AdminTailleServlet extends HttpServlet {

    private final TailleDAO tailleDAO = new TailleDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        int chaussureId = Integer.parseInt(req.getParameter("chaussureId"));
        try {
            switch (action) {
                case "ajouter":
                    Taille t = new Taille();
                    t.setChaussureId(chaussureId);
                    t.setTailleUs(Double.parseDouble(req.getParameter("tailleUs")));
                    t.setTailleFr(Double.parseDouble(req.getParameter("tailleFr")));
                    t.setStock(Integer.parseInt(req.getParameter("stock")));
                    tailleDAO.ajouter(t);
                    break;
                case "modifier-stock":
                    tailleDAO.modifierStock(Integer.parseInt(req.getParameter("tailleId")),
                            Integer.parseInt(req.getParameter("stock")));
                    break;
                case "supprimer":
                    tailleDAO.supprimer(Integer.parseInt(req.getParameter("tailleId")));
                    break;
            }
            resp.sendRedirect(req.getContextPath() + "/admin/chaussures?action=detail-tailles&id=" + chaussureId);
        } catch (SQLException | NumberFormatException e) {
            throw new ServletException(e);
        }
    }
}
