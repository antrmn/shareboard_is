<%--@elvariable id="user" type="model.User"--%>

<jsp:useBean id="creationDate" class="java.util.Date" />
<jsp:setProperty name="creationDate" property="time" value="${user.creationDate.toEpochMilli()}" />

<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <jsp:include page="./partials/head.jsp">
    <jsp:param name="currentPage" value="${user.username}" />
    <jsp:param name="styles" value="section" />
    <jsp:param name="scripts" value="user,post,postloader,filter" />
  </jsp:include>
</head>
<body>
<jsp:include page="/WEB-INF/views/partials/navbar.jsp">
  <jsp:param name="isLogged" value="true" />
  <jsp:param name="currentSection" value="${user.username}" />
  <jsp:param name="userName" value="Testus" />
  <jsp:param name="userKarma" value="4316" />
</jsp:include>

<div class = "grid-y-nw" style = "height:324px; left:0; right:0; position:relative; top:30px;">
  <div id = "header-image" style ='background: url("${pageContext.request.contextPath}/images//user-default-banner.jpg") no-repeat scroll center center / cover;'>
    <a href="${pageContext.request.contextPath}" style = "height:inherit; width:100%; display: inline-block;"></a>
  </div>
  <div id="header-container" class = "grid-x-nw" >

    <span>
      <c:choose>
        <c:when test="${not empty user.picture}">
          <img id="header-icon" src= "${pageContext.request.contextPath}/images/${user.picture}" >
        </c:when>
        <c:otherwise>
          <img id = "header-icon" src="${pageContext.request.contextPath}/images/default-user-icon.webp">
        </c:otherwise>
      </c:choose>
    </span>
    <span class="grid-y">
      <h2>${user.username}</h2>
      <span>Utente dal <fmt:formatDate value="${creationDate}" pattern="dd/MM/yyyy"/></span>
      <c:if test="${currentUser.id == user.id || currentUser.admin == true}">
        <a href="${pageContext.request.contextPath}/edituser?id=${user.id}"
           style="margin-right: auto; margin-bottom:10px; margin-top:10px" class="roundButton darkGreyButton">
        Edit profile
        </a>
      </c:if>
    </span>
  </div>
</div>

<div id="body-container">
  <div id="left-container" class="selected-container">
    <jsp:include page="./partials/filter.jsp">
      <jsp:param name="isHome" value="false"/>
    </jsp:include>
    <div id="post-container" author="${user.username}">
        <%-- vedi user.js --%>
    </div>
    <div id="posts-delimiter"></div>
  </div>


  <div id="right-container">
      <jsp:include page="./partials/user-info.jsp">
        <jsp:param name="username" value="${user.username}" />
        <jsp:param name="description" value="${empty user.description ? 'Nessuna descrizione' : user.description}" />
      </jsp:include>
    <jsp:include page="./partials/footer.jsp"/>
  </div>
</div>
</body>
</html>