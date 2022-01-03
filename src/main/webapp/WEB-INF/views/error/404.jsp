<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <head>
        <jsp:include page="../partials/head.jsp">
            <jsp:param name="currentPage" value="Not found" />
            <jsp:param name="styles" value="" />
            <jsp:param name="scripts" value="" />
        </jsp:include>
    </head>
</head>
<body>
<div id="error-body">
    <div id="error-pane">
        <div id="error-code">
            <h1>404</h1>
        </div>
        <h2>Not found</h2>
        <p>Non abbiamo trovato la risorsa che stavi cercando</p>
        <a href="${pageContext.request.contextPath}/">Torna alla homepage</a>
    </div>
</div>
</body>
</html>
