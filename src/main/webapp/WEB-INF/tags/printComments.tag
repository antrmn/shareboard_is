<%@ tag body-content="scriptless" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="sb"%>
<%@ attribute name="comments" type="java.util.Map" required="true"%>
<%@ attribute name="idParent" type="java.lang.Integer" required="true" %>
<%@ attribute name="depth" type="java.lang.Integer" required="true" %>
<%@ attribute name="isUserBanned" required="true" %>
<%@ attribute name="commentFragment" fragment="true"%>
<%@ variable name-given="comment" variable-class="usecase.comment.CommentDTO" scope="NESTED" %>
<%@ variable name-given="childComments" variable-class="java.lang.String" scope="NESTED"%>

<c:forEach items="${comments[idParent]}" var="comment">
    <c:set var="childComments">
        <c:choose>
            <c:when test="${depth < applicationScope.maxCommentDepth}">
                <c:set var = "actualDepth"  value = "${depth+1}"/>
                <sb:printComments comments="${comments}" idParent="${comment.id}" depth="${depth+1}" isUserBanned="${isUserBanned}">
                                <jsp:attribute name="commentFragment">
                                    <%@ include file="/WEB-INF/views/partials/comment.jsp" %>
                                </jsp:attribute>
                </sb:printComments>
            </c:when>
            <c:otherwise>
                <div class="grid-x-nw">
                    <a href="${pageContext.request.contextPath}/post/${comment.postId}?comment=${comment.id}#comment-container"
                       class="underline-some"
                       style="margin-top:10px">
                        <span class="to-underline">Visualizza risposte a questo commento</span>
                        &nbsp;<i class="fas fa-long-arrow-alt-right"></i>
                    </a>
                </div>
            </c:otherwise>
        </c:choose>
    </c:set>
    <jsp:invoke fragment="commentFragment"/>
</c:forEach>