package com.boutique.servlet;

import com.boutique.dao.MessageDAO;
import com.boutique.modele.MessageContact;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/contact")
public class ContactServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/contact.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String nom = req.getParameter("nom");
        String email = req.getParameter("email");
        String sujet = req.getParameter("sujet");
        String message = req.getParameter("message");

        if (nom == null || nom.trim().isEmpty() || email == null || !email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
                || message == null || message.trim().isEmpty()) {
            req.setAttribute("erreur", "Merci de renseigner un nom, un email valide et un message.");
            req.getRequestDispatcher("/WEB-INF/views/contact.jsp").forward(req, resp);
            return;
        }

        MessageContact m = new MessageContact();
        m.setNom(nom);
        m.setEmail(email);
        m.setSujet(sujet);
        m.setMessage(message);
        try {
            new MessageDAO().creer(m);
            req.setAttribute("succes", "Votre message a bien ete envoye, merci !");
        } catch (SQLException e) {
            throw new ServletException(e);
        }
        req.getRequestDispatcher("/WEB-INF/views/contact.jsp").forward(req, resp);
    }
}
