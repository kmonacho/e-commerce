package com.boutique.modele;

public class PanierItem {
    private int id;
    private int utilisateurId;
    private int tailleId;
    private int quantite;

    private String modele;
    private String nomMarque;
    private double tailleUs;
    private double tailleFr;
    private double prixUnitaire;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUtilisateurId() { return utilisateurId; }
    public void setUtilisateurId(int utilisateurId) { this.utilisateurId = utilisateurId; }
    public int getTailleId() { return tailleId; }
    public void setTailleId(int tailleId) { this.tailleId = tailleId; }
    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }
    public String getModele() { return modele; }
    public void setModele(String modele) { this.modele = modele; }
    public String getNomMarque() { return nomMarque; }
    public void setNomMarque(String nomMarque) { this.nomMarque = nomMarque; }
    public double getTailleUs() { return tailleUs; }
    public void setTailleUs(double tailleUs) { this.tailleUs = tailleUs; }
    public double getTailleFr() { return tailleFr; }
    public void setTailleFr(double tailleFr) { this.tailleFr = tailleFr; }
    public double getPrixUnitaire() { return prixUnitaire; }
    public void setPrixUnitaire(double prixUnitaire) { this.prixUnitaire = prixUnitaire; }
    public double getSousTotal() { return prixUnitaire * quantite; }
}
