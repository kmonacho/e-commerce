package com.boutique.modele;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Chaussure {
    private int id;
    private String modele;
    private int marqueId;
    private String nomMarque;
    private Date dateSortie;
    private double prix;
    private Genre genre;
    private int collectionId;
    private String nomCollection;
    private String description;
    private String imageUrl;
    private List<Taille> tailles = new ArrayList<>();

    public Chaussure() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getModele() { return modele; }
    public void setModele(String modele) { this.modele = modele; }
    public int getMarqueId() { return marqueId; }
    public void setMarqueId(int marqueId) { this.marqueId = marqueId; }
    public String getNomMarque() { return nomMarque; }
    public void setNomMarque(String nomMarque) { this.nomMarque = nomMarque; }
    public Date getDateSortie() { return dateSortie; }
    public void setDateSortie(Date dateSortie) { this.dateSortie = dateSortie; }
    public double getPrix() { return prix; }
    public void setPrix(double prix) { this.prix = prix; }
    public Genre getGenre() { return genre; }
    public void setGenre(Genre genre) { this.genre = genre; }
    public int getCollectionId() { return collectionId; }
    public void setCollectionId(int collectionId) { this.collectionId = collectionId; }
    public String getNomCollection() { return nomCollection; }
    public void setNomCollection(String nomCollection) { this.nomCollection = nomCollection; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public List<Taille> getTailles() { return tailles; }
    public void setTailles(List<Taille> tailles) { this.tailles = tailles; }
}
