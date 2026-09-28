<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="titrePage" value="Administration - Tableau de bord" scope="request"/>
<%@ include file="../commun/entete.jsp" %>

<h1>Tableau de bord administrateur</h1>
<%@ include file="menu.jsp" %>

<div class="grille-tableau-bord">
    <div class="carte-stat"><div class="valeur">${nbChaussures}</div><div class="libelle">Modeles de chaussures</div></div>
    <div class="carte-stat"><div class="valeur">${nbCommandes}</div><div class="libelle">Commandes</div></div>
    <div class="carte-stat"><div class="valeur">${nbPartenaires}</div><div class="libelle">Partenaires</div></div>
    <div class="carte-stat"><div class="valeur">${nbMessages}</div><div class="libelle">Messages de contact</div></div>
</div>

<%@ include file="../commun/pied.jsp" %>
