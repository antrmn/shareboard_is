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
 * Classe che permette la modifica dei commenti.
 */
@WebServlet("/editcomment")
@AuthorizationConstraints(REQUIRE_AUTHENTICATION)
class EditCommentServlet extends InterceptableServlet {
    @Inject private CommentService service;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ParameterConverter converter = new ParameterConverter(request);
        int commentId = converter.getIntParameter("id").orElse(0);
        String text = request.getParameter("text");
        service.editComment(commentId,text);

        int parentPostId = service.getComment(commentId).getPostId();

        response.sendRedirect( getServletContext().getContextPath() + "/usecase/post/" + parentPostId);
    }
}
