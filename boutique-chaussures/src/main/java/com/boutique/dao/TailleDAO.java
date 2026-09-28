package com.boutique.dao;

import com.boutique.modele.Taille;
import com.boutique.util.ConnexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TailleDAO {

    public List<Taille> listerParChaussure(int chaussureId) throws SQLException {
        String sql = "SELECT * FROM taille WHERE chaussure_id=? ORDER BY taille_us";
        List<Taille> resultat = new ArrayList<>();
        try (Connection conn = ConnexionBD.obtenirConnexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, chaussureId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) resultat.add(mapper(rs));
            }
        }
        return resultat;
    }

    public Taille trouverParId(int id) throws SQLException {
        try (Connection conn = ConnexionBD.obtenirConnexion();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM taille WHERE id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapper(rs);
            }
        }
        return null;
    }

    public int ajouter(Taille t) throws SQLException {
        String sql = "INSERT INTO taille (chaussure_id, taille_us, taille_fr, stock) VALUES (?,?,?,?)";
        try (Connection conn = ConnexionBD.obtenirConnexion();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, t.getChaussureId());
            ps.setDouble(2, t.getTailleUs());
            ps.setDouble(3, t.getTailleFr());
            ps.setInt(4, t.getStock());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    public void modifierStock(int tailleId, int nouveauStock) throws SQLException {
        try (Connection conn = ConnexionBD.obtenirConnexion();
             PreparedStatement ps = conn.prepareStatement("UPDATE taille SET stock=? WHERE id=?")) {
            ps.setInt(1, nouveauStock);
            ps.setInt(2, tailleId);
            ps.executeUpdate();
        }
    }

    /** Decremente le stock (utilise lors de la validation d'une commande). */
    public boolean decrementerStock(Connection conn, int tailleId, int quantite) throws SQLException {
        String sql = "UPDATE taille SET stock = stock - ? WHERE id = ? AND stock >= ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantite);
            ps.setInt(2, tailleId);
            ps.setInt(3, quantite);
            return ps.executeUpdate() == 1;
        }
    }

    /** Reincremente le stock (utilise lors de l'annulation d'une commande). */
    public void incrementerStock(Connection conn, int tailleId, int quantite) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("UPDATE taille SET stock = stock + ? WHERE id=?")) {
            ps.setInt(1, quantite);
            ps.setInt(2, tailleId);
            ps.executeUpdate();
        }
    }

    public void supprimer(int id) throws SQLException {
        try (Connection conn = ConnexionBD.obtenirConnexion();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM taille WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private Taille mapper(ResultSet rs) throws SQLException {
        Taille t = new Taille();
        t.setId(rs.getInt("id"));
        t.setChaussureId(rs.getInt("chaussure_id"));
        t.setTailleUs(rs.getDouble("taille_us"));
        t.setTailleFr(rs.getDouble("taille_fr"));
        t.setStock(rs.getInt("stock"));
        return t;
    }
}
