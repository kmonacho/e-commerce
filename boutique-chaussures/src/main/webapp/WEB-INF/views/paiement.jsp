<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="titrePage" value="Paiement - ShoeBoutique" scope="request"/>
<%@ include file="commun/entete.jsp" %>

<h1>Paiement de la commande #${commande.id}</h1>
<p class="sous-titre">Montant a payer : <strong><fmt:formatNumber value="${commande.total}" type="currency" currencySymbol="CHF "/></strong></p>

<c:if test="${not empty erreur}"><div class="alerte erreur" style="max-width:720px; margin:0 auto 1.5rem;">${erreur}</div></c:if>

<div class="formulaire large">
    <div class="onglets-paiement">
        <input type="radio" name="ongletMode" id="ongletCarte" checked onclick="afficherPanneau('carte')">
        <label for="ongletCarte">Carte bancaire</label>

        <input type="radio" name="ongletMode" id="ongletTwint" onclick="afficherPanneau('twint')">
        <label for="ongletTwint">TWINT</label>

        <input type="radio" name="ongletMode" id="ongletGoogle" onclick="afficherPanneau('google')">
        <label for="ongletGoogle">Carte cadeau Google</label>
    </div>

    <!-- Paiement par carte Visa / Mastercard -->
    <form action="${pageContext.request.contextPath}/paiement" method="post" class="panneau-paiement actif" id="panneau-carte">
        <input type="hidden" name="commandeId" value="${commande.id}">
        <input type="hidden" name="mode" value="CARTE">
        <div class="champ">
            <label for="numeroCarte">Numero de carte (Visa ou Mastercard)</label>
            <input type="text" id="numeroCarte" name="numeroCarte" placeholder="4111 1111 1111 1111" required
                   inputmode="numeric" maxlength="19">
            <div class="aide" id="typeCarteDetecte"></div>
        </div>
        <div style="display:flex; gap:1rem;">
            <div class="champ" style="flex:1;">
                <label for="moisExpiration">Mois expiration</label>
                <input type="text" id="moisExpiration" name="moisExpiration" placeholder="MM" maxlength="2" required>
            </div>
            <div class="champ" style="flex:1;">
                <label for="anneeExpiration">Annee expiration</label>
                <input type="text" id="anneeExpiration" name="anneeExpiration" placeholder="AA" maxlength="2" required>
            </div>
            <div class="champ" style="flex:1;">
                <label for="cvv">CVV</label>
                <input type="text" id="cvv" name="cvv" placeholder="123" maxlength="4" required>
            </div>
        </div>
        <button type="submit" class="bouton" style="width:100%;">Payer par carte</button>
    </form>

    <!-- Paiement TWINT -->
    <form action="${pageContext.request.contextPath}/paiement" method="post" class="panneau-paiement" id="panneau-twint">
        <input type="hidden" name="commandeId" value="${commande.id}">
        <input type="hidden" name="mode" value="TWINT">
        <div class="champ">
            <label for="telephoneTwint">Numero de telephone lie a TWINT</label>
            <input type="tel" id="telephoneTwint" name="telephoneTwint" placeholder="079 123 45 67" required>
            <div class="aide">Une notification de confirmation vous sera envoyee dans l'application TWINT.</div>
        </div>
        <button type="submit" class="bouton" style="width:100%;">Payer avec TWINT</button>
    </form>

    <!-- Paiement par carte cadeau Google -->
    <form action="${pageContext.request.contextPath}/paiement" method="post" class="panneau-paiement" id="panneau-google">
        <input type="hidden" name="commandeId" value="${commande.id}">
        <input type="hidden" name="mode" value="GOOGLE_GIFT_CARD">
        <div class="champ">
            <label for="codeCarteCadeau">Code de la carte cadeau Google</label>
            <input type="text" id="codeCarteCadeau" name="codeCarteCadeau" placeholder="GGC-XXXX-XXXX-XXXX" required>
        </div>
        <button type="submit" class="bouton" style="width:100%;">Payer avec la carte cadeau</button>
    </form>
</div>

<script>
function afficherPanneau(nom) {
    document.querySelectorAll('.panneau-paiement').forEach(function (p) { p.classList.remove('actif'); });
    document.getElementById('panneau-' + nom).classList.add('actif');
}

// Detection en direct (cote client, juste pour l'ergonomie) du type de carte,
// la validation definitive (Luhn + prefixe) est toujours refaite cote serveur.
document.getElementById('numeroCarte').addEventListener('input', function (e) {
    const numero = e.target.value.replace(/[^0-9]/g, '');
    let type = 'Type non reconnu';
    if (/^4/.test(numero)) type = 'Visa detectee';
    else if (/^5[1-5]/.test(numero) || /^(222[1-9]|22[3-9][0-9]|2[3-6][0-9]{2}|27[01][0-9]|2720)/.test(numero)) type = 'Mastercard detectee';
    document.getElementById('typeCarteDetecte').textContent = numero.length >= 4 ? type : '';
});
</script>

<%@ include file="commun/pied.jsp" %>
