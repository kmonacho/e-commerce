<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><c:out value="${titrePage != null ? titrePage : 'Boutique Chaussures'}"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<header class="entete">
    <a class="logo" href="${pageContext.request.contextPath}/accueil">Shoe<span>Boutique</span></a>
    <nav class="principale">
        <ul>
            <li><a href="${pageContext.request.contextPath}/accueil">Accueil</a></li>
            <li><a href="${pageContext.request.contextPath}/femmes">Femmes</a></li>
            <li><a href="${pageContext.request.contextPath}/hommes">Hommes</a></li>
            <li><a href="${pageContext.request.contextPath}/partenaires">Partenaires</a></li>
            <li><a href="${pageContext.request.contextPath}/contact">Contact</a></li>
        </ul>
    </nav>
    <div class="zone-compte">
        <c:choose>
            <c:when test="${not empty sessionScope.utilisateur}">
                <c:if test="${sessionScope.utilisateur.admin}">
                    <a href="${pageContext.request.contextPath}/admin/tableau-de-bord">Administration</a>
                </c:if>
                <c:if test="${!sessionScope.utilisateur.admin}">
                    <a href="${pageContext.request.contextPath}/panier">Panier</a>
                    <a href="${pageContext.request.contextPath}/commande">Mes commandes</a>
                </c:if>
                <span>Bonjour ${sessionScope.utilisateur.prenom}</span>
                <a class="bouton-connexion" href="${pageContext.request.contextPath}/deconnexion">Se deconnecter</a>
            </c:when>
            <c:otherwise>
                <a class="bouton-connexion" href="${pageContext.request.contextPath}/connexion">Se connecter</a>
            </c:otherwise>
        </c:choose>
    </div>
</header>
<main class="conteneur">
