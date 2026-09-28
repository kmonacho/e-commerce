<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="titrePage" value="Mes commandes - ShoeBoutique" scope="request"/>
<%@ include file="commun/entete.jsp" %>

<h1>Mes commandes</h1>

<c:if test="${not empty sessionScope.messageCommande}">
    <div class="alerte succes">${sessionScope.messageCommande}</div>
    <c:remove var="messageCommande" scope="session"/>
</c:if>

<c:if test="${empty commandes}">
    <p>Vous n'avez pas encore passe de commande.</p>
</c:if>

<c:forEach var="cmd" items="${commandes}">
    <table class="tableau-donnees" style="margin-bottom:1.5rem;">
        <thead>
            <tr>
                <th colspan="3">
                    Commande #${cmd.id} &mdash; <fmt:formatDate value="${cmd.dateCommande}" pattern="dd/MM/yyyy HH:mm"/>
                    <span class="statut ${cmd.statut}" style="margin-left:.6rem;">${cmd.statut}</span>
                </th>
            </tr>
            <tr><th>Modele</th><th>Taille</th><th>Quantite / Prix</th></tr>
        </thead>
        <tbody>
            <c:forEach var="l" items="${cmd.lignes}">
                <tr>
                    <td>${l.modele}</td>
                    <td>US ${l.tailleUs} / FR ${l.tailleFr}</td>
                    <td>x${l.quantite} &mdash; <fmt:formatNumber value="${l.prixUnitaire * l.quantite}" type="currency" currencySymbol="CHF "/></td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    <div style="display:flex; justify-content:space-between; align-items:center; margin:-1rem 0 2rem;">
        <strong>Total : <fmt:formatNumber value="${cmd.total}" type="currency" currencySymbol="CHF "/></strong>
        <div style="display:flex; gap:.6rem;">
            <c:if test="${cmd.statut == 'EN_ATTENTE'}">
                <a class="bouton petit" href="${pageContext.request.contextPath}/paiement?commandeId=${cmd.id}">Payer</a>
                <a class="bouton danger petit" href="${pageContext.request.contextPath}/commande?action=annuler&id=${cmd.id}"
                   onclick="return confirm('Annuler cette commande ?');">Annuler</a>
            </c:if>
        </div>
    </div>
</c:forEach>

<%@ include file="commun/pied.jsp" %>
