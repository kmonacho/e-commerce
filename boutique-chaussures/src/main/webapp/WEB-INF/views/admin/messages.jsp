<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="titrePage" value="Administration - Messages" scope="request"/>
<%@ include file="../commun/entete.jsp" %>

<h1>Messages de contact</h1>
<%@ include file="menu.jsp" %>

<table class="tableau-donnees">
    <thead><tr><th>De</th><th>Email</th><th>Sujet</th><th>Message</th><th>Date</th><th>Statut</th></tr></thead>
    <tbody>
        <c:forEach var="m" items="${messages}">
            <tr style="${m.lu ? '' : 'font-weight:700;'}">
                <td>${m.nom}</td>
                <td>${m.email}</td>
                <td>${m.sujet}</td>
                <td style="max-width:320px;">${m.message}</td>
                <td><fmt:formatDate value="${m.dateEnvoi}" pattern="dd/MM/yyyy HH:mm"/></td>
                <td>
                    <c:choose>
                        <c:when test="${m.lu}"><span class="statut PAYEE">Lu</span></c:when>
                        <c:otherwise>
                            <a class="bouton petit secondaire" href="${pageContext.request.contextPath}/admin/messages?action=marquer-lu&id=${m.id}">Marquer comme lu</a>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty messages}"><tr><td colspan="6">Aucun message recu.</td></tr></c:if>
    </tbody>
</table>

<%@ include file="../commun/pied.jsp" %>
