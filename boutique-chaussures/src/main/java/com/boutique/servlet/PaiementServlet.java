package com.boutique.servlet;

import com.boutique.dao.CarteCadeauDAO;
import com.boutique.dao.CommandeDAO;
import com.boutique.dao.PaiementDAO;
import com.boutique.modele.Commande;
import com.boutique.modele.ModePaiement;
import com.boutique.modele.Utilisateur;
import com.boutique.util.CarteCreditValidator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Gere le paiement d'une commande EN_ATTENTE via 3 moyens :
 *  - CARTE   : verification Visa/Mastercard (Luhn + prefixe) + expiration + CVV
 *  - TWINT   : simulation via un numero de telephone (paiement mobile suisse)
 *  - GOOGLE_GIFT_CARD : verification du code et du solde de la carte cadeau
 */
@WebServlet("/paiement")
public class PaiementServlet extends HttpServlet {

    private final CommandeDAO commandeDAO = new CommandeDAO();
    private final PaiementDAO paiementDAO = new PaiementDAO();
    private final CarteCadeauDAO carteCadeauDAO = new CarteCadeauDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Utilisateur u = utilisateurConnecte(req);
        try {
            int commandeId = Integer.parseInt(req.getParameter("commandeId"));
            Commande commande = commandeDAO.trouverParId(commandeId);
            if (commande == null || commande.getUtilisateurId() != u.getId()) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
            req.setAttribute("commande", commande);
            req.getRequestDispatcher("/WEB-INF/views/paiement.jsp").forward(req, resp);
        } catch (NumberFormatException | SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Utilisateur u = utilisateurConnecte(req);
        try {
            int commandeId = Integer.parseInt(req.getParameter("commandeId"));
            Commande commande = commandeDAO.trouverParId(commandeId);
            if (commande == null || commande.getUtilisateurId() != u.getId()) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            String modeParam = req.getParameter("mode"); // CARTE, TWINT, GOOGLE_GIFT_CARD
            ModePaiement mode = ModePaiement.valueOf(modeParam);
            boolean succes;
            String reference;
            String erreur = null;

            switch (mode) {
                case CARTE:
                    String numeroCarte = req.getParameter("numeroCarte");
                    String mois = req.getParameter("moisExpiration");
                    String annee = req.getParameter("anneeExpiration");
                    String cvv = req.getParameter("cvv");

                    if (!CarteCreditValidator.carteValide(numeroCarte)) {
                        erreur = "Numero de carte invalide : seules les cartes Visa et Mastercard sont acceptees.";
                    } else if (!CarteCreditValidator.dateExpirationValide(mois, annee)) {
                        erreur = "Date d'expiration invalide ou carte expiree.";
                    } else if (!CarteCreditValidator.cvvValide(cvv)) {
                        erreur = "Code CVV invalide.";
                    }
                    succes = (erreur == null);
                    reference = succes
                            ? CarteCreditValidator.determinerType(numeroCarte) + " se terminant par "
                              + numeroCarte.replaceAll("[^0-9]", "").replaceAll(".(?=.{4})", "*")
                            : "ECHEC";
                    break;

                case TWINT:
                    String telephone = req.getParameter("telephoneTwint");
                    // Simulation : un numero suisse valide (format +41 7X XXX XX XX ou 07X XXX XX XX)
                    succes = telephone != null && telephone.matches("^(\\+41|0)7[0-9](\\s?[0-9]{2,3}){3}$");
                    if (!succes) erreur = "Numero de telephone TWINT invalide.";
                    reference = succes ? "TWINT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase() : "ECHEC";
                    break;

                case GOOGLE_GIFT_CARD:
                    String codeCarte = req.getParameter("codeCarteCadeau");
                    succes = carteCadeauDAO.utiliser(codeCarte, commande.getTotal());
                    if (!succes) erreur = "Code de carte cadeau invalide, deja utilise, ou solde insuffisant.";
                    reference = succes ? codeCarte : "ECHEC";
                    break;

                default:
                    erreur = "Moyen de paiement inconnu.";
                    succes = false;
                    reference = "ECHEC";
            }

            paiementDAO.enregistrer(commande.getId(), mode, reference, succes);

            if (succes) {
                commandeDAO.marquerPayee(commande.getId(), mode);
                req.getSession().setAttribute("messageCommande", "Paiement accepte, votre commande est validee !");
                resp.sendRedirect(req.getContextPath() + "/commande");
            } else {
                req.setAttribute("commande", commande);
                req.setAttribute("erreur", erreur);
                req.getRequestDispatcher("/WEB-INF/views/paiement.jsp").forward(req, resp);
            }
        } catch (NumberFormatException | SQLException | IllegalArgumentException e) {
            throw new ServletException(e);
        }
    }

    private Utilisateur utilisateurConnecte(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return (Utilisateur) session.getAttribute("utilisateur");
    }
}
