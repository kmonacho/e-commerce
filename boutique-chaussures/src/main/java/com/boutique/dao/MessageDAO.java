package com.boutique.dao;

import com.boutique.modele.MessageContact;
import com.boutique.util.ConnexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {

    public int creer(MessageContact m) throws SQLException {
        String sql = "INSERT INTO message_contact (nom, email, sujet, message) VALUES (?,?,?,?)";
        try (Connection conn = ConnexionBD.obtenirConnexion();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, m.getNom());
            ps.setString(2, m.getEmail());
            ps.setString(3, m.getSujet());
            ps.setString(4, m.getMessage());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    public List<MessageContact> listerTous() throws SQLException {
        List<MessageContact> resultat = new ArrayList<>();
        try (Connection conn = ConnexionBD.obtenirConnexion();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM message_contact ORDER BY date_envoi DESC")) {
            while (rs.next()) resultat.add(mapper(rs));
        }
        return resultat;
    }

    public void marquerLu(int id) throws SQLException {
        try (Connection conn = ConnexionBD.obtenirConnexion();
             PreparedStatement ps = conn.prepareStatement("UPDATE message_contact SET lu=TRUE WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private MessageContact mapper(ResultSet rs) throws SQLException {
        MessageContact m = new MessageContact();
        m.setId(rs.getInt("id"));
        m.setNom(rs.getString("nom"));
        m.setEmail(rs.getString("email"));
        m.setSujet(rs.getString("sujet"));
        m.setMessage(rs.getString("message"));
        m.setDateEnvoi(rs.getTimestamp("date_envoi"));
        m.setLu(rs.getBoolean("lu"));
        return m;
    }
}
