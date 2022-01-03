<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <head>
        <jsp:include page="../partials/head.jsp">
            <jsp:param name="currentPage" value="Unauthorized" />
            <jsp:param name="styles" value="" />
            <jsp:param name="scripts" value="" />
        </jsp:include>
    </head>
</head>
<body>
<div id="error-body">
    <div id="error-pane">
        <div id="error-code">
            <h1>401</h1>
        </div>
        <h2>Unauthorized</h2>
        <p>Il server non ha potuto verificare se disponi dei permessi necessari per accedere alla risorsa richiesta.<br>
            Ciò potrebbe essere dovuto a una mancata autenticazione o a credenziali non valide.</p>
        <p>${fn:escapeXml(requestScope['javax.servlet.error.message'])}</p>
        <a href="${pageContext.request.contextPath}/login">Effettua il login</a><br>
        <a href="${pageContext.request.contextPath}/">Torna alla homepage</a>
    </div>
</div>
</body>
</html>
