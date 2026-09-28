package com.boutique.servlet;

import com.boutique.dao.PanierDAO;
import com.boutique.dao.TailleDAO;
import com.boutique.modele.PanierItem;
import com.boutique.modele.Taille;
import com.boutique.modele.Utilisateur;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Gere le panier. Protege par AuthFiltre : l'utilisateur est forcement connecte ici.
 * action=ajouter (depuis une fiche produit), action=supprimer.
 */
@WebServlet("/panier")
public class PanierServlet extends HttpServlet {

    private final PanierDAO panierDAO = new PanierDAO();
    private final TailleDAO tailleDAO = new TailleDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Utilisateur u = utilisateurConnecte(req);
        try {
            List<PanierItem> items = panierDAO.listerParUtilisateur(u.getId());
            double total = items.stream().mapToDouble(PanierItem::getSousTotal).sum();
            req.setAttribute("items", items);
            req.setAttribute("total", total);
            req.getRequestDispatcher("/WEB-INF/views/panier.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Utilisateur u = utilisateurConnecte(req);
        String action = req.getParameter("action");
        try {
            if ("ajouter".equals(action)) {
                int tailleId = Integer.parseInt(req.getParameter("tailleId"));
                int quantite = Math.max(1, Integer.parseInt(req.getParameter("quantite")));

                Taille t = tailleDAO.trouverParId(tailleId);
                if (t == null || t.getStock() < quantite) {
                    req.getSession().setAttribute("erreurPanier", "Stock insuffisant pour cette taille.");
                } else {
                    panierDAO.ajouterOuIncrementer(u.getId(), tailleId, quantite);
                    req.getSession().setAttribute("messagePanier", "Article ajoute au panier.");
                }
            } else if ("supprimer".equals(action)) {
                int itemId = Integer.parseInt(req.getParameter("itemId"));
                panierDAO.supprimerItem(itemId, u.getId());
            }
        } catch (NumberFormatException | SQLException e) {
            throw new ServletException(e);
        }
        resp.sendRedirect(req.getContextPath() + "/panier");
    }

    private Utilisateur utilisateurConnecte(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return (Utilisateur) session.getAttribute("utilisateur");
    }
}
