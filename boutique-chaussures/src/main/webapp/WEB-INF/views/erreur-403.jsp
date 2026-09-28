<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="titrePage" value="Acces refuse" scope="request"/>
<%@ include file="commun/entete.jsp" %>
<h1>403 &mdash; Acces refuse</h1>
<p>Vous n'avez pas les droits necessaires pour acceder a cette page.</p>
<a class="bouton" href="${pageContext.request.contextPath}/accueil">Retour a l'accueil</a>
<%@ include file="commun/pied.jsp" %>
