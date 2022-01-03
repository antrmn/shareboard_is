<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set var="context" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="../partials/head.jsp">
        <jsp:param name="currentPage" value="Admin" />
        <jsp:param name="styles" value="admin" />
        <jsp:param name="scripts" value="charts,admin" />
    </jsp:include>
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
</head>
<body>
    <jsp:include page="/WEB-INF/views/partials/navbar.jsp">
        <jsp:param name="currentSection" value="Admin" />
    </jsp:include>

    <jsp:include page="../partials/admin-sidebar.jsp">
        <jsp:param name="currentSection" value="dashboard" />
    </jsp:include>

    <div id="admin-panel-container" class = "grid-x justify-center align-center">

        <div class="dashboard-card dashboard-card-big grid-x hide">
            <div id="posts-bysection-chart" class="chart"></div>
        </div>
        <div class="dashboard-card dashboard-card-big grid-x hide">
            <div id="users-bysection-chart" class="chart"></div>
        </div>
        <div class="dashboard-card dashboard-card-big grid-x hide">
            <div id="recent-registrations-chart" class="chart"></div>
        </div>
        <div class="dashboard-card dashboard-card-big grid-x hide">
            <div id="recent-posts-chart" class="chart"></div>
        </div>
    </div>
</body>
</html>
