<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="titrePage" value="Administration - Modele de chaussure" scope="request"/>
<%@ include file="../commun/entete.jsp" %>

<h1>${not empty chaussure ? 'Modifier' : 'Ajouter'} un modele de chaussure</h1>
<%@ include file="menu.jsp" %>

<div class="formulaire large">
    <form action="${pageContext.request.contextPath}/admin/chaussures" method="post">
        <c:if test="${not empty chaussure}">
            <input type="hidden" name="id" value="${chaussure.id}">
        </c:if>
        <input type="hidden" name="action" value="${not empty chaussure ? 'modifier' : 'creer'}">

        <div class="champ">
            <label for="modele">Nom du modele</label>
            <input type="text" id="modele" name="modele" required value="${not empty chaussure ? chaussure.modele : ''}">
        </div>

        <div class="champ">
            <label for="marqueId">Marque</label>
            <select id="marqueId" name="marqueId" required>
                <c:forEach var="m" items="${marques}">
                    <option value="${m.id}" ${not empty chaussure && chaussure.marqueId == m.id ? 'selected' : ''}>${m.nom}</option>
                </c:forEach>
            </select>
        </div>

        <div class="champ">
            <label for="genre">Genre</label>
            <select id="genre" name="genre" required>
                <c:forEach var="g" items="${genres}">
                    <option value="${g}" ${not empty chaussure && chaussure.genre == g ? 'selected' : ''}>${g}</option>
                </c:forEach>
            </select>
        </div>

        <div class="champ">
            <label for="collectionId">Collection</label>
            <select id="collectionId" name="collectionId">
                <c:forEach var="col" items="${collections}">
                    <option value="${col.id}" ${not empty chaussure && chaussure.collectionId == col.id ? 'selected' : ''}>${col.nom}</option>
                </c:forEach>
            </select>
            <div class="aide">Une nouvelle chaussure est initialisee sur la collection "Aucune" par defaut.</div>
        </div>

        <div class="champ">
            <label for="dateSortie">Date de sortie</label>
            <input type="date" id="dateSortie" name="dateSortie"
                   value="${not empty chaussure && not empty chaussure.dateSortie ? chaussure.dateSortie : ''}">
        </div>

        <div class="champ">
            <label for="prix">Prix (CHF)</label>
            <input type="number" id="prix" name="prix" step="0.05" min="0" required
                   value="${not empty chaussure ? chaussure.prix : ''}">
        </div>

        <div class="champ">
            <label for="imageUrl">URL de l'image (ex : images/monmodele.jpg)</label>
            <input type="text" id="imageUrl" name="imageUrl" value="${not empty chaussure ? chaussure.imageUrl : ''}">
        </div>

        <div class="champ">
            <label for="description">Description</label>
            <textarea id="description" name="description">${not empty chaussure ? chaussure.description : ''}</textarea>
        </div>

        <button type="submit" class="bouton">Enregistrer</button>
    </form>
</div>

<%@ include file="../commun/pied.jsp" %>
