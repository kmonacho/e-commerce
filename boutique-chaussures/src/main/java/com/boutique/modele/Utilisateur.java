package com.boutique.modele;

public class Utilisateur {
    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String motDePasseHash;
    private String sel;
    private Role role;
    private boolean accepteCookies;

    public Utilisateur() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getMotDePasseHash() { return motDePasseHash; }
    public void setMotDePasseHash(String motDePasseHash) { this.motDePasseHash = motDePasseHash; }
    public String getSel() { return sel; }
    public void setSel(String sel) { this.sel = sel; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public boolean isAccepteCookies() { return accepteCookies; }
    public void setAccepteCookies(boolean accepteCookies) { this.accepteCookies = accepteCookies; }
    public boolean isAdmin() { return role == Role.ADMIN; }
}
