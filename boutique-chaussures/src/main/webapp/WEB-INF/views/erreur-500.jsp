<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="titrePage" value="Erreur serveur" scope="request"/>
<%@ include file="commun/entete.jsp" %>
<h1>Une erreur est survenue</h1>
<p>Desole, quelque chose s'est mal passe. Merci de reessayer dans un instant.</p>
<a class="bouton" href="${pageContext.request.contextPath}/accueil">Retour a l'accueil</a>
<%@ include file="commun/pied.jsp" %>
