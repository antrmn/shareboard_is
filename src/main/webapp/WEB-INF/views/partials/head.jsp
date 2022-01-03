<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<c:set var="context" value="${pageContext.request.contextPath}" />
<head>
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Cache-Control" content="no-cache">
    <meta http-equiv="Expires" content="Sat, 01 Dec 2001 00:00:00 GMT">
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, viewport-fit=cover">
    <title><c:out value="${param.currentPage}" /></title>
    <meta name = "description" content = "Shareboard forum">
    <link rel = "icon" type = "image/png" href = "${pageContext.request.contextPath}/images/logo.ico">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="format-detection" content = "telephone-no">
    <meta name ="apple-mobile-web-app-title" content = "Shareboard">
    <meta name="apple-mobile-web-app-status-bar-style" content = "default">
    <link rel = "apple-touch-icon" href = "images/logo.ico">
    <link rel="apple-touch-startup-image" href="images/logo.ico">
    <meta name="theme-color" content="#000000">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.12.0-2/css/all.min.css"
          integrity="sha256-46r060N2LrChLLb5zowXQ72/iKKNiw/lAmygmHExk/o=" crossorigin="anonymous" />
    <link rel="stylesheet" href="${context}/css/normalize.css" type="text/css">
    <link rel="stylesheet" href="${context}/css/style.css" type="text/css">
<%--    Load Styles--%>
    <c:if test="${not empty param.styles}">
        <c:forTokens items="${param.styles}" delims="," var="style">
            <link rel="stylesheet" href="${context}/css/${style}.css" type="text/css">
        </c:forTokens>
    </c:if>

<%--    Load Scripts--%>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js" defer></script>
    <script src="${context}/js/script.js" defer></script>
    <c:if test="${not empty param.scripts}">
        <c:forTokens items="${param.scripts}" delims="," var="script">
            <script src="${context}/js/${script}.js" defer></script>
        </c:forTokens>
    </c:if>
</head>