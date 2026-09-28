package com.boutique.modele;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Commande {
    private int id;
    private int utilisateurId;
    private String emailClient;
    private Timestamp dateCommande;
    private StatutCommande statut;
    private double total;
    private ModePaiement modePaiement;
    private List<LigneCommande> lignes = new ArrayList<>();

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUtilisateurId() { return utilisateurId; }
    public void setUtilisateurId(int utilisateurId) { this.utilisateurId = utilisateurId; }
    public String getEmailClient() { return emailClient; }
    public void setEmailClient(String emailClient) { this.emailClient = emailClient; }
    public Timestamp getDateCommande() { return dateCommande; }
    public void setDateCommande(Timestamp dateCommande) { this.dateCommande = dateCommande; }
    public StatutCommande getStatut() { return statut; }
    public void setStatut(StatutCommande statut) { this.statut = statut; }
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
    public ModePaiement getModePaiement() { return modePaiement; }
    public void setModePaiement(ModePaiement modePaiement) { this.modePaiement = modePaiement; }
    public List<LigneCommande> getLignes() { return lignes; }
    public void setLignes(List<LigneCommande> lignes) { this.lignes = lignes; }
}
