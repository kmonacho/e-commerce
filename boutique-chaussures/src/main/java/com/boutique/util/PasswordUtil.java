package com.boutique.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Hachage sale (SHA-256 + sel aleatoire) et evaluation de la force
 * du mot de passe selon 3 criteres : chiffre, majuscule, caractere special.
 */
public class PasswordUtil {

    private static final SecureRandom RANDOM = new SecureRandom();

    private PasswordUtil() {}

    /** Genere un sel aleatoire encode en Base64. */
    public static String genererSel() {
        byte[] sel = new byte[16];
        RANDOM.nextBytes(sel);
        return Base64.getEncoder().encodeToString(sel);
    }

    /** Hache motDePasse+sel avec SHA-256, retourne le condense en hexadecimal. */
    public static String hacher(String motDePasse, String sel) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(sel.getBytes("UTF-8"));
            byte[] resultat = digest.digest(motDePasse.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : resultat) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException | java.io.UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean verifier(String motDePasseSaisi, String sel, String hashAttendu) {
        return hacher(motDePasseSaisi, sel).equals(hashAttendu);
    }

    /** Le mot de passe doit contenir au moins 8 caracteres. */
    public static boolean longueurValide(String motDePasse) {
        return motDePasse != null && motDePasse.length() >= 8;
    }

    public static boolean contientChiffre(String mdp) { return mdp != null && mdp.matches(".*[0-9].*"); }
    public static boolean contientMajuscule(String mdp) { return mdp != null && mdp.matches(".*[A-Z].*"); }
    public static boolean contientCaractereSpecial(String mdp) {
        return mdp != null && mdp.matches(".*[^a-zA-Z0-9].*");
    }

    /**
     * Retourne une indication de force du mot de passe en fonction des 3 criteres :
     * 1. presence d'un chiffre, 2. presence d'une majuscule, 3. presence d'un caractere special.
     * Score 0-3 (nombre de criteres remplis, la longueur >= 8 etant un prerequis).
     */
    public static ResultatForce evaluerForce(String motDePasse) {
        int score = 0;
        if (contientChiffre(motDePasse)) score++;
        if (contientMajuscule(motDePasse)) score++;
        if (contientCaractereSpecial(motDePasse)) score++;

        String libelle;
        if (!longueurValide(motDePasse)) {
            libelle = "Trop court (8 caracteres minimum)";
        } else if (score == 0) {
            libelle = "Faible";
        } else if (score == 1) {
            libelle = "Moyen";
        } else if (score == 2) {
            libelle = "Fort";
        } else {
            libelle = "Tres fort";
        }
        return new ResultatForce(score, libelle);
    }

    public static class ResultatForce {
        public final int score;
        public final String libelle;
        public ResultatForce(int score, String libelle) { this.score = score; this.libelle = libelle; }
    }
}
