package com.boutique.servlet;

import com.boutique.util.PasswordUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Petit endpoint AJAX (appele en JS depuis le formulaire d'inscription) qui renvoie
 * en JSON simple l'indication de force du mot de passe saisi, en fonction de :
 * 1. presence d'un chiffre, 2. presence d'une majuscule, 3. presence d'un caractere special.
 */
@WebServlet("/api/force-mot-de-passe")
public class EvaluerMotDePasseServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String mdp = req.getParameter("motDePasse");
        PasswordUtil.ResultatForce r = PasswordUtil.evaluerForce(mdp == null ? "" : mdp);

        resp.setContentType("application/json;charset=UTF-8");
        try (PrintWriter out = resp.getWriter()) {
            out.printf("{\"score\":%d,\"libelle\":\"%s\",\"longueurValide\":%b,\"chiffre\":%b,\"majuscule\":%b,\"special\":%b}",
                    r.score, r.libelle,
                    PasswordUtil.longueurValide(mdp),
                    PasswordUtil.contientChiffre(mdp),
                    PasswordUtil.contientMajuscule(mdp),
                    PasswordUtil.contientCaractereSpecial(mdp));
        }
    }
}
