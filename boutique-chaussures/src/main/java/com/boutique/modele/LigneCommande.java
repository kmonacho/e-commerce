package com.boutique.modele;

public class LigneCommande {
    private int id;
    private int commandeId;
    private int tailleId;
    private int quantite;
    private double prixUnitaire;
    private String modele;
    private double tailleUs;
    private double tailleFr;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getCommandeId() { return commandeId; }
    public void setCommandeId(int commandeId) { this.commandeId = commandeId; }
    public int getTailleId() { return tailleId; }
    public void setTailleId(int tailleId) { this.tailleId = tailleId; }
    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }
    public double getPrixUnitaire() { return prixUnitaire; }
    public void setPrixUnitaire(double prixUnitaire) { this.prixUnitaire = prixUnitaire; }
    public String getModele() { return modele; }
    public void setModele(String modele) { this.modele = modele; }
    public double getTailleUs() { return tailleUs; }
    public void setTailleUs(double tailleUs) { this.tailleUs = tailleUs; }
    public double getTailleFr() { return tailleFr; }
    public void setTailleFr(double tailleFr) { this.tailleFr = tailleFr; }
}
