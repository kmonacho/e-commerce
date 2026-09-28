package com.boutique.dao;

import com.boutique.modele.Partenaire;
import com.boutique.util.ConnexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PartenaireDAO {

    public List<Partenaire> listerTous() throws SQLException {
        List<Partenaire> resultat = new ArrayList<>();
        try (Connection conn = ConnexionBD.obtenirConnexion();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM partenaire ORDER BY nom")) {
            while (rs.next()) resultat.add(mapper(rs));
        }
        return resultat;
    }

    public int creer(Partenaire p) throws SQLException {
        String sql = "INSERT INTO partenaire (nom, description, logo_url, site_web) VALUES (?,?,?,?)";
        try (Connection conn = ConnexionBD.obtenirConnexion();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getNom());
            ps.setString(2, p.getDescription());
            ps.setString(3, p.getLogoUrl());
            ps.setString(4, p.getSiteWeb());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    public void supprimer(int id) throws SQLException {
        try (Connection conn = ConnexionBD.obtenirConnexion();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM partenaire WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private Partenaire mapper(ResultSet rs) throws SQLException {
        Partenaire p = new Partenaire();
        p.setId(rs.getInt("id"));
        p.setNom(rs.getString("nom"));
        p.setDescription(rs.getString("description"));
        p.setLogoUrl(rs.getString("logo_url"));
        p.setSiteWeb(rs.getString("site_web"));
        return p;
    }
}
