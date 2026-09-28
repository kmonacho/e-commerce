package com.boutique.dao;

import com.boutique.modele.*;
import com.boutique.util.ConnexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommandeDAO {

    private final PanierDAO panierDAO = new PanierDAO();
    private final TailleDAO tailleDAO = new TailleDAO();

    /**
     * Cree une commande a partir du panier de l'utilisateur, decremente le stock,
     * puis vide le panier. Le tout dans une transaction unique.
     * Retourne l'id de la commande creee, ou -1 si le stock est insuffisant.
     */
    public int passerCommande(int utilisateurId) throws SQLException {
        List<PanierItem> items = panierDAO.listerParUtilisateur(utilisateurId);
        if (items.isEmpty()) return -1;

        try (Connection conn = ConnexionBD.obtenirConnexion()) {
            conn.setAutoCommit(false);
            try {
                double total = 0;
                for (PanierItem item : items) total += item.getSousTotal();

                int commandeId;
                try (PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO commande (utilisateur_id, statut, total) VALUES (?, 'EN_ATTENTE', ?)",
                        Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, utilisateurId);
                    ps.setDouble(2, total);
                    ps.executeUpdate();
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        rs.next();
                        commandeId = rs.getInt(1);
                    }
                }

                for (PanierItem item : items) {
                    boolean ok = tailleDAO.decrementerStock(conn, item.getTailleId(), item.getQuantite());
                    if (!ok) { conn.rollback(); return -1; } // stock insuffisant

                    try (PreparedStatement ps = conn.prepareStatement(
                            "INSERT INTO ligne_commande (commande_id, taille_id, quantite, prix_unitaire) VALUES (?,?,?,?)")) {
                        ps.setInt(1, commandeId);
                        ps.setInt(2, item.getTailleId());
                        ps.setInt(3, item.getQuantite());
                        ps.setDouble(4, item.getPrixUnitaire());
                        ps.executeUpdate();
                    }
                }

                panierDAO.viderPanier(conn, utilisateurId);
                conn.commit();
                return commandeId;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    /** Annule une commande EN_ATTENTE et remet le stock. */
    public boolean annulerCommande(int commandeId, int utilisateurId) throws SQLException {
        try (Connection conn = ConnexionBD.obtenirConnexion()) {
            conn.setAutoCommit(false);
            try {
                Commande cmd = trouverParId(commandeId);
                if (cmd == null || cmd.getUtilisateurId() != utilisateurId || cmd.getStatut() != StatutCommande.EN_ATTENTE) {
                    conn.rollback();
                    return false;
                }
                for (LigneCommande ligne : cmd.getLignes()) {
                    tailleDAO.incrementerStock(conn, ligne.getTailleId(), ligne.getQuantite());
                }
                try (PreparedStatement ps = conn.prepareStatement("UPDATE commande SET statut='ANNULEE' WHERE id=?")) {
                    ps.setInt(1, commandeId);
                    ps.executeUpdate();
                }
                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    public boolean marquerPayee(int commandeId, ModePaiement mode) throws SQLException {
        try (Connection conn = ConnexionBD.obtenirConnexion();
             PreparedStatement ps = conn.prepareStatement(
                     "UPDATE commande SET statut='PAYEE', mode_paiement=? WHERE id=? AND statut='EN_ATTENTE'")) {
            ps.setString(1, mode.name());
            ps.setInt(2, commandeId);
            return ps.executeUpdate() == 1;
        }
    }

    public Commande trouverParId(int id) throws SQLException {
        String sql = "SELECT co.*, u.email FROM commande co JOIN utilisateur u ON u.id = co.utilisateur_id WHERE co.id=?";
        try (Connection conn = ConnexionBD.obtenirConnexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Commande c = mapper(rs);
                    c.setLignes(listerLignes(conn, id));
                    return c;
                }
            }
        }
        return null;
    }

    public List<Commande> listerParUtilisateur(int utilisateurId) throws SQLException {
        String sql = "SELECT co.*, u.email FROM commande co JOIN utilisateur u ON u.id = co.utilisateur_id " +
                     "WHERE co.utilisateur_id=? ORDER BY co.date_commande DESC";
        List<Commande> resultat = new ArrayList<>();
        try (Connection conn = ConnexionBD.obtenirConnexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, utilisateurId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Commande c = mapper(rs);
                    c.setLignes(listerLignes(conn, c.getId()));
                    resultat.add(c);
                }
            }
        }
        return resultat;
    }

    /** Toutes les commandes, pour l'administrateur. */
    public List<Commande> listerToutes() throws SQLException {
        String sql = "SELECT co.*, u.email FROM commande co JOIN utilisateur u ON u.id = co.utilisateur_id " +
                     "ORDER BY co.date_commande DESC";
        List<Commande> resultat = new ArrayList<>();
        try (Connection conn = ConnexionBD.obtenirConnexion();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Commande c = mapper(rs);
                c.setLignes(listerLignes(conn, c.getId()));
                resultat.add(c);
            }
        }
        return resultat;
    }

    private List<LigneCommande> listerLignes(Connection conn, int commandeId) throws SQLException {
        String sql = "SELECT lc.*, c.modele, t.taille_us, t.taille_fr FROM ligne_commande lc " +
                     "JOIN taille t ON t.id = lc.taille_id JOIN chaussure c ON c.id = t.chaussure_id " +
                     "WHERE lc.commande_id=?";
        List<LigneCommande> lignes = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, commandeId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LigneCommande l = new LigneCommande();
                    l.setId(rs.getInt("id"));
                    l.setCommandeId(rs.getInt("commande_id"));
                    l.setTailleId(rs.getInt("taille_id"));
                    l.setQuantite(rs.getInt("quantite"));
                    l.setPrixUnitaire(rs.getDouble("prix_unitaire"));
                    l.setModele(rs.getString("modele"));
                    l.setTailleUs(rs.getDouble("taille_us"));
                    l.setTailleFr(rs.getDouble("taille_fr"));
                    lignes.add(l);
                }
            }
        }
        return lignes;
    }

    private Commande mapper(ResultSet rs) throws SQLException {
        Commande c = new Commande();
        c.setId(rs.getInt("id"));
        c.setUtilisateurId(rs.getInt("utilisateur_id"));
        c.setEmailClient(rs.getString("email"));
        c.setDateCommande(rs.getTimestamp("date_commande"));
        c.setStatut(StatutCommande.valueOf(rs.getString("statut")));
        c.setTotal(rs.getDouble("total"));
        String mode = rs.getString("mode_paiement");
        if (mode != null) c.setModePaiement(ModePaiement.valueOf(mode));
        return c;
    }
}
