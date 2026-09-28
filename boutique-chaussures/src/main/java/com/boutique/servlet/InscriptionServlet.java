package com.boutique.servlet;

import com.boutique.dao.UtilisateurDAO;
import com.boutique.modele.Role;
import com.boutique.modele.Utilisateur;
import com.boutique.util.PasswordUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/inscription")
public class InscriptionServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/inscription.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String nom = req.getParameter("nom");
        String prenom = req.getParameter("prenom");
        String email = req.getParameter("email");
        String motDePasse = req.getParameter("motDePasse");
        String confirmation = req.getParameter("confirmation");
        String accepteCookies = req.getParameter("accepteCookies"); // "on" si coche

        req.setAttribute("nom", nom);
        req.setAttribute("prenom", prenom);
        req.setAttribute("email", email);

        try {
            if (nom == null || nom.trim().isEmpty() || prenom == null || prenom.trim().isEmpty()
                    || email == null || !email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
                req.setAttribute("erreur", "Merci de renseigner correctement tous les champs.");
                req.getRequestDispatcher("/WEB-INF/views/inscription.jsp").forward(req, resp);
                return;
            }
            if (!PasswordUtil.longueurValide(motDePasse)) {
                req.setAttribute("erreur", "Le mot de passe doit contenir au moins 8 caracteres.");
                req.getRequestDispatcher("/WEB-INF/views/inscription.jsp").forward(req, resp);
                return;
            }
            if (!motDePasse.equals(confirmation)) {
                req.setAttribute("erreur", "Les deux mots de passe ne correspondent pas.");
                req.getRequestDispatcher("/WEB-INF/views/inscription.jsp").forward(req, resp);
                return;
            }
            if (new UtilisateurDAO().emailExiste(email)) {
                req.setAttribute("erreur", "Un compte existe deja avec cet email.");
                req.getRequestDispatcher("/WEB-INF/views/inscription.jsp").forward(req, resp);
                return;
            }

            Utilisateur u = new Utilisateur();
            u.setNom(nom);
            u.setPrenom(prenom);
            u.setEmail(email);
            u.setRole(Role.CLIENT);
            u.setAccepteCookies("on".equals(accepteCookies));

            String sel = PasswordUtil.genererSel();
            u.setSel(sel);
            u.setMotDePasseHash(PasswordUtil.hacher(motDePasse, sel));

            new UtilisateurDAO().creer(u);

            // Applique le choix "cookies" immediatement si accepte
            if (u.isAccepteCookies()) {
                javax.servlet.http.Cookie cookieConsent = new javax.servlet.http.Cookie("cookiesAcceptes", "true");
                cookieConsent.setMaxAge(60 * 60 * 24 * 365);
                cookieConsent.setPath("/");
                resp.addCookie(cookieConsent);
            }

            req.getSession(true).setAttribute("messageInscription", "Compte cree avec succes, vous pouvez vous connecter.");
            resp.sendRedirect(req.getContextPath() + "/connexion");
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
