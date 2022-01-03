<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="../partials/head.jsp">
        <jsp:param name="currentPage" value="New Post" />
        <jsp:param name="styles" value="createpost" />
        <jsp:param name="scripts" value="createpost" />
    </jsp:include>
</head>
<body>
<jsp:include page="/WEB-INF/views/partials/navbar.jsp">
    <jsp:param name="currentSection" value="Create Post" />
</jsp:include>

<div id="body-container" class = "justify-center align-center">
        <div class = "grid-x-nw" style = "flex-basis: 1280px;">
            <div id = left-container class="selected-container">
                <h2 style = "border-bottom-style: solid; border-bottom-width: 1px; padding-bottom: 10px;">Create Post</h2>
                <div id = "post-data" class = "greyContainer">
                    <div class = "grid-x-nw" style = "flex-grow: 1">
                        <button id = "text-button" class = "post-type-button post-type-button-left ${param.type == "text" ? "post-type-button-selected" : ""}" onclick = "togglePostType(this, true)">
                            <i class="fas fa-comment-alt" style = "display: inline"></i>
                            <p style = "display: inline">Post</p>
                        </button>
                        <button id = "image-button" class = "post-type-button post-type-button-right ${param.type  == "picture" ? "post-type-button-selected" : ""}" onclick = "togglePostType(this, true)">
                            <i class="fas fa-image"  style = "display: inline"></i>
                            <p style = "display: inline">Image</p>
                        </button>
                    </div>
                    <div id="action-container" style = "margin:8px;">
                        <form id = "create-post-form" class = "grid-y-nw align-center justify-center" action="${pageContext.request.contextPath}/newpost" method="post" enctype="multipart/form-data">
                            <div style = "align-self: start">
                                <label for="section-select" style = "display: inline">Choose a section:</label>
                                <select name="section" id="section-select" class = "dark-select">
                                    <c:forEach items="${applicationScope.sections}" var="section">
                                        <option ${section.value.id == param.section ? 'selected' : ''}
                                                value="${section.value.id}">
                                                ${section.value.name}
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>

                            <input type="text" id="title-field" class = "input-field" name="title" placeholder="Title" value="${param.title}" pattern="^.{1,255}$" required>
                            <textarea id="text-field" class = "input-field" name = "content" rows="5" placeholder="Text" pattern="^.{0,1000}$">${param.content}</textarea>

                            <label for="img" hidden>Select image:</label>
                            <input type="file" id="img" name="picture" accept="image/*" hidden>

                            <input id = "post-type" type="hidden" name = "type" value = "text">

                            <ul>
                                <c:if test = "${not empty requestScope.errors}">
                                    <c:forEach items="${requestScope.errors}" var="error">
                                        <li>${error}</li>
                                    </c:forEach>
                                </c:if>
                            </ul>

                            <input type="submit" value="Post" class="roundButton interactable" onclick="validateTextAreaById('text-field', 'Massimo 1000 caratteri')">
                        </form>
                    </div>
                </div>
            </div>
            <div id="right-container" style = "margin-top:55px;">
                <jsp:include page="../partials/rules.jsp"/>
            </div>
        </div>
    </div>
</div>
</body>
</html>
