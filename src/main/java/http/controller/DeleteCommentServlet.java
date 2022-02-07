package http.controller;

import http.controller.interceptor.AuthorizationConstraints;
import http.util.ParameterConverter;
import service.CommentService;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static http.controller.interceptor.AuthorizationConstraints.Types.REQUIRE_AUTHENTICATION;

@WebServlet("/deletecomment")
@AuthorizationConstraints(REQUIRE_AUTHENTICATION)
public class DeleteCommentServlet extends HttpServlet {
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
