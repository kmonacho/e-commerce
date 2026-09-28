<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="titrePage" value="Partenaires - ShoeBoutique" scope="request"/>
<%@ include file="commun/entete.jsp" %>

<h1>Nos partenaires</h1>
<p class="sous-titre">Les distributeurs et grossistes qui nous font confiance.</p>

<div class="grille-partenaires">
    <c:forEach var="p" items="${partenaires}">
        <div class="carte-partenaire">
            <c:if test="${not empty p.logoUrl}">
                <img src="${pageContext.request.contextPath}/${p.logoUrl}" alt="${p.nom}">
            </c:if>
            <h2>${p.nom}</h2>
            <p>${p.description}</p>
            <c:if test="${not empty p.siteWeb}">
                <a class="bouton secondaire petit" href="${p.siteWeb}" target="_blank" rel="noopener">Visiter le site</a>
            </c:if>
        </div>
    </c:forEach>
    <c:if test="${empty partenaires}"><p>Aucun partenaire enregistre pour le moment.</p></c:if>
</div>

<%@ include file="commun/pied.jsp" %>
