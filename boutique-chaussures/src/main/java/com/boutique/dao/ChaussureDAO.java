package com.boutique.dao;

import com.boutique.modele.Chaussure;
import com.boutique.modele.Genre;
import com.boutique.modele.Taille;
import com.boutique.util.ConnexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChaussureDAO {

    private static final String SELECT_BASE =
        "SELECT c.id, c.modele, c.marque_id, m.nom AS nom_marque, c.date_sortie, c.prix, c.genre, " +
        "c.collection_id, col.nom AS nom_collection, c.description, c.image_url " +
        "FROM chaussure c " +
        "JOIN marque m ON m.id = c.marque_id " +
        "JOIN collection col ON col.id = c.collection_id ";

    public List<Chaussure> listerToutes() throws SQLException {
        return listerAvecFiltre(null);
    }

    public List<Chaussure> listerParGenre(Genre genre) throws SQLException {
        return listerAvecFiltre(genre);
    }

    private List<Chaussure> listerAvecFiltre(Genre genre) throws SQLException {
        String sql = SELECT_BASE + (genre != null ? "WHERE c.genre = ? OR c.genre = 'UNISEXE' " : "") + "ORDER BY c.id DESC";
        List<Chaussure> resultat = new ArrayList<>();
        try (Connection conn = ConnexionBD.obtenirConnexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (genre != null) ps.setString(1, genre.name());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) resultat.add(mapper(rs));
            }
        }
        return resultat;
    }

    public Chaussure trouverParId(int id) throws SQLException {
        String sql = SELECT_BASE + "WHERE c.id = ?";
        try (Connection conn = ConnexionBD.obtenirConnexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Chaussure ch = mapper(rs);
                    ch.setTailles(new TailleDAO().listerParChaussure(id));
                    return ch;
                }
            }
        }
        return null;
    }

    public int creer(Chaussure c) throws SQLException {
        String sql = "INSERT INTO chaussure (modele, marque_id, date_sortie, prix, genre, collection_id, description, image_url) " +
                     "VALUES (?,?,?,?,?,?,?,?)";
        try (Connection conn = ConnexionBD.obtenirConnexion();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getModele());
            ps.setInt(2, c.getMarqueId());
            ps.setDate(3, c.getDateSortie());
            ps.setDouble(4, c.getPrix());
            ps.setString(5, c.getGenre().name());
            ps.setInt(6, c.getCollectionId());
            ps.setString(7, c.getDescription());
            ps.setString(8, c.getImageUrl());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    public void modifier(Chaussure c) throws SQLException {
        String sql = "UPDATE chaussure SET modele=?, marque_id=?, date_sortie=?, prix=?, genre=?, " +
                     "collection_id=?, description=?, image_url=? WHERE id=?";
        try (Connection conn = ConnexionBD.obtenirConnexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getModele());
            ps.setInt(2, c.getMarqueId());
            ps.setDate(3, c.getDateSortie());
            ps.setDouble(4, c.getPrix());
            ps.setString(5, c.getGenre().name());
            ps.setInt(6, c.getCollectionId());
            ps.setString(7, c.getDescription());
            ps.setString(8, c.getImageUrl());
            ps.setInt(9, c.getId());
            ps.executeUpdate();
        }
    }

    public void supprimer(int id) throws SQLException {
        try (Connection conn = ConnexionBD.obtenirConnexion();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM chaussure WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private Chaussure mapper(ResultSet rs) throws SQLException {
        Chaussure c = new Chaussure();
        c.setId(rs.getInt("id"));
        c.setModele(rs.getString("modele"));
        c.setMarqueId(rs.getInt("marque_id"));
        c.setNomMarque(rs.getString("nom_marque"));
        c.setDateSortie(rs.getDate("date_sortie"));
        c.setPrix(rs.getDouble("prix"));
        c.setGenre(Genre.valueOf(rs.getString("genre")));
        c.setCollectionId(rs.getInt("collection_id"));
        c.setNomCollection(rs.getString("nom_collection"));
        c.setDescription(rs.getString("description"));
        c.setImageUrl(rs.getString("image_url"));
        return c;
    }
}
