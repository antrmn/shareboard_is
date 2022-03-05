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

@WebServlet("/newcomment")
@AuthorizationConstraints(REQUIRE_AUTHENTICATION)
class NewCommentServlet extends InterceptableServlet {
    @Inject private CommentService service;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ParameterConverter converter = new ParameterConverter(request);
        int postId = converter.getIntParameter("id").orElse(0);
        int parentId = converter.getIntParameter("parent").orElse(0);
        String text = request.getParameter("text");

        if(postId > 0){
            service.newComment(text,postId);
        }else{
            service.newCommentReply(text,parentId);
        }
        response.sendRedirect(request.getContextPath() + "/post/" + postId +"#comment-container");
    }
}
