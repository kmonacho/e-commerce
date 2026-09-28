package com.boutique.util;

/**
 * Validation d'un numero de carte bancaire :
 * - algorithme de Luhn (cle de controle)
 * - detection du type (VISA / MASTERCARD) via le prefixe et la longueur
 */
public class CarteCreditValidator {

    private CarteCreditValidator() {}

    public enum TypeCarte { VISA, MASTERCARD, INCONNU }

    /** Ne garde que les chiffres du numero saisi (retire espaces / tirets). */
    private static String nettoyer(String numero) {
        return numero == null ? "" : numero.replaceAll("[^0-9]", "");
    }

    /** Algorithme de Luhn : verifie la cle de controle du numero de carte. */
    public static boolean luhnValide(String numeroBrut) {
        String numero = nettoyer(numeroBrut);
        if (numero.length() < 12) return false;

        int somme = 0;
        boolean doubler = false;
        for (int i = numero.length() - 1; i >= 0; i--) {
            int chiffre = numero.charAt(i) - '0';
            if (doubler) {
                chiffre *= 2;
                if (chiffre > 9) chiffre -= 9;
            }
            somme += chiffre;
            doubler = !doubler;
        }
        return somme % 10 == 0;
    }

    /** Determine le type de carte a partir du prefixe (IIN) et de la longueur. */
    public static TypeCarte determinerType(String numeroBrut) {
        String numero = nettoyer(numeroBrut);

        // VISA : commence par 4, longueur 13, 16 ou 19
        if (numero.matches("^4[0-9]{12}(?:[0-9]{3})?(?:[0-9]{3})?$")) {
            return TypeCarte.VISA;
        }
        // MASTERCARD : 51-55xxxx (16 chiffres) ou 2221-2720xxxx (nouvelle plage, 16 chiffres)
        if (numero.matches("^5[1-5][0-9]{14}$")) {
            return TypeCarte.MASTERCARD;
        }
        if (numero.length() == 16) {
            try {
                int prefixe4 = Integer.parseInt(numero.substring(0, 4));
                if (prefixe4 >= 2221 && prefixe4 <= 2720) {
                    return TypeCarte.MASTERCARD;
                }
            } catch (NumberFormatException ignore) { /* pas un numero valide */ }
        }
        return TypeCarte.INCONNU;
    }

    /** Une carte est acceptee si elle passe Luhn ET correspond a Visa ou Mastercard. */
    public static boolean carteValide(String numero) {
        return luhnValide(numero) && determinerType(numero) != TypeCarte.INCONNU;
    }

    /** Validation basique de la date d'expiration au format MM/AA. */
    public static boolean dateExpirationValide(String moisStr, String anneeStr) {
        try {
            int mois = Integer.parseInt(moisStr);
            int annee = Integer.parseInt(anneeStr);
            if (annee < 100) annee += 2000;
            if (mois < 1 || mois > 12) return false;
            java.time.YearMonth expiration = java.time.YearMonth.of(annee, mois);
            return !expiration.isBefore(java.time.YearMonth.now());
        } catch (Exception e) {
            return false;
        }
    }

    /** Le CVV doit contenir 3 ou 4 chiffres. */
    public static boolean cvvValide(String cvv) {
        return cvv != null && cvv.matches("^[0-9]{3,4}$");
    }
}
