package http.controller;

import service.CommentService;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/newcomment")
public class NewCommentServlet extends HttpServlet {

    @Inject private CommentService service;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String _postId = request.getParameter("id");
        String _parentId = request.getParameter("parent");
        String text = request.getParameter("text");

        int postId = 0;
        if(_postId != null && _postId.matches("\\d*")){
            postId = Integer.parseInt(_postId);
        }
        int parentId = 0;
        if(_parentId != null && _parentId.matches("\\d*")){
            parentId = Integer.parseInt(_parentId);
        }

        if(postId > 0){
            service.newComment(text,postId);
        }else{
            service.newCommentReply(text,parentId,postId);
        }
        response.sendRedirect(request.getContextPath() + "/post/" + postId +"#comment-container");
    }
}
