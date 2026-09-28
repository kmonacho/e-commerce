package com.boutique.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Fournit une connexion JDBC vers la base MySQL.
 * Adapte URL / UTILISATEUR / MOT_DE_PASSE a ton environnement,
 * ou passe-les en parametres JNDI / variables d'environnement en production.
 */
public class ConnexionBD {

    private static final String URL =
            "jdbc:mysql://localhost:3306/boutique_chaussures?useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8";
    private static final String UTILISATEUR = "root";
    private static final String MOT_DE_PASSE = "Passs1479634564862";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver MySQL introuvable (mysql-connector-j-8.0.32.jar)", e);
        }
    }

    private ConnexionBD() {}

    public static Connection obtenirConnexion() throws SQLException {
        return DriverManager.getConnection(URL, UTILISATEUR, MOT_DE_PASSE);
    }
}
