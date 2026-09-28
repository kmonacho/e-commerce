package com.boutique.dao;

import com.boutique.modele.Role;
import com.boutique.modele.Utilisateur;
import com.boutique.util.ConnexionBD;

import java.sql.*;

public class UtilisateurDAO {

    public Utilisateur trouverParEmail(String email) throws SQLException {
        String sql = "SELECT * FROM utilisateur WHERE email=?";
        try (Connection conn = ConnexionBD.obtenirConnexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapper(rs);
            }
        }
        return null;
    }

    public Utilisateur trouverParId(int id) throws SQLException {
        try (Connection conn = ConnexionBD.obtenirConnexion();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM utilisateur WHERE id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapper(rs);
            }
        }
        return null;
    }

    public boolean emailExiste(String email) throws SQLException {
        return trouverParEmail(email) != null;
    }

    public int creer(Utilisateur u) throws SQLException {
        String sql = "INSERT INTO utilisateur (nom, prenom, email, mot_de_passe_hash, sel, role, accepte_cookies) " +
                     "VALUES (?,?,?,?,?,?,?)";
        try (Connection conn = ConnexionBD.obtenirConnexion();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.getNom());
            ps.setString(2, u.getPrenom());
            ps.setString(3, u.getEmail());
            ps.setString(4, u.getMotDePasseHash());
            ps.setString(5, u.getSel());
            ps.setString(6, u.getRole().name());
            ps.setBoolean(7, u.isAccepteCookies());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    private Utilisateur mapper(ResultSet rs) throws SQLException {
        Utilisateur u = new Utilisateur();
        u.setId(rs.getInt("id"));
        u.setNom(rs.getString("nom"));
        u.setPrenom(rs.getString("prenom"));
        u.setEmail(rs.getString("email"));
        u.setMotDePasseHash(rs.getString("mot_de_passe_hash"));
        u.setSel(rs.getString("sel"));
        u.setRole(Role.valueOf(rs.getString("role")));
        u.setAccepteCookies(rs.getBoolean("accepte_cookies"));
        return u;
    }
}
