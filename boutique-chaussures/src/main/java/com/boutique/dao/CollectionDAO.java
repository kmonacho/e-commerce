package com.boutique.dao;

import com.boutique.modele.Collection;
import com.boutique.util.ConnexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CollectionDAO {
    public List<Collection> listerToutes() throws SQLException {
        List<Collection> resultat = new ArrayList<>();
        try (Connection conn = ConnexionBD.obtenirConnexion();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM collection ORDER BY nom")) {
            while (rs.next()) resultat.add(new Collection(rs.getInt("id"), rs.getString("nom")));
        }
        return resultat;
    }

    public int creer(String nom) throws SQLException {
        try (Connection conn = ConnexionBD.obtenirConnexion();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO collection (nom) VALUES (?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, nom);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }
}
