package usecase.comment;

import common.http.ParameterConverter;
import common.http.interceptor.InterceptableServlet;
import usecase.auth.AuthorizationConstraints;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static usecase.auth.AuthorizationConstraints.Types.REQUIRE_AUTHENTICATION;

/**
 * Classe che permette l'eliminazione dei commenti.
 */
@WebServlet("/deletecomment")
@AuthorizationConstraints(REQUIRE_AUTHENTICATION)
class DeleteCommentServlet extends InterceptableServlet {
    @Inject private CommentService service;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ParameterConverter converter = new ParameterConverter(request);
        int commentId = converter.getIntParameter("id").orElse(0);
        int postId = service.getComment(commentId).getPostId();
        service.delete(commentId);
        response.sendRedirect(getServletContext().getContextPath() + "/post/" + postId);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
