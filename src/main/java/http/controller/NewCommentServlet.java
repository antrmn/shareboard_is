package http.controller;

import http.util.ParameterConverter;
import service.CommentService;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/newcomment")
public class NewCommentServlet extends HttpServlet {
    @Inject private ParameterConverter converter;
    @Inject private CommentService service;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int postId = converter.getIntParameter("id").orElse(0);
        int parentId = converter.getIntParameter("parent").orElse(0);
        String text = request.getParameter("text");

        if(postId > 0){
            service.newComment(text,postId);
        }else{
            service.newCommentReply(text,parentId,postId);
        }
        response.sendRedirect(request.getContextPath() + "/post/" + postId +"#comment-container");
    }
}
