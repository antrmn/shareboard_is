package http.controller;

import service.CommentService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/editcomment")
public class EditCommentServlet extends HttpServlet {
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

        CommentService service = new CommentService();

        service.EditComment(commentId,text);

        int parentPostId = service.getComment(commentId).getPost().getId();

        response.sendRedirect( getServletContext().getContextPath() + "/post/" + parentPostId);
    }
}
