<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="titrePage" value="Administration - Commandes" scope="request"/>
<%@ include file="../commun/entete.jsp" %>

<h1>Gestion des commandes</h1>
<%@ include file="menu.jsp" %>

<table class="tableau-donnees">
    <thead>
        <tr><th>#</th><th>Client</th><th>Date</th><th>Statut</th><th>Paiement</th><th>Total</th></tr>
    </thead>
    <tbody>
        <c:forEach var="cmd" items="${commandes}">
            <tr>
                <td>${cmd.id}</td>
                <td>${cmd.emailClient}</td>
                <td><fmt:formatDate value="${cmd.dateCommande}" pattern="dd/MM/yyyy HH:mm"/></td>
                <td><span class="statut ${cmd.statut}">${cmd.statut}</span></td>
                <td>${not empty cmd.modePaiement ? cmd.modePaiement : '-'}</td>
                <td><fmt:formatNumber value="${cmd.total}" type="currency" currencySymbol="CHF "/></td>
            </tr>
        </c:forEach>
        <c:if test="${empty commandes}"><tr><td colspan="6">Aucune commande enregistree.</td></tr></c:if>
    </tbody>
</table>

<%@ include file="../commun/pied.jsp" %>
