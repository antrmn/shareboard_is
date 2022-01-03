<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set var="context" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="partials/head.jsp">
        <jsp:param name="currentPage" value="Home" />
        <jsp:param name="scripts" value="home,post,postloader,filter" />
    </jsp:include>
</head>
<body>
        <jsp:include page="/WEB-INF/views/partials/navbar.jsp">
            <jsp:param name="currentSection" value="Home" />
            <jsp:param name="userName" value="${empty requestScope.loggedUser ? 'unlogged' : requestScope.loggedUser.username}" />
            <jsp:param name="userKarma" value="4316" />
        </jsp:include>

        <div id="body-container">
            <div id="left-container" class="selected-container">
                <jsp:include page="partials/filter.jsp">
                    <jsp:param name="isHome" value="true"/>
                </jsp:include>
                <div id="post-container">
                    <%-- Vedi home.js --%>
                </div>
                <div id="posts-delimiter"></div>
            </div>
            <div id="right-container">
                <div class="greyContainer">
                    <div class="card-image" style = "background-image: url('${pageContext.request.contextPath}/images/bg-planet2.png'); height: 34px;
                            background-position-y: center;
                            background-position-x: center;"></div>
                    <c:choose>
                        <c:when test="${empty requestScope.loggedUser}">
                            <h4 style = "margin-left: 10px;">Welcome on Shareboard!</h4>
                        </c:when>
                        <c:otherwise>
                            <h4 style = "margin-left: 10px;">Welcome back ${requestScope.loggedUser.username}</h4>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="greyContainer">
                    <div class="card-image" style = "background-image: url('${pageContext.request.contextPath}/images/banner-background.png');height: 80px; align-items: center">
                        <h2 class="card-img-text">Top Sections</h2>
                    </div>
                    <c:forEach items="${requestScope.topSections}" var="section" end="4">
                        <div class = "trending-container">
                            <c:choose>
                                <c:when test="${empty section.picture }">
                                    <img class = "small-round-image" src="${pageContext.request.contextPath}/images/default-logo.png">
                                </c:when>
                                <c:otherwise>
                                    <img class = "small-round-image" src= "${applicationScope.picsLocation}/${section.picture}">
                                </c:otherwise>
                            </c:choose>
                            <p style = "margin-left: 10px;">${section.name}</p>
                            <button type="submit" class = "${userFollows.contains(section.id) ? 'roundButton darkGreyButton follow-button follow-roundbutton follow-button-isfollowing' :  'roundButton lightGreyButton follow-button follow-roundbutton' } "
                                    onclick="toggleFollow(this)" data-section-id = "${section.id}" style = "margin-left: auto; margin-right: 10px;">
                                    ${userFollows.contains(section.id) ? 'Joined' : 'Join'}
                            </button>
                        </div>
                    </c:forEach>
                </div>
                <div class="greyContainer">
                    <div class="card-image" style = "background-image: url('${pageContext.request.contextPath}/images/bg-orange.png');">
                        <h2 class="card-img-text">Trending Sections</h2>
                    </div>
                        <c:forEach items="${requestScope.trendingSections}" var="section" end="4"  varStatus="loop">

                            <a href = "${context}/s/${section.name}" style = "width: 100%;" class = "trending-container">
                                <p style = "margin-left: 10px;">${loop.index+1}</p>
                                <i class="fas fa-chevron-up" style = "color:rgb(70, 209, 96); margin-left: 10px;"></i>
                                <c:choose>
                                    <c:when test="${empty section.picture }">
                                        <img class = "small-round-image" src="${pageContext.request.contextPath}/images/default-logo.png">
                                    </c:when>
                                    <c:otherwise>
                                        <img class = "small-round-image" src= "${applicationScope.picsLocation}/${section.picture}">
                                    </c:otherwise>
                                </c:choose>
                                <p style = "margin-left: 10px;">${section.name}</p>
                            </a>
                        </c:forEach>
                </div>
                <jsp:include page="partials/footer.jsp"/>
            </div>
        </div>
</body>
</html>