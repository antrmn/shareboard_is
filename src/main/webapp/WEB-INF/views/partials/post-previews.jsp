<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<c:forEach items="${requestScope.posts}" var="post">
    <%-- Vuoi capire la differenza tra @include e jsp:include? Prova a sostituire. --%>
    <%@ include file="./post-preview.jsp" %>
</c:forEach>