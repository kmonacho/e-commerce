<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="titrePage" value="${genre == 'FEMME' ? 'Femmes' : 'Hommes'} - ShoeBoutique" scope="request"/>
<%@ include file="commun/entete.jsp" %>

<h1>${genre == 'FEMME' ? 'Chaussures femmes' : 'Chaussures hommes'}</h1>
<p class="sous-titre">Decouvrez notre selection ${genre == 'FEMME' ? 'feminine' : 'masculine'}.</p>

<div class="grille-produits">
    <c:forEach var="c" items="${chaussures}">
        <a class="carte-produit" href="${pageContext.request.contextPath}/chaussure?id=${c.id}">
            <div class="image-produit"
                 <c:if test="${not empty c.imageUrl}">style="background-image:url('${pageContext.request.contextPath}/${c.imageUrl}')"</c:if>>
                <c:if test="${empty c.imageUrl}">Image non disponible</c:if>
            </div>
            <div class="contenu">
                <c:if test="${c.nomCollection != 'Aucune'}">
                    <span class="badge-collection">${c.nomCollection}</span>
                </c:if>
                <span class="marque">${c.nomMarque}</span>
                <span class="modele">${c.modele}</span>
                <span class="prix"><fmt:formatNumber value="${c.prix}" type="currency" currencySymbol="CHF "/></span>
            </div>
        </a>
    </c:forEach>
    <c:if test="${empty chaussures}">
        <p>Aucun modele disponible dans cette rubrique.</p>
    </c:if>
</div>

<%@ include file="commun/pied.jsp" %>
