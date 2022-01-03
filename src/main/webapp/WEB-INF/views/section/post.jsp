<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<%@ taglib prefix="sb" tagdir="/WEB-INF/tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%-- Sta roba (l'anyMatch) è un optional diverso da java.util.stream. Vedi org.apache.el.stream.Optional  --%>
<c:set var="isUserBanned"
       value="${not empty requestScope.loggedUserBans.stream().filter(ban -> ban.global.booleanValue() == true
                                                                            || ban.section.id == post.section.id)
                                                              .findFirst().orElse(null)}"/>

<!DOCTYPE html>
<html>
<jsp:include page="../partials/head.jsp">
  <jsp:param name="currentPage" value="${fn:escapeXml(post.title)}" />
  <jsp:param name="styles" value="section,comment,post" />
  <jsp:param name="scripts" value="comment" />
</jsp:include>
<body>
<jsp:include page="/WEB-INF/views/partials/navbar.jsp">
  <jsp:param name="currentSection" value="${post.section.name}" />
</jsp:include>

<div id="body-container" class = "justify-center align-center">

    <div id = left-container class = "greyContainer selected-container mobile-fullscreen-container">
        <div id="post-container" class = "grid-x-nw">
            <div id = "post-data" style = "flex-grow: 1;">
                <div class = "grid-x-nw" style = "align-items: start; margin-top:10px;">
                    <div class = "vote-container">
                        <input type = "hidden" name = "id" value = ${post.id}>
                        <c:choose>
                            <c:when test="${post.vote == 1}">
                                <i class="fas fa-chevron-up voteIcon upvoteIcon interactable upvote-icon-active" onclick="toggleVote(this, 'upvote', 'post')"></i>
                            </c:when>
                            <c:otherwise>
                                <i class="fas fa-chevron-up voteIcon upvoteIcon interactable" onclick="toggleVote(this, 'upvote', 'post')"></i>
                            </c:otherwise>
                        </c:choose>
                        <c:choose>
                            <c:when test="${post.votes != 0}">
                                <c:set var="vote" value="${post.votes}" />
                            </c:when>
                            <c:otherwise>
                                <c:set var="vote" value="Vote" />
                            </c:otherwise>
                        </c:choose>
                        <div class = "vote-count" style="word-break: initial; text-align: center; font-size: 12px;font-weight: 700; line-height: 16px;">${vote}</div>
                        <c:choose>
                            <c:when test="${post.vote == -1}">
                                <i class="fas fa-chevron-down voteIcon downvoteIcon interactable downvote-icon-active" onclick="toggleVote(this, 'downvote', 'post')"></i>
                            </c:when>
                            <c:otherwise>
                                <i class="fas fa-chevron-down voteIcon downvoteIcon interactable" onclick="toggleVote(this, 'downvote', 'post')"></i>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class = "grid-y-nw" style="flex-grow:1; align-items: start; margin-right: 40px;">
                        <div style = "flex-basis: 100%">
                            <a href="/shareboard/s/${post.section.name}" class = white-text>s/${fn:toLowerCase(requestScope.post.section.name)}</a>
                            <a href="/shareboard/u/${post.author.username}" class = grey-text>posted by ${post.author.username}</a>
                        </div>
                        <div class = "white-text ${empty post.content ? 'post-big-title' : ''}">
                            <h3>${fn:escapeXml(post.title)}</h3>
                        </div>

                        <div>
                            <c:choose>
                                <c:when test="${post.type == 'IMG'}">
                                    <img src= "${applicationScope.picsLocation}/${post.content}" class = "post-image">
                                </c:when>
                                <c:when test="${post.type == 'TEXT' && not empty post.content}">
                                    <p class = white-text style = "border: solid 1px gray; border-radius: 4px; padding: 7px; word-break: break-word;">
                                            ${fn:escapeXml(post.content)}
                                    </p>
                                </c:when>
                            </c:choose>
                        </div>
                        <div class = "grey-text" >
                            <i class="fas fa-comment-dots"></i>
                            ${post.nComments}
                            <c:if test="${requestScope.loggedUser.id == post.author.id || requestScope.loggedUser.admin.booleanValue() == true}">
                                <c:if test="${!isUserBanned}">
                                    <a href="${pageContext.request.contextPath}/editpost?id=${post.id}">Edit</a>
                                </c:if>
                                <a href="${pageContext.request.contextPath}/deletepost?id=${post.id}" onclick="return confirm('Cancellare il post?')">Delete</a>
                            </c:if>
                        </div>
                        <c:if test="${not empty requestScope.loggedUser && !isUserBanned}">
                            <jsp:include page="../partials/comment-form.jsp">
                                <jsp:param name="id" value="${post.id}"/>
                            </jsp:include>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
        <div id = "comments-flex" class = "grid-x-nw">
            <div class ="grid-x-nw" style = "width: 100%">
                <div id = "comments-container" style = "margin:8px; width: 100%">
                    <c:if test="${not empty param.comment}">
                        <a href="${pageContext.request.contextPath}/post/${post.id}#comment-container" class="underline-some"><span class="to-underline">Torna ai commenti principali</span> <i class="fas fa-long-arrow-alt-up"></i></a>
                    </c:if>
                    <c:choose>
                        <c:when test="${empty requestScope.comments}">
                            <div class = "grid-x-nw justify-center align-center">
                                <i class="fas fa-comment-alt" style = " color: rgb(215, 218, 220); font-size: 35px; margin-right: 15px;"></i>
                                <h2 style = " color: rgb(215, 218, 220); font-size: 20px;">
                                    Non ci sono commenti
                                </h2>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <sb:printComments comments="${requestScope.comments}" idParent="${requestScope.initialIndex}" depth="${0}" isUserBanned="${isUserBanned}">
                              <jsp:attribute name="commentFragment">
                                  <%@ include file="/WEB-INF/views/partials/comment.jsp" %>
                              </jsp:attribute>
                            </sb:printComments>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>
    <div id="right-container">
        <jsp:include page="../partials/section-info.jsp">
            <jsp:param name="description" value="${applicationScope.sections[post.section.id].description}" />
            <jsp:param name="nFollowers" value="${applicationScope.sections[post.section.id].nFollowersTotal}" />
            <jsp:param name="sectionId" value="${post.section.id}" />
        </jsp:include>
        <jsp:include page="../partials/rules.jsp"/>
        <jsp:include page="../partials/footer.jsp"/>
    </div>

</div>
</body>
</html>
