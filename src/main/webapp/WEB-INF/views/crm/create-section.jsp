<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="../partials/head.jsp">
        <jsp:param name="currentPage" value="Crea Sezione" />
        <jsp:param name="styles" value="admin" />
        <jsp:param name="scripts" value="admin" />
    </jsp:include>
</head>
<body>
    <jsp:include page="/WEB-INF/views/partials/navbar.jsp">
        <jsp:param name="currentSection" value="Crea Sezione" />
    </jsp:include>

    <jsp:include page="../partials/admin-sidebar.jsp">
        <jsp:param name="currentSection" value="dashboard" />
    </jsp:include>
    <div class = "grid-y-nw align-center justify-center" style = "margin-top: 100px;">
        <div id = "size-container">
            <h2 style = "border-bottom-style: solid; border-bottom-width: 1px; padding-bottom: 10px;">Crea Sezione</h2>
            <div id = "section-data" class = "greyContainer">
                <div id="action-container" style = "margin:8px;">
                    <form id = "create-post-form" class = "grid-y-nw align-center justify-center" action="${pageContext.request.contextPath}/admin/newsection" method="post" enctype="multipart/form-data">

                        <input type="text" id="title-field" class = "input-field" name="name" placeholder="Nome" value="${param.title}" minlength="1" maxlength="50" required>
                        <textarea id="text-field" class = "input-field" name = "description" rows="5" placeholder="Descrizione" pattern="^.{0,255}$">${param.content}</textarea>

                        <label for="img">Immagine:</label>
                        <input type="file" id="img" name="picture" accept="image/*">

                        <label for="img">Banner:</label>
                        <input type="file" id="banner" name="banner" accept="image/*">

                        <ul>
                            <c:if test = "${not empty requestScope.errors}">
                                <c:forEach items="${requestScope.errors}" var="error">
                                    <li>${error}</li>
                                </c:forEach>
                            </c:if>
                        </ul>

                        <input type="submit" value="Crea" class="roundButton" onclick="validateTextAreaBySibling(this, 'Lunghezza massima: 255')">
                    </form>
                </div>
        </div>
    </div>
</div>
</div>
</body>
</html>
