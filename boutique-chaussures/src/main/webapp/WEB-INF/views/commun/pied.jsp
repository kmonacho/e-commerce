<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
</main>
<footer class="pied">
    &copy; 2026 ShoeBoutique &mdash; Projet pedagogique JSP / Servlets / MySQL
</footer>

<c:if test="${empty cookie.cookiesAcceptes}">
<div class="bandeau-cookies" id="bandeauCookies">
    <p>Ce site utilise des cookies pour ameliorer votre experience (session, panier). Acceptez-vous les cookies ?</p>
    <div class="actions">
        <a href="${pageContext.request.contextPath}/api/cookies?choix=accepter" class="bouton petit">Accepter</a>
        <a href="${pageContext.request.contextPath}/api/cookies?choix=refuser" class="bouton petit secondaire">Refuser</a>
    </div>
</div>
</c:if>
</body>
</html>
