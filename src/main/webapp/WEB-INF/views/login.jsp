<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <jsp:include page="partials/head.jsp">
    <jsp:param name="currentPage" value="Login" />
  </jsp:include>
</head>
<body>
<jsp:include page="/WEB-INF/views/partials/navbar.jsp">
  <jsp:param name="currentSection" value="Login" />
</jsp:include>

<div class = "auth-body-container">
  <div class="greyContainer auth-container" >
    <img id = "user-auth-image" src="images/bg-planet.png">
    <div style="display: flex; justify-content: center; align-items:center; flex-direction: column; flex-grow:1;">
      <h2>Login</h2>
      <ul>
        <c:if test = "${not empty requestScope.errors}">
          <c:forEach items="${requestScope.errors}" var="error">
            <li>${error}</li>
          </c:forEach>
        </c:if>
        <c:if test="${param.error != null}">
          <li>Credenziali non valide</li>
        </c:if>
      </ul>
      <form class = "grid-y-nw justify-center" id = "login-form" action="${pageContext.request.contextPath}/login" method="post">
        <label for="username">
          Username
          <i class="fas fa-info-circle tooltipicon">
            <div class="tooltip">Min: 3 char, Max: 30 char</div>
          </i>
        </label>
        <input class = "auth-input-field" type="text" id="username" name="username" value="${fn:trim(fn:escapeXml(param.username))}" required minlength="3" maxlength="30">
        <label for="pass">
          Password
          <i class="fas fa-info-circle tooltipicon">
            <div class="tooltip">Min: 3 char, Max: 255 char</div>
          </i>
        </label>
        <input class = "auth-input-field" type="password" id="pass" name="pass" minlength="3" maxlength="255" required>
        <input type="submit" value="Log In" class="roundButton" style = "margin-top:10px; align-self: center;">

        <span class = "auth-alternative-text">Non hai un account? <a href="./register" class="auth-alternative-link">Registrati</a></span>
        <div id="tooltip"></div>
      </form>
    </div>
  </div>
</div>
</body>
</html>
