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
        <th>Followers</th>
        <th>Azioni</th>
      </tr>
      <c:forEach items="${applicationScope.sections}" var="section">
        <tr>
          <td>${section.value.id}</td>
          <td>${section.value.name}</td>
          <td>${section.value.nFollowersTotal}</td>
          <td>
            <a href = "${context}/admin/editsection?sectionId=${section.value.id}">
              <i class="fas fa-edit"></i>
            </a>
            <a href = "${context}/admin/deletesection?sectionId=${section.value.id}" onclick="return confirm('Cancellare la sezione?')">
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
