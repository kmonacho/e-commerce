<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="titrePage" value="Connexion - ShoeBoutique" scope="request"/>
<%@ include file="commun/entete.jsp" %>

<h1>Connexion</h1>
<p class="sous-titre">Connectez-vous pour acceder a votre panier et vos commandes.</p>

<div class="formulaire">
    <c:if test="${not empty erreur}"><div class="alerte erreur">${erreur}</div></c:if>
    <c:if test="${not empty sessionScope.messageInscription}">
        <div class="alerte succes">${sessionScope.messageInscription}</div>
        <c:remove var="messageInscription" scope="session"/>
    </c:if>

    <form action="${pageContext.request.contextPath}/connexion" method="post">
        <div class="champ">
            <label for="email">Email</label>
            <input type="email" id="email" name="email" required value="${not empty email ? email : ''}">
        </div>
        <div class="champ">
            <label for="motDePasse">Mot de passe</label>
            <input type="password" id="motDePasse" name="motDePasse" required>
        </div>
        <div class="champ">
            <label class="case-a-cocher">
                <input type="checkbox" name="accepteCookies">
                J'accepte que ce site utilise des cookies pour ma session et mon panier.
            </label>
        </div>
        <button type="submit" class="bouton" style="width:100%;">Se connecter</button>
    </form>
    <p style="text-align:center; margin-top:1rem; font-size:.9rem;">
        Pas encore de compte ? <a href="${pageContext.request.contextPath}/inscription" style="color:var(--couleur-accent); font-weight:600;">Inscrivez-vous</a>
    </p>
</div>

<%@ include file="commun/pied.jsp" %>
