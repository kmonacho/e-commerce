<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="titrePage" value="Mon panier - ShoeBoutique" scope="request"/>
<%@ include file="commun/entete.jsp" %>

<h1>Mon panier</h1>

<c:if test="${not empty sessionScope.messagePanier}">
    <div class="alerte succes">${sessionScope.messagePanier}</div>
    <c:remove var="messagePanier" scope="session"/>
</c:if>
<c:if test="${not empty sessionScope.erreurPanier}">
    <div class="alerte erreur">${sessionScope.erreurPanier}</div>
    <c:remove var="erreurPanier" scope="session"/>
</c:if>

<c:choose>
    <c:when test="${empty items}">
        <p>Votre panier est vide. <a href="${pageContext.request.contextPath}/accueil" style="color:var(--couleur-accent); font-weight:600;">Continuer mes achats</a></p>
    </c:when>
    <c:otherwise>
        <c:forEach var="item" items="${items}">
            <div class="ligne-panier">
                <div class="details">
                    <strong>${item.nomMarque} &mdash; ${item.modele}</strong>
                    <span>Taille US ${item.tailleUs} / FR ${item.tailleFr} &middot; Quantite : ${item.quantite}</span>
                </div>
                <div style="display:flex; align-items:center; gap:1.2rem;">
                    <span><fmt:formatNumber value="${item.sousTotal}" type="currency" currencySymbol="CHF "/></span>
                    <form action="${pageContext.request.contextPath}/panier" method="post">
                        <input type="hidden" name="action" value="supprimer">
                        <input type="hidden" name="itemId" value="${item.id}">
                        <button type="submit" class="bouton danger petit">Retirer</button>
                    </form>
                </div>
            </div>
        </c:forEach>

        <div class="total-panier">Total : <fmt:formatNumber value="${total}" type="currency" currencySymbol="CHF "/></div>

        <form action="${pageContext.request.contextPath}/commande" method="post" style="text-align:right;">
            <button type="submit" class="bouton">Passer la commande</button>
        </form>
    </c:otherwise>
</c:choose>

<%@ include file="commun/pied.jsp" %>
