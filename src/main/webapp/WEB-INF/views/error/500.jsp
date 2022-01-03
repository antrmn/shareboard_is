<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <head>
        <jsp:include page="../partials/head.jsp">
            <jsp:param name="currentPage" value="Internal Server Error" />
            <jsp:param name="styles" value="" />
            <jsp:param name="scripts" value="" />
        </jsp:include>
    </head>
</head>
<body>
<div id="error-body">
    <div id="error-pane">
        <div id="error-code">
            <h1>500</h1>
        </div>
        <h2>Internal Server Error</h2>
        <p>Si è verificato un imprevisto che ha impedito al server di soddisfare la richiesta. <br>
            Provare a ripetere la richiesta e contattare l'amministratore se il problema persiste</p>
        <a href="${pageContext.request.contextPath}/">Torna alla homepage</a>

        <%-- SOLO a scopo di debug --%>
        <div class="greyContainer">
            ${pageContext.out.flush()}
            ${fn:escapeXml(pageContext.errorData.throwable.printStackTrace(pageContext.response.writer))}
        </div>
    </div>
</div>
</body>
</html>
