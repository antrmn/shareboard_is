package http.controller;

import service.CommentService;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/editcomment")
public class EditCommentServlet extends HttpServlet {

    @Inject private CommentService service;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String _commentId = request.getParameter("id");
        int commentId = 0;
        if(_commentId != null && _commentId.matches("\\d*")){
            commentId = Integer.parseInt(_commentId);
        }
        String text = request.getParameter("text");

        service.editComment(commentId,text);

        int parentPostId = service.getComment(commentId).getPost().getId();

        response.sendRedirect( getServletContext().getContextPath() + "/post/" + parentPostId);
    }
}
