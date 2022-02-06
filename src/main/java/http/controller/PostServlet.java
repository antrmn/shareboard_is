package http.controller;

import http.util.ParameterConverter;
import service.CommentService;
import service.dto.CommentDTO;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/post")
public class PostServlet extends HttpServlet {

    @Inject private CommentService service;

    @Override
    public void init() throws ServletException {
        getServletContext().setAttribute("maxCommentDepth", 3);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ParameterConverter converter = new ParameterConverter(req);
        int _postId = converter.getIntParameter("id").orElse(0);
        int _commentId = converter.getIntParameter("comment").orElse(0);
        Map<Integer, List<CommentDTO>> comments;

        if (_commentId != 0) {
            comments = service.getReplies(_commentId);
        } else{
            comments = service.getPostComments(_postId);
        }

        System.out.println(_postId);
        req.setAttribute("comments", comments);
        req.getRequestDispatcher("/WEB-INF/views/section/post.jsp").forward(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }
}