package com.boutique.dao;

import com.boutique.modele.ModePaiement;
import com.boutique.util.ConnexionBD;

import java.sql.*;

public class PaiementDAO {

    public void enregistrer(int commandeId, ModePaiement mode, String reference, boolean reussi) throws SQLException {
        String sql = "INSERT INTO paiement (commande_id, mode, reference, statut) VALUES (?,?,?,?)";
        try (Connection conn = ConnexionBD.obtenirConnexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, commandeId);
            ps.setString(2, mode.name());
            ps.setString(3, reference);
            ps.setString(4, reussi ? "REUSSI" : "ECHOUE");
            ps.executeUpdate();
        }
    }
}
