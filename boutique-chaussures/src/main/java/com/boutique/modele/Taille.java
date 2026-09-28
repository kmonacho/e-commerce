package com.boutique.modele;

public class Taille {
    private int id;
    private int chaussureId;
    private double tailleUs;
    private double tailleFr;
    private int stock;

    public Taille() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getChaussureId() { return chaussureId; }
    public void setChaussureId(int chaussureId) { this.chaussureId = chaussureId; }
    public double getTailleUs() { return tailleUs; }
    public void setTailleUs(double tailleUs) { this.tailleUs = tailleUs; }
    public double getTailleFr() { return tailleFr; }
    public void setTailleFr(double tailleFr) { this.tailleFr = tailleFr; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
}
