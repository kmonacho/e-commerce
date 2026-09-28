package com.boutique.servlet;

import com.boutique.dao.ChaussureDAO;
import com.boutique.modele.Chaussure;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/accueil")
public class AccueilServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Chaussure> chaussures = new ChaussureDAO().listerToutes();
            req.setAttribute("chaussures", chaussures);
            req.getRequestDispatcher("/WEB-INF/views/accueil.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
