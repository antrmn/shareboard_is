<%@ taglib prefix="sbfn" uri="/WEB-INF/tlds/tagUtils.tld" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%--@elvariable id="comment" type="service.dto.CommentDTO"--%>
<%-- Quello sopra è un commento di IntelliJ che permette di ignorare l'errore "cannot resolve variable" e
     fornisce l'auto-complete anche se l'oggetto non è presente (Ancora) in nessuno scope --%>

<div id = "${comment.id}" class = "grid-x-nw comment-container" style = "width: 100%; align-items: start; margin-top:10px; background-color: ${actualDepth%2 eq 0 ? "#242323" : "#1A1A1B"}; border-radius: 4px; border: solid 1px #313132; ">
    <div class = "vote-container">
        <input type = "hidden" name = "id" value = ${comment.id}>
        <i class="fas fa-chevron-up voteIcon upvoteIcon interactable ${comment.vote == 1 ? "upvote-icon-active" : ""}" onclick = "toggleVote(this, 'upvote', 'comment')"></i>
        <div class = "vote-count" style="word-break: initial; text-align: center; font-size: 12px;font-weight: 700; line-height: 16px;">
            ${comment.votes}
        </div>
        <i class="fas fa-chevron-down voteIcon downvoteIcon interactable ${comment.vote == -1 ? "downvote-icon-active" : ""}" onclick = "toggleVote(this, 'downvote', 'comment')"></i>
    </div>
    <div class = "grid-y-nw" style="flex-grow:1; align-items: start;padding-bottom: 10px; padding-right: 10px;">
        <div style = "margin-top:5px;">
            <a class = "grey-text" href="${pageContext.request.contextPath}/u/${comment.authorUsername}">${comment.authorUsername}</a>
            <a href="javascript:void(0)" class="grey-text" title="${sbfn:getDate(comment.creationDate)}" >${sbfn:printTimeSince(comment.creationDate)} fa</a>
        </div>
        <div>
            <p class = "white-text comment-text">
                ${fn:escapeXml(comment.content)}
            </p>
        </div>
        <div>
            <c:if test="${comment.parentCommentId > 0 && empty actualDepth}">
                <span id="parent-button" class="grey-text"><a href="${pageContext.request.contextPath}/post/${comment.postId}?comment=${comment.parentCommentId}"><i class="fas fa-level-up-alt"></i>&nbsp;<span>Parent</span></a></span>
            </c:if>
            <c:if test="${currentUser.loggedIn && isUserBanned == false}">
             <span id = "reply-button" class = "grey-text" onclick="toggleTextArea(this)">
                <input type = "hidden" name = "commentId" value = ${comment.id}>
                <i class="fas fa-comment-dots"></i>
                <span>Reply</span>
             </span>
            </c:if>
            <c:if test="${currentUser.loggedIn
                        and (comment.authorId == currentUser.id or currentUser.admin)}">
                <c:if test="${isUserBanned == false}">
                    <span id = "edit-button" class = "grey-text" onclick="toggleTextArea(this)"><i class="fas fa-pencil-alt"></i>&nbsp;Edit</span>
                </c:if>
                <span class="grey-text"><a href="${pageContext.request.contextPath}/deletecomment?id=${comment.id}" id="delete-button" onclick="return confirm('Cancellare il commento?')"><i class="far fa-trash-alt"></i>&nbsp;Delete</a></span>
            </c:if>
        </div>
        <form class = "comment-form reply-form" method = "POST" action= "${pageContext.request.contextPath}/newcomment" hidden>
            <input type = "hidden" name = "parent" value = ${comment.id}>
            <input type = "hidden" name = "id" value = ${comment.postId}>
            <textarea class = 'dark-textarea' name = "text" rows="5" placeholder="Scrivi una risposta..." pattern="^.{1,255}$"></textarea>
            <br>
            <button class = roundButton onclick="validateTextAreaBySibling(this, 'lunghezza massima: 1000, lunghezza minima: 1')">Rispondi</button>
        </form>
        <form class = "comment-form edit-form" method = "POST" action= "${pageContext.request.contextPath}/editcomment" hidden>
            <input type = "hidden" name = "id" value = ${comment.id}>
            <textarea class = 'dark-textarea' name = "text" rows="5" pattern="^.{1,255}$">${fn:escapeXml(comment.content)}</textarea>
            <br>
            <button class = roundButton onclick="validateTextAreaBySibling(this, 'lunghezza massima: 1000, lunghezza minima: 1')">Modifica</button>
        </form>

        ${childComments}
    </div>
</div>