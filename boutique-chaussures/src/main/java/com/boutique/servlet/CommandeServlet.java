package com.boutique.servlet;

import com.boutique.dao.CommandeDAO;
import com.boutique.modele.Commande;
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
 * action=lister (par defaut) : historique des commandes du client
 * action=creer : transforme le panier courant en commande EN_ATTENTE, redirige vers /paiement
 * action=annuler : annule une commande EN_ATTENTE et remet le stock
 */
@WebServlet("/commande")
public class CommandeServlet extends HttpServlet {

    private final CommandeDAO commandeDAO = new CommandeDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Utilisateur u = utilisateurConnecte(req);
        String action = req.getParameter("action");
        try {
            if ("annuler".equals(action)) {
                int commandeId = Integer.parseInt(req.getParameter("id"));
                boolean ok = commandeDAO.annulerCommande(commandeId, u.getId());
                req.getSession().setAttribute("messageCommande",
                        ok ? "Commande annulee, le stock a ete restitue." : "Impossible d'annuler cette commande.");
                resp.sendRedirect(req.getContextPath() + "/commande");
                return;
            }

            List<Commande> commandes = commandeDAO.listerParUtilisateur(u.getId());
            req.setAttribute("commandes", commandes);
            req.getRequestDispatcher("/WEB-INF/views/commandes.jsp").forward(req, resp);
        } catch (NumberFormatException | SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Utilisateur u = utilisateurConnecte(req);
        try {
            int commandeId = commandeDAO.passerCommande(u.getId());
            if (commandeId == -1) {
                req.getSession().setAttribute("erreurPanier", "Votre panier est vide ou le stock est insuffisant.");
                resp.sendRedirect(req.getContextPath() + "/panier");
                return;
            }
            resp.sendRedirect(req.getContextPath() + "/paiement?commandeId=" + commandeId);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private Utilisateur utilisateurConnecte(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return (Utilisateur) session.getAttribute("utilisateur");
    }
}
