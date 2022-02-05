package http.controller;

import http.util.ParameterConverter;
import service.CommentService;
import service.PostService;
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
    @Inject private ParameterConverter converter;
    @Inject private PostService postService;
    @Inject private CommentService commentService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int postId = converter.getIntParameter("id").orElse(0);
        int commentId = converter.getIntParameter("comment").orElse(0);

        Map<Integer, List<CommentDTO>> replies;
        if(commentId > 0){
            replies = commentService.getReplies(commentId);
            //todo error check
        } else {
            replies = commentService.getPostComments(postId);
        }

        req.setAttribute("post", postService.getPost(postId));
        req.setAttribute("comments", replies);
        req.getRequestDispatcher("/WEB-INF/views/section/post.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
