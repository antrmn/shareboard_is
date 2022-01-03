<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="./partials/head.jsp">
        <jsp:param name="currentPage" value="Edit User" />
        <jsp:param name="styles" value="" />
        <jsp:param name="scripts" value="" />
    </jsp:include>
</head>
<body>
<jsp:include page="/WEB-INF/views/partials/navbar.jsp">
    <jsp:param name="currentSection" value="Edit User" />
</jsp:include>

<div id="body-container" class = "justify-center align-center">
        <div class = "grid-x-nw" style = "flex-basis: 768px;">
            <div id = left-container class="selected-container">
                <h2 style="border-bottom-style: solid; border-bottom-width: 1px; padding-bottom: 10px;">Edit user</h2>
                <div id="post-data" class="greyContainer">
<%--TODO: rendi decente --%>
                    <div id="action-container" style="margin:8px">
                        <form id="edit-user-form" class="grid-y-nw align-center" action="${pageContext.request.contextPath}/edituser" method="post" enctype="multipart/form-data" style = "align-items: flex-start;">
                            <label>Email:</label>
                            <input type="email" id="email-field" class="input-field" name="email" placeholder="Email" value="${user.email}" maxlength="255" style = "width: 300px;">
                            <label>Description:</label>
                            <textarea id="description-field" class="input-field" name="description" rows="3" placeholder="..." pattern="^.{0,255}$" style="height: 100px; width: 80%;">${user.description}</textarea>

                            <label for="img">Immagine del profilo: </label>
                            <input type="file" id="img" name="picture" accept="image/*">

                            <input id="user-id" type="hidden" name="id" value="${user.id}">
                            <label> Password:</label>
                            <input type="password" class="input-field" id = "pass" name="pass" minlength="3" maxlength="255"  style = "width: 300px;">
                            <label> Conferma password:</label>
                            <input type="password" class="input-field" id = "pass2" name="pass2" minlength="3" maxlength="255"   style = "width: 300px;">
                            <ul>
                                <c:forEach items="${errors}" var="error">
                                    <li>${error}</li>
                                </c:forEach>
                            </ul>

                            <input type="submit" value="Submit" class="roundButton" onclick="validateUserEdit(this)" style = "align-self: center;">
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
