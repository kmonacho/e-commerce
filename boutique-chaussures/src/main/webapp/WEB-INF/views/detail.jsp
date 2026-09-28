<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="titrePage" value="${chaussure.modele} - ShoeBoutique" scope="request"/>
<%@ include file="commun/entete.jsp" %>

<div style="display:flex; gap:2.5rem; flex-wrap:wrap;">
    <div class="image-produit" style="flex:1; min-width:280px; max-width:420px; aspect-ratio:1;
         <c:if test="${not empty chaussure.imageUrl}">background-image:url('${pageContext.request.contextPath}/${chaussure.imageUrl}')</c:if>">
        <c:if test="${empty chaussure.imageUrl}">Image non disponible</c:if>
    </div>

    <div style="flex:1; min-width:280px;">
        <c:if test="${chaussure.nomCollection != 'Aucune'}">
            <span class="badge-collection">${chaussure.nomCollection}</span>
        </c:if>
        <p class="marque" style="font-size:.9rem;">${chaussure.nomMarque}</p>
        <h1>${chaussure.modele}</h1>
        <p class="prix" style="font-size:1.6rem;"><fmt:formatNumber value="${chaussure.prix}" type="currency" currencySymbol="CHF "/></p>
        <p>${chaussure.description}</p>
        <c:if test="${not empty chaussure.dateSortie}">
            <p class="sous-titre">Sortie le <fmt:formatDate value="${chaussure.dateSortie}" pattern="dd/MM/yyyy"/></p>
        </c:if>

        <c:if test="${not empty errorPanier}"><div class="alerte erreur">${errorPanier}</div></c:if>

        <form action="${pageContext.request.contextPath}/panier" method="post">
            <input type="hidden" name="action" value="ajouter">
            <div class="champ">
                <label for="tailleId">Choisissez une taille</label>
                <select name="tailleId" id="tailleId" required>
                    <c:forEach var="t" items="${chaussure.tailles}">
                        <option value="${t.id}" ${t.stock == 0 ? 'disabled' : ''}>
                            US ${t.tailleUs} / FR ${t.tailleFr} &mdash;
                            <c:choose>
                                <c:when test="${t.stock == 0}">Rupture de stock</c:when>
                                <c:otherwise>${t.stock} en stock</c:otherwise>
                            </c:choose>
                        </option>
                    </c:forEach>
                </select>
            </div>
            <div class="champ">
                <label for="quantite">Quantite</label>
                <input type="number" id="quantite" name="quantite" value="1" min="1" max="10">
            </div>
            <button type="submit" class="bouton">Ajouter au panier</button>
        </form>
    </div>
</div>

<%@ include file="commun/pied.jsp" %>
