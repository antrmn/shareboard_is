<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="partials/head.jsp">
        <jsp:param name="currentPage" value="${section.name}" />
        <jsp:param name="styles" value="section" />
        <jsp:param name="scripts" value="post,search,postloader" />
    </jsp:include>
</head>
<body>
    <jsp:include page="/WEB-INF/views/partials/navbar.jsp">
        <jsp:param name="currentSection" value="Cerca" />
    </jsp:include>

    <div id="body-container">
        <div id="left-container" class="selected-container">
            <div id="post-container">
                <%-- vedi search.js --%>

            </div>
            <div id="posts-delimiter"></div>
        </div>
        <div id = right-container >
            <div class = "greyContainer">
                <div id="action-container" style = "margin:8px;">
                    <form id = "search-form" class = "grid-y-nw align-center justify-center" action="javascript:void(0)">
                        <div style = "align-self: start">
                            <h5 style = "border-bottom: solid #353535; border-bottom-width: 1px; padding-bottom: 10px;">
                                <label for="content-input" style = "display: inline">Contenuto</label>
                            </h5>
                            <input id="content-input" type="text" class="input-field" name="content" value="${fn:trim(param.content)}"/>
                            <h5 style = "border-bottom: solid #353535; border-bottom-width: 1px; padding-bottom: 10px;">
                                Sezione
                            </h5>
                            <select name="section" id="section-select" class = "dark-select" style = "margin-bottom:5px; display: block; width:40%">
                                <option></option>
                                <c:forEach var="section" items="${applicationScope.sections}">
                                    <option ${section.value.name == param.section ? "selected" : ""} value="${section.value.name}">${section.value.name}</option>
                                </c:forEach>
                            </select>
                            <label>
                                <input name="onlyfollow"  type="checkbox" ${param.onlyfollow == 'on' ? "checked" : ""}/>
                                Solo sezioni seguite
                            </label>
                                <h5 style = "border-bottom: solid #353535; border-bottom-width: 1px; padding-bottom: 10px;">
                                    <label for="author-input" style = "display: inline">Autore</label>
                                </h5>
                                <input id="author-input" type="text" class="input-field" name="author" value="${fn:trim(param.author)}"/>
                                <h5 style = "border-bottom: solid #353535; border-bottom-width: 1px; padding-bottom: 10px;">
                                    Data di pubblicazione
                                </h5>
                                <label>Da <input type="date" class="input-field" name="postedafter" value="${param.postedafter}" }/></label>
                                <label>A <input type="date" class="input-field" name="postedbefore" value="${param.postbefore}"></label>

                                <h5 style = "border-bottom: solid #353535; border-bottom-width: 1px; padding-bottom: 10px;">
                                    <label for="order-by-select" style = "display: inline">Ordina per</label>
                                </h5>
                                <select name="orderby" id="order-by-select" class = "dark-select">
                                    <option ${param.orderby == "newest" ? "selected" : ""} value="newest">Più recenti</option>
                                    <option ${param.orderby == "oldest" ? "selected" : ""} value="oldest">Più vecchi</option>
                                    <option ${param.orderby == "mostvoted" ? "selected" : ""} value="mostvoted">Più votati</option>
                                    <option ${param.orderby == "leastvoted" ? "selected" : ""} value="leastvoted">Meno votati</option>
                                </select>
                        </div>
                        <br>
                        <input type="submit" value="Cerca" class="roundButton">
                    </form>
                </div>
            </div>
        </div>
    </div>
</body>
</html>