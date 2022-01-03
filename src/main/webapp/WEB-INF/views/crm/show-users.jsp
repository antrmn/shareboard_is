<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set var="context" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="../partials/head.jsp">
        <jsp:param name="currentPage" value="Admin" />
        <jsp:param name="styles" value="admin" />
        <jsp:param name="scripts" value="admin" />
    </jsp:include>
</head>
<body>
<jsp:include page="/WEB-INF/views/partials/navbar.jsp">
    <jsp:param name="currentSection" value="Admin" />
</jsp:include>

<div class = "grid-y justify-center align-center" style = "margin-top: 150px;">
    <jsp:include page="../partials/admin-sidebar.jsp">
        <jsp:param name="currentSection" value="dashboard" />
    </jsp:include>
    <table>
        <tr>
            <th>Id</th>
            <th>Nome</th>
            <th>Admin</th>
            <th>Azioni</th>
        </tr>
        <c:forEach items="${requestScope.users}" var="user">
            <tr>
                <td>${user.id}</td>
                <td><a href="${pageContext.servletContext.contextPath}/u/${user.username}">${user.username}</a></td>
                <td>
                    <c:choose>
                        <c:when test="${user.admin}">
                            <input type="checkbox" name="isAdmin" onchange="toggleAdminStatus(this, ${user.id});" checked autocomplete="off">
                        </c:when>
                        <c:otherwise>
                            <input type="checkbox" name="isAdmin" onchange="toggleAdminStatus(this, ${user.id});" autocomplete="off">
                        </c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <a href = "${context}/admin/showbans?userId=${user.id}">
                        <i class="fas fa-gavel"></i>
                    </a>
                    <a href = "${context}/admin/deleteuser?userId=${user.id}" onclick="return confirm('Cancellare l utente?')">
                        <i class="fas fa-minus-circle"></i>
                    </a>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>
<%--</div>--%>
</body>
</html>
