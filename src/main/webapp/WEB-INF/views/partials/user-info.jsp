<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<c:set var="context" value="${pageContext.request.contextPath}" />
<div class="greyContainer grid-y-nw  align-center">
  <h3>About ${param.username}</h3>
  <div class="user-description"> ${param.description}</div>
</div>