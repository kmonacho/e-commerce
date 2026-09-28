<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="titrePage" value="Administration - Chaussures" scope="request"/>
<%@ include file="../commun/entete.jsp" %>

<h1>Gestion des chaussures</h1>
<%@ include file="menu.jsp" %>

<p><a class="bouton" href="${pageContext.request.contextPath}/admin/chaussures?action=nouveau">+ Ajouter un modele</a></p>

<table class="tableau-donnees">
    <thead>
        <tr><th>Modele</th><th>Marque</th><th>Genre</th><th>Collection</th><th>Prix</th><th>Actions</th></tr>
    </thead>
    <tbody>
        <c:forEach var="c" items="${chaussures}">
            <tr>
                <td>${c.modele}</td>
                <td>${c.nomMarque}</td>
                <td>${c.genre}</td>
                <td>${c.nomCollection}</td>
                <td><fmt:formatNumber value="${c.prix}" type="currency" currencySymbol="CHF "/></td>
                <td style="display:flex; gap:.4rem;">
                    <a class="bouton petit secondaire" href="${pageContext.request.contextPath}/admin/chaussures?action=modifier-formulaire&id=${c.id}">Modifier</a>
                    <a class="bouton petit secondaire" href="${pageContext.request.contextPath}/admin/chaussures?action=detail-tailles&id=${c.id}">Tailles</a>
                    <a class="bouton petit danger" href="${pageContext.request.contextPath}/admin/chaussures?action=supprimer&id=${c.id}"
                       onclick="return confirm('Supprimer ce modele ?');">Supprimer</a>
                </td>
            </tr>
        </c:forEach>
    </tbody>
</table>

<%@ include file="../commun/pied.jsp" %>
