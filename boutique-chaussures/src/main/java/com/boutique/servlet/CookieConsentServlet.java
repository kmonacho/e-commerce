package com.boutique.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Permet au visiteur d'accepter ou refuser les cookies depuis le bandeau
 * affiche en bas de page (choix independant du formulaire de connexion/inscription).
 */
@WebServlet("/api/cookies")
public class CookieConsentServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String choix = req.getParameter("choix");
        Cookie cookie = new Cookie("cookiesAcceptes", "accepter".equals(choix) ? "true" : "false");
        cookie.setMaxAge(60 * 60 * 24 * 365);
        cookie.setPath("/");
        resp.addCookie(cookie);

        String referer = req.getHeader("Referer");
        resp.sendRedirect(referer != null ? referer : req.getContextPath() + "/accueil");
    }
}
