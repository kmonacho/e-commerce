package com.boutique.servlet.admin;

import com.boutique.dao.ChaussureDAO;
import com.boutique.dao.CollectionDAO;
import com.boutique.dao.MarqueDAO;
import com.boutique.modele.Chaussure;
import com.boutique.modele.Genre;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;

/**
 * CRUD des modeles de chaussures pour l'administrateur.
 * action=liste (defaut) | nouveau (formulaire) | creer | modifier-formulaire | modifier | supprimer
 */
@WebServlet("/admin/chaussures")
public class AdminChaussureServlet extends HttpServlet {

    private final ChaussureDAO chaussureDAO = new ChaussureDAO();
    private final MarqueDAO marqueDAO = new MarqueDAO();
    private final CollectionDAO collectionDAO = new CollectionDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        action = action == null ? "liste" : action;
        try {
            switch (action) {
                case "nouveau":
                    chargerListesReference(req);
                    req.getRequestDispatcher("/WEB-INF/views/admin/chaussure-formulaire.jsp").forward(req, resp);
                    break;
                case "modifier-formulaire":
                    int id = Integer.parseInt(req.getParameter("id"));
                    req.setAttribute("chaussure", chaussureDAO.trouverParId(id));
                    chargerListesReference(req);
                    req.getRequestDispatcher("/WEB-INF/views/admin/chaussure-formulaire.jsp").forward(req, resp);
                    break;
                case "supprimer":
                    chaussureDAO.supprimer(Integer.parseInt(req.getParameter("id")));
                    resp.sendRedirect(req.getContextPath() + "/admin/chaussures");
                    break;
                case "detail-tailles":
                    int cid = Integer.parseInt(req.getParameter("id"));
                    req.setAttribute("chaussure", chaussureDAO.trouverParId(cid));
                    req.getRequestDispatcher("/WEB-INF/views/admin/tailles.jsp").forward(req, resp);
                    break;
                default:
                    req.setAttribute("chaussures", chaussureDAO.listerToutes());
                    req.getRequestDispatcher("/WEB-INF/views/admin/chaussures.jsp").forward(req, resp);
            }
        } catch (SQLException | NumberFormatException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            Chaussure c = new Chaussure();
            String idParam = req.getParameter("id");
            if (idParam != null && !idParam.isEmpty()) c.setId(Integer.parseInt(idParam));

            c.setModele(req.getParameter("modele"));
            c.setMarqueId(Integer.parseInt(req.getParameter("marqueId")));
            c.setPrix(Double.parseDouble(req.getParameter("prix")));
            c.setGenre(Genre.valueOf(req.getParameter("genre")));
            String collectionId = req.getParameter("collectionId");
            c.setCollectionId(collectionId == null || collectionId.isEmpty() ? 1 : Integer.parseInt(collectionId));
            c.setDescription(req.getParameter("description"));
            c.setImageUrl(req.getParameter("imageUrl"));
            String dateSortie = req.getParameter("dateSortie");
            if (dateSortie != null && !dateSortie.isEmpty()) c.setDateSortie(Date.valueOf(dateSortie));

            if ("modifier".equals(action)) {
                chaussureDAO.modifier(c);
            } else {
                chaussureDAO.creer(c);
            }
            resp.sendRedirect(req.getContextPath() + "/admin/chaussures");
        } catch (SQLException | IllegalArgumentException e) {
            throw new ServletException(e);
        }
    }

    private void chargerListesReference(HttpServletRequest req) throws SQLException {
        req.setAttribute("marques", marqueDAO.listerToutes());
        req.setAttribute("collections", collectionDAO.listerToutes());
        req.setAttribute("genres", Genre.values());
    }
}
