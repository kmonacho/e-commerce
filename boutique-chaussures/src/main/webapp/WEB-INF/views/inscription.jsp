<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="titrePage" value="Inscription - ShoeBoutique" scope="request"/>
<%@ include file="commun/entete.jsp" %>

<h1>Creer un compte</h1>
<p class="sous-titre">Rejoignez ShoeBoutique en quelques secondes.</p>

<div class="formulaire">
    <c:if test="${not empty erreur}"><div class="alerte erreur">${erreur}</div></c:if>

    <form action="${pageContext.request.contextPath}/inscription" method="post" id="formInscription">
        <div class="champ">
            <label for="prenom">Prenom</label>
            <input type="text" id="prenom" name="prenom" required value="${not empty prenom ? prenom : ''}">
        </div>
        <div class="champ">
            <label for="nom">Nom</label>
            <input type="text" id="nom" name="nom" required value="${not empty nom ? nom : ''}">
        </div>
        <div class="champ">
            <label for="email">Email</label>
            <input type="email" id="email" name="email" required value="${not empty email ? email : ''}">
        </div>
        <div class="champ">
            <label for="motDePasse">Mot de passe</label>
            <input type="password" id="motDePasse" name="motDePasse" required minlength="8">
            <div class="force-mdp">
                <div class="barre"><span id="barreForce"></span></div>
                <div class="libelle" id="libelleForce">Au moins 8 caracteres</div>
                <ul class="criteres">
                    <li id="critereLongueur">8 caracteres minimum</li>
                    <li id="critereChiffre">Au moins un chiffre</li>
                    <li id="critereMajuscule">Au moins une majuscule</li>
                    <li id="critereSpecial">Au moins un caractere special</li>
                </ul>
            </div>
        </div>
        <div class="champ">
            <label for="confirmation">Confirmer le mot de passe</label>
            <input type="password" id="confirmation" name="confirmation" required minlength="8">
        </div>
        <div class="champ">
            <label class="case-a-cocher">
                <input type="checkbox" name="accepteCookies">
                J'accepte que ce site utilise des cookies (session, panier).
            </label>
        </div>
        <button type="submit" class="bouton" style="width:100%;">Creer mon compte</button>
    </form>
</div>

<script>
// Evalue localement la force du mot de passe (les memes regles que cote serveur :
// PasswordUtil.evaluerForce) pour un retour visuel immediat.
const champMdp = document.getElementById('motDePasse');
const barre = document.getElementById('barreForce');
const libelle = document.getElementById('libelleForce');
const couleurs = ['#e94560', '#e94560', '#f0ad4e', '#5cb85c', '#2e7d32'];
const libelles = ['Trop court', 'Faible', 'Moyen', 'Fort', 'Tres fort'];

function majCritere(id, ok) {
    const el = document.getElementById(id);
    el.classList.toggle('ok', ok);
}

champMdp.addEventListener('input', function () {
    const val = champMdp.value;
    const longueurOk = val.length >= 8;
    const chiffreOk = /[0-9]/.test(val);
    const majusculeOk = /[A-Z]/.test(val);
    const specialOk = /[^a-zA-Z0-9]/.test(val);

    majCritere('critereLongueur', longueurOk);
    majCritere('critereChiffre', chiffreOk);
    majCritere('critereMajuscule', majusculeOk);
    majCritere('critereSpecial', specialOk);

    let score = 0;
    if (!longueurOk) {
        barre.style.width = '10%';
        barre.style.background = couleurs[0];
        libelle.textContent = libelles[0];
        libelle.style.color = couleurs[0];
        return;
    }
    if (chiffreOk) score++;
    if (majusculeOk) score++;
    if (specialOk) score++;

    barre.style.width = (25 + score * 25) + '%';
    barre.style.background = couleurs[score + 1];
    libelle.textContent = libelles[score + 1];
    libelle.style.color = couleurs[score + 1];
});
</script>

<%@ include file="commun/pied.jsp" %>
