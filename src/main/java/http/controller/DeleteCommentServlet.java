package http.controller;

import service.CommentService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/deletecomment")
public class DeleteCommentServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String _commentId = request.getParameter("id");
        CommentService service = new CommentService();
        int commentId = 0;
        if(_commentId != null && _commentId.matches("\\d*")){
            commentId = Integer.parseInt(_commentId);
        }
        int postId = service.getComment(commentId).getPost().getId();
        service.Delete(commentId);
        response.sendRedirect(getServletContext().getContextPath() + "/post/" + postId);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
