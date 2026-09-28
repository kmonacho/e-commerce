package com.boutique.servlet;

import com.boutique.dao.ChaussureDAO;
import com.boutique.modele.Chaussure;
import com.boutique.modele.Genre;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/** Gere les rubriques /femmes et /hommes (le genre est deduit de l'URL). */
@WebServlet({"/femmes", "/hommes"})
public class CategorieServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String chemin = req.getServletPath();
        Genre genre = chemin.equals("/femmes") ? Genre.FEMME : Genre.HOMME;
        try {
            List<Chaussure> chaussures = new ChaussureDAO().listerParGenre(genre);
            req.setAttribute("chaussures", chaussures);
            req.setAttribute("genre", genre.name());
            req.getRequestDispatcher("/WEB-INF/views/categorie.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
