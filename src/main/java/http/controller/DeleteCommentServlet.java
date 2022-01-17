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
        int commentId = 0;
        if(_commentId != null && _commentId.matches("\\d*")){
            commentId = Integer.parseInt(_commentId);
        }
        new CommentService().Delete(commentId);

        //response.sendRedirect(getServletContext().getContextPath() + "/post/" + comment.getPost().getId()); serve l'id del post relativo al commento
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
