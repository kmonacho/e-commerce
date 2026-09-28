<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="titrePage" value="Administration - Partenaires" scope="request"/>
<%@ include file="../commun/entete.jsp" %>

<h1>Gestion des partenaires</h1>
<%@ include file="menu.jsp" %>

<table class="tableau-donnees" style="margin-bottom:2rem;">
    <thead><tr><th>Nom</th><th>Description</th><th>Site web</th><th>Actions</th></tr></thead>
    <tbody>
        <c:forEach var="p" items="${partenaires}">
            <tr>
                <td>${p.nom}</td>
                <td>${p.description}</td>
                <td><c:if test="${not empty p.siteWeb}"><a href="${p.siteWeb}" target="_blank" rel="noopener">${p.siteWeb}</a></c:if></td>
                <td>
                    <a class="bouton petit danger" href="${pageContext.request.contextPath}/admin/partenaires?action=supprimer&id=${p.id}"
                       onclick="return confirm('Supprimer ce partenaire ?');">Supprimer</a>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty partenaires}"><tr><td colspan="4">Aucun partenaire enregistre.</td></tr></c:if>
    </tbody>
</table>

<h2>Ajouter un partenaire</h2>
<div class="formulaire">
    <form action="${pageContext.request.contextPath}/admin/partenaires" method="post">
        <div class="champ">
            <label for="nom">Nom</label>
            <input type="text" id="nom" name="nom" required>
        </div>
        <div class="champ">
            <label for="description">Description</label>
            <textarea id="description" name="description"></textarea>
        </div>
        <div class="champ">
            <label for="logoUrl">URL du logo</label>
            <input type="text" id="logoUrl" name="logoUrl">
        </div>
        <div class="champ">
            <label for="siteWeb">Site web</label>
            <input type="text" id="siteWeb" name="siteWeb" placeholder="https://...">
        </div>
        <button type="submit" class="bouton" style="width:100%;">Ajouter</button>
    </form>
</div>

<%@ include file="../commun/pied.jsp" %>
