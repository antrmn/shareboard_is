<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="../partials/head.jsp">
        <jsp:param name="currentPage" value="Modifica Sezione" />
        <jsp:param name="styles" value="admin" />
        <jsp:param name="scripts" value="admin" />
    </jsp:include>
</head>
<body>
<jsp:include page="/WEB-INF/views/partials/navbar.jsp">
    <jsp:param name="currentSection" value="Modifica Sezione" />
</jsp:include>

<jsp:include page="../partials/admin-sidebar.jsp">
    <jsp:param name="currentSection" value="dashboard" />
</jsp:include>
<div class = "grid-y-nw align-center justify-center" style = "margin-top: 100px;">
    <div id = "size-container">
        <h2 style = "border-bottom-style: solid; border-bottom-width: 1px; padding-bottom: 10px;">Modifica Sezione "${requestScope.section.name}"</h2>
        <div id = "section-data" class = "greyContainer">
            <div id="action-container" style = "margin:8px;">
                <form id = "create-post-form" class = "grid-y-nw align-center justify-center" action="${pageContext.request.contextPath}/admin/editsection" method="post" enctype="multipart/form-data">

                    <textarea id="text-field" class = "input-field" name = "description" rows="5" placeholder="Descrizione" pattern="^.{0,255}$">${requestScope.section.description}</textarea>
                    <label for="img" hidden>Select image:</label>
                    <input type="file" id="img" name="picture" accept="image/*">
                    <label for="img">Banner:</label>
                    <input type="file" id="banner" name="banner" accept="image/*">
                    <input type="hidden" id="sectionId" name="sectionId" value="${requestScope.section.id}">

                    <ul>
                        <c:if test = "${not empty requestScope.errors}">
                            <c:forEach items="${requestScope.errors}" var="error">
                                <li>${error}</li>
                            </c:forEach>
                        </c:if>
                    </ul>

                    <input type="submit" value="Aggiorna" class="roundButton">
                </form>
            </div>
        </div>
    </div>
</div>
</div>
</body>
</html>
