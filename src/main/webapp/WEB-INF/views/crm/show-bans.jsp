<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
  <button class="lightGreyButton roundButton" onclick="openModal();">Aggiungi Ban</button>
  <table>
    <tr>
      <th>Id</th>
      <th>Sezione</th>
      <th>Data Inizio</th>
      <th>Data Fine</th>
      <th>Bannato da</th>
      <th>Azioni</th>
    </tr>
    <c:forEach items="${requestScope.bans}" var="ban">
      <jsp:useBean id="endDate" class="java.util.Date" />
      <jsp:setProperty name="endDate" property="time" value="${ban.endTime.toEpochMilli()}" />
      <jsp:useBean id="startDate" class="java.util.Date" />
      <jsp:setProperty name="startDate" property="time" value="${ban.startTime.toEpochMilli()}" />
      <tr>
        <td>${ban.id}</td>
        <c:choose>
          <c:when test="${ban.section.id eq 0}">
            <td>Tutte</td>
          </c:when>
          <c:otherwise>
            <td>${applicationScope.sections[ban.section.id].name}</td>
          </c:otherwise>
        </c:choose>
        <td><fmt:formatDate value="${startDate}" pattern="dd/MM/yyyy"/></td>
        <c:choose>
          <c:when test="${not empty ban.endTime}">
            <td><fmt:formatDate value="${endDate}" pattern="dd/MM/yyyy"/></td>
          </c:when>
          <c:otherwise>
            <td>MAI</td>
          </c:otherwise>
        </c:choose>
        <td>${ban.admin.username} (${ban.admin.id})</td>
        <td>
          <a href = "${context}/admin/deleteban?banId=${ban.id}&userId=${requestScope.userId}" onclick="return confirm('Cancellare il ban?')">
            <i class="fas fa-minus-circle"></i>
          </a>
        </td>
      </tr>
    </c:forEach>
  </table>
  <div id="myModal" class="modal">

    <div class="modal-content">
      <div class="modal-header">
        <span class="close" onclick="closeModal();">&times;</span>
        <h2>Aggiungi Ban</h2>
      </div>
      <div class="modal-body">
        <ul id="error-list"></ul>

        <form id = "ban-form" class="grid-y align-center justify-center">
          <input type="hidden" value="${requestScope.userId}" name="userId">
          <div>
            <label for="end-date" style = "display: inline">Data Fine:</label>
            <input type = "date" name="endDate" id = "end-date" class = "dark-date-select" required>
          </div>
          <div>
            <label for="section-select" style = "display: inline">Scegli sezione:</label>
            <select name="sectionId" id="section-select" class = "dark-select">
              <option value="-1" selected>Tutte</option>
              <c:forEach items="${applicationScope.sections}" var="section">
                <option value="${section.value.id}">${section.value.name}</option>
              </c:forEach>
            </select>
          </div>
          <button class = "lightGreyButton roundButton">Aggiungi</button>
        </form>
      </div>
    </div>

  </div>
</div>
</body>
</html>