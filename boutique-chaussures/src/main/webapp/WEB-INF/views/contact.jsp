<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="titrePage" value="Contact - ShoeBoutique" scope="request"/>
<%@ include file="commun/entete.jsp" %>

<h1>Nous contacter</h1>
<p class="sous-titre">Une question ? Ecrivez-nous, notre equipe vous repond rapidement.</p>

<div class="formulaire">
    <c:if test="${not empty erreur}"><div class="alerte erreur">${erreur}</div></c:if>
    <c:if test="${not empty succes}"><div class="alerte succes">${succes}</div></c:if>

    <form action="${pageContext.request.contextPath}/contact" method="post">
        <div class="champ">
            <label for="nom">Nom</label>
            <input type="text" id="nom" name="nom" required value="${not empty param.nom ? param.nom : ''}">
        </div>
        <div class="champ">
            <label for="email">Email</label>
            <input type="email" id="email" name="email" required value="${not empty param.email ? param.email : ''}">
        </div>
        <div class="champ">
            <label for="sujet">Sujet</label>
            <input type="text" id="sujet" name="sujet" value="${not empty param.sujet ? param.sujet : ''}">
        </div>
        <div class="champ">
            <label for="message">Message</label>
            <textarea id="message" name="message" required>${not empty param.message ? param.message : ''}</textarea>
        </div>
        <button type="submit" class="bouton">Envoyer le message</button>
    </form>
</div>

<%@ include file="commun/pied.jsp" %>
