package com.boutique.dao;

import com.boutique.modele.PanierItem;
import com.boutique.util.ConnexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PanierDAO {

    private static final String SELECT_BASE =
        "SELECT p.id, p.utilisateur_id, p.taille_id, p.quantite, " +
        "c.modele, m.nom AS nom_marque, t.taille_us, t.taille_fr, c.prix " +
        "FROM panier_item p " +
        "JOIN taille t ON t.id = p.taille_id " +
        "JOIN chaussure c ON c.id = t.chaussure_id " +
        "JOIN marque m ON m.id = c.marque_id " +
        "WHERE p.utilisateur_id = ?";

    public List<PanierItem> listerParUtilisateur(int utilisateurId) throws SQLException {
        List<PanierItem> resultat = new ArrayList<>();
        try (Connection conn = ConnexionBD.obtenirConnexion();
             PreparedStatement ps = conn.prepareStatement(SELECT_BASE)) {
            ps.setInt(1, utilisateurId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) resultat.add(mapper(rs));
            }
        }
        return resultat;
    }

    public void ajouterOuIncrementer(int utilisateurId, int tailleId, int quantite) throws SQLException {
        String sqlSelect = "SELECT id, quantite FROM panier_item WHERE utilisateur_id=? AND taille_id=?";
        try (Connection conn = ConnexionBD.obtenirConnexion()) {
            try (PreparedStatement ps = conn.prepareStatement(sqlSelect)) {
                ps.setInt(1, utilisateurId);
                ps.setInt(2, tailleId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        int nouvelleQuantite = rs.getInt("quantite") + quantite;
                        try (PreparedStatement psU = conn.prepareStatement("UPDATE panier_item SET quantite=? WHERE id=?")) {
                            psU.setInt(1, nouvelleQuantite);
                            psU.setInt(2, rs.getInt("id"));
                            psU.executeUpdate();
                        }
                        return;
                    }
                }
            }
            try (PreparedStatement psI = conn.prepareStatement(
                    "INSERT INTO panier_item (utilisateur_id, taille_id, quantite) VALUES (?,?,?)")) {
                psI.setInt(1, utilisateurId);
                psI.setInt(2, tailleId);
                psI.setInt(3, quantite);
                psI.executeUpdate();
            }
        }
    }

    public void supprimerItem(int itemId, int utilisateurId) throws SQLException {
        try (Connection conn = ConnexionBD.obtenirConnexion();
             PreparedStatement ps = conn.prepareStatement(
                     "DELETE FROM panier_item WHERE id=? AND utilisateur_id=?")) {
            ps.setInt(1, itemId);
            ps.setInt(2, utilisateurId);
            ps.executeUpdate();
        }
    }

    public void viderPanier(Connection conn, int utilisateurId) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM panier_item WHERE utilisateur_id=?")) {
            ps.setInt(1, utilisateurId);
            ps.executeUpdate();
        }
    }

    private PanierItem mapper(ResultSet rs) throws SQLException {
        PanierItem p = new PanierItem();
        p.setId(rs.getInt("id"));
        p.setUtilisateurId(rs.getInt("utilisateur_id"));
        p.setTailleId(rs.getInt("taille_id"));
        p.setQuantite(rs.getInt("quantite"));
        p.setModele(rs.getString("modele"));
        p.setNomMarque(rs.getString("nom_marque"));
        p.setTailleUs(rs.getDouble("taille_us"));
        p.setTailleFr(rs.getDouble("taille_fr"));
        p.setPrixUnitaire(rs.getDouble("prix"));
        return p;
    }
}
