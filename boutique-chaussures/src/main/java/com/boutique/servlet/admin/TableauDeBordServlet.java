package com.boutique.servlet.admin;

import com.boutique.dao.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/admin/tableau-de-bord")
public class TableauDeBordServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setAttribute("nbChaussures", new ChaussureDAO().listerToutes().size());
            req.setAttribute("nbCommandes", new CommandeDAO().listerToutes().size());
            req.setAttribute("nbMessages", new MessageDAO().listerTous().size());
            req.setAttribute("nbPartenaires", new PartenaireDAO().listerTous().size());
            req.getRequestDispatcher("/WEB-INF/views/admin/tableau-de-bord.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
