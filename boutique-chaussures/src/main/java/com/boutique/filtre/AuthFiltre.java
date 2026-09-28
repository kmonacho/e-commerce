package com.boutique.filtre;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Protege l'acces au panier, a la commande et au paiement :
 * le client doit etre connecte (session avec attribut "utilisateur").
 */
@WebFilter({"/panier", "/commande", "/paiement"})
public class AuthFiltre implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("utilisateur") == null) {
            request.setAttribute("erreur", "Vous devez vous connecter pour acceder a votre panier.");
            request.getSession(true).setAttribute("apresConnexion", request.getRequestURI());
            response.sendRedirect(request.getContextPath() + "/connexion");
            return;
        }
        chain.doFilter(req, resp);
    }
}
