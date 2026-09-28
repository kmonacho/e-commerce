package com.boutique.util;

import com.boutique.dao.UtilisateurDAO;
import com.boutique.modele.Role;
import com.boutique.modele.Utilisateur;

/**
 * Utilitaire en ligne de commande pour creer le premier compte administrateur
 * (le mot de passe doit etre hache correctement, on ne peut donc pas l'inserer
 * directement en SQL brut).
 *
 * Utilisation :
 *   java -cp target/classes;mysql-connector-j-8.0.32.jar com.boutique.util.CreerAdmin admin@boutique.com MotDePasse1!
 */
public class CreerAdmin {
    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Usage: CreerAdmin <email> <motDePasse>");
            return;
        }
        String email = args[0];
        String motDePasse = args[1];

        if (!PasswordUtil.longueurValide(motDePasse)) {
            System.out.println("Le mot de passe doit contenir au moins 8 caracteres.");
            return;
        }

        Utilisateur admin = new Utilisateur();
        admin.setNom("Admin");
        admin.setPrenom("Boutique");
        admin.setEmail(email);
        admin.setRole(Role.ADMIN);
        admin.setAccepteCookies(true);

        String sel = PasswordUtil.genererSel();
        admin.setSel(sel);
        admin.setMotDePasseHash(PasswordUtil.hacher(motDePasse, sel));

        UtilisateurDAO dao = new UtilisateurDAO();
        dao.creer(admin);
        System.out.println("Administrateur cree avec succes : " + email);
    }
}
