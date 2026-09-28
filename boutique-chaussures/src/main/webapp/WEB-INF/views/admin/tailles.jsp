<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="titrePage" value="Administration - Tailles" scope="request"/>
<%@ include file="../commun/entete.jsp" %>

<h1>Tailles &mdash; ${chaussure.modele}</h1>
<%@ include file="menu.jsp" %>

<table class="tableau-donnees" style="margin-bottom:2rem;">
    <thead><tr><th>Taille US</th><th>Taille FR</th><th>Stock</th><th>Actions</th></tr></thead>
    <tbody>
        <c:forEach var="t" items="${chaussure.tailles}">
            <tr>
                <td>${t.tailleUs}</td>
                <td>${t.tailleFr}</td>
                <td>
                    <form action="${pageContext.request.contextPath}/admin/tailles" method="post" style="display:flex; gap:.4rem;">
                        <input type="hidden" name="action" value="modifier-stock">
                        <input type="hidden" name="chaussureId" value="${chaussure.id}">
                        <input type="hidden" name="tailleId" value="${t.id}">
                        <input type="number" name="stock" value="${t.stock}" min="0" style="width:80px;">
                        <button type="submit" class="bouton petit secondaire">Mettre a jour</button>
                    </form>
                </td>
                <td>
                    <a class="bouton petit danger"
                       href="${pageContext.request.contextPath}/admin/tailles?action=supprimer&chaussureId=${chaussure.id}&tailleId=${t.id}"
                       onclick="event.preventDefault(); document.getElementById('formSupp${t.id}').submit();">Supprimer</a>
                    <form id="formSupp${t.id}" action="${pageContext.request.contextPath}/admin/tailles" method="post" style="display:none;">
                        <input type="hidden" name="action" value="supprimer">
                        <input type="hidden" name="chaussureId" value="${chaussure.id}">
                        <input type="hidden" name="tailleId" value="${t.id}">
                    </form>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty chaussure.tailles}">
            <tr><td colspan="4">Aucune taille enregistree pour ce modele.</td></tr>
        </c:if>
    </tbody>
</table>

<h2>Ajouter une taille</h2>
<div class="formulaire">
    <form action="${pageContext.request.contextPath}/admin/tailles" method="post">
        <input type="hidden" name="action" value="ajouter">
        <input type="hidden" name="chaussureId" value="${chaussure.id}">
        <div class="champ">
            <label for="tailleUs">Taille US</label>
            <input type="number" id="tailleUs" name="tailleUs" step="0.5" required>
        </div>
        <div class="champ">
            <label for="tailleFr">Taille FR</label>
            <input type="number" id="tailleFr" name="tailleFr" step="0.5" required>
        </div>
        <div class="champ">
            <label for="stock">Stock initial</label>
            <input type="number" id="stock" name="stock" min="0" value="0" required>
        </div>
        <button type="submit" class="bouton" style="width:100%;">Ajouter la taille</button>
    </form>
</div>

<%@ include file="../commun/pied.jsp" %>
