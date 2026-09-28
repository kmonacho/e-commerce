package com.boutique.servlet;

import com.boutique.dao.UtilisateurDAO;
import com.boutique.modele.Utilisateur;
import com.boutique.util.PasswordUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/connexion")
public class ConnexionServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/connexion.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String motDePasse = req.getParameter("motDePasse");
        String accepteCookies = req.getParameter("accepteCookies");

        try {
            Utilisateur u = new UtilisateurDAO().trouverParEmail(email);
            if (u == null || !PasswordUtil.verifier(motDePasse, u.getSel(), u.getMotDePasseHash())) {
                req.setAttribute("erreur", "Email ou mot de passe incorrect.");
                req.setAttribute("email", email);
                req.getRequestDispatcher("/WEB-INF/views/connexion.jsp").forward(req, resp);
                return;
            }

            HttpSession session = req.getSession(true);
            session.setAttribute("utilisateur", u);

            // Gestion du choix cookies au moment de la connexion
            Cookie cookieConsent = new Cookie("cookiesAcceptes", "on".equals(accepteCookies) ? "true" : "false");
            cookieConsent.setMaxAge(60 * 60 * 24 * 365);
            cookieConsent.setPath("/");
            resp.addCookie(cookieConsent);

            String apresConnexion = (String) session.getAttribute("apresConnexion");
            session.removeAttribute("apresConnexion");

            if (u.isAdmin()) {
                resp.sendRedirect(req.getContextPath() + "/admin/tableau-de-bord");
            } else if (apresConnexion != null) {
                resp.sendRedirect(req.getContextPath() + apresConnexion);
            } else {
                resp.sendRedirect(req.getContextPath() + "/accueil");
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
