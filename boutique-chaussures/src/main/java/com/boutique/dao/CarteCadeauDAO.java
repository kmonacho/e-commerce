package com.boutique.dao;

import com.boutique.util.ConnexionBD;

import java.sql.*;

public class CarteCadeauDAO {

    /** Retourne le montant de la carte si le code existe et n'a pas encore ete utilise, sinon -1. */
    public double solde(String code) throws SQLException {
        String sql = "SELECT montant FROM carte_cadeau_google WHERE code=? AND utilisee=FALSE";
        try (Connection conn = ConnexionBD.obtenirConnexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, code);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getDouble("montant");
            }
        }
        return -1;
    }

    /** Marque la carte comme utilisee si son solde couvre le montant demande. */
    public boolean utiliser(String code, double montantRequis) throws SQLException {
        double solde = solde(code);
        if (solde < 0 || solde < montantRequis) return false;
        try (Connection conn = ConnexionBD.obtenirConnexion();
             PreparedStatement ps = conn.prepareStatement(
                     "UPDATE carte_cadeau_google SET utilisee=TRUE WHERE code=? AND utilisee=FALSE")) {
            ps.setString(1, code);
            return ps.executeUpdate() == 1;
        }
    }
}
